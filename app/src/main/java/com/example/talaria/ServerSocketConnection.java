package com.example.talaria;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerSocketConnection {

    private Socket socket;
    private ServerSocket serverSocket;


    public void initServerSocketConnection(Integer port) throws IOException {
        serverSocket = new ServerSocket(port);
        socket = serverSocket.accept();
    }
}
