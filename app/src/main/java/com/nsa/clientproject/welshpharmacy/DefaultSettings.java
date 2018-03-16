package com.nsa.clientproject.welshpharmacy;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatCheckBox;

public class DefaultSettings extends AppCompatActivity {

    private AppCompatCheckBox cbBP;
    private AppCompatCheckBox cbFS;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_default_settings);

        initViews();

    }

    private void initViews() {
        cbBP = (AppCompatCheckBox) findViewById(R.id.cbBP);
        cbFS = (AppCompatCheckBox) findViewById(R.id.cbFS);

    }

    public void
}
