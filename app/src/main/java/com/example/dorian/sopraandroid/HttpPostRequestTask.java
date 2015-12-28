package com.example.dorian.sopraandroid;

import android.os.AsyncTask;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created by amandine on 14/12/2015.
 */
public class HttpPostRequestTask extends AsyncTask<String, Void, ResponseHTTP> {
    protected ResponseHTTP doInBackground(String... urls) {
        HttpURLConnection httpConnection = null;
        ResponseHTTP responseHTTP;
        try {
            httpConnection = (HttpURLConnection) new URL(urls[0]).openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            httpConnection.setRequestMethod("POST");
        } catch (ProtocolException e) {
            e.printStackTrace();
        }
        httpConnection.setDoOutput(true); // Triggers POST.
        httpConnection.setRequestProperty("Accept-Charset", "UTF-8");

        httpConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + "UTF-8");
        InputStream response = null;
        int status = 0;
        try {
            OutputStream output = httpConnection.getOutputStream();
            // S'il y a des paramètres
            if (urls.length == 2) {
                output.write(urls[1].getBytes("UTF-8"));
            }
            response = httpConnection.getInputStream();
            status = httpConnection.getResponseCode();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        responseHTTP = new ResponseHTTP(response, status);

        return responseHTTP;
    }
}