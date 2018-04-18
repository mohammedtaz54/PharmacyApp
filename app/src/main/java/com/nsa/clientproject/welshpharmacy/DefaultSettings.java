package com.nsa.clientproject.welshpharmacy;

import android.content.SharedPreferences;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import java.security.Key;

public class DefaultSettings extends AppCompatActivity implements View.OnClickListener {


    private ConstraintLayout servicesSettings;
    private ConstraintLayout welshServicesSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_default_settings);
        servicesSettings = findViewById(R.id.services_settings);
        welshServicesSettings = findViewById(R.id.welsh_services_settings);
        welshServicesSettings.setVisibility(View.GONE);
        servicesSettings.findViewById(R.id.show_welsh_services).setOnClickListener(this);
        welshServicesSettings.findViewById(R.id.show_english_services).setOnClickListener(this);
        welshServicesSettings.findViewById(R.id.show_location_options).setOnClickListener(this);
    }

    /**
     * Takes care of the previous/next buttons in the activity, as well as the submit
     */
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.show_welsh_services) {
            servicesSettings.setVisibility(View.GONE);
            welshServicesSettings.setVisibility(View.VISIBLE);
        }
        if (v.getId() == R.id.show_english_services) {
            welshServicesSettings.setVisibility((View.GONE));
            servicesSettings.setVisibility(View.VISIBLE);
        }
        if (v.getId() == R.id.show_location_options) {
            //todo: this should be moved to the final option when it's implemented
            saveSettings();
        }
    }

    /**
     * Saves all the settings into shared preferences
     */
    private void saveSettings() {

        SharedPreferences defaultSettings = getSharedPreferences("DEFAULT_SETTINGS", MODE_PRIVATE);
        SharedPreferences.Editor editor = defaultSettings.edit();
        //Reference: https://stackoverflow.com/questions/7784418/get-all-child-views-inside-linearlayout-at-once?utm_medium=organic&utm_source=google_rich_qa&utm_campaign=google_rich_qa
        //Accessed 18 April 2018
        LinearLayoutCompat servicesRequired = findViewById(R.id.services_required);
        for (int i = 0; i < servicesRequired.getChildCount(); i++) {
            CheckBox currentCheckBox = (CheckBox) servicesRequired.getChildAt(i);
            editor.putBoolean("REQUIRES_SERVICE_" + currentCheckBox.getTag(), currentCheckBox.isChecked());


        }

        LinearLayoutCompat servicesRequiredWelsh = findViewById(R.id.services_required_welsh);
        for (int i = 0; i < servicesRequiredWelsh.getChildCount(); i++) {
            CheckBox currentCheckBox = (CheckBox) servicesRequiredWelsh.getChildAt(i);
            editor.putBoolean("REQUIRES_SERVICE_WELSH_" + currentCheckBox.getTag(), currentCheckBox.isChecked());
        }
        editor.apply();
        Toast.makeText(this, R.string.saved_message, Toast.LENGTH_SHORT).show();
    }
}