package com.nsa.clientproject.welshpharmacy;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.View;
import android.widget.Toast;

public class DefaultSettings extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener, SharedPreferences.OnSharedPreferenceChangeListener {


    private AppCompatCheckBox bloodPressure;
    private AppCompatCheckBox fluShot;
    private AppCompatButton saveButton;

    private SharedPreferences sharedPreferences;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_default_settings);

        //fetches widgets

        this.bloodPressure = this.findViewById(R.id.cbBP);
        this.fluShot = this.findViewById(R.id.cbFS);
        this.saveButton = this.findViewById(R.id.savebutton);

        //initialises sharededPreferences object
        this.sharedPreferences = this.getPreferences(MODE_PRIVATE);

        // method used to set the current state of widgets in sharedPreferences
        initValues();

        this.saveButton.setOnClickListener(this);
        this.saveButton.setOnLongClickListener(this);
    }


    private void initValues() {
        if (this.sharedPreferences !=null) {
            this.bloodPressure.setChecked(sharedPreferences.getBoolean(KeyValueHelper.KEY_BLOODPRESSURE_CHECKBOX, KeyValueHelper.DEFAULT_BLOODPRESSURE_CHECKBOX));
            this.fluShot.setChecked(sharedPreferences.getBoolean(KeyValueHelper.KEY_FLUSHOT_CHECKBOX, KeyValueHelper.DEFAULT_FLUSHOT_CHECKBOX));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (this.sharedPreferences !=null)
            this.sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (this.sharedPreferences != null)
            this.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.savebutton && this.sharedPreferences != null) {
            SharedPreferences.Editor editor = this.sharedPreferences.edit();
            editor.putBoolean(KeyValueHelper.KEY_BLOODPRESSURE_CHECKBOX, this.bloodPressure.isChecked());
            editor.putBoolean(KeyValueHelper.KEY_FLUSHOT_CHECKBOX, this.fluShot.isChecked());

            editor.apply();
        }
    }

    @Override
    public boolean onLongClick(View view) {
        int id = view.getId();

        if (id == R.id.savebutton && this.sharedPreferences != null) {
            this.sharedPreferences.edit().clear().commit();
            Toast.makeText(this, getString(R.string.reset_text), Toast.LENGTH_SHORT).show();
            initValues();
        }
        return true;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        Toast.makeText(this, R.string.saved_message, Toast.LENGTH_SHORT).show();
    }
}