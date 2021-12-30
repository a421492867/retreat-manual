package org.lordy.concurrent;

import net.jcip.annotations.NotThreadSafe;

/**
 * 若执行时机不对 两个线程在调用getNext时会得到相同的值
 * value++并不是单个操作
 * 读取value value + 1 结果写入value
 */
@NotThreadSafe
public class UnsafeSequence {

    private int value;

    public int getNext(){
        return value++;
    }

}
