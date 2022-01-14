package org.lordy.concurrent.taskexecution.example;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * 如果渲染文本的速度远远高于下载图像的速度  那么程序最终性能与串行执行时的性能差别不大
 */
public abstract class FutureRenderer {

    private final ExecutorService executor = Executors.newCachedThreadPool();

    void renderPage(CharSequence source){
        final List<ImageInfo> imageInfos = scanForImageInfo(source);
        Callable<List<ImageData>> task = new Callable<List<ImageData>>() {
            @Override
            public List<ImageData> call() throws Exception {
                List<ImageData> result = new ArrayList<>();
                for(ImageInfo imageInfo : imageInfos){
                    result.add(imageInfo.downloadImage());
                }
                return result;
            }
        };
        Future<List<ImageData>> future = executor.submit(task);
        renderText(source);

        try {
            List<ImageData> imageData = future.get();
            for(ImageData data : imageData){
                renderImage(data);
            }
        }catch (InterruptedException e){
            Thread.currentThread().interrupt();
            future.cancel(true);
        }catch (ExecutionException e){
            throw launderThrowable(e.getCause());
        }
    }

    interface ImageData{}

    interface ImageInfo{
        ImageData downloadImage();
    }

    abstract List<ImageInfo> scanForImageInfo(CharSequence s);

    abstract void renderText(CharSequence s);

    abstract void renderImage(ImageData i);

    public RuntimeException launderThrowable(Throwable t){
        if(t instanceof  RuntimeException){
            return (RuntimeException) t;
        }else if (t instanceof Error){
            throw (Error) t;
        }else {
            throw new IllegalStateException(t);
        }
    }
}
