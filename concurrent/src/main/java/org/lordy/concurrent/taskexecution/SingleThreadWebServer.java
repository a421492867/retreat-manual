package org.lordy.concurrent.taskexecution;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 性能糟糕
 */
public class SingleThreadWebServer {

    public static void main(String[] args) throws IOException {
        ServerSocket socket = new ServerSocket(80);
        while (true){
            Socket connection = socket.accept();
            handleReq(connection);
        }
    }

    public static void handleReq(Socket conn){}
}
