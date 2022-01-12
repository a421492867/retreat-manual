package org.lordy.concurrent.taskexecution;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 显式为任务创建线程
 * 为每个请求创建一个新的线程提供服务
 *
 * 线程生命周期的开销非常高
 * 资源消耗
 * 稳定性
 */
public class ThreadPerTaskWebServer {

    public static void main(String[] args) throws IOException {
        ServerSocket socket = new ServerSocket(80);
        while (true){
            final Socket connection = socket.accept();
            Runnable task = new Runnable() {
                @Override
                public void run() {
                    handleReq(connection);
                }
            };
            new Thread(task).start();
        }
    }

    public static void handleReq(Socket conn){}
}
