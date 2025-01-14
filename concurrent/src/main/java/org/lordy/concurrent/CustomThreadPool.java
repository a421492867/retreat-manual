package org.lordy.concurrent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class CustomThreadPool {

    private final BlockingQueue<Runnable> taskQueue;

    private final List<WorkerThread> workers;

    private volatile boolean isShutdown = false;

    public CustomThreadPool(int numThreads, int queueCapacity) {
        taskQueue = new ArrayBlockingQueue<Runnable>(queueCapacity);
        workers = new ArrayList<WorkerThread>(numThreads);
        for(int i = 0; i < numThreads; i++) {
            WorkerThread workerThread = new WorkerThread(taskQueue);
            workers.add(workerThread);
            workerThread.start();
        }
    }

    public void execute(Runnable task) {
        if(isShutdown) {
            throw new IllegalStateException("ThreadPool is closed");
        }
        try {
            taskQueue.put(task);
        }catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void shutdown(){
        isShutdown = true;
        for(WorkerThread worker : workers) {
            worker.interrupt();
        }
    }

    private static class WorkerThread extends Thread {

        private final BlockingQueue<Runnable> taskQueue;

        public WorkerThread(BlockingQueue<Runnable> workQueue) {
            this.taskQueue = workQueue;
        }

        @Override
        public void run() {
            while (!isInterrupted()) {
                try {
                    Runnable task = taskQueue.take();
                    task.run();
                }catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }catch (Exception e){

                }
            }
        }
    }

    public static void main(String[] args) {
        CustomThreadPool threadPool = new CustomThreadPool(5, 10);
        for(int i = 0; i < 20; i++){
            int taskNumber = i;
            threadPool.execute(() -> {
                System.out.println("Executing task " + taskNumber + " by " + Thread.currentThread().getName());
                try {
                    Thread.sleep(1000);
                }catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }
        threadPool.shutdown();
    }
}
