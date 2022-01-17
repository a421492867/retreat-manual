package org.lordy.designpattern.builder;

public class CarDirector {

    private CarBuilder carBuilder;

    public CarDirector(CarBuilder carBuilder) {
        this.carBuilder = carBuilder;
    }

    public Car build(){
        carBuilder.buildEngine();
        carBuilder.buildShell();
        carBuilder.buildWheel();
        carBuilder.buildSteeringWheel();
        return carBuilder.getCar();
    }
}
