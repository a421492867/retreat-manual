package org.lordy.basic.collections.map;

import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CodeOfConcurrentHashMap {
    //HashTable效率低主要是其使用synchronized对put等操作进行加锁 锁住了整个Hash表
    Map<String, Object> hashTable = new Hashtable<String, Object>();

    Map<String, Object> map = Collections.synchronizedMap(new HashMap<String, Object>());


    /**
     * put
     * 通过key计算hash
     * 判断是否需要初始化
     * f为当前key定位的Node 为空则利用CAS尝试写入 否则自旋 (f = tabAt(tab, i = (n - 1) & hash)) == null
     * 如果当前位置的 hashcode == MOVED == -1,则需要进行扩容 (fh = f.hash) == MOVED
     * 如果都不满足，则利用 synchronized 锁写入数据  synchronized (f)
     * 如果数量大于 TREEIFY_THRESHOLD 则要转换为红黑树
     */
    Map<String, Object> concurrentHashMap = new ConcurrentHashMap<String, Object>();
}
