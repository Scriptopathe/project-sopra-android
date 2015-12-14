package com.example.dorian.sopraandroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class ConnectActivity extends AppCompatActivity {
    Button connectButton = null;
    private EditText nick = null;
    private EditText pass = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);
        connectButton = (Button) findViewById(R.id.connectButton);
        nick = (EditText)findViewById(R.id.editText);
        pass = (EditText)findViewById(R.id.editText);

        this.connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Réagir au clic
                // Le premier paramètre est le nom de l'activité actuelle
                // Le second est le nom de l'activité de destination

                String nickname = nick.getText().toString();
                String password = pass.getText().toString();
                String response;

                String charset = "UTF-8";  // Or in Java 7 and later, use the constant: java.nio.charset.StandardCharsets.UTF_8.name()
                String param1 = nickname;
                String param2 = password;
// ...

                try {
                    String query = String.format("username=%s&password=%s",
                            URLEncoder.encode(param1, charset),
                            URLEncoder.encode(param2, charset));

                    String targetURL = ("http://localhost:8080/Api/Login");
                    System.out.println("////////////////////// avant l'excute Post ! /////////////////////////");
                    new HttpPostRequestTask().execute(targetURL, query);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
           //     response = excutePost(targetURL, urlParameters);
         //       System.out.println(response);
                Intent secondeActivite = new Intent(ConnectActivity.this, MainActivity.class);

                // Puis on lance l'intent !
                startActivity(secondeActivite);
            }
        });



        /*Spinner spinner = (Spinner) findViewById(R.id.sites_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.sites_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);*/
    }
    public static String excutePost(String targetURL, String urlParameters) {
        HttpURLConnection connection = null;
        try {
            //Create connection
            URL url = new URL(targetURL);
            connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");

            connection.setRequestProperty("Content-Length",
                    Integer.toString(urlParameters.getBytes().length));
            connection.setRequestProperty("Content-Language", "en-US");

            connection.setUseCaches(false);
            connection.setDoOutput(true);

            //Send request
            DataOutputStream wr = new DataOutputStream(
                    connection.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.close();

            //Get Response
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            StringBuilder response = new StringBuilder(); // or StringBuffer if not Java 5+
            String line;
            while((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();
            return response.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if(connection != null) {
                connection.disconnect();
            }
        }
    }

}
