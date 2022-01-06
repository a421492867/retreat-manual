package org.lordy.concurrent.combination;

import net.jcip.annotations.NotThreadSafe;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 委托失效
 */
@NotThreadSafe
public class NumberRange {

    //lower <= upper
    private final AtomicInteger lower = new AtomicInteger(0);
    private final AtomicInteger upper = new AtomicInteger(0);

    public void setLower(int i){
        //不安全的  先检查后执行
        if(i > upper.get()){
            throw new IllegalArgumentException("");
        }
        lower.set(i);
    }

    public void setUpper(int i){
        if(i < lower.get()){
            throw new IllegalArgumentException("");
        }
        upper.set(i);
    }

    public boolean isInRange(int i){
        return (i >= lower.get() && i <= upper.get());
    }
}
