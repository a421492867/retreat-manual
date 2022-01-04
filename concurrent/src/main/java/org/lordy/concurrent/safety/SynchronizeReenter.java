package org.lordy.concurrent.safety;

/**
 * 验证可重入
 * 若不可重入  则会发生死锁
 *
 * 每个doSomething方法都会获得Widget上的锁
 */
public class SynchronizeReenter {

    static class Widget{
        synchronized void doSomething(){
            System.out.println("super");
        }
    }

    static class LoggingWidget extends Widget{
        @Override
        synchronized void doSomething() {
            System.out.println("subClass");
            super.doSomething();
        }
    }

    public static void main(String[] args) {
        LoggingWidget loggingWidget = new LoggingWidget();
        loggingWidget.doSomething();
    }
}
