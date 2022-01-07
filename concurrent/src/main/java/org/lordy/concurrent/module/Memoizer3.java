package org.lordy.concurrent.module;

import java.util.Map;
import java.util.concurrent.*;

/**
 * 首先检查某个相应的计算是否已经开始 如果还没有启动  那么创建一个FutureTask 注册到Map中 然后启动计算 如果已经启动 那么等待现有计算结果
 *
 * 仍然存在线程计算出相同值得漏洞  因为if代码块仍然是非原子的“先检查再执行”  因此两个线程仍有可能在同一时间内调用compute来计算相同的值
 * @param <A>
 * @param <V>
 */
public class Memoizer3<A,V> implements Computable<A,V> {

    private final Map<A, Future<V>> cache = new ConcurrentHashMap<A, Future<V>>();

    private final Computable<A, V> c;

    public Memoizer3(Computable<A, V> c) {
        this.c = c;
    }

    public V compute(final A arg) throws InterruptedException {
        Future<V> f = cache.get(arg);
        if(f == null){
            Callable<V> eval = new Callable<V>() {
                public V call() throws Exception {
                    return c.compute(arg);
                }
            };
            FutureTask<V> ft = new FutureTask<V>(eval);
            f = ft;
            cache.put(arg, ft);
            ft.run();
        }
        try {
            return f.get();
        }catch (ExecutionException e){
            throw launderThrowable(e.getCause());
        }
    }

    public RuntimeException launderThrowable(Throwable t){
        if(t instanceof  RuntimeException){
            return (RuntimeException) t;
        }else if (t instanceof Error){
            throw (Error) t;
        }else {
            throw new IllegalStateException(t);
        }
    }
}
