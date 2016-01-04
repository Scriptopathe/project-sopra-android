package com.example.dorian.sopraandroid;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by amandine on 14/12/2015.
 */
public class ResponseHTTP {
    private int responseCode;
    private String responseString;

    public ResponseHTTP(String responseString, int responseCode) {
        this.responseCode = responseCode;
        this.responseString = responseString;
    }

    public int getResponseCode() {
        return this.responseCode;
    }

    public String getResponseString() {
        return this.responseString;
    }
}
