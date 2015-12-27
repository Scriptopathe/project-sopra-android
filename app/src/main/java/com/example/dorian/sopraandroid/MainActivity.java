package com.example.dorian.sopraandroid;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TimePicker;

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
    private SimpleDateFormat timeFormatter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Spinner spinner = (Spinner) findViewById(R.id.sites_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.sites_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        findViewsById();
        setDateTimeField();

        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        fromDateEtxt.setText(dateFormatter.format(new Date().getTime()));
        toDateEtxt.setText(dateFormatter.format(new Date().getTime()));


        dateFormatter = new SimpleDateFormat("kk:mm");
        fromTimeEtxt.setText(dateFormatter.format(new Date().getTime()));
        toTimeEtxt.setText(dateFormatter.format(new Date().getTime()));
    }

    private void findViewsById() {
        fromDateEtxt = (EditText) findViewById(R.id.from_Date);
        fromDateEtxt.setInputType(InputType.TYPE_NULL);
        fromDateEtxt.requestFocus();
        toDateEtxt = (EditText) findViewById(R.id.to_Date);
        toDateEtxt.setInputType(InputType.TYPE_NULL);

        fromTimeEtxt = (EditText) findViewById(R.id.from_Time);
        fromTimeEtxt.setInputType(InputType.TYPE_NULL);
        toTimeEtxt = (EditText) findViewById(R.id.to_Time);
        toTimeEtxt.setInputType(InputType.TYPE_NULL);
    }

    private void setDateTimeField() {
        fromDateEtxt.setOnClickListener(this);
        toDateEtxt.setOnClickListener(this);

        fromTimeEtxt.setOnClickListener(this);
        toTimeEtxt.setOnClickListener(this);

        Calendar newCalendar = Calendar.getInstance();

        fromDatePickerDialog = new DatePickerDialog(this, new OnDateSetListener() {
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
    }


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

}