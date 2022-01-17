package org.lordy.designpattern.prototype;

public class PrototypePatternDemo {

    public static void main(String[] args) throws CloneNotSupportedException {
        ShapeCache.loadCache();

        Shape clonedShape =  ShapeCache.getShape("1");
        System.out.println(clonedShape.getType());

        Shape clonedShape2 = ShapeCache.getShape("2");
        System.out.println("Shape : " + clonedShape2.getType());

        Shape clonedShape3 = ShapeCache.getShape("3");
        System.out.println("Shape : " + clonedShape3.getType());
    }
}
