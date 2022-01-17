package org.lordy.designpattern.builder;

public class BenzBuilder extends CarBuilder{

    private Car car = new Car();

    public void buildWheel() {
        car.setWheel("benz");
    }

    public void buildShell() {
        car.setShell("benz");
    }

    public void buildEngine() {
        car.setEngine("benz");
    }

    public void buildSteeringWheel() {
        car.setSteeringWheel("benz");
    }

    public Car getCar() {
        return car;
    }
}
