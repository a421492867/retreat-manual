package org.lordy.concurrent.module;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Semaphore;

/**
 * 通过信号量时容器变为 有界阻塞容器
 * @param <T>
 */
public class BoundedHashSet<T> {

    private final Set<T> set;

    private final Semaphore sem;

    public BoundedHashSet(int bound) {
        this.set = Collections.synchronizedSet(new HashSet<T>());
        sem = new Semaphore(bound);
    }

    public boolean add(T o) throws InterruptedException{
        sem.acquire();
        boolean wasAdded = false;
        try {
            wasAdded = set.add(o);
            return wasAdded;
        }finally {
            if(!wasAdded) sem.release();
        }
    }

    public boolean remove(T t){
        boolean wasRemoved = set.remove(t);
        if(wasRemoved) sem.release();
        return wasRemoved;
    }
}
