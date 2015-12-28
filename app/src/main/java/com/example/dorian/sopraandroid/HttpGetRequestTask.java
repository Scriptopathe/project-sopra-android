package com.example.dorian.sopraandroid;

import android.os.AsyncTask;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by dorian on 28/12/15.
 */
public class HttpGetRequestTask extends AsyncTask<String, Void, ResponseHTTP> {
    protected ResponseHTTP doInBackground(String... urls) {
        HttpURLConnection httpConnection = null;
        ResponseHTTP responseHTTP;

        InputStream response = null;
        int status = 0;

        try {
            // S'il n'y a pas de paramètres à la requête HTTP
            if (urls.length == 1) {
                httpConnection = (HttpURLConnection) new URL(urls[0]).openConnection();
                System.out.println("Code de réponse (pas de paramètres) : " + httpConnection.getResponseCode());
            }
            // S'il y a des paramètres à la requête HTTP
            else if (urls.length == 2) {
                httpConnection = (HttpURLConnection) new URL(urls[0] + "?" + urls[1]).openConnection();
                System.out.println("Code de réponse (avec paramètres) : "+httpConnection.getResponseCode());
            }
            else {
                // Lever un exception disant que la méthode a été appelée avec trop d'arguments
            }
            //httpConnection.setRequestProperty("Accept-Charset", "UTF-8");
            response = httpConnection.getInputStream();
            status = httpConnection.getResponseCode();
        } catch (IOException e) {
            e.printStackTrace();
        }
        responseHTTP = new ResponseHTTP(response, status);

        return responseHTTP;
    }
}