package com.example.talaria;

import java.io.BufferedReader;
import java.io.PrintWriter;

public class CommunicationService {
    private BufferedReader input;
    private PrintWriter output;

    public CommunicationService(BufferedReader in, PrintWriter out){
        input = in;
        output = out;
    }

}
