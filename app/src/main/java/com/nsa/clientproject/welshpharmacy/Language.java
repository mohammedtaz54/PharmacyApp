package com.nsa.clientproject.welshpharmacy;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Context;
import java.util.Locale;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.View;

import com.nsa.clientproject.welshpharmacy.models.PharmacyList;

public class Language extends AppCompatActivity implements View.OnClickListener {

    private ConstraintLayout languageSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        languageSettings = findViewById(R.id.language_settings);
        languageSettings.findViewById(R.id.english_button).setOnClickListener(this);
        languageSettings.findViewById(R.id.welsh_button).setOnClickListener(this);


    }
    @Override
    public void onClick(View x) {
        if (x.getId() == R.id.english_button) {
            Context context = this; // or ActivityNotification.this
            Locale language_code = Locale.ENGLISH;
            Resources res = context.getResources();
            // Change locale settings in the app.
            DisplayMetrics dm = res.getDisplayMetrics();
            android.content.res.Configuration conf = res.getConfiguration();
            conf.setLocale(language_code); // API 17+ only.
// Use conf.locale = new Locale(...) if targeting lower versions
            res.updateConfiguration(conf, dm);
            startActivity(new Intent(this, Language.class));
        }
//        if (x.getId() == R.id.welsh_button) {
//            Context context = this; // or ActivityNotification.this
//            Locale language_code = Locale.cy;
//            Resources res = context.getResources();
//            // Change locale settings in the app.
//            DisplayMetrics dm = res.getDisplayMetrics();
//            android.content.res.Configuration conf = res.getConfiguration();
//            conf.setLocale(language_code); // API 17+ only.
//// Use conf.locale = new Locale(...) if targeting lower versions
//            res.updateConfiguration(conf, dm);
//        }
    }
    }


