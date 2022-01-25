package org.lordy.designpattern.bridge;

public class RedCircle implements DrawApi{

    public void drawCircle(int radius, int x, int y) {
        System.out.println("red , radius : " + radius + ", x : " + x + " , y : " + y);
    }
}
