package com.example.dorian.sopraandroid;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
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

        this.connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Réagir au clic
                // Le premier paramètre est le nom de l'activité actuelle
                // Le second est le nom de l'activité de destination

                String nickname = nick.getText().toString();
                String password = pass.getText().toString();

                String charset = "UTF-8";  // Or in Java 7 and later, use the constant: java.nio.charset.StandardCharsets.UTF_8.name()
                String param1 = nickname;
                String param2 = password;
// ...

                try {
                    String query = String.format("username=%s&password=%s",
                            URLEncoder.encode(param1, charset),
                            URLEncoder.encode(param2, charset));

                    String targetURL = ("http://10.0.2.2:8080/Api/Login");
                    System.out.println("////////////////////// avant l'excute Post ! /////////////////////////");
                    final AsyncTask<String, Void, ResponseHTTP> execute = new HttpPostRequestTask().execute(targetURL, query);
                    try {
                        ResponseHTTP result = execute.get();
                        int responseCode = result.getResponseCode();
                        switch(responseCode) {
                            case 200:
                                InputStream is = result.getResponse();
                                String response = convertStreamToString(is);
                                System.out.println("String réponse : "+response);

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

    private static String convertStreamToString(InputStream is) {

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
