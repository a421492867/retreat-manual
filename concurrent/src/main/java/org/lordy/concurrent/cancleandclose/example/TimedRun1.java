package org.lordy.concurrent.cancleandclose.example;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 破坏了以下规则
 * 在中断线程之前 应该了解它的中断策略
 */
public class TimedRun1 {

    private static final ScheduledExecutorService cancelExec = Executors.newScheduledThreadPool(1);

    public static void timedRun(Runnable r, long timeout, TimeUnit unit){
        final Thread taskThread = Thread.currentThread();
        cancelExec.schedule(() -> taskThread.interrupt(), timeout, unit);
        r.run();
    }
}
