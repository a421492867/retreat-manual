package org.lordy.designpattern.bridge;

public class Test {

    public static void main(String[] args) {
        Shape redCircle = new Circle(new RedCircle(), 100, 100, 10);
        Shape greenCircle = new Circle(new GreenCircle(), 100, 100, 10);

        redCircle.draw();
        greenCircle.draw();
    }
}
