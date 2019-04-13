package com.example.talaria;

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerSocketConnection {

    private Socket socket = null;
    private ServerSocket serverSocket;

    private BufferedReader input;
    private PrintWriter output;


    public void initServerSocketConnection(Integer port) throws IOException {
        serverSocket = new ServerSocket(port);
        Log.d("SERWER", "JESTEM");
        socket = serverSocket.accept();
        Log.d("SERWER", "JESTEM ZA SOCKETEM");
        output = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
        input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    public void close() throws IOException {
        serverSocket.close();
    }

    public Socket getSocket() {
        return socket;
    }

    public BufferedReader getInput() {
        return input;
    }

    public PrintWriter getOutput() {
        return output;
    }
}
