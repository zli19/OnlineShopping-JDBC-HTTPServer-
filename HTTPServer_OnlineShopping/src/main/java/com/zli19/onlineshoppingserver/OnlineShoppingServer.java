
package com.zli19.onlineshoppingserver;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author zhiku
 */
public class OnlineShoppingServer {

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket();
        serverSocket.bind(new InetSocketAddress(InetAddress.getLoopbackAddress(), 8080));       
        
        while(true){
            Socket socket = serverSocket.accept();           
           new Thread(new SocketHandler(socket)).start();  
//            new SocketHandler(socket).run();
        }
    }      
}

