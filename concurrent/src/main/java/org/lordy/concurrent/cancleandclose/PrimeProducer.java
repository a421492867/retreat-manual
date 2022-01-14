package org.lordy.concurrent.cancleandclose;

import java.math.BigInteger;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * 两个位置可以检测中断 ：
 * 1、在阻塞的put方法调用中 put方法时阻塞的  隐式的检测
 * 2、循环开始处
 */
public class PrimeProducer extends Thread{

    private final BlockingQueue<BigInteger> queue;

    public PrimeProducer(BlockingQueue<BigInteger> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        try {
            BigInteger p = BigInteger.ONE;
            while (!Thread.currentThread().isInterrupted()){
                queue.put(p = p.nextProbablePrime());
            }
        }catch (InterruptedException e){
            System.out.println("interrupt");
        }
    }

    public void cancel(){
        interrupt();
    }

    public static void main(String[] args) throws InterruptedException{
        BlockingQueue<BigInteger> queue = new LinkedBlockingQueue<>();
        PrimeProducer primeProducer = new PrimeProducer(queue);
        primeProducer.start();
        try {
            TimeUnit.SECONDS.sleep(1);
        }finally {
            primeProducer.cancel();
        }

    }
}
