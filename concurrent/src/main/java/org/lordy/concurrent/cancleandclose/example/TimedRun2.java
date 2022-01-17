package org.lordy.concurrent.cancleandclose.example;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 在专门的线程中 中断任务
 */
public class TimedRun2 {

    private static final ScheduledExecutorService cancelExec = Executors.newScheduledThreadPool(1);

    public static void timedRun(final Runnable r, long timeout, TimeUnit unit) throws InterruptedException{
        class RethrowableTask implements Runnable{
            private volatile Throwable t;

            @Override
            public void run() {
                try {
                    r.run();
                }catch (Throwable t){
                    this.t = t;
                }
            }

            void rethorw(){
                if(t != null){
                    throw launderThrowable(t);
                }
            }

            public RuntimeException launderThrowable(Throwable t){
                if(t instanceof  RuntimeException){
                    return (RuntimeException) t;
                }else if (t instanceof Error){
                    throw (Error) t;
                }else {
                    throw new IllegalStateException(t);
                }
            }
        }

        RethrowableTask task = new RethrowableTask();
        final Thread taskThread = new Thread(task);
        taskThread.start();
        cancelExec.schedule(() -> taskThread.interrupt(), timeout, unit);
        taskThread.join(unit.toMillis(timeout));
        task.rethorw();
    }
}
