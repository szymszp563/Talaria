package com.example.talaria;

import android.media.session.MediaSession;

import com.github.nkzawa.socketio.client.Socket;

public class TokenKeeper {

    private String token;
    private Float opponentDistance, myDistance;
    private static TokenKeeper instance;
    private Socket socket;

    private TokenKeeper() {
        opponentDistance = 0.0f;
        myDistance = 0.0f;
    }

    public static TokenKeeper getInstance() {
        if (instance == null) {
            instance = new TokenKeeper();
        }


        return instance;
    }


    public String getToken(){
        return token;
    }

    public void setToken(String _token){
        token = _token;
    }

    public Float getOpponentDistance() {
        return opponentDistance;
    }

    public void setOpponentDistance(Float opponentDistance) {
        this.opponentDistance = opponentDistance;
    }

    public Float getMyDistance() {
        return myDistance;
    }

    public void setMyDistance(Float myDistance) {
        this.myDistance = myDistance;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public void emitProgress(Float progress){
        socket.emit("progress", myDistance.toString());
    }
}
