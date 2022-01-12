package org.lordy.concurrent.taskexecution;

import java.util.concurrent.Executor;

/**
 * 为每一个请求启动一个新线程的Executor
 */
public class ThreadPerTaskExecutor implements Executor {

    @Override
    public void execute(Runnable command) {
        new Thread(command).start();
    }
}
