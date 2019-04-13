package com.example.talaria;

import android.content.Context;
import android.view.View;

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

    public boolean initConnect(boolean isServer, String IP, View appView) throws IOException {

        BufferedReader input;
        PrintWriter output;

        if(isServer){ //create serversocket
            ServerSocketConnection ssConnection = new ServerSocketConnection();
            ssConnection.initServerSocketConnection(port);
            socket = ssConnection.getSocket();
            input = ssConnection.getInput();
            output = ssConnection.getOutput();

            //Connection never closed!!!!!
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
