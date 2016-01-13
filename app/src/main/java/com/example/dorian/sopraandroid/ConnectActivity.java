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

public class ConnectActivity extends AppCompatActivity {
    Button connectButton = null;
    private EditText nick = null;
    private EditText pass = null;

    public static final String PREFS_NAME = "MyPrefsFile";
    private static final String PREF_USERNAME = "username";
    private static final String PREF_PASSWORD = "password";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Permet la gestion des cookies
        CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));

        /**SharedPreferences pref = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String username = pref.getString(PREF_USERNAME, null);
        String password = pref.getString(PREF_PASSWORD, null);*/

        /**if (username == null || password == null) {**/
            /** Si c'est le premier lancement **/
            setContentView(R.layout.activity_connect);
            connectButton = (Button) findViewById(R.id.connectButton);
            nick = (EditText) findViewById(R.id.editTextUsername);
            pass = (EditText) findViewById(R.id.editTextPassword);

            this.connectButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Réagir au clic
                    // Le premier paramètre est le nom de l'activité actuelle
                    // Le second est le nom de l'activité de destination

                    String username = nick.getText().toString();
                    String password = pass.getText().toString();

                    effectuerLaConnexion(username,password);
                }
            });
        /**} else {
            /** Si ce n'est pas le premier lancement **/
            /**effectuerLaConnexion(username,password);
        }**/
    }

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

        String targetURL = ("http://10.0.2.2:8080/Api/Login");
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
