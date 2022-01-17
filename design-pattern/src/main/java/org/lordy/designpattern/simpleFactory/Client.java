package org.lordy.designpattern.simpleFactory;

public class Client {

    public static void main(String[] args) {
        Product product = SimpleFactory.makeProduct(Const.PRODUCT_A);
        product.show();
    }

    public interface Product{
        void show();
    }

    static class ConcreteProduct1 implements Product{
        public void show() {
            System.out.println("Product1");
        }
    }

    static class ConcreteProduct2 implements Product {
        public void show() {
            System.out.println("具体产品2显示...");
        }
    }

    final class Const {
        static final int PRODUCT_A = 0;
        static final int PRODUCT_B = 1;
        static final int PRODUCT_C = 2;
    }

    static class SimpleFactory {
        public static Product makeProduct(int kind) {
            switch (kind) {
                case Const.PRODUCT_A:
                    return new ConcreteProduct1();
                case Const.PRODUCT_B:
                    return new ConcreteProduct2();
            }
            return null;
        }
    }
}
