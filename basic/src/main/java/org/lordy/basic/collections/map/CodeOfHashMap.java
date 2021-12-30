package org.lordy.basic.collections.map;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 散列表实现
 * 扰动函数
 * 初始化容量
 * 负载因子
 * 扩容元素拆分
 * 链表树化
 * 红黑树
 * 插入
 * 查找
 * 删除
 * 遍历
 * 分段锁
 */
public class CodeOfHashMap {

    /**
     * 若元素位置不够 散列碰撞严重 失去散列表存放的意义
     * 需要数组长度是2的倍数 怎么进行初始化这个数组大小
     * 数组越小 碰撞越大 数组越大 碰撞越小 时间空间如何取舍
     * 链表越来越长怎么优化
     * 随着元素的不断添加 数组长度不足扩容时 怎么把原有的元素拆分到新位置上去
     */
    public static void easyMap(List<String> list){
        String[] tab = new String[8];
        for(String key : list){
            int idx = key.hashCode() & (tab.length - 1);
            System.out.printf("key : %s Idx : %d%n", key, idx);
            if(tab[idx] == null){
                tab[idx] = key;
                continue;
            }
            tab[idx] = tab[idx] + "->" + key;
        }
        System.out.println(JSON.toJSONString(tab));
    }

    public static void main(String[] args) {
        List<String> list = new ArrayList<String>();
        list.add("abc");
        list.add("def");
        list.add("ghi");
        list.add("jkl");
        list.add("mno");
        list.add("pqr");
        list.add("stu");
        list.add("vwx");
        list.add("yz");
        easyMap(list);
        Map<String, Object> map = new HashMap<String, Object>();

        /**
         * 扰动函数
         * 混合原哈希值中的高位和低位 增大了随机性
         * static final int hash(Object key) {
         *         int h;
         *         return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
         *     }
         */


        /**
         * threshold 阈值 通过tableSizeFor计算
         * 寻找比初始值大 最小的2进制数 比如传了17 找到的是32
         * public HashMap(int initialCapacity, float loadFactor) {
         *         if (initialCapacity < 0)
         *             throw new IllegalArgumentException("Illegal initial capacity: " +
         *                                                initialCapacity);
         *         if (initialCapacity > MAXIMUM_CAPACITY)
         *             initialCapacity = MAXIMUM_CAPACITY;
         *         if (loadFactor <= 0 || Float.isNaN(loadFactor))
         *             throw new IllegalArgumentException("Illegal load factor: " +
         *                                                loadFactor);
         *         this.loadFactor = loadFactor;
         *         this.threshold = tableSizeFor(initialCapacity);
         *     }
         *
         *     主要为了把二进制各个位置都填上1 再把结果加1
         *     static final int tableSizeFor(int cap) {
         *         int n = cap - 1;
         *         n |= n >>> 1;
         *         n |= n >>> 2;
         *         n |= n >>> 4;
         *         n |= n >>> 8;
         *         n |= n >>> 16;
         *         return (n < 0) ? 1 : (n >= MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : n + 1;
         *     }
         */

        /**
         * 负载因子
         * static final float DEFAULT_LOAD_FACTOR = 0.75f;
         */

        /**
         * put流程
         * 1、首先进行哈希值的扰动 获取一个新的哈希值 (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16)
         * 2、判断tab是否为空或者长度为0 如果是则进行扩容操作 if ((tab = table) == null || (n = tab.length) == 0)
         *             n = (tab = resize()).length;
         * 3、根据哈希值计算下标  如果对应的下标刚好没有存放数据 则直接插入 否则需要覆盖    p = tab[i = (n - 1) & hash]) == null
         * 4、判断tab[i]是否为树节点 否则向链表中插入数据 是则向树中插入节点
         * 5、如果链表中插入节点的时候 链表长度大于等于8 则需要树化 treeifyBin(tab, hash);
         * 6、最后所有元素处理完成后 判断是否超过阈值 超过则扩容
         *
         * treeifyBin 是一个链表树化的方法 但不是所有的链表长度为8后都会转成树  还需要判断存放key值得数组桶长是否小于64  MIN_TREEIFY_CAPACITY 小于则扩容 扩容后链表长度缩短
         */
        map.put("key", "value");
    }
}
