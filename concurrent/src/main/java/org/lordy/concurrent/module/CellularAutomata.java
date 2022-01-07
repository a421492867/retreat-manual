package org.lordy.concurrent.module;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * 工作线程为各自子问题中的所有细胞计算新值
 * 当所有工作线程都到达栅栏时
 * 栅栏会把这些新值交给数据模型
 * 在栅栏的操作执行完以后 工作线程开始下一步计算
 */
public class CellularAutomata {

    private final Board mainBoard;

    private final CyclicBarrier barrier;

    private final Worker[] workers;

    public CellularAutomata(Board board) {
        this.mainBoard = board;
        int count = Runtime.getRuntime().availableProcessors();
        this.barrier = new CyclicBarrier(count, new Runnable() {
            public void run() {
                mainBoard.commitNewValues();
            }
        });
        this.workers = new Worker[count];
        for(int i = 0; i < count; i++){
            workers[i] = new Worker(mainBoard.getSubBoard(count, i));
        }
    }

    public void start(){
        for(int i = 0; i < workers.length; i++){
            new Thread(workers[i]).start();
        }
        mainBoard.waitForConvergence();
    }

    private class Worker implements Runnable{
        private final Board board;

        public Worker(Board board) {
            this.board = board;
        }

        public void run() {
            while (!board.hasConverged()){
                for(int x = 0; x < board.getMaxX(); x++){
                    for(int y = 0; y < board.getMaxY(); y++){
                        board.setNewValue(x, y, computeValue(x, y));
                    }try {
                        barrier.await();
                    }catch (InterruptedException ex){
                        return;
                    }catch (BrokenBarrierException ex){
                        return;
                    }
                }
            }
        }

        private int computeValue(int x, int y){
            return 0;
        }
    }

    interface Board{
        int getMaxX();
        int getMaxY();
        int getValue(int x, int y);
        int setNewValue(int x, int y, int value);
        void commitNewValues();
        boolean hasConverged();
        void waitForConvergence();
        Board getSubBoard(int numPartitions, int index);
    }
}
