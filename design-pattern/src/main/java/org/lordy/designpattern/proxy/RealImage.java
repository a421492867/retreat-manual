package org.lordy.designpattern.proxy;

public class RealImage implements Image{

    private String fileName;

    public RealImage(String fileName) {
        this.fileName = fileName;
        loadFromDisk(fileName);
    }

    public void display() {
        System.out.println("display " + fileName);
    }

    private void loadFromDisk(String fileName){
        System.out.println("loading " + fileName);
    }
}
