package org.lordy.concurrent;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Test {

    static ReentrantLock reentrantLock = new ReentrantLock();

    ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    public static void main(String[] args) throws Exception{
        reentrantLock.lock();
        reentrantLock.lockInterruptibly();
    }
}
