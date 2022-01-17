package org.lordy.designpattern.prototype;

public class Circle extends Shape{

    public Circle() {
        type = "circle";
    }

    @Override
    void draw() {
        System.out.println("circle");
    }
}
