package com.example.dorian.sopraandroid.http;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by dorian on 28/12/15.
 */
public class HttpGetRequestTask extends AsyncTask<String, Void, ResponseHTTP> {
    protected ResponseHTTP doInBackground(String... urls) {
        HttpURLConnection httpConnection = null;
        ResponseHTTP responseHTTP = null;

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
            responseHTTP = new ResponseHTTP(this.convertStreamToString(response), status);
        } catch (IOException e) {
            e.printStackTrace();
        }


        return responseHTTP;
    }

    private String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append((line + "\n"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return sb.toString();
    }
}