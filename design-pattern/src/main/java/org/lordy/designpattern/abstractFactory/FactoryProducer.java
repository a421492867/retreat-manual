package org.lordy.designpattern.abstractFactory;

public class FactoryProducer {

    public static AbstractFactory getFactory(Integer choice){
        if(choice == 1){
            return new Factory1();
        }
        return new Factory2();
    }
}
