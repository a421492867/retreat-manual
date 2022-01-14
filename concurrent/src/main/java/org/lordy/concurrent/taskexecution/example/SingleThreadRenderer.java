package org.lordy.concurrent.taskexecution.example;

import com.sun.scenario.effect.ImageData;

import java.util.ArrayList;
import java.util.List;

/**
 * 图像下载过程大部分时间都是在等待I/O操作完成 这期间CPU几乎不做任何工作
 * 因此 这种串行执行方法没有充分利用CPU
 */
public abstract class SingleThreadRenderer {

    void renderPage(CharSequence source){
        renderText(source);
        List<ImageData> imageData = new ArrayList<>();
        for(ImageInfo imageInfo : scanForImageInfo(source)){
            imageData.add(imageInfo.downloadImage());
        }
        for(ImageData data : imageData){
            renderImage(data);
        }
    }

    abstract void renderText(CharSequence s);

    abstract List<ImageInfo> scanForImageInfo(CharSequence s);

    abstract void renderImage(ImageData i);

    interface ImageData{}

    interface ImageInfo{
        ImageData downloadImage();
    }
}
