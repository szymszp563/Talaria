package com.example.talaria;

import android.view.View;
import android.widget.EdgeEffect;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class CommunicationService {
    private BufferedReader input;
    private PrintWriter output;
    private View appView;

    public CommunicationService(BufferedReader in, PrintWriter out, View appView){
        input = in;
        output = out;
    }

    public boolean runSercive() throws IOException {
        TextView tv = (TextView)appView.findViewById(R.id.tvText);
        tv.setText("CONNECTED");
        EditText et = (EditText)appView.findViewById(R.id.etMessage);
        while(true){
            if(input.ready()) {
                String message = input.readLine();
                tv = (TextView) appView.findViewById(R.id.tvText);
                tv.setText("Message from your opponent: " + message);
            }

        }
    }
}
