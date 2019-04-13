package com.example.talaria;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
//import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URISyntaxException;

public class SocketConnection {

    private String ip;
    private final static Integer port = 8080;

    private Socket socket;

    public void connect(boolean isServer, String IP, View appView){

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
                //socket = IO.socket(ip);
                //socket.connect();
                //socket.open();

               /* while(!socket.connected()){
                    //Toast.makeText(context, "NIEPOLACZONE", Toast.LENGTH_SHORT).show();
                    Log.d("CONNECTION", "NIEPOLACZONE");
                }


                socket.on("test", new Emitter.Listener() {
                    @Override
                    public void call(Object... args) {
                        JSONObject data = (JSONObject)args[0];
                        Toast.makeText(context, data.toString(), Toast.LENGTH_SHORT).show();
                        Log.d("CONNECTION", "JSON LECI");
                    }
                });*/
            }

            CommunicationService service = new CommunicationService(input, output, appView);
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
        /*catch (URISyntaxException e) {
            e.printStackTrace();
        }*/
    }
}
