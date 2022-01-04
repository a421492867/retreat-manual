package org.lordy.concurrent.shared;

/**
 * 不正确的发布
 */
public class Holder {

    private int n;

    public Holder(int n) {
        this.n = n;
    }

    public void assertSanity(){
        if(n != n){
            throw new AssertionError("This statement is false.");
        }
    }

    public static void main(String[] args) {
        for(int i = 0; i < 1000; i++){
            final int finalI = i;
            new Thread(new Runnable() {
                public void run() {
                    Holder holder = new Holder(finalI);
                    holder.assertSanity();
                }
            }).start();
        }
    }
}
