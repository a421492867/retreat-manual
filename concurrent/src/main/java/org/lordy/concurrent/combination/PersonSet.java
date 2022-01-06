package org.lordy.concurrent.combination;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

import java.util.HashSet;
import java.util.Set;

/**
 * PersonSet的状态由HashSet管理 而HashSet并非线程安全的  但是由于mySet是私有的并且不会逸出 因此HashSet被封闭在PersonSet中
 * 唯一能访问mySet的代码路径是addPerson 和 ContainsPerson 他们都获得PersonSet上的锁，所以线程安全
 */
@ThreadSafe
public class PersonSet {

    @GuardedBy("this")
    private final Set<Person> mySet = new HashSet<Person>();

    public synchronized void addPerson(Person p){
        mySet.add(p);
    }

    public synchronized boolean containsPerson(Person p){
        return mySet.contains(p);
    }


    private final class Person{}
}
