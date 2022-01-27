package org.lordy.designpattern.decorator;

public class Test {

    public static void main(String[] args) {
        Shape shape = new Circle();
        ShapeDecorator redCircle = new RedShapeDecorator(new Circle());
        ShapeDecorator redRectangle = new RedShapeDecorator(new Rectangle());

        System.out.println("circle with normal border");
        shape.draw();

        System.out.println("red border circle");
        redCircle.draw();

        System.out.println("red border rectangle");
        redRectangle.draw();
    }
}
