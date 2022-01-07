package org.lordy.concurrent.module;

import java.util.concurrent.CountDownLatch;

/**
 * 在启动门 startGate上等待 确保所有线程都就绪才开始执行
 * 每个线程最后要做一件事情是调用结束门countDown - 1 使主线程等待所有工作线程都执行完成
 */
public class TestHarness {

    public static long timeTasks(int nThreads, final Runnable task)  throws InterruptedException{
        final CountDownLatch startGate = new CountDownLatch(1);
        final CountDownLatch endGate = new CountDownLatch(nThreads);
        for(int i = 0; i < nThreads; i++){
            new Thread(new Runnable() {
                public void run() {
                    try {
                        startGate.await();
                        try {
                            task.run();
                        }finally {
                            endGate.countDown();
                        }
                    }catch (InterruptedException ignored){}
                }
            }).start();
        }
        System.out.println("线程创建完毕");
        long start = System.nanoTime();
        startGate.countDown();
        endGate.await();
        long end = System.nanoTime();
        System.out.println(end - start);
        return end - start;
    }

    public static void main(String[] args) throws Exception{
        timeTasks(10, new Runnable() {
            public void run() {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName() + "运行完毕");
            }
        });
    }
}
