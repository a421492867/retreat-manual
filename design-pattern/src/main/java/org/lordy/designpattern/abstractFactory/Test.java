package org.lordy.designpattern.abstractFactory;

public class Test {

    public static void main(String[] args) {
        AbstractFactory factory = FactoryProducer.getFactory(1);
        factory.getColor().fill();
        factory.getShape().draw();
    }



}
