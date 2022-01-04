package org.lordy.concurrent.shared;

/**
 * 可能会无限循环 因为线程可能永远读不到ready的值
 *
 * 也有可能会输出 0  因为线程可能读到了ready的值，但是却没有看到写入的num的值 这种现象被称为 “重排序”
 *
 * 失效数据
 */
public class NoVisibility {

    private static boolean ready;

    private static int num;

    private static class ReaderThread extends Thread{
        public void run(){
            while (!ready){
                Thread.yield();
            }
            System.out.println(num);
        }
    }

    public static void main(String[] args) {
        new ReaderThread().start();
        num = 42;
        ready = true;
    }
}
