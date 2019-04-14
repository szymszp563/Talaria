package com.example.talaria;

public class TokenKeeper {

    private String token;
    private static TokenKeeper instance;

    private TokenKeeper() {
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
}
