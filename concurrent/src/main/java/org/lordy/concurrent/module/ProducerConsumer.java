package org.lordy.concurrent.module;

import java.io.File;
import java.io.FileFilter;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ProducerConsumer {

    static class FileCrawler implements Runnable{
        private final BlockingQueue<File> fileQueue;

        private final FileFilter fileFilter;
        private final File root;

        public FileCrawler(BlockingQueue<File> fileQueue, final FileFilter fileFilter, File root) {
            this.fileQueue = fileQueue;
            this.fileFilter = new FileFilter() {
                public boolean accept(File pathname) {
                    return pathname.isDirectory() || fileFilter.accept(pathname);
                }
            };
            this.root = root;
        }
        private boolean alreadyIndexed(File file){
            return false;
        }

        public void run() {
            try {
                crawl(root);
            }catch (InterruptedException e){
                // 恢复中断状态
                Thread.currentThread().interrupt();
            }
        }

        private void crawl(File root) throws InterruptedException{
            File[] entries = root.listFiles(fileFilter);
            if(entries != null){
                for(File file : entries){
                    if(file.isDirectory()){
                        crawl(file);
                    }else if(!alreadyIndexed(file)){
                        fileQueue.put(file);
                    }
                }
            }
        }
    }

    static class Indexer implements Runnable{
        private final BlockingQueue<File> queue;

        public Indexer(BlockingQueue<File> queue) {
            this.queue = queue;
        }

        public void run() {
            try {
                while (true){
                    indexFile(queue.take());
                }
            }catch (InterruptedException e){
                Thread.currentThread().interrupt();
            }
        }

        public void indexFile(File file) throws InterruptedException{}
    }

    private static final int BOUND = 10;
    private static final int N_CONSUMERS = Runtime.getRuntime().availableProcessors();

    public static void startIndexing(File[] roots){
        BlockingQueue<File> queue = new LinkedBlockingQueue<File>(BOUND);
        FileFilter fileFilter = new FileFilter() {
            public boolean accept(File pathname) {
                return true;
            }
        };
        for(File root : roots){
            new Thread(new FileCrawler(queue, fileFilter, root)).start();
        }
        for(int i = 0; i < N_CONSUMERS; i++){
            new Thread(new Indexer(queue)).start();
        }
    }
}
