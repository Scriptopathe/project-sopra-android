package com.example.dorian.sopraandroid;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.URLEncoder;
import java.util.concurrent.ExecutionException;

/**
 * This Activity is the Activity Launch at the startup of the application
 */
public class ConnectActivity extends AppCompatActivity {
    Button connectButton = null;
    private EditText nick = null;
    private EditText pass = null;

    public static final String PREFS_NAME = "MyPrefsFile";
    private static final String PREF_USERNAME = "username";
    private static final String PREF_PASSWORD = "password";

    /**
     * L'activité vérifie si elle est en mode "Remember user" ou non, si oui elle essait de récupérer
     * les infos sauvegardées. Si elle les trouve elle lance une requête HTTP au serveur demandant de
     * se connecter. Si elle ne les trouve pas ou si elle n'est pas en mode "Remember user", elle
     * affiche les formulaire de connexion.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Permet la gestion des cookies afin que le serveur se souvienne de nous
        CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));

        String username = null;
        String password = null;
        if (Constantes.REMEMBER_USER) {
            // On récupère les informations enregistrées de l'utilisateur
            SharedPreferences pref = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
            username = pref.getString(PREF_USERNAME, null);
            password = pref.getString(PREF_PASSWORD, null);
        }

        if (username == null || password == null) {
            /** Si c'est le premier lancement ou si REMEMBER_USER == false**/
            setContentView(R.layout.activity_connect);
            connectButton = (Button) findViewById(R.id.connectButton);
            nick = (EditText) findViewById(R.id.editTextUsername);
            pass = (EditText) findViewById(R.id.editTextPassword);

            this.connectButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String username = nick.getText().toString();
                    String password = pass.getText().toString();

                    effectuerLaConnexion(username,password);
                }
            });
        } else {
            /** Si ce n'est pas le premier lancement **/
            effectuerLaConnexion(username,password);
        }
    }

    /**
     *  Cette méthode prépare avec les bons arguments et envoie la requête HTTP POST qui permet la
     *  connexion.
     * @param username The username of the user who wants to connect
     * @param password The password of the user who wants to connect
     */
    private void effectuerLaConnexion(String username, String password) {
        String charset = "UTF-8";
        String param1 = username;
        String param2 = password;
        String query = null;
        try {
            query = String.format("username=%s&password=%s",
                    URLEncoder.encode(param1, charset),
                    URLEncoder.encode(param2, charset));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String targetURL = ("http://"+Constantes.SERVER+"/Api/Login");
        System.out.println("////////////////////// avant l'execute Post ! /////////////////////////");
        final AsyncTask<String, Void, ResponseHTTP> execute = new HttpPostRequestTask().execute(targetURL, query);
        try {
            ResponseHTTP result = execute.get();
            if (result != null) {
                int responseCode = result.getResponseCode();
                switch (responseCode) {
                    case 200:
                        String response = result.getResponseString();
                        System.out.println("[" + responseCode + "] String réponse à /Api/Login : " + response);


                        getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
                                .edit()
                                .putString(PREF_USERNAME, username)
                                .putString(PREF_PASSWORD, password)
                                .commit();

                        Intent secondeActivite = new Intent(ConnectActivity.this, MainActivity.class);
                        // Puis on lance l'intent !
                        startActivity(secondeActivite);
                        break;
                }
            } else {
                Toast.makeText(getApplicationContext(), "Bad login or password", Toast.LENGTH_SHORT).show();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}
