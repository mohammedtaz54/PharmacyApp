package com.nsa.clientproject.welshpharmacy;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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

public class Language extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
