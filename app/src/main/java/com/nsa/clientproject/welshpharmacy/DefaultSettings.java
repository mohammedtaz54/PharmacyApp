package com.nsa.clientproject.welshpharmacy;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;
import android.widget.Toast;

import java.security.Key;

public class DefaultSettings extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener, SharedPreferences.OnSharedPreferenceChangeListener {


    private AppCompatCheckBox bloodPressure;
    private AppCompatCheckBox fluShot;
    private AppCompatButton saveButton;
    private AppCompatEditText postcode;
    private AppCompatEditText maxdistance;

    private SharedPreferences sharedPreferences;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_default_settings);

        //fetches widgets

        this.bloodPressure = this.findViewById(R.id.cbBP);
        this.fluShot = this.findViewById(R.id.cbFS);
        this.saveButton = this.findViewById(R.id.savebutton);
        this.postcode = this.findViewById(R.id.user_postcode);
        this.maxdistance = this.findViewById(R.id.pharmacy_max_distance);

        //initialises sharededPreferences object
        this.sharedPreferences = this.getPreferences(MODE_PRIVATE);

        // method used to set the current state of widgets in sharedPreferences
        initValues();

        this.saveButton.setOnClickListener(this);
        this.saveButton.setOnLongClickListener(this);
    }

    //Sets the values for the widgets based on values in sharedPreferences
    private void initValues() {
        if (this.sharedPreferences !=null) {
            this.bloodPressure.setChecked(sharedPreferences.getBoolean(KeyValueHelper.KEY_BLOODPRESSURE_CHECKBOX, KeyValueHelper.DEFAULT_BLOODPRESSURE_CHECKBOX));
            this.fluShot.setChecked(sharedPreferences.getBoolean(KeyValueHelper.KEY_FLUSHOT_CHECKBOX, KeyValueHelper.DEFAULT_FLUSHOT_CHECKBOX));
            this.postcode.setText(this.sharedPreferences.getString(KeyValueHelper.KEY_POSTCODE_TEXT, KeyValueHelper.DEFAULT_POSTCODE_TEXT));
            this.maxdistance.setText(this.sharedPreferences.getString(KeyValueHelper.KEY_MAXDISTANCE_TEXT, KeyValueHelper.DEFAULT_MAXDISTANCE_TEXT));
        }
    }

    //Register a listener object on sharedPreferences that listens to the changes if object exists
    @Override
    protected void onResume() {
        super.onResume();
        if (this.sharedPreferences !=null)
            this.sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    //Unregister a listener when activity is paused as activity is used for listener
    @Override
    protected void onPause() {
        super.onPause();
        if (this.sharedPreferences != null)
            this.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
    }

    /* Check whether view clicked is save button and sharedpreferences has been initialised.
    If it has the current state is added to sharedpreferences */
    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.savebutton && this.sharedPreferences != null) {
            SharedPreferences.Editor editor = this.sharedPreferences.edit();
            editor.putBoolean(KeyValueHelper.KEY_BLOODPRESSURE_CHECKBOX, this.bloodPressure.isChecked());
            editor.putBoolean(KeyValueHelper.KEY_FLUSHOT_CHECKBOX, this.fluShot.isChecked());
            editor.putString(KeyValueHelper.KEY_POSTCODE_TEXT, this.postcode.getText().toString());
            editor.putString(KeyValueHelper.KEY_MAXDISTANCE_TEXT, this.maxdistance.getText().toString());

            editor.apply();
        }
    }

    /* If the view clicked is submit button and sharedpreferences has been initialised,
     it clears all entries and shows a Toast indicating values have been reset
     Values go back to their default after this*/
    @Override
    public boolean onLongClick(View view) {
        int id = view.getId();

        if (id == R.id.savebutton && this.sharedPreferences != null) {
            this.sharedPreferences.edit().clear().apply();
            //triggered when either of these methods are called
            Toast.makeText(this, getString(R.string.reset_text), Toast.LENGTH_SHORT).show();
            initValues();
        }
        return true;
    }

    // Adds a toast message that tells user changes have been saved
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        Toast.makeText(this, R.string.saved_message, Toast.LENGTH_SHORT).show();
    }
}