package org.lordy.concurrent.taskexecution;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class TaskExecutionWebServer {

    private static final int N_THREAD = 100;
    private static final Executor exec = Executors.newFixedThreadPool(N_THREAD);


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
            exec.execute(task);
        }
    }

    public static void handleReq(Socket conn){}
}
