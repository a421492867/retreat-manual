package org.lordy.concurrent.combination.example;

import net.jcip.annotations.NotThreadSafe;

@NotThreadSafe
public class MutablePoint {

    public int x, y;

    public MutablePoint() {
        this.x = 0;
        this.y = 0;
    }

    public MutablePoint(MutablePoint p){
        this.x = p.x;
        this.y = p.y;
    }
}
