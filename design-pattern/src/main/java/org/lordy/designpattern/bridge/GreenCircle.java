package org.lordy.designpattern.bridge;

public class GreenCircle implements DrawApi{
    public void drawCircle(int radius, int x, int y) {
        System.out.println("green radius : " + radius + " x : " + x +" y : " + y);
    }
}
