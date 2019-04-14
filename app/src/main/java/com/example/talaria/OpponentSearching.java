package com.example.talaria;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONObject;

import java.io.IOException;
import java.net.URISyntaxException;

public class OpponentSearching extends AppCompatActivity {

    Button bSendReqest;
    TextView etResponse;
    HttpRequestsCommander http;
    TokenKeeper token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opponent_searching);

        bSendReqest = (Button) findViewById(R.id.bSendOpponentRequest);
        etResponse = (TextView) findViewById(R.id.etResponse);
        token = TokenKeeper.getInstance();
        http = new HttpRequestsCommander();

        bSendReqest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String response = http.postWithToken("http://51.38.134.31:3501/v1/queue/versus/signUp",
                            "{\"activityType\":0,\"environmentType\":10,\"distance\":\"1000\"}" ,
                            token.getToken());
                    etResponse.setText(response);
                    //////////////////////////////////////////////////////////////////
                    Socket socket = null;

                    try {
                        JSONObject json = new JSONObject(response);
                        String IP = json.getString("url");
                        Log.d("ADRES", IP);
                        IO.Options opt = new IO.Options();
                        opt.path = json.getString("path");
                        socket = IO.socket(IP, opt);
                        socket.connect();

                        //socket.emit("event", "ILYES");

                        socket.on("matched", new Emitter.Listener() {
                            @Override
                            public void call(Object... args) {
                                JSONObject data = (JSONObject)args[0];
//                            Toast.makeText(getApplicationContext(), data.toString(), Toast.LENGTH_SHORT).show();
                                Log.d("CONNECTION", "JSON LECI");
                            }
                        });
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                    //////////////////////////////////////////////////////////////////
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
