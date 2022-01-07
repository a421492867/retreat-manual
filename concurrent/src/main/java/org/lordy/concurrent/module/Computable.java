package org.lordy.concurrent.module;

public interface Computable<A, V> {
    V compute(A arg) throws InterruptedException;


}
