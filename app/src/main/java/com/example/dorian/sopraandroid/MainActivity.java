package com.example.dorian.sopraandroid;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.example.dorian.sopraandroid.model.Site;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

public class MainActivity extends Activity implements OnClickListener {

    //UI References
    private EditText fromDateEtxt;
    private EditText toDateEtxt;

    private DatePickerDialog fromDatePickerDialog;
    private DatePickerDialog toDatePickerDialog;
    private SimpleDateFormat dateFormatter;

    private EditText fromTimeEtxt;
    private EditText toTimeEtxt;

    private TimePickerDialog fromTimePickerDialog;
    private TimePickerDialog toTimePickerDialog;

    private Button searchButton;

    private Spinner siteSpinner;
    private EditText tailleEtxt;

    private ArrayList<Site> listeSites;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.listeSites = new ArrayList<Site>();
        setContentView(R.layout.activity_main);

       /* Spinner spinner = (Spinner) findViewById(R.id.sites_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.sites_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);*/

        List<String> spinnerArray =  new ArrayList<String>();

        String targetURL = ("http://10.0.2.2:8080/Api/Sites");
        System.out.println("////////////////////// avant l'execute Get ! /////////////////////////");
        final AsyncTask<String, Void, ResponseHTTP> execute = new HttpGetRequestTask().execute(targetURL);
        try {
            ResponseHTTP result = execute.get();
            int responseCode = result.getResponseCode();
            switch(responseCode) {
                case 200:
                    String response = result.getResponseString();
                    System.out.println("["+responseCode+"] String réponse à /Api/Sites : "+response);
                    SAXBuilder saxBuilder = new SAXBuilder();
                    try {
                        Document doc = saxBuilder.build(new StringReader(response));
                        Element xmlfile = doc.getRootElement();
                        System.out.println("ROOT -->"+xmlfile);
                        List<Element> list = xmlfile.getChildren("Site");
                        System.out.println("Nombre de sites : "+list.size());

                        Iterator i = list.iterator();

                        while (i.hasNext()) {
                            Element courant = (Element)i.next();
                            Site s = new Site(courant);
                            System.out.println(s);
                            this.listeSites.add(s);
                        }
                        // you need to have a list of data that you want the spinner to display
                        for (int j = 0; j < this.listeSites.size(); ++j) {
                            spinnerArray.add(this.listeSites.get(j).getName());
                        }
                    } catch (JDOMException e) {
                        // handle JDOMExceptio n
                    } catch (IOException e) {
                        // handle IOException
                    }
                    break;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }



        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, spinnerArray);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner spinner = (Spinner) findViewById(R.id.sites_spinner);
        spinner.setAdapter(adapter);

        findViewsById();
        setDateTimeFields();

        this.dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        this.fromDateEtxt.setText(this.dateFormatter.format(new Date().getTime()));
        this.toDateEtxt.setText(this.dateFormatter.format(new Date().getTime()));


        DateFormat timeFormatter = new SimpleDateFormat("kk:mm");
        this.fromTimeEtxt.setText(timeFormatter.format(new Date().getTime()));
        this.toTimeEtxt.setText(timeFormatter.format(new Date().getTime()));
    }

    private void findViewsById() {
        this.fromDateEtxt = (EditText) findViewById(R.id.from_Date);
        this.fromDateEtxt.setInputType(InputType.TYPE_NULL);
        this.fromDateEtxt.requestFocus();
        this.toDateEtxt = (EditText) findViewById(R.id.to_Date);
        this.toDateEtxt.setInputType(InputType.TYPE_NULL);

        this.fromTimeEtxt = (EditText) findViewById(R.id.from_Time);
        this.fromTimeEtxt.setInputType(InputType.TYPE_NULL);
        this.toTimeEtxt = (EditText) findViewById(R.id.to_Time);
        this.toTimeEtxt.setInputType(InputType.TYPE_NULL);

        this.searchButton = (Button) findViewById(R.id.search);

        this.siteSpinner =(Spinner) findViewById(R.id.sites_spinner);
        this.tailleEtxt = (EditText) findViewById(R.id.taille);
    }

    private void setDateTimeFields() {
        this.fromDateEtxt.setOnClickListener(this);
        this.toDateEtxt.setOnClickListener(this);

        this.fromTimeEtxt.setOnClickListener(this);
        this.toTimeEtxt.setOnClickListener(this);

        Calendar newCalendar = Calendar.getInstance();

        this.fromDatePickerDialog = new DatePickerDialog(this, new OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                fromDateEtxt.setText(dateFormatter.format(newDate.getTime()));
                if (!before(dateFormatter.format(newDate.getTime()),toDateEtxt.getText().toString())){
                    toDateEtxt.setText(dateFormatter.format(newDate.getTime()));
                }
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        toDatePickerDialog = new DatePickerDialog(this, new OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                toDateEtxt.setText(dateFormatter.format(newDate.getTime()));
                if (!before(fromDateEtxt.getText().toString(),dateFormatter.format(newDate.getTime()))){
                    toDateEtxt.setText(dateFormatter.format(newDate.getTime()));
                }
            }
        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));


        fromTimePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                fromTimeEtxt.setText(hourOfDay + ":" + minute);
            }
        }, newCalendar.get(Calendar.HOUR_OF_DAY), newCalendar.get(Calendar.MINUTE), true);
        //fromTimePickerDialog.show();

        toTimePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                toTimeEtxt.setText(hourOfDay + ":" + minute);
            }
        }, newCalendar.get(Calendar.HOUR_OF_DAY), newCalendar.get(Calendar.MINUTE), true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
      //  getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void onClick(View view) {
        if(view == fromDateEtxt) {
            fromDatePickerDialog.show();
        }
        else if(view == toDateEtxt) {
            toDatePickerDialog.show();
        }
        else if(view == fromTimeEtxt) {
            fromTimePickerDialog.show();
        }
        else if(view == toTimeEtxt) {
            toTimePickerDialog.show();
        }
        else if (view == searchButton) {
            /*String charset = "UTF-8";  // Or in Java 7 and later, use the constant: java.nio.charset.StandardCharsets.UTF_8.name()
            int siteId=-1, int personCount=-1, List<ParticularityIdentifier> particularities=null
            String param1 = this.siteSpinner.getSelectedItem().toString();
            String param2 = this.tailleEtxt.getText().toString();

            try {
                String query = String.format("siteId=%s&personCount=%s",
                        URLEncoder.encode(param1, charset),ok
                        URLEncoder.encode(param2, charset));

                String targetURL = ("http://10.0.2.2:8080/Api/Search");
                System.out.println("////////////////////// avant l'execute Post ! /////////////////////////");
                final AsyncTask<String, Void, ResponseHTTP> execute = new HttpPostRequestTask().execute(targetURL, query);
                try {
                    ResponseHTTP result = execute.get();
                    int responseCode = result.getResponseCode();
                    switch(responseCode) {
                        case 200:
                            InputStream is = result.getResponse();
                            String response = convertStreamToString(is);
                            System.out.println("[" + responseCode + "] String réponse à la connexion: " + response);
                            break;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }*/
        }
    }

    // Exemple de paramètres HTTP contenant un tableau de valeurs : ?cars[]=Saab&cars[]=Audi

    private boolean before(String from, String to){
        String[] from_tab = from.split("-");
        String[] to_tab = to.split("-");

        if (Integer.parseInt(from_tab[2])<Integer.parseInt(to_tab[2])) {
            return true;
        }
        else if (Integer.parseInt(from_tab[2])==Integer.parseInt(to_tab[2])) {
            if (Integer.parseInt(from_tab[1])<Integer.parseInt(to_tab[1])){
                return true;
            }
            else if (Integer.parseInt(from_tab[1])==Integer.parseInt(to_tab[1])){
                if (Integer.parseInt(from_tab[0])<=Integer.parseInt(to_tab[0])){
                    return true;
                }
                else{
                    return false;
                }
            }
            else{
                return false;
            }
        }
        else {
            return false;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        String targetURL = ("http://10.0.2.2:8080/Api/Logout");
        System.out.println("////////////////////// avant l'execute Get ! /////////////////////////");
        final AsyncTask<String, Void, ResponseHTTP> execute = new HttpGetRequestTask().execute(targetURL);
        try {
            ResponseHTTP result = execute.get();
            int responseCode = result.getResponseCode();
            switch(responseCode) {
                case 200:
                    String response = result.getResponseString();
                    System.out.println("["+ responseCode + "] String réponse /Api/Logout : "+response);
                    break;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }


}