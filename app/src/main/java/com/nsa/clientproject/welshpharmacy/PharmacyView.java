package com.nsa.clientproject.welshpharmacy;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.ListView;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.LinkedHashMap;

import android.widget.ArrayAdapter;
import android.widget.Toast;
import android.widget.AdapterView;
import android.view.View.OnClickListener;


import com.nsa.clientproject.welshpharmacy.models.Pharmacy;
import com.nsa.clientproject.welshpharmacy.models.PharmacyList;
import com.nsa.clientproject.welshpharmacy.models.PharmacyServices;


public class PharmacyView extends AppCompatActivity {
    /**
     * Stores the plus menu instance
     */
    private FloatingActionButton plusmenu;
    /**
     * Stores the thumbs up button
     */
    private FloatingActionButton thumbsup;
    /**
     * Stores the thumbs down button
     */
    private FloatingActionButton thumbsdown;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.pharmacy_view);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final Pharmacy pharmacy = (Pharmacy) getIntent().getSerializableExtra("pharmacy");

        final Button btn = (Button) findViewById(R.id.call_button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri number = Uri.parse("tel:" + pharmacy.getPhone());
                Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
                startActivity(callIntent);
            }
        });

        Button btn1 = (Button) findViewById(R.id.map_button);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + pharmacy.getLocation());
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });


        plusmenu = findViewById(R.id.plusmenu);
        thumbsup = findViewById(R.id.thumbsup);
        thumbsdown = findViewById(R.id.thumbsdown);

        /*Floating action button methods shows / hides the thumbs up if menu is clicked
        The default for the thumbs up / thumbs down button is set to invisible. Once the plus
        button is clicked, the two thumbs buttons appear. Once clicked again they disappear.
        */
        plusmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (thumbsup.getVisibility() == View.VISIBLE && thumbsdown.getVisibility() == View.VISIBLE) {
                    thumbsup.setVisibility(View.GONE);
                    thumbsdown.setVisibility(View.GONE);
                } else {
                    thumbsup.setVisibility(View.VISIBLE);
                    thumbsdown.setVisibility(View.VISIBLE);
                }
            }
        });
        //toast if the thumbs up button is clicked "Liked"
        thumbsup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                thumbsup.setVisibility(View.GONE);
                thumbsdown.setVisibility(View.GONE);

                Toast.makeText(PharmacyView.this, R.string.liked, Toast.LENGTH_SHORT).show();
            }
        });
        //toast if the thumbs down button is clicked "Disliked"
        thumbsdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                thumbsup.setVisibility(View.GONE);
                thumbsdown.setVisibility(View.GONE);

                Toast.makeText(PharmacyView.this, R.string.disliked, Toast.LENGTH_SHORT).show();
            }
        });


        //Change Text in name text field in single pharmacy view
        TextView singleName = findViewById(R.id.single_name);
        singleName.setText(pharmacy.getName());
        //Not necessary as we're showing all of them lower
        //Change Text in times text field in single pharmacy view
//        TextView singleTimes = (TextView) findViewById(R.id.single_times);
//        singleTimes.setText(getString(R.string.opening_closing_time, pharmacy.getOpenTime(), pharmacy.getCloseTime()));

        //Change Text in location text field in single pharmacy view
        TextView singleLocation = findViewById(R.id.single_location);
        singleLocation.setText(getString(R.string.address, pharmacy.getLocation()));

        //Change Text in website text field in single pharmacy view
        TextView webLink = findViewById(R.id.link);
        webLink.setText(pharmacy.getWebsite());

        //Change items in services list view in single pharmacy view
        ArrayList<String> services;

        //Reference: http://www.learn-android-easily.com/2013/05/populating-listview-with-arraylist.html
        //accessed 20 March 2018
        // Get the reference of ListView
        ListView serviceList = findViewById(R.id.single_services);


        services = new ArrayList<String>();

        for (PharmacyServices pharmacyService : pharmacy.getServicesOffered()) {
            //Reference:https://stackoverflow.com/questions/4427608/android-getting-resource-id-from-string
            //Accessed 3 April 2018
            services.add(getString(getResources().getIdentifier(pharmacyService.name(), "string", getPackageName())));
        }

        // Create The Adapter with passing ArrayList as 3rd parameter
        ArrayAdapter<String> arrayAdapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, services);
        // Set The Adapter
        serviceList.setAdapter(arrayAdapter);

        // register onClickListener to handle click events on each item

        //Reference: http://www.learn-android-easily.com/2013/05/populating-listview-with-arraylist.html
        //accessed 20 March 2018
        // Get the reference of ListView
        ListView serviceListWelsh = findViewById(R.id.single_welsh_services);


        services = new ArrayList<String>();

        for (PharmacyServices pharmacyService : pharmacy.getServicesInWelsh()) {
            //Reference:https://stackoverflow.com/questions/4427608/android-getting-resource-id-from-string
            //Accessed 3 April 2018
            services.add(getString(getResources().getIdentifier(pharmacyService.name(), "string", getPackageName())));
        }

        // Create The Adapter with passing ArrayList as 3rd parameter
        arrayAdapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, services);
        // Set The Adapter
        serviceListWelsh.setAdapter(arrayAdapter);

        // register onClickListener to handle click events on each item

        //Change items in services list view in single pharmacy view
        ArrayList<String> opening;

        //Reference: http://www.learn-android-easily.com/2013/05/populating-listview-with-arraylist.html
        //accessed 20 March 2018
        // Get the reference of ListView
        ListView serviceOpening = findViewById(R.id.single_opening);

        ArrayList<String> timesArrayList = new ArrayList<>();


        for (int i = 0; i < pharmacy.getOpeningTimes().values().size(); ++i) {
            //Reference:https://stackoverflow.com/questions/4427608/android-getting-resource-id-from-string
            //Accessed 3 April 2018

            String openingString = new ArrayList<>(pharmacy.getOpeningTimes().values()).get(i).toString();
            String closingString = new ArrayList<>(pharmacy.getClosingTimes().values()).get(i).toString();
            String dayIdentifier = new ArrayList<>(pharmacy.getClosingTimes().keySet()).get(i).toString();
            String dayString = getString(getResources().getIdentifier(dayIdentifier, "string", getPackageName()));
            if (openingString.equals(closingString)) {
                timesArrayList.add(dayString + ": " + getString(R.string.closed));
            } else {
                timesArrayList.add(dayString + ": " + openingString + " - " + closingString);
            }
        }

        // Create The Adapter with passing ArrayList as 3rd parameter
        ArrayAdapter<String> arrayAdapterTime =
                new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, timesArrayList);
        // Set The Adapter
        serviceOpening.setAdapter(arrayAdapterTime);
        setListViewHeightBasedOnChildren(serviceOpening);
        setListViewHeightBasedOnChildren(serviceList);
        setListViewHeightBasedOnChildren(serviceListWelsh);
        // register onClickListener to handle click events on each item


    }

    //Reference: https://stackoverflow.com/questions/18367522/android-list-view-inside-a-scroll-view/20475821
    //Accessed on 4 April 2018
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }
}
