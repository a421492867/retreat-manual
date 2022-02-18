package org.lordy.concurrent;

import java.util.concurrent.ThreadPoolExecutor;

public class Pool {

    /**
     * Executor 将任务提交和任务执行进行解耦
     */
    /**
     * ExecutorService接口增加了一些能力
     * 1、扩充执行任务的能力，补充可以为一个或一批异步任务生成Future的方法
     * 2、提供了管控线程池的方法，比如停止线程池的运行
     */
    /**
     * AbstractExecutorService
     * 将执行任务的流程串联了起来，保证下层的实现只需关注一个执行任务的方法即可
     */
    /**
     * ThreadPoolExecutor
     * 实现最复杂的运行部分，ThreadPoolExecutor将会一方面维护自身的生命周期，另一方面同时管理线程和任务，使两者良好的结合从而执行并行任务
     */
    ThreadPoolExecutor threadPoolExecutor;
}
