package org.lordy.concurrent;


import org.openjdk.jol.info.ClassLayout;

public class LockUpgrade {

    public static void main(String[] args) throws Exception{
        User userTemp = new User();
        System.out.println("无状态" + ClassLayout.parseInstance(userTemp).toPrintable());

        /**
         * JVM默认延时4s自动开启偏向锁
         * -XX:BiasedLockingStartupDelay=0取消延时
         * -XX:-UseBiasedLocking=false 取消偏向锁
         */
        Thread.sleep(5000);
        User user = new User();
        System.out.println("偏向锁 " + ClassLayout.parseInstance(user).toPrintable());

        for(int i = 0; i < 2; i++){
            synchronized (user){
                System.out.println("偏向锁 线程Id" + ClassLayout.parseInstance(user).toPrintable());
            }
            System.out.println("释放偏向锁 线程Id" + ClassLayout.parseInstance(user).toPrintable());
        }

        new Thread(() ->{
            synchronized (user){
            System.out.println("轻量级锁" + ClassLayout.parseInstance(user).toPrintable());
            try {
                Thread.sleep(3000);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
            System.out.println("轻量 -> 重量" + ClassLayout.parseInstance(user).toPrintable());
        }
        }).start();
        Thread.sleep(1000);
        new Thread(() ->{
            synchronized (user){
                System.out.println("重量级锁" + ClassLayout.parseInstance(user).toPrintable());
            }
        }).start();
    }
}
