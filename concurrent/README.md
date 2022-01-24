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
### AQS
> head  tail  thread waitstatus
> 
```Node t = tail; // Read fields in reverse initialization order
Node h = head;
Node s;
return h != t &&
((s = h.next) == null || s.thread != Thread.currentThread());```