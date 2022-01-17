package org.lordy.designpattern.builder;

public abstract class CarBuilder {

    public abstract void buildWheel();

    public abstract void buildShell();

    public abstract void buildEngine();

    public abstract void buildSteeringWheel();

    public abstract Car getCar();
}
