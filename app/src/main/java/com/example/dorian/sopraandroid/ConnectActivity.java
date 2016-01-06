package com.example.dorian.sopraandroid;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);
        connectButton = (Button) findViewById(R.id.connectButton);
        nick = (EditText)findViewById(R.id.editTextUsername);
        pass = (EditText)findViewById(R.id.editTextPassword);


        // Permet la gestion des cookies
        CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));

        this.connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Réagir au clic
                // Le premier paramètre est le nom de l'activité actuelle
                // Le second est le nom de l'activité de destination

                String nickname = nick.getText().toString();
                String password = pass.getText().toString();

                String charset = "UTF-8";
                String param1 = nickname;
                String param2 = password;
// ...

                try {
                    String query = String.format("username=%s&password=%s",
                            URLEncoder.encode(param1, charset),
                            URLEncoder.encode(param2, charset));

                    String targetURL = ("http://10.0.2.2:8080/Api/Login");
                    System.out.println("////////////////////// avant l'execute Post ! /////////////////////////");
                    final AsyncTask<String, Void, ResponseHTTP> execute = new HttpPostRequestTask().execute(targetURL, query);
                    try {
                        ResponseHTTP result = execute.get();
                        int responseCode = result.getResponseCode();
                        switch(responseCode) {
                            case 200:
                                String response = result.getResponseString();
                                System.out.println("["+responseCode+"] String réponse à /Api/Login : "+response);

                                Intent secondeActivite = new Intent(ConnectActivity.this, MainActivity.class);
                                // Puis on lance l'intent !
                                startActivity(secondeActivite);
                                break;
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
