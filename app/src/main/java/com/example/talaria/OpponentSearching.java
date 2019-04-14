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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Date;

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


                    try {
                        JSONObject json = new JSONObject(response);
                        String IP = json.getString("url");
                        Log.d("ADRES", IP);
                        IO.Options opt = new IO.Options();
                        opt.path = json.getString("path");
                        final Socket socket = IO.socket(IP, opt);
                        socket.connect();

                        //socket.emit("event", "ILYES");

                        socket.on("matched", new Emitter.Listener() {
                            @Override
                            public void call(Object... args) {
                                JSONObject data = (JSONObject)args[0];
//                            Toast.makeText(getApplicationContext(), data.toString(), Toast.LENGTH_SHORT).show();
                                Log.d("JSON", data.toString());
                                String roomUrl, path;
                                try {
                                    JSONObject important = data.getJSONObject("data");
                                    roomUrl = important.getString("roomUrl");
                                    final String clientId = important.getString("clientId");
                                    path = important.getString("path");
                                    Log.d("WYLUSKANE", roomUrl + "XX" + clientId + "XX" + path);

                                    IO.Options o = new IO.Options();
                                    o.path = path;
                                    final Socket anotherSocket = IO.socket(roomUrl, o);
                                    token.setSocket(anotherSocket);
                                    anotherSocket.connect();
                                    socket.close();

                                    anotherSocket.on("userConnected", new Emitter.Listener() {
                                        @Override
                                        public void call(Object... args) {
                                            Log.d("userConnected", "JAKIS PROGRES PRZECIWNIKA");
                                            anotherSocket.emit("join", clientId);
                                        }
                                    });

                                    anotherSocket.on("ready", new Emitter.Listener() {
                                        @Override
                                        public void call(Object... args) {
                                            Log.d("READY", "Ready event occured");
                                        }
                                    });

                                    anotherSocket.on("countdown", new Emitter.Listener() {
                                        @Override
                                        public void call(Object... args) {
                                            Integer counter = (Integer)args[0];
                                            Log.d("COUNTDOWN", counter.toString());
                                        }
                                    });

                                    anotherSocket.on("start", new Emitter.Listener() {
                                        @Override
                                        public void call(Object... args) {
                                            Log.d("START", "Versus STARTED!");
                                            //anotherSocket.emit("progress", token.getMyDistance().toString()); //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>EMITER
                                        }
                                    });

                                    anotherSocket.on("progressChange", new Emitter.Listener() {
                                        @Override
                                        public void call(Object... args) {
                                            token.setOpponentDistance(Float.parseFloat((String)args[0]));
                                            Log.d("progressChanged", "JAKIS PROGRES PRZECIWNIKA");
                                        }
                                    });

                                    anotherSocket.on("finish", new Emitter.Listener() {
                                        @Override
                                        public void call(Object... args) {
                                            JSONObject json = (JSONObject)args[0];
                                            try {
                                                String winnerId = json.getString("winnerId");
                                                Date duration = new Date(json.getLong("time"));
                                                if(winnerId.equals(clientId))
                                                    Log.d("finish!", "Jestes zwyciezca!!!");
                                                else
                                                    Log.d("finish!", "Przegrales!!!");
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            } finally{
                                                anotherSocket.disconnect();
                                            }

                                        }
                                    });

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                } catch (URISyntaxException e) {
                                    e.printStackTrace();
                                }

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
