package org.lordy.concurrent.safety;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 服务响应性非常低
 */
@ThreadSafe
public class SynchronizedFactorizer {

    @GuardedBy("this") private final AtomicReference<BigInteger> lastNumber = new AtomicReference<BigInteger>();

    @GuardedBy("this") private final AtomicReference<BigInteger[]> lastFactors = new AtomicReference<BigInteger[]>();

    public synchronized void service(BigInteger bigInteger){
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
