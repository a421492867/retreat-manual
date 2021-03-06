### 任务执行
> 任务通常是一些抽象的且离散的工作单元
> 
> Executor框架   任务执行抽象的不是Thread 而是Executor
- Executor基于生产者-消费者模式,提交任务的操作相当于生产者 执行任务的线程相当于消费者
>
> 执行策略: what where when how
- 在什么（what）线程中执行任务
- 任务按照什么（what）顺序执行（FIFO、LIFO、优先级）
- 有多少个（how many）任务能并发执行
- 在队列中有多少个（how many）任务在等待执行
- 如果系统由于负载而需要拒绝一个任务，那么应该选择哪一个（which）任务 如何（how）通知应用程序有任务被拒绝
- 在执行一个任务之前或之后，应该进行哪些（what）动作

> 线程池  是指管理一组同构工作线程的资源池  线程池是与工作队列密切相关的,其中在工作队列中保存了所有等待执行的任务  工作者线程从工作队列获取一个任务 执行任务 然后返回线程池并等待下一个任务
> 
> 在线程池中执行任务  比   为每一个任务分配一个线程  优势更多:
- 通过重用现有的线程而不是创建新线程 可以在处理多个请求时分摊在线程创建和销毁过程中产生的巨大开销
- 当请求达到时,工作线程通常已经存在 因此不会犹由于等待创建线程而延迟任务的执行 从而提高了响应性
- 通过适当调整线程池大小  可以创建足够多的线程以便使处理器保持忙碌状态 还可以防止过多线程相互竞争资源而使应用程序耗尽内存或失败

> 通过调用Executors中的静态工厂方法之一来创建一个线程池:
- newFixedThreadPool 创建一个固定长度的线程池 当每次提交一个任务时就会创建一个线程 直到达到线程池的最大数量 这个线程池的规模将不再变化（如果某个线程由于发生了未预期的Exception而结束 那么线程池会补充一个新的线程）
- newCachedThreadPool 创建一个可缓存的线程池 如果线程池的当前规模超过了处理需求时，那么将回收空闲的线程 需求增加时 则可以添加新的线程 线程池的规模不存在任何限制
- newSingleThreadExecutor 单线程Executor 创建单个工作者线程来执行任务 如果这个线程异常结束 会创建另一个线程来代替  能确保依照任务在队列的顺序来串行执行（FIFO、LIFO、优先级）
- newScheduledThreadPool 创建了一个固定长度的线程池 而且以延迟或定时的方式来执行任务  类似于Timer


> Executor的生命周期  ExecutorService接口
- ExecutorService生命周期有3中状态： 运行、关闭和已终止
- ExecutorService在初始创建时处于运行状态
- shutdown方法将执行平缓的关闭过程： 不再接受新的任务，同时等待已经提交的任务执行完成（包括那些还未开始执行的任务）  shutdownNow方法将执行粗暴的关闭过程： 它将尝试取消所有运行中的任务，并且不再启动队列中尚未开始执行的任务
- 在ExecutorService关闭后提交的任务将由“拒绝执行处理器”来处理，它会抛弃任务，或者使得execute方法抛出一个未检查的RejectedExecutionException。等待所有任务都完成后，ExecutorService将转入终止状态 可以调用awaitTermination来等待ExecutorService到达终止状态，或者通过调用isTerminated来轮训ExecutorService是否已经终止  通常在调用awaitTermination之后会立即调用shutdown 从而产生同步的关闭ExecutorService的效果

### 延迟任务与周期任务
> Timer 负责管理延迟任务或周期任务 存在一些缺陷 考虑使用ScheduledThreadPoolExecutor来代替
> Timer在执行所有定时任务时只会创建一个线程 如果某个任务的执行时间长 那么将破坏其他TimerTask的定时精确性； TimerTask如果抛出一个未检查的异常 Timer线程并不捕获异常 将终止定时线程 这种情况下 Timer也不会恢复线程的执行 错误地认为整个Timer被取消 因此 已经被调度但尚未执行的TimerTask将不会再执行 新的任务也不能被调度（线程泄露）
> 
> Runnable和Callable描述的都是抽象的计算任务。这些任务通常是有范围的 即都有一个明确的起始点 并且最终都会结束  
> Executor执行任务有4个生命周期阶段： 创建、提交、开始和完成  在Executor框架中 已提交但尚未开始的任务都可以取消 对于那些已经开始执行的任务 只有它们能响应中断时才能取消
> Future表示一个任务的生命周期 并提供了相应的方法判断是否已经完成或者取消 以及获取任务的结果或取消任务等
> 
> CompletionService：Executor与BlockingQueue
> CompletionService将Executor和BlockingQueue的功能融合在一起 可以将Callable任务提交给它执行 使用类似队列操作的take和poll等方法来获得已完成的结果 这些结果会再完成时封装成为Future
> ExecutorCompletionService实现了CompletionService  在构造函数中创建一个BlockingQueue来保存计算完成的结果 当计算完成时 调用FutureTask的done方法  当提交某个任务时 该任务首先包装称谓一个QueueingFuture 在改写done方法 并将结果放入BlockingQueue