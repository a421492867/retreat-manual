package org.lordy.concurrent.shared;

import net.jcip.annotations.NotThreadSafe;

/**
 * 如果某个线程调用了set 另一个正在调用get的线程可能会看到更新后的value 也可能看不到
 */
@NotThreadSafe
public class MutableInteger {

    private int value;

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
