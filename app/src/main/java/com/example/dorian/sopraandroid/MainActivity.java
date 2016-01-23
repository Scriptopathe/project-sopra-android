package com.example.dorian.sopraandroid;

import java.io.IOException;
import java.io.StringReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.example.dorian.sopraandroid.model.Booking;
import com.example.dorian.sopraandroid.model.Particularity;
import com.example.dorian.sopraandroid.model.Room;
import com.example.dorian.sopraandroid.model.Site;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

/**
 * This activity is the main activity of the application, where the research of rooms is made and where
 * the results are displayed.
 */
public class MainActivity extends AppCompatActivity implements OnClickListener {

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
    private Spinner timeDurationSpinner;
    private EditText tailleEtxt;

    private LinearLayout checkBoxesLayout;

    private ArrayList<Site> listeSites;
    private ArrayList<Particularity> listeParticularities;

    private ExpandableListView expList;
    private ExpandableListAdapter listAdapter;
    private List<String> listDataHeader;
    private HashMap<String, List<String>> listDataChild;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.listeSites = new ArrayList<Site>();
        this.listeParticularities = new ArrayList<Particularity>();

        setContentView(R.layout.activity_main);
        findViewsById();

        this.timeDurationSpinner = (Spinner) findViewById(R.id.time_duration_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapterTimeDurationSpinner = ArrayAdapter.createFromResource(this,
                R.array.time_duration_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapterTimeDurationSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        this.timeDurationSpinner.setAdapter(adapterTimeDurationSpinner);

        List<String> spinnerArray =  new ArrayList<String>();

        /*** Requete Sites ***/
        String targetURL = ("http://"+Constantes.SERVER+"/Api/Sites");
        System.out.println("////////////////////// avant l'execute Get requete Sites ! /////////////////////////");
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
        this.siteSpinner.setAdapter(adapter);


        /*** Requete Location ***/
        targetURL = ("http://"+Constantes.SERVER+"/Api/Location");
        System.out.println("////////////////////// avant l'execute Get Requete Location ! /////////////////////////");
        final AsyncTask<String, Void, ResponseHTTP> execute2 = new HttpGetRequestTask().execute(targetURL);
        try {
            ResponseHTTP result = execute2.get();
            int responseCode = result.getResponseCode();
            switch(responseCode) {
                case 200:
                    String str = result.getResponseString().replaceAll("(\\r|\\n)", "");
                    int response = Integer.parseInt(str);
                    System.out.println("["+responseCode+"] String réponse à /Api/Location : "+response);
                    this.siteSpinner.setSelection(response-1);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }



        setDateTimeFields();

        this.dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        this.fromDateEtxt.setText(this.dateFormatter.format(new Date().getTime()));
        this.toDateEtxt.setText(this.dateFormatter.format(new Date().getTime()));


        DateFormat timeFormatter = new SimpleDateFormat("HH:mm");
        this.fromTimeEtxt.setText(timeFormatter.format(new Date().getTime()));
        this.toTimeEtxt.setText(timeFormatter.format(new Date().getTime()));

        /*** Requete Particulariré ***/
        targetURL = ("http://"+Constantes.SERVER+"/Api/Particularities");
        System.out.println("////////////////////// avant l'execute Get requete particularite! /////////////////////////");
        final AsyncTask<String, Void, ResponseHTTP> execute3 = new HttpGetRequestTask().execute(targetURL);
        try {
            ResponseHTTP result = execute3.get();
            int responseCode = result.getResponseCode();
            switch(responseCode) {
                case 200:
                    String response = result.getResponseString();
                    System.out.println("["+responseCode+"] String réponse à /Api/Particularities : "+response);
                    SAXBuilder saxBuilder = new SAXBuilder();
                    try {
                        Document doc = saxBuilder.build(new StringReader(response));
                        Element xmlfile = doc.getRootElement();
                        System.out.println("ROOT -->"+xmlfile);
                        List<Element> list = xmlfile.getChildren("Particularity");
                        Iterator i = list.iterator();

                        while (i.hasNext()) {
                            Element courant = (Element)i.next();
                            Particularity p = new Particularity(courant);
                            this.listeParticularities.add(p);
                        }
                        for (int j = 0; j < this.listeParticularities.size(); ++j) {
                            CheckBox cb = new CheckBox(getApplicationContext());
                            cb.setText(this.listeParticularities.get(j).getName());
                            cb.setId(this.listeParticularities.get(j).getId());
                            cb.setTextColor(Color.BLACK);
                            cb.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                            this.checkBoxesLayout.addView(cb);
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

        /*** Result ***/
        this.listAdapter = new ExpandableListAdapter(this, null, null);
        this.listDataHeader = new ArrayList<String>();
        this.listDataChild = new HashMap<String, List<String>>();
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
        this.searchButton.setOnClickListener(this);

        this.siteSpinner = (Spinner) findViewById(R.id.sites_spinner);
        this.timeDurationSpinner = (Spinner) findViewById(R.id.time_duration_spinner);
        this.tailleEtxt = (EditText) findViewById(R.id.taille);

        this.checkBoxesLayout = (LinearLayout) findViewById(R.id.checkBoxesLayout);

        this.expList = (ExpandableListView) findViewById(R.id.lvExp);
    }

    /**
     * This method is here to set the time and date fields with the current time and date values. And
     * it also configures the specific date and time pickers.
     */
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

        toTimePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                toTimeEtxt.setText(hourOfDay + ":" + minute);
            }
        }, newCalendar.get(Calendar.HOUR_OF_DAY), newCalendar.get(Calendar.MINUTE), true);
    }

    /**
     * Handles the displaying of the different date and time pickers.
     * And it's also in this method that we display the rooms corresponding to the response of our
     * search request when we click on the "Search" button.
     * @param view
     */
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
            int param1 = this.siteSpinner.getSelectedItemPosition()+1;
            Integer param2 = null;
            if (!this.tailleEtxt.getText().toString().equals("")) {
                param2 = Integer.parseInt(this.tailleEtxt.getText().toString());
            }
            Integer param3 = null;
            int timeDurationSelected = this.timeDurationSpinner.getSelectedItemPosition();
            switch (timeDurationSelected) {
                case 0: param3 = 15;
                    break;
                case 1: param3 = 30;
                    break;
                case 2: param3 = 45;
                    break;
                case 3: param3 = 60;
                    break;
                case 4: param3 = 75;
                    break;
                case 5: param3 = 90;
                    break;
                case 6: param3 = 105;
                    break;
                case 7: param3 = 120;
                    break;
                case 8: param3 = 135;
                    break;
                case 9: param3= 150;
                    break;
                case 10: param3 = 165;
                    break;
                case 11: param3 = 180;
                    break;
                case 12: param3 = 195;
                    break;
                case 13: param3 = 210;
                    break;
                case 14: param3 = 225;
                    break;
                case 15: param3 = 240;
                    break;
            }
            String startDate = fromDateEtxt.getText().toString().replaceAll("-","/");
            String endDate = toDateEtxt.getText().toString().replaceAll("-","/");
            String param4 = startDate+"-"+fromTimeEtxt.getText().toString()+":00";
            String param5 = endDate+"-"+toTimeEtxt.getText().toString()+":00";
            String query;
            if (param2 != null) {
                query = String.format("siteId=" + param1 + "&personCount=" + param2 + "&meetingDuration=" + param3 + "&startDate=" + param4 + "&endDate=" + param5);
            }
            else {
                query = String.format("siteId=" + param1 + "&personCount=1&meetingDuration=" + param3 + "&startDate=" + param4 + "&endDate=" + param5);
            }
            String targetURL = ("http://"+Constantes.SERVER+"/Api/Search");
            System.out.println("////////////////////// avant l'execute Get Search ! /////////////////////////");
            final AsyncTask<String, Void, ResponseHTTP> execute = new HttpGetRequestTask().execute(targetURL, query);
            ResponseHTTP result = null;
            try {
                result = execute.get();
                int responseCode = result.getResponseCode();
                switch(responseCode) {
                    case 200:
                        String response = result.getResponseString();
                        System.out.println("[" + responseCode + "] String réponse à la connexion: " + response);
                        SAXBuilder saxBuilder = new SAXBuilder();
                        try {
                            Document doc = saxBuilder.build(new StringReader(response));
                            Element xmlfile = doc.getRootElement();
                            System.out.println("ROOT -->" + xmlfile);
                            //Liste d'element contenant une salle et ses bookings dispo
                            List<Element> listOfRoomSearchResult = xmlfile.getChildren("RoomSearchResult");
                            Iterator i = listOfRoomSearchResult.iterator();
                            System.out.println("list of roomSearchResult : "+listOfRoomSearchResult.size());
                            int l =0;
                            while (i.hasNext() ) {
                                //liste temporaire pour les fils
                                List<String> tempList = new ArrayList<String>();

                                //On récupère l'element courant de RoomSearchResult
                                Element courantRoomSearch = (Element) i.next();

                                System.out.println("on sauvegarde la valeur de room");
                                //on sauvegarde la room
                                Room r = new Room(courantRoomSearch);

                                //Le nom de la salle devient un header pour l'affichage
                                this.listDataHeader.add(r.getName());

                                //les bookings de la salle deviennent des childen pour l'affichage
                                ArrayList<Booking> bookings = r.getBookings();
                                for (int k=0; k<bookings.size(); k++) {
                                    tempList.add(bookings.get(k).getDay() + " from " + bookings.get(k).getStartTime() + " to " + bookings.get(k).getEndTime());
                                }
                                this.listDataChild.put(listDataHeader.get(l), tempList); // Header, Child data
                                l++;
                            }
                        } catch (JDOMException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            System.out.println("List Data Header : " +this.listDataHeader.toString());
            listAdapter.setLists(this.listDataHeader, this.listDataChild);
            // setting list adapter
            this.expList.setAdapter(listAdapter);
        }
    }

    /**
     * Permet de vérifier que la date de début ne soit pas après la date de fin et inversement
     * @param from La chaîne de caractères correspondant à la date de début
     * @param to La chaîne de caractères correspondant à la date de fin
     * @return Vrai si la date de début est bien avant la date de fin, faux sinon.
     */
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

    /**
     * Quand on quitte l'activité en touchant le bouton "Précédent" d'Android, cela nous déconnecte (n'est pas appelé quand l'application est quitté autrement)
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        String targetURL = ("http://"+Constantes.SERVER+"/Api/Logout");
        System.out.println("////////////////////// avant l'execute Get Logout! /////////////////////////");
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
