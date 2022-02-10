## Java并发编程

### safety 
> 通过同步来避免多个线程在同一时刻访问相同的数据

### shared
> 共享和发布对象 使它们能够安全地由多个线程同时访问
> 
### combination
> 对象的组合
> 
### module
> 基础模块的构建
> 
### taskexecution
> 任务执行
> 
> 
> 
>

### 线程池的底层工作原理
> 线程池内部是通过 队列 + 线程实现的 当我们利用线程池执行任务的时候:
> 1.如果此时线程池中的数量小于corePoolSize 即使线程池都处于空闲状态 也要创建新的线程来处理被添加的任务
> 2.如果线程数量等于corePoolSize 但是缓冲队列workQueue未满 那么任务被放入缓冲队列
> 3.如果线程数量大于等于corePoolSize 缓冲队列workQueue满 并且线程池中的数量小于maximumPoolSize 创建新的线程来处理被添加的任务
> 4.如果线程数量大于corePoolSize 缓冲队列workQueue满 且线程池中数量等于maximumPoolSize 那么通过handler指定的策略来处理此任务
> 5.当线程池中数量大于corePoolSize 若果某个线程空闲时间超过keepAliveTime 线程将被终止 这样 线程池可以动态调整池中的线程数

### ThreadLocal的底层原理
>ThreadLocal是Java提供的线程本地存储机制 可以利用该机制将数据缓存在某个线程内部 该县城可以在任意时刻 任意方法中获取缓存的数据
> ThreadLocal底层通过ThreadLocalMap来实现的  每个Thread对象中都存在一个ThreadLocalMap  Map的key为ThreadLocal对象  Map的value值为要缓存的值
> 如果线程池中使用ThreadLocal会造成内存泄露  因为当ThreadLocal对象使用完之后 应该要把设置的Key value 也就是Entry对象进行回收  但是线程池中的线程不会回收  而线程对象是通过强引用指向ThreadLocalMap ThreadLocalMap也是通过强引用指向Entry对象 线程不被回收 Entry对象也就不会回收 从而出现内存泄露, 需要在使用了ThreadLocal对象之后 手动调用ThreadLocal的remove方法 手动清除Entry对象
> ThreadLocal经典应用就是连接管理  一个线程有一个连接 该连接对象可以在不同的方法之间进行传递 线程之间不共享同一个连接

### volatile关键字
> 保证属性的可见性 对于加了volatile关键字的属性 在对这个属性进行修改时 会直接将CPU高速缓存中的数据写回到主存 对这个变量的读取也会直接从主存中读取 从而保证可见性
> 底层通过操作系统的内存屏障实现 由于使用了内存屏障 所以还会禁止指令重拍  也就保证了有序性

### ReentrantLock中公平锁和非公平锁的底层实现
> 底层会用AQS进行排队  

### ReentrantLock lock方法 和 tryLock方法的区别
> lock方法 无返回值  阻塞加锁
> tryLock 有返回值  非阻塞加锁  通常情况下 自旋锁  while(!reentrantlock.tryLock()){}
> 
### sychronized 和 ReentrantLock的区别
> sychronized是关键字  ReentrantLock是类
> sychronized会自动加锁与释放锁  ReentrantLock需要手动加锁和释放锁
> sychronized底层是JVM层面的锁  ReentrantLock是API层面的锁
> sychronized是非公平锁 ReentrantLock可以选择公平与否
> sychronized锁的是对象 锁信息保存在对象头  ReentrantLock通过代码中的state标识锁状态
> sychronized底层有一个锁升级过程

### CountDownLatch 和 Semaphore的区别和底层原理
> CountDownLatch表示计数器 可以给CountDownLatch设置一个数字 一个线程调用CountDownLatch的await方法 将会阻塞 其他线程调用countDown方法对CountDownLatch中的数字减1 当数字被减成0后 所有await的线程都会被唤醒
> 底层原理是调用await方法的线程会利用AQS排队 一旦数字被减成0 则会将AQS中排队的线程依次唤醒
> 
> Semaphore表示信号量  可以设置许可的个数 表示同时允许最多 多少个线程使用该信号量 通过acquire来获得许可  如果没有许可可用则线程阻塞 并通过AQS来排队  通过release方法来释放许可  当某个线程释放了某个许可 会从AQS中正在排队的第一个线程开始依次唤醒 直到没有空闲许可



> CAS : 无锁  自旋锁  乐观锁 轻量级锁   compareAndSwap
> 1、原子性问题 ： 硬件级别加锁 通过指令集 loc k cmpxchgq   缓存行锁/总线锁(超过64个字节会成为总线锁)
> 2、ABA问题： 版本号解决
> 3、轻量级锁 不一定 比重量级锁性能高
> 
> synchronized锁优化： 无状态 -> 偏向锁  -> 轻量级锁 -> 重量级锁
> 一个线程加锁 将线程Id写入对象头markword 成为偏向锁; 当多个线程进行加锁时  CAS轻度竞争 升级为轻量级锁   CAS自旋不成功(锁膨胀) 重度竞争 自适应自旋 成为重量级锁
> LockUpgrade  锁升级过程
> 
> 分段CAS LongAdder   cell数组
>  
> 
> 
> 
### AQS
> head  tail  thread waitstatus
> 
```Node t = tail; // Read fields in reverse initialization order
Node h = head;
Node s;
return h != t &&
((s = h.next) == null || s.thread != Thread.currentThread());```


