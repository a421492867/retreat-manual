package org.lordy.concurrent.safety;

import net.jcip.annotations.NotThreadSafe;

import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 无法同时更新lastNumber 和 lastFactors 如果只修改了其中一个变量，不变性条件被破坏；同样我们不能保证同时获取两个值
 */
@NotThreadSafe
public class UnsafeCachingFactorizer {

    private final AtomicReference<BigInteger> lastNumber = new AtomicReference<BigInteger>();

    private final AtomicReference<BigInteger[]> lastFactors = new AtomicReference<BigInteger[]>();

    public void service(BigInteger bigInteger){
        if(bigInteger.equals(lastNumber.get())){
            return;
        }
        BigInteger[] factors = factoizer(bigInteger);
        lastNumber.set(bigInteger);
        lastFactors.set(factors);
    }

    public BigInteger[] factoizer(BigInteger bigInteger){
        return new BigInteger[]{BigInteger.ZERO, BigInteger.ZERO};
    }
}
