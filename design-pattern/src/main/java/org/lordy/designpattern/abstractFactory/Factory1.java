package org.lordy.designpattern.abstractFactory;

public class Factory1 extends AbstractFactory{

    @Override
    public Shape getShape() {
        return new Circle();
    }

    @Override
    public Color getColor() {
        return new Red();
    }
}
