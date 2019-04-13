package com.example.talaria;


import android.view.View;

import java.io.IOException;

public class ConnectionThread extends Thread {
    private SocketConnection sConnection;
    private String IP;
    private boolean isServer;
    private View appView;

    public ConnectionThread(String ip, boolean serv, View av){
        isServer = serv;
        IP = ip;
        appView = av;
    }

    public void run(){
        sConnection = new SocketConnection();
        try {
            sConnection.initConnect(isServer, IP, appView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
