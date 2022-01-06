package org.lordy.concurrent.combination;

import net.jcip.annotations.NotThreadSafe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 无论List用哪一个锁来保护它的状态 可以确定的是 这个锁并不是ListHelper上的锁
 * @param <E>
 */
@NotThreadSafe
public class ListHelper<E> {

    public List<E> list = Collections.synchronizedList(new ArrayList<E>());

    public synchronized boolean putIfAbsent(E e){
        boolean absent = !list.contains(e);
        if(absent){
            list.add(e);
        }
        return absent;
    }
}
