package org.lordy.designpattern.prototype;

/**
 * Basic
 */
public class Realizetype implements Cloneable{

    public Realizetype(){}

    @Override
    protected Object clone() throws CloneNotSupportedException {
        System.out.println("clone");
        return (Realizetype)super.clone();
    }

    static class Test{
        public static void main(String[] args) throws CloneNotSupportedException{
            Realizetype o1 = new Realizetype();
            Realizetype o2 = (Realizetype) o1.clone();
            System.out.println(o1 == o2);
        }
    }
}
