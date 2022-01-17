package org.lordy.concurrent.cancleandclose.example;

import java.util.concurrent.*;

/**
 * 使用Future来取消任务
 */
public class TimedRun {

    private static final ExecutorService taskExec = Executors.newCachedThreadPool();

    public static void timedRun(Runnable r, long timeout, TimeUnit unit) throws InterruptedException{
        Future<?> task = taskExec.submit(r);
        try {
            task.get(timeout, unit);
        }catch (TimeoutException e){
            //task will be cancelled below
        }catch (ExecutionException e){
            throw launderThrowable(e.getCause());
        }finally {
            task.cancel(true);
        }
    }

    public static RuntimeException launderThrowable(Throwable t){
        if(t instanceof  RuntimeException){
            return (RuntimeException) t;
        }else if (t instanceof Error){
            throw (Error) t;
        }else {
            throw new IllegalStateException(t);
        }
    }
}
