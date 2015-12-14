package com.example.dorian.sopraandroid;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by amandine on 14/12/2015.
 */
public class ResponseHTTP {
    private InputStream response;
    private int responseCode;

    public ResponseHTTP(InputStream response, int responseCode) {
        this.response = response;
        this.responseCode = responseCode;
    }

    public InputStream getResponse() {
        String s = "";
        try  {
            BufferedReader reader = new BufferedReader(new InputStreamReader(response, "UTF-8"));
            for (String line; (line = reader.readLine()) != null;) {
                    s = s+line;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return this.response;
    }

    public int getResponseCode() {
        return this.responseCode;
    }
}
