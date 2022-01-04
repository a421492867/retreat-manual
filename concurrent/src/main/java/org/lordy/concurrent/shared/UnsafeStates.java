package org.lordy.concurrent.shared;

/**
 * 逸出（发布了本应为私有的状态数组）
 *
 * 任何调用者都可以修改这个数组的内容
 *
 */
public class UnsafeStates {

    private String[] states = new String[]{"AK", "AL"};

    public String[] getStates() {
        return states;
    }


    public static void main(String[] args) {
        UnsafeStates unsafeStates = new UnsafeStates();
        String[] arr = unsafeStates.getStates();
        arr[0] = "AT";
        System.out.println(unsafeStates.getStates()[0]);
    }
}
