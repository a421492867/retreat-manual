package org.lordy.designpattern.builder;

public class Test {



    public static void main(String[] args) {
        CarDirector carDirector = new CarDirector(new BenzBuilder());
        Car car = carDirector.build();
        System.out.println(car.getEngine());
    }
}
