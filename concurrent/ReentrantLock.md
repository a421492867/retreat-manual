# ReentrantLock

|            | ReentrantLock                  | Synchronized     |
| ---------- | :----------------------------- | ---------------- |
| 锁实现机制 | 依赖AQS                        | 监视器模式       |
| 灵活性     | 支持响应中断、超时、尝试获取锁 | 不灵活           |
| 释放形式   | 必须显示调用unlock()释放锁     | 自动释放监视器   |
| 锁类型     | 公平锁&非公平锁                | 非公平锁         |
| 条件队列   | 可关联多个条件队列             | 关联一个条件队列 |
| 可重入性   | 可重入                         | 可重入           |

## ReentrantLock与AQS

*非公平锁加锁流程*

```java
final void lock() {
            if (compareAndSetState(0, 1))
                setExclusiveOwnerThread(Thread.currentThread());
            else
                acquire(1);
        }
```

> 1.若通过CAS设置State(同步变量)成功 也就是获取锁成功 则将线程设置为独占锁
>
> 2.若通过CAS设置State失败 也就是获取锁失败 则进入Acquire方法进行后续处理

*当某个线程获取锁失败后，存在某种排队等候机制，线程继续等待，仍然保留获取锁的可能，获取锁流程仍在继续*

*公平锁获取锁的方式*

```java
        final void lock() {
            acquire(1);
        }
```

*上述可以看出，对于公平锁和非公平锁 都有调用acquire方法 这个方法位于AbstractQueuedSynchronizer中。即AQS*



## AQS

### 原理概述

*AQS的核心思想是 ： 如果被请求的共享资源空闲，那么就将当前请求资源的线程设置为有效的工作线程，将共享资源设置为锁定状态；如果共享资源被占用，就需要一定的阻塞等待唤醒机制来保证分配锁 这个机制主要用的是CLH队列的变体实现 将暂时获取不到锁的线程加入到队列中*

*CLH ： Craig、Landin and Hagersten队列， 是单向链表，AQS中的队列是CLH变体的虚拟双向队列(FIFO) AQS通过将每条请求共享资源的线程封装成一个节点来实现锁的分配*

*AQS 使用一个volatile的int类型的成员变量来表示同步状态，通过内置FIFI队列来完成资源获取的排队工作 通过CAS完成对state值的修改*

#### AQS数据结构

*Node*

```java
static final class Node {
        /** Marker to indicate a node is waiting in shared mode */
        static final Node SHARED = new Node();
        /** Marker to indicate a node is waiting in exclusive mode */
        static final Node EXCLUSIVE = null;

        /** waitStatus value to indicate thread has cancelled */
        static final int CANCELLED =  1;
        /** waitStatus value to indicate successor's thread needs unparking */
        static final int SIGNAL    = -1;
        /** waitStatus value to indicate thread is waiting on condition */
        static final int CONDITION = -2;
        /**
         * waitStatus value to indicate the next acquireShared should
         * unconditionally propagate
         */
        static final int PROPAGATE = -3;

        /**
         * Status field, taking on only the values:
         *   SIGNAL:     The successor of this node is (or will soon be)
         *               blocked (via park), so the current node must
         *               unpark its successor when it releases or
         *               cancels. To avoid races, acquire methods must
         *               first indicate they need a signal,
         *               then retry the atomic acquire, and then,
         *               on failure, block.
         *   CANCELLED:  This node is cancelled due to timeout or interrupt.
         *               Nodes never leave this state. In particular,
         *               a thread with cancelled node never again blocks.
         *   CONDITION:  This node is currently on a condition queue.
         *               It will not be used as a sync queue node
         *               until transferred, at which time the status
         *               will be set to 0. (Use of this value here has
         *               nothing to do with the other uses of the
         *               field, but simplifies mechanics.)
         *   PROPAGATE:  A releaseShared should be propagated to other
         *               nodes. This is set (for head node only) in
         *               doReleaseShared to ensure propagation
         *               continues, even if other operations have
         *               since intervened.
         *   0:          None of the above
         *
         * The values are arranged numerically to simplify use.
         * Non-negative values mean that a node doesn't need to
         * signal. So, most code doesn't need to check for particular
         * values, just for sign.
         *
         * The field is initialized to 0 for normal sync nodes, and
         * CONDITION for condition nodes.  It is modified using CAS
         * (or when possible, unconditional volatile writes).
         */
        volatile int waitStatus;

        /**
         * Link to predecessor node that current node/thread relies on
         * for checking waitStatus. Assigned during enqueuing, and nulled
         * out (for sake of GC) only upon dequeuing.  Also, upon
         * cancellation of a predecessor, we short-circuit while
         * finding a non-cancelled one, which will always exist
         * because the head node is never cancelled: A node becomes
         * head only as a result of successful acquire. A
         * cancelled thread never succeeds in acquiring, and a thread only
         * cancels itself, not any other node.
         */
        volatile Node prev;

        /**
         * Link to the successor node that the current node/thread
         * unparks upon release. Assigned during enqueuing, adjusted
         * when bypassing cancelled predecessors, and nulled out (for
         * sake of GC) when dequeued.  The enq operation does not
         * assign next field of a predecessor until after attachment,
         * so seeing a null next field does not necessarily mean that
         * node is at end of queue. However, if a next field appears
         * to be null, we can scan prev's from the tail to
         * double-check.  The next field of cancelled nodes is set to
         * point to the node itself instead of null, to make life
         * easier for isOnSyncQueue.
         */
        volatile Node next;

        /**
         * The thread that enqueued this node.  Initialized on
         * construction and nulled out after use.
         */
        volatile Thread thread;

        /**
         * Link to next node waiting on condition, or the special
         * value SHARED.  Because condition queues are accessed only
         * when holding in exclusive mode, we just need a simple
         * linked queue to hold nodes while they are waiting on
         * conditions. They are then transferred to the queue to
         * re-acquire. And because conditions can only be exclusive,
         * we save a field by using special value to indicate shared
         * mode.
         */
        Node nextWaiter;

        /**
         * Returns true if node is waiting in shared mode.
         */
        final boolean isShared() {
            return nextWaiter == SHARED;
        }

        /**
         * Returns previous node, or throws NullPointerException if null.
         * Use when predecessor cannot be null.  The null check could
         * be elided, but is present to help the VM.
         *
         * @return the predecessor of this node
         */
        final Node predecessor() throws NullPointerException {
            Node p = prev;
            if (p == null)
                throw new NullPointerException();
            else
                return p;
        }

        Node() {    // Used to establish initial head or SHARED marker
        }

        Node(Thread thread, Node mode) {     // Used by addWaiter
            this.nextWaiter = mode;
            this.thread = thread;
        }

        Node(Thread thread, int waitStatus) { // Used by Condition
            this.waitStatus = waitStatus;
            this.thread = thread;
        }
    }

```

>waitStatus：当前节点在队列中的状态
>
>thread：表示处于该节点的线程
>
>prev：前驱指针
>
>predecessor：返回前驱节点 如果没有的话抛出空指针异常
>
>nextWaiter：指向下一个处于Condition状态的节点
>
>next：后继指针

*线程有两种锁的模式：1.SHARED 表示线程以共享的模式等待锁  2.EXCLUSIVE：表示线程正在以独占的方式等待锁*

*waitStatus的枚举值：*

>0 ：当一个Node被初始化时候的默认值
>
>CANCELLED：为1 表示线程获取锁的请求已取消
>
>CONDITION：为-2 表示节点在等待队列中 节点线程等待唤醒
>
>PROPAGATE：为-3 当前线程处于SHARED情况下才会使用
>
>SIGNAL：为-1 表示该线程已经准备好 就等资源释放了

#### 同步状态State

```java
volatile int waitStatus;
```

*通过修改State字段表示的同步状态来实现多线程的独占模式和共享模式*

### 通过ReentrantLock理解AQS

```java
final void lock() {
            if (compareAndSetState(0, 1))
                setExclusiveOwnerThread(Thread.currentThread());
            else
                acquire(1);
        }
```

*跟进aquire方法*

```java
    public final void acquire(int arg) {
        if (!tryAcquire(arg) &&
            acquireQueued(addWaiter(Node.EXCLUSIVE), arg))
            selfInterrupt();
    }
```

*跟进tryAcquire*

```java
    protected boolean tryAcquire(int arg) {
        throw new UnsupportedOperationException();
    }
```

*AQS的简单实现 具体获取锁的实现方法是由各自的公平锁和非公平锁单独实现的 如果这个方法返回了true 则说明当前线程获取锁成功了 就不用在往后执行了；如果获取失败 就需要加入等待队列*

#### 线程加入等待队列

##### 加入等待队列的机制

*当Acquire(1)时，会通过tryAcquire获取锁。在这种情况下 如果获取锁失败 就会调用addWaiter加入到等待队列中去*

##### 如何加入等待队列

```java
addWaiter(Node.EXCLUSIVE), arg)
```

```java
    private Node addWaiter(Node mode) {
        Node node = new Node(Thread.currentThread(), mode);
        // Try the fast path of enq; backup to full enq on failure
        Node pred = tail;
        if (pred != null) {
            node.prev = pred;
            if (compareAndSetTail(pred, node)) {
                pred.next = node;
                return node;
            }
        }
        enq(node);
        return node;
    }
```

> 1.通过当前的线程和锁模式建立一个新的节点
>
> 2.Pred指针指向尾节点Tail
>
> 3.将new中Node热Prev指针指向Pred
>
> 4.通过compareAndSetTail方法 完成尾节点的设置

```java
static {
        try {
            stateOffset = unsafe.objectFieldOffset
                (AbstractQueuedSynchronizer.class.getDeclaredField("state"));
            headOffset = unsafe.objectFieldOffset
                (AbstractQueuedSynchronizer.class.getDeclaredField("head"));
            tailOffset = unsafe.objectFieldOffset
                (AbstractQueuedSynchronizer.class.getDeclaredField("tail"));
            waitStatusOffset = unsafe.objectFieldOffset
                (Node.class.getDeclaredField("waitStatus"));
            nextOffset = unsafe.objectFieldOffset
                (Node.class.getDeclaredField("next"));

        } catch (Exception ex) { throw new Error(ex); }
    }
```

> 5.如果Pred指针式Null，或者当前Pred指针和Tail指向的位置不同，即需要进去enq方法

```java
    private Node enq(final Node node) {
        for (;;) {
            Node t = tail;
            if (t == null) { // Must initialize
                if (compareAndSetHead(new Node()))
                    tail = head;
            } else {
                node.prev = t;
                if (compareAndSetTail(t, node)) {
                    t.next = node;
                    return t;
                }
            }
        }
    }
```

*如果没有初始化 需要进行初始化一个头节点出来 但是 初始化的头节点并不是当前线程节点 而是调用了无参构造函数的节点  如果经历了初始化或并发导致队列中有元素 则与之前方法相同。addWaiter就是在一个双端链表添加尾节点的操作 需要注意的是 双端链表的头节点是一个无参构造函数的头节点*

*在公平锁中 tryAcquire还需要首先调用hasQueuedPredecessors*

```java
    public final boolean hasQueuedPredecessors() {
        // The correctness of this depends on head being initialized
        // before tail and on head.next being accurate if the current
        // thread is first in queue.
        Node t = tail; // Read fields in reverse initialization order
        Node h = head;
        Node s;
        return h != t &&
            ((s = h.next) == null || s.thread != Thread.currentThread());
    }
```

*双向链表中 第一个节点为虚节点，并不存储任何信息 只是站位。真正的第一个有数据的节点，是在第二个节点开始的。当 h != t时：如果(s = h.next) == null ，等待队列中正有线程进行初始化(因为已经存在了head这个虚节点) 但是只进行到了Tail指向Head 没有将Head指向Tail 此时队列中有元素 需要返回true；如果(s = h.next) != null 说明等待队列中至少有一个有效节点 如果此时s.thread == Thread.currentThread 说明等待队列第一个有效节点与当前线程相同 那么当前线程可以获取资源，如果s.thread != Thread.currentThread 说明等待队列第一个有效的节点线程与当前线程不同 当前线程必须加入等待队列。 这里可以解决极端情况下的并发问题*

##### 等待队列中线程出队列的时机

```java
    final boolean acquireQueued(final Node node, int arg) {
      // 标记是否成功拿到锁
        boolean failed = true;
        try {
          // 标记等待过程是否中断过
            boolean interrupted = false;
          // 开始自旋 要么获得锁 要么中断
            for (;;) {
              // 获取当前节点的前驱节点
                final Node p = node.predecessor();
              // 如果p是头节点 说明当前节点在真实数据队列的首部 尝试获得锁
                if (p == head && tryAcquire(arg)) {
                  // 获得锁成功 头指针移动到当前node
                    setHead(node);
                    p.next = null; // help GC
                    failed = false;
                    return interrupted;
                }
              // 说明p为头节点且当前没有获得到锁(可能是非公平锁被抢占了)
              // 或者 p不是头节点 这时候要判断当前node是否需要被阻塞
              // 被阻塞的条件为 ： 前驱节点的waitStatus为 -1 防止无限循环浪费资源
                if (shouldParkAfterFailedAcquire(p, node) &&
                    parkAndCheckInterrupt())
                    interrupted = true;
            }
        } finally {
            if (failed)
                cancelAcquire(node);
        }
    }
```

 

```java
    // 靠前驱节点判断当前节点是否应该被阻塞
		private static boolean shouldParkAfterFailedAcquire(Node pred, Node node) {
      // 获取头节点的节点状态
        int ws = pred.waitStatus;
      // 说明头节点处于唤醒状态
        if (ws == Node.SIGNAL)
            /*
             * This node has already set status asking a release
             * to signal it, so it can safely park.
             */
            return true;
      // ws > 0是取消状态
        if (ws > 0) {
            /*
             * Predecessor was cancelled. Skip over predecessors and
             * indicate retry.
             */
            do {
              //循环向前查找取消节点 把取消节点从队列中提出
                node.prev = pred = pred.prev;
            } while (pred.waitStatus > 0);
            pred.next = node;
        } else {
            /*
             * waitStatus must be 0 or PROPAGATE.  Indicate that we
             * need a signal, but don't park yet.  Caller will need to
             * retry to make sure it cannot acquire before parking.
             */
          // 设置前任节点状态为SIGNAL
            compareAndSetWaitStatus(pred, ws, Node.SIGNAL);
        }
        return false;
    }
```

```java
    private final boolean parkAndCheckInterrupt() {
        LockSupport.park(this);
        return Thread.interrupted();
    }
```

*用于挂起当前线程 阻塞调用占 返回当前线程的中断状态*



