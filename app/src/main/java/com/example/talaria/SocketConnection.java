package com.example.talaria;

import android.util.Log;
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

    public void connect(boolean isServer, String IP, View appView) {

        BufferedReader input;
        PrintWriter output;
        ServerSocketConnection ssConnection = null;

        try {
            if (isServer) { //create serversocket
                ssConnection = new ServerSocketConnection();
                ssConnection.initServerSocketConnection(port);
                socket = ssConnection.getSocket();
                input = ssConnection.getInput();
                output = ssConnection.getOutput();
            } else { //connect to serversocket;
                ip = IP;
                socket = new Socket(ip, port); //throws IOException
                input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                output = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
            }

            CommunicationService service = new CommunicationService(input, output, appView);
            Log.d("SOCKET",socket.getInetAddress().toString());
            if (service.runSercive()) {
                socket.close();
                if (ssConnection != null) {
                    ssConnection.close();
                }
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
}
