package org.lordy.concurrent.cancleandclose;

import java.math.BigInteger;
import java.util.concurrent.BlockingQueue;

/**
 * 生产者的速度超过消费者的处理速度 队列将被填满 put方法会阻塞
 * 当调用cancel方法来设置时 此时生产者永远不能检查这个标志 因为它无法从阻塞的put方法中恢复过来
 */
public class BrokenPrimeProducer extends Thread{

    private final BlockingQueue<BigInteger> queue;

    private volatile boolean cancelled = false;

    public BrokenPrimeProducer(BlockingQueue<BigInteger> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        try {
            BigInteger p = BigInteger.ONE;
            while (!cancelled){
                queue.put(p = p.nextProbablePrime());
            }
        }catch (InterruptedException e){}
    }
}
