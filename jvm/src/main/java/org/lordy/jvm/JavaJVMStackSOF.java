package org.lordy.jvm;

/**
 * JVM args -Xss160k
 */
public class JavaJVMStackSOF {

    private int stackLength = 1;

    public void stackLeak(){
        stackLength++;
        stackLeak();
    }

    public static void main(String[] args) throws Throwable {
        JavaJVMStackSOF sof = new JavaJVMStackSOF();
        try {
            sof.stackLeak();
        }catch (Throwable e){
            System.out.println(sof.stackLength);
            throw e;
        }
    }
}
