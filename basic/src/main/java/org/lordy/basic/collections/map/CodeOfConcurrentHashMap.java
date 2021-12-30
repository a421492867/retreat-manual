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
     * JDK 1.7 使用分段锁机制实现 ConcurrentHashMap
     *
     * ConcurrentHashMap在对象中保存了一个Segment数组，即将整个Hash表划分为多个分段；而每个Segment元素，即每个分段则类似于一个Hashtable
     * 在执行put操作时首先根据hash算法定位到元素属于哪个Segment，然后对该Segment加锁即可
     */
    Map<String, Object> concurrentHashMap = new ConcurrentHashMap<String, Object>();
}
