package com.example.talaria;


import java.io.IOException;

public class ConnectionThread extends Thread {
    private SocketConnection sConnection;
    private String IP;
    private boolean isServer;

    public ConnectionThread(String ip, boolean serv){
        isServer = serv;
        IP = ip;
    }

    public void run(){
        sConnection = new SocketConnection();
        try {
            sConnection.initConnect(isServer, IP);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
