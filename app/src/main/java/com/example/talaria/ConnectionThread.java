package com.example.talaria;


import android.content.Context;
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
        sConnection.connect(isServer, IP, appView);
    }
}
