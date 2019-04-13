package com.example.talaria;

import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class CommunicationService {
    private BufferedReader input;
    private PrintWriter output;
    private View appView;

    public CommunicationService(BufferedReader in, PrintWriter out, View av){
        input = in;
        output = out;
        appView = av;
    }

    public boolean runSercive(){
        TextView tv = (TextView)appView.findViewById(R.id.tvText);
        tv.setText("CONNECTED");
        EditText et = (EditText)appView.findViewById(R.id.etMessage);
        String message = "";

        while(true){
            try {
                if(input.ready()) {
                    message = input.readLine();
                    tv = (TextView) appView.findViewById(R.id.tvText);
                    tv.setText("Message from your opponent: " + message);
                    if(message.toUpperCase().equals("QUIT"))
                        break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            message = et.getText().toString();

            if( message!= null && !message.equals("")){
                output.println(message);
            }
        }
        return true;
    }
}
