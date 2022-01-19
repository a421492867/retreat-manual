package org.lordy.concurrent.activity;

import java.util.concurrent.CountDownLatch;

public class LeftRightDeadLock {

    private final Object left = new Object();
    private final Object right = new Object();

    public void leftRight() throws InterruptedException {
        synchronized (left){
            Thread.sleep(5);
            synchronized (right){
                doSomething();
            }
        }
    }

    public void rightLeft() throws InterruptedException {
        synchronized (right){
            Thread.sleep(5);
            synchronized (left){
                doSomethingElse();
            }
        }
    }


    private void doSomething(){
        System.out.println("doSomething");
    }

    private void doSomethingElse(){
        System.out.println("doSomethingElse");
    }

    public static void main(String[] args) {
        CountDownLatch countDownLatch = new CountDownLatch(1);

        LeftRightDeadLock leftRightDeadLock = new LeftRightDeadLock();
        new Thread(()-> {
            try {
                leftRightDeadLock.leftRight();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        new Thread(() -> {
            try {
                leftRightDeadLock.rightLeft();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

    }
}
