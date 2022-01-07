package org.lordy.concurrent.module;

import java.math.BigInteger;

public class ExpensiveFunction implements Computable<String, BigInteger> {
    public BigInteger compute(String arg) throws InterruptedException {
        //在经过很长时间计算后
        return new BigInteger(arg);
    }
}
