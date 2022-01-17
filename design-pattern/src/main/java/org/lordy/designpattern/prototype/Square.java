package org.lordy.designpattern.prototype;

public class Square extends Shape{

    public Square() {
        type = "square";
    }


    @Override
    void draw() {
        System.out.println("square");
    }
}
