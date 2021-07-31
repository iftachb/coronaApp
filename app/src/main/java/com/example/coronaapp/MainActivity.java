package com.example.coronaapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;

import android.content.Context;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements Event {
    final static String LOCAL_EXTENDED = "T00:00:00Z";
    private ListView listView;
    private TextView textViewCountry;
    private String[] listItem;
    private Button buttonTimeRange;
    private MaterialDatePicker picker;
    private TextView textViewFrom;
    private TextView textViewTo;
    private Button buttonGetByCountry;
    private Button buttonGetByMyCountry;
    private Button buttonBlueTooth;

    private TextView textViewConfirmed;
    private TextView textViewRecovered;
    private TextView textViewDeaths;

    private MainManager mainManager;

    private String country;
    private String fromDateString;
    private String toDateString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainManager = MainManager.getInstance();
        mainManager.registerToEvents(this);
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mainManager.setLocationManager(locationManager, this);
        mainManager.bluetoothEnableDiscoverability(this);

        listView = findViewById(R.id.listViewCountries);
        textViewCountry = findViewById(R.id.textViewCountry);
        listItem = getResources().getStringArray(R.array.array_technology);
        textViewFrom = findViewById(R.id.textViewFrom);
        textViewTo = findViewById(R.id.textViewTo);

        textViewConfirmed = findViewById(R.id.textViewConfirmed);
        textViewRecovered = findViewById(R.id.textViewRecovered);
        textViewDeaths = findViewById(R.id.textViewDeaths);

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, listItem);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((adapterView, view, position, l) -> {
            country = adapter.getItem(position);
            textViewCountry.setText("Country: " + country);
            if (fromDateString != null && toDateString != null) {
                buttonGetByCountry.setEnabled(true);
            }


            Log.d("a", mainManager.getLocalCountry());
        });

        buttonTimeRange = findViewById(R.id.buttonTimeRange);
        buttonTimeRange.setOnClickListener(v ->
                picker.show(MainActivity.this.getSupportFragmentManager(), picker.toString())
        );

        buttonGetByCountry = findViewById(R.id.buttonGetByCountry);
        buttonGetByCountry.setOnClickListener(v -> {
            // ask the data
            if (country == null || fromDateString == null || toDateString == null) {
                Toast.makeText(MainActivity.this,
                        "No enough information to get by country",
                        Toast.LENGTH_LONG).show();
            }
            else {
                mainManager.getByCountry(country, fromDateString + LOCAL_EXTENDED, toDateString + LOCAL_EXTENDED);
            }

        });

        buttonGetByMyCountry = findViewById(R.id.buttonGetByMyCountry);
        buttonGetByMyCountry.setOnClickListener(v -> {
            // ask the data
            if ( fromDateString == null || toDateString == null) {
                Toast.makeText(MainActivity.this,
                        "No enough information to get by country",
                        Toast.LENGTH_LONG).show();
                String Country = mainManager.getLocalCountry();
            }
            else {
                String localCountry = mainManager.getLocalCountry();
                if (localCountry == null) {
                    Toast.makeText(MainActivity.this,
                            "don't know the local country",
                            Toast.LENGTH_LONG).show();
                }
                else {
                    mainManager.getByCountry(localCountry, fromDateString + "T00:00:00Z", toDateString + "T00:00:00Z");
                }
            }

        });
        buttonBlueTooth = findViewById(R.id.buttonBlueTooth);
        buttonBlueTooth.setOnClickListener(v -> {
            // activate bluetooth discovery
            mainManager.getBluetoothNearby();
        });

        initPicker();

        picker.addOnPositiveButtonClickListener((MaterialPickerOnPositiveButtonClickListener<Pair<Long, Long>>) selection -> {
            fromDateString = new SimpleDateFormat("MM/dd/yyyy").format(new Date(selection.first));
            toDateString = new SimpleDateFormat("MM/dd/yyyy").format(new Date(selection.second));
            textViewFrom.setText("From " + fromDateString);
            textViewTo.setText("To " + toDateString);
            if (country != null) {
                buttonGetByCountry.setEnabled(true);
            }
            buttonGetByMyCountry.setEnabled(true);
        });


    }

    private void initPicker() {
        MaterialDatePicker.Builder<Pair<Long, Long>> builder = MaterialDatePicker.Builder.dateRangePicker();
        Calendar now = Calendar.getInstance();
        builder.setSelection(new Pair(now.getTimeInMillis(), now.getTimeInMillis()));
        picker = builder.build();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mainManager.unregisterToEvents(this);
        mainManager.bluetoothDisableDiscoverability(this);
    }

    @Override
    public void reportCovid(String status, int sum) {
        Log.d("reportCovid", "status "+status +", "+sum);
        if (status.equals(CommApi.CONFIRMED)) {
            textViewConfirmed.setText(CommApi.CONFIRMED + ": " + sum);
        } else  if (status.equals(CommApi.RECOVERED)) {
            textViewRecovered.setText(CommApi.RECOVERED + ": " + sum);
        } else  if (status.equals(CommApi.DEATHS)) {
            textViewDeaths.setText(CommApi.DEATHS + ": " + sum);
        }
    }

    @Override
    public void findBluetoothNearby(String name) {
        Toast.makeText(MainActivity.this,
                "Find infected user nearby. User "+ name,
                Toast.LENGTH_LONG).show();

    }
}