package com.nsa.clientproject.welshpharmacy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.ListView;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.widget.ArrayAdapter;
import android.widget.Toast;
import android.widget.AdapterView;
import android.view.View.OnClickListener;


import com.nsa.clientproject.welshpharmacy.models.Pharmacy;
import com.nsa.clientproject.welshpharmacy.models.PharmacyList;
import com.nsa.clientproject.welshpharmacy.models.PharmacyServices;


public class PharmacyView extends AppCompatActivity {
    ListView lv;
    String[] servicesConvert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.pharmacy_view);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Pharmacy pharmacy = (Pharmacy) getIntent().getSerializableExtra("pharmacy");

        //Change Text in name text field in single pharmacy view
        TextView singleName = (TextView) findViewById(R.id.single_name);
        singleName.setText(pharmacy.getName());
        //Change Text in times text field in single pharmacy view
        TextView singleTimes = (TextView) findViewById(R.id.single_times);
        singleTimes.setText(getString(R.string.opening_closing_time, pharmacy.getOpenTime(), pharmacy.getCloseTime()));

        //Change Text in location text field in single pharmacy view
        TextView singleLocation = (TextView) findViewById(R.id.single_location);
        singleLocation.setText(getString(R.string.address, pharmacy.getLocation()));

        //Change items in services list view in single pharmacy view
        ArrayList<String> services;

        //Reference: http://www.learn-android-easily.com/2013/05/populating-listview-with-arraylist.html
        //accessed 20 March 2018
        // Get the reference of ListView
        ListView serviceList = (ListView) findViewById(R.id.single_services);


        services = new ArrayList<String>();

        for(PharmacyServices pharmacyService: pharmacy.getServicesOffered()){
            //Reference:https://stackoverflow.com/questions/4427608/android-getting-resource-id-from-string
            //Accessed 3 April 2018
            services.add(getString(getResources().getIdentifier(pharmacyService.name(),"string",getPackageName())));
        }

        // Create The Adapter with passing ArrayList as 3rd parameter
        ArrayAdapter<String> arrayAdapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, services);
        // Set The Adapter
        serviceList.setAdapter(arrayAdapter);

        // register onClickListener to handle click events on each item
        ;

        //Reference: http://www.learn-android-easily.com/2013/05/populating-listview-with-arraylist.html
        //accessed 20 March 2018
        // Get the reference of ListView
        ListView serviceListWelsh = (ListView) findViewById(R.id.single_welsh_services);


        services = new ArrayList<String>();

        for(PharmacyServices pharmacyService: pharmacy.getServicesInWelsh()){
            //Reference:https://stackoverflow.com/questions/4427608/android-getting-resource-id-from-string
            //Accessed 3 April 2018
            services.add(getString(getResources().getIdentifier(pharmacyService.name(),"string",getPackageName())));
        }

        // Create The Adapter with passing ArrayList as 3rd parameter
        arrayAdapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, services);
        // Set The Adapter
        serviceListWelsh.setAdapter(arrayAdapter);

        // register onClickListener to handle click events on each item
        ;
/////////////////////////////////////////////////////////
        //Change items in services list view in single pharmacy view
        ArrayList<String> opening;

        //Reference: http://www.learn-android-easily.com/2013/05/populating-listview-with-arraylist.html
        //accessed 20 March 2018
        // Get the reference of ListView
        ListView serviceOpening = (ListView) findViewById(R.id.single_opening);

        ArrayList<String> timesArrayList = new ArrayList<>();

//        String value = (new ArrayList<String>(linkedHashMap.values())).get(pos);

        for(int i = 0; i < pharmacy.getOpeningTimes().values().size(); ++i){
                String openingString = new ArrayList<>(pharmacy.getOpeningTimes().values()).get(i).toString();
                String closingString = new ArrayList<>(pharmacy.getClosingTimes().values()).get(i).toString();
                String dayString = new ArrayList<>(pharmacy.getClosingTimes().keySet()).get(i).toString();
                timesArrayList.add(dayString +  ": " + openingString + " - " + closingString);
        }

        // Create The Adapter with passing ArrayList as 3rd parameter
        ArrayAdapter<String> arrayAdapterTime =
                new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, timesArrayList);
        // Set The Adapter
        serviceOpening.setAdapter(arrayAdapterTime);

        // register onClickListener to handle click events on each item
        ;


    }
}
