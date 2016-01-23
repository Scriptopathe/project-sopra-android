package com.example.dorian.sopraandroid.http;

/**
 * Created by amandine on 14/12/2015.
 *
 * Classe permettant de représenter les résultat d'une requête HTTP avec son code réponse et son contenu.
 */
public class ResponseHTTP {
    /**
     * Le code de retour de la réponse HTTP à notre requête HTTP
     */
    private int responseCode;
    /**
     * Le contenu (souvent XML) de la réponse HTTP à notre requête HTTP
     */
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
