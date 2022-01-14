package org.lordy.concurrent.cancleandclose;

import java.util.concurrent.BlockingQueue;

/**
 * 不可取消的任务在退出前恢复中断
 *
 * 如果过早设置中断状态  就可能引起无限循环  因为大多数可中断的阻塞方法都会在入口处检查中断状态  并且当发现该状态已被设置时会立即抛出InterruptedException
 */
public class NoncancleableTask {

    public Task getNextTask(BlockingQueue<Task> queue){
        boolean interrupted = false;
        try {
            while (true){
                try {
                    return queue.take();
                }catch (InterruptedException e){
                    interrupted = true;
                    //重新尝试
                }
            }
        }finally {
            if(interrupted){
                Thread.currentThread().interrupt();
            }
        }
    }

    interface Task{}
}
