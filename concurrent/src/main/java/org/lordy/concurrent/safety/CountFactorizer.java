package org.lordy.concurrent.safety;

import net.jcip.annotations.ThreadSafe;

import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * java.util.concurrent.atomic中包含一些原子变量类 用于实现在数值和对象引用上的原子状态转换
 *
 * 在无状态类中添加一个状态时， 如果该状态完全由线程安全的对象来管理，那么这个类仍然是线程安全的
 */
@ThreadSafe
public class CountFactorizer {

    private final AtomicLong count = new AtomicLong(0);

    public AtomicLong getCount() {
        return count;
    }

    public void service(BigInteger bigInteger){
        BigInteger[] factors = factor(bigInteger);
        count.incrementAndGet();
    }

    public BigInteger[] factor(BigInteger bigInteger){
        return new BigInteger[]{BigInteger.ZERO, BigInteger.ZERO};
    }
}
