package org.lordy.concurrent.module;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * 使用future task来提前加载稍后需要的数据
 */
public class Preloader {

    ProductInfo loadProductInfo() throws DataLoadException{
        return null;
    }

    private final FutureTask<ProductInfo> futureTask = new FutureTask<ProductInfo>(new Callable<ProductInfo>() {
        public ProductInfo call() throws Exception {
            return loadProductInfo();
        }
    });

    private final Thread thread = new Thread(futureTask);

    public void start(){thread.start();}

    public ProductInfo get() throws DataLoadException, InterruptedException{
        try {
            return futureTask.get();
        }catch (ExecutionException e){
            Throwable cause = e.getCause();
            if(cause instanceof DataLoadException) throw (DataLoadException) cause;
            else throw launderThrowable(cause);
        }
    }

    public RuntimeException launderThrowable(Throwable t){
        if(t instanceof  RuntimeException){
            return (RuntimeException) t;
        }else if (t instanceof Error){
            throw (Error) t;
        }else {
            throw new IllegalStateException(t);
        }
    }

    class ProductInfo{}

    class DataLoadException extends Exception{}


}
