package org.lordy.concurrent.cancleandclose;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 让素数生成器运行1秒后取消  素数生成器通常不会刚好在运行1秒钟之后停止  因为在请求取消的时刻和run方法中循环执行下一次检查之间可能存在延迟
 * cancel方法由finally块调用 从而确保即使在调用sleep时被中断也能取消素数生成器的执行
 * 如果cancel没有被调用 那么搜索素数的线程将永远运行下去，不断消耗CPU的时钟周期 并使得JVM不能正常退出
 */
@ThreadSafe
public class PrimeGenerator implements Runnable {

    @GuardedBy("this")
    private final List<BigInteger> primes = new ArrayList<>();

    // volatile必须
    private volatile boolean cancelled;

    @Override
    public void run() {
        BigInteger p = BigInteger.ONE;
        while (!cancelled){
            p = p.nextProbablePrime();
            synchronized (this){
                primes.add(p);
            }
        }
    }

    public void cancel(){
        cancelled = true;
    }

    public synchronized List<BigInteger> get(){
        return new ArrayList<>(primes);
    }

    public static void main(String[] args) throws InterruptedException{
        PrimeGenerator primeGenerator = new PrimeGenerator();
        new Thread(primeGenerator).start();
        try {
            TimeUnit.SECONDS.sleep(1);
        } finally {
            primeGenerator.cancel();
        }
        List<BigInteger> list = primeGenerator.get();
        System.out.println(list.size());
    }
}
