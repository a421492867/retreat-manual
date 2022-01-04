package org.lordy.concurrent.safety;

import net.jcip.annotations.NotThreadSafe;

import java.math.BigInteger;

/**
 * ++操作并非原子的
 * 存在竞态条件 线程不安全
 * 读取-修改-写入 的竞态条件
 */
@NotThreadSafe
public class UnsafeCountingFactorizer {

    private long count = 0;

    public long getCount() {
        return count;
    }

    public void service(BigInteger bigInteger){
        BigInteger[] factors = factor(bigInteger);
        count++;
    }

    public BigInteger[] factor(BigInteger bigInteger){
        return new BigInteger[]{BigInteger.ZERO, BigInteger.ZERO};
    }
}
