package org.lordy.concurrent.taskexecution;

import java.util.concurrent.Executor;

/**
 * 在调用线程中以同步方式执行所有任务的Executor
 */
public class WithinThreadExecutor implements Executor {

    @Override
    public void execute(Runnable command) {
        command.run();
    }
}
