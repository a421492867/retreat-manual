package org.lordy.designpattern.abstractFactory;

public class Factory2 extends AbstractFactory{

    @Override
    public Color getColor() {
        return new Green();
    }

    @Override
    public Shape getShape() {
        return new Rectangle();
    }
}
