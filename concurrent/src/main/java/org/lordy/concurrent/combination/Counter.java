package org.lordy.concurrent.combination;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

/**
 * 域只有value 因此这个域就是Counter的所有状态
 */
@ThreadSafe
public final class Counter {

    @GuardedBy("this") private long value = 0;

    public synchronized long getValue() {
        return value;
    }

    public synchronized long increment(){
        if(value == Long.MAX_VALUE){
            throw new IllegalStateException("");
        }
        return ++value;
    }

    public static void main(String[] args) {
        Counter counter = new Counter();
        System.out.println(counter.increment());
    }
}
