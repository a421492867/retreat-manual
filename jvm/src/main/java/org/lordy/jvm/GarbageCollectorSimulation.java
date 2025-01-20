package org.lordy.jvm;

import java.util.*;

public class GarbageCollectorSimulation {

    static class Object{
        int id;
        boolean marked;

        Object(int id){
            this.id = id;
            this.marked = false;
        }
    }

    static class Heap{
        List<Object> objects = new ArrayList<>();

        void addObject(Object obj){
            objects.add(obj);
        }

        void printHeap(){
            System.out.println("Heap State:");
            for(Object obj : objects){
                System.out.println("Object id : " + obj.id + ", marked : " + obj.marked);
            }
            System.out.println();
        }
    }

    static class GarbageCollector{

        Heap heap;
        GarbageCollector(Heap heap){
            this.heap = heap;
        }

        void mark(Set<Object> roots){
            System.out.println("Mark phase:");
            for(Object obj : roots){
                markRecursively(obj);
            }
        }

        private void markRecursively(Object obj){
            if(obj  == null || obj.marked){
                return;
            }
            obj.marked = true;
            System.out.println("Marking Object Id: " + obj.id);
        }

        void sweep(){
            System.out.println("Sweep phase:");
            Iterator<Object> iterator = heap.objects.iterator();
            while (iterator.hasNext()){
                Object obj = iterator.next();
                if(!obj.marked){
                    System.out.println("Sweeping Object Id: " + obj.id);
                    iterator.remove();
                }else {
                    obj.marked = false;
                }
            }
        }

        void run(Set<Object> roots){
            mark(roots);
            sweep();
        }
    }

    public static void main(String[] args) {
        // 创建堆和垃圾回收器
        Heap heap = new Heap();
        GarbageCollector gc = new GarbageCollector(heap);

        // 模拟创建对象
        Object obj1 = new Object(1);
        Object obj2 = new Object(2);
        Object obj3 = new Object(3);

        heap.addObject(obj1);
        heap.addObject(obj2);
        heap.addObject(obj3);

        heap.printHeap();

        // 模拟根集（活跃的引用）
        Set<Object> roots = new HashSet<>();
        roots.add(obj1);

        // 执行垃圾回收
        gc.run(roots);

        heap.printHeap();
    }
}
