package org.lordy.concurrent.combination;

import net.jcip.annotations.GuardedBy;

public class PrivateLock {

    private final Object myLock = new Object();

    @GuardedBy("myLock") Widget widget;

    void someMethod(){
        synchronized (myLock){
            //修改widget
        }
    }

    private final class Widget{}
}
