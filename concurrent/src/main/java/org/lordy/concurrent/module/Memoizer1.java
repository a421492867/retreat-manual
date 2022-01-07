package org.lordy.concurrent.module;

import net.jcip.annotations.GuardedBy;

import java.util.HashMap;
import java.util.Map;

/**
 * HashMap保存之前计算的结果  compute方法先检查需要的结果是否已经在缓存中 如果在则返回之前计算的值 否则 把计算结果缓存在HashMap中 然后返回
 * 通过 synchronized 同步 每次只有一个线程能执行compute 如果另外一个线程正在计算结果 那么其他调用compute的线程很可能被阻塞很长时间
 * @param <A>
 * @param <V>
 */
public class Memoizer1<A, V> implements Computable<A, V> {

    @GuardedBy("this")
    private final Map<A,V> cache = new HashMap<A, V>();
    private final Computable<A, V> c;

    public Memoizer1(Computable<A, V> c) {
        this.c = c;
    }

    public synchronized V compute(A args) throws InterruptedException{
        V result = cache.get(args);
        if(result == null){
            result = c.compute(args);
            cache.put(args, result);
        }
        return result;
    }
}
