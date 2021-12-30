package org.lordy.concurrent.safety;

import net.jcip.annotations.ThreadSafe;

import java.math.BigInteger;

/**
 * 无状态
 * 不包含域 也没有引用其他的域
 * 线程之间相互不共享状态
 *
 * 无状态对象永远是线程安全的
 */
@ThreadSafe
public class StatelessFactorizer {

    public void service(BigInteger bigInteger){
        BigInteger[] factors = factor(bigInteger);
    }

    public BigInteger[] factor(BigInteger bigInteger){
        return new BigInteger[]{BigInteger.ZERO, BigInteger.ZERO};
    }


}
