package org.lordy.concurrent;

import java.util.concurrent.locks.ReentrantLock;

public class Test {

    static ReentrantLock reentrantLock = new ReentrantLock();

    public static void main(String[] args) {
        reentrantLock.lock();
    }
}
