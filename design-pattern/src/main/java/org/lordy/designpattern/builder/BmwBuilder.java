package org.lordy.designpattern.builder;

public class BmwBuilder extends CarBuilder{

    private Car car = new Car();

    public void buildWheel() {
        car.setWheel("bmw");
    }

    public void buildShell() {
        car.setShell("bmw");
    }

    public void buildEngine() {
        car.setEngine("bmw");
    }

    public void buildSteeringWheel() {

        car.setSteeringWheel("bmw");
    }

    public Car getCar() {
        return car;
    }
}
