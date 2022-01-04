package org.lordy.concurrent.shared;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

/**
 * 若只对set进行同步 调用get的线程仍然可能看到失效值
 */
@ThreadSafe
public class SynchronizedInteger {

    @GuardedBy("this") private int value;

    public synchronized int getValue() {
        return value;
    }

    public synchronized void setValue(int value) {
        this.value = value;
    }
}
