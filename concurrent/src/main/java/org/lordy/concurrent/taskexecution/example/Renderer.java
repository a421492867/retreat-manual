package org.lordy.concurrent.taskexecution.example;

import java.util.List;
import java.util.concurrent.*;

/**
 * 为每一个图像下载都创建一个独立的任务 将串行下载过程转换为并行过程  减少下载图像时间
 */
public abstract class Renderer {

    private final ExecutorService executor;

    public Renderer(ExecutorService executor) {
        this.executor = executor;
    }

    void renderPage(CharSequence source){
        final List<ImageInfo> infos = scanForImageInfo(source);
        CompletionService<ImageData> completionService = new ExecutorCompletionService<>(executor);
        for(final ImageInfo imageInfo : infos){
            completionService.submit(() -> imageInfo.downloadImage());
        }
        renderText(source);
        try {
            for(int t = 0; t < infos.size(); t++){
                Future<ImageData> f = completionService.take();
                ImageData imageData = f.get();
                renderImage(imageData);
            }
        }catch (InterruptedException e){
            Thread.currentThread().interrupt();
        }catch (ExecutionException e){
            throw launderThrowable(e.getCause());
        }
    }


    interface ImageData {
    }

    interface ImageInfo {
        ImageData downloadImage();
    }

    abstract void renderText(CharSequence s);

    abstract List<ImageInfo> scanForImageInfo(CharSequence s);

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
