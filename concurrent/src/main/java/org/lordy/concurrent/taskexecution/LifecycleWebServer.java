package org.lordy.concurrent.taskexecution;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 可以通过两种方法来关闭Web服务器  在程序中调用stop 或者以客户端请求形式向Web服务器发送一个特定格式的HTTP请求
 */
public class LifecycleWebServer {

    private final ExecutorService exec = Executors.newCachedThreadPool();

    public  void start() throws IOException{
        ServerSocket socket = new ServerSocket(80);
        while (!exec.isShutdown()){
            try {
                final Socket conn = socket.accept();
                exec.execute(new Runnable() {
                    @Override
                    public void run() {
                        handleReq(conn);
                    }
                });
            }catch (RejectedExecutionException e){
                if(!exec.isShutdown()){
                    e.printStackTrace();
                }
            }
        }
    }

    private class Request{}

    public void stop(){exec.shutdown();}

    void handleReq(Socket conn){
        Request req = readReq(conn);
        if(isShutdownRequest(req)){
            stop();
        }else {
            dispatchRequest(req);
        }
    }

    Request readReq(Socket conn){
        return new Request();
    }

    private Request readRequest(Socket s) {
        return null;
    }

    private void dispatchRequest(Request r) {
    }

    private boolean isShutdownRequest(Request r) {
        return false;
    }
}
