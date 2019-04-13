package com.example.talaria;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class SocketConnection {

    private String ip;
    private final static Integer port = 8080;

    private Socket socket;
    private ServerSocketConnection ssConnection;

    private BufferedReader input;
    private PrintWriter output;

    public boolean initConnect(boolean isServer, String IP) throws IOException {

        if(isServer){ //create serversocket
            ssConnection = new ServerSocketConnection();
            ssConnection.initServerSocketConnection(port);
        }
        else { //connect to serversocket;
            ip = IP;
            socket = new Socket(ip, port); //throws IOException
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
        }

        return true;
    }
}
