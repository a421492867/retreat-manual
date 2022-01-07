package org.lordy.concurrent.module;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 用ConcurrentHashMap代替1中的HashMap 比1有更好的并发行为 ： 多个线程可以并发地使用
 * 当两个线程同时调用compute时，可能会导致计算得到相同的数值  缓存的作用是避免相同数据被计算多次
 * 如果当某个线程启动了一个开销很大的计算 而其他线程并不知道这个计算正在进行 那么很可能会重复这个计算  引入FutureTask
 * @param <A>
 * @param <V>
 */
public class Memoizer2<A,V> implements Computable<A, V> {

    private final Map<A, V> cache = new ConcurrentHashMap<A, V>();
    private final Computable<A, V> c;

    public Memoizer2(Computable<A, V> c) {
        this.c = c;
    }

    public V compute(A arg) throws InterruptedException{
        V result = cache.get(arg);
        if(result == null){
            result = c.compute(arg);
            cache.put(arg, result);
        }
        return result;
    }
}
