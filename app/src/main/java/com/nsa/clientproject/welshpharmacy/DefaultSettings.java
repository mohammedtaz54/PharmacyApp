package com.nsa.clientproject.welshpharmacy;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.nsa.clientproject.welshpharmacy.models.PharmacyServices;

import java.io.IOException;
import java.security.Key;
import java.util.List;


public class DefaultSettings extends AppCompatActivity implements View.OnClickListener {

    /**
     * Stores the layout that contains the required services in english
     */
    private ConstraintLayout servicesSettings;
    /**
     * Stores the layout that contains the required services in Welsh
     */
    private ConstraintLayout welshServicesSettings;
    /**
     * Stores the layout that contains the location settings
     */
    private ConstraintLayout locationSettings;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings_pharmacies_list,menu);
        return true;
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()){
////            case R.id.add:
////                Intent i = new Intent(this,AddPharmacyActivity.class);
////                startActivity(i);
////                break;
//            case R.id.settings_language:
//
//                Intent languageIntent = new Intent(this, Language.class);
//                languageIntent.putExtra("previousActivity", "Settings");
//                startActivity(languageIntent);
//
//                this.setTitle(getString(R.string.title_activity_pharmacy_list));
//                break;
//        }
//        return true;
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MultiPharmacyActivity.setLanguage(this);

        this.setTitle(getString(R.string.app_name));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_default_settings);
        servicesSettings = findViewById(R.id.services_settings);
        welshServicesSettings = findViewById(R.id.welsh_services_settings);
        locationSettings = findViewById(R.id.location_settings);
        LinearLayoutCompat servicesRequired = findViewById(R.id.services_required);
        LinearLayoutCompat servicesRequiredWelsh = findViewById(R.id.services_required_welsh);

        for (PharmacyServices service : PharmacyServices.values()) {
            AppCompatCheckBox currentCheckbox = new AppCompatCheckBox(this);
            currentCheckbox.setTag(service.name());
            currentCheckbox.setText(getResources().getIdentifier(service.name(), "string", getPackageName()));
            AppCompatCheckBox currentCheckboxWelsh = new AppCompatCheckBox(this);
            currentCheckboxWelsh.setTag(service.name());
            currentCheckboxWelsh.setText(getResources().getIdentifier(service.name(), "string", getPackageName()));
            servicesRequired.addView(currentCheckbox);
            servicesRequiredWelsh.addView(currentCheckboxWelsh);
        }
        welshServicesSettings.setVisibility(View.GONE);
        locationSettings.setVisibility(View.GONE);

        servicesSettings.findViewById(R.id.show_welsh_services).setOnClickListener(this);
        welshServicesSettings.findViewById(R.id.show_english_services).setOnClickListener(this);
        welshServicesSettings.findViewById(R.id.show_location_options).setOnClickListener(this);
        locationSettings.findViewById(R.id.finish).setOnClickListener(this);
        locationSettings.findViewById(R.id.show_welsh_services_back).setOnClickListener(this);

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
            welshServicesSettings.setVisibility((View.GONE));
            locationSettings.setVisibility(View.VISIBLE);
        }
        if (v.getId() == R.id.show_welsh_services_back) {
            locationSettings.setVisibility(View.GONE);
            welshServicesSettings.setVisibility(View.VISIBLE);
        }
        if(v.getId()==R.id.finish){
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
            editor.putBoolean(KeyValueHelper.KEY_DEFAULT_SERVICES_PREFIX + currentCheckBox.getTag(), currentCheckBox.isChecked());


        }

        LinearLayoutCompat servicesRequiredWelsh = findViewById(R.id.services_required_welsh);
        for (int i = 0; i < servicesRequiredWelsh.getChildCount(); i++) {
            CheckBox currentCheckBox = (CheckBox) servicesRequiredWelsh.getChildAt(i);
            editor.putBoolean(KeyValueHelper.KEY_DEFAULT_SERVICES_WELSH_PREFIX + currentCheckBox.getTag(), currentCheckBox.isChecked());
        }

        EditText location = findViewById(R.id.postcode_given);
        Geocoder geocoder = new Geocoder(this);
        CheckBox useLocationByDefault = findViewById(R.id.use_location);
        EditText maximumDistance = findViewById(R.id.maximum_distance);
        boolean postcodeFound = true;
        boolean validDistance = false;
        float maxDistance = 0;
        try {
            maxDistance = Float.parseFloat(maximumDistance.getText().toString());
            if(maxDistance>0){
                validDistance = true;
            }
        }
        catch(NumberFormatException ex){
            //do nothing
        }
        List<Address> addresses = null; //what the fuck
        try {
            addresses = geocoder.getFromLocationName(location.getText().toString(), 1);
        } catch (IOException e) {
            postcodeFound = false;
        }
        if(addresses!=null) {
            if (addresses.size() == 0) {
                postcodeFound = false;
            }
        }

        if(!postcodeFound){
            Toast.makeText(this,R.string.postcode_not_found_not_saved,Toast.LENGTH_LONG).show();
        }
        if(!validDistance){
            Toast.makeText(this, R.string.changes_not_saved_invalid_distance,Toast.LENGTH_LONG).show();
        }
        if(postcodeFound && validDistance){
            editor.putFloat(KeyValueHelper.KEY_USER_LAT,(float)addresses.get(0).getLatitude());
            editor.putFloat(KeyValueHelper.KEY_USER_LNG,(float)addresses.get(0).getLongitude());
            editor.putString(KeyValueHelper.KEY_POSTCODE_TEXT,location.getText().toString());
            editor.putBoolean(KeyValueHelper.KEY_USE_LOCATION_DEFAULT,useLocationByDefault.isChecked());
            editor.putFloat(KeyValueHelper.KEY_MAXDISTANCE_TEXT,maxDistance);
            editor.putBoolean(KeyValueHelper.KEY_FINISHED_WIZARD,true);
            editor.apply();
            Toast.makeText(this, R.string.saved_message, Toast.LENGTH_SHORT).show();
            Intent i = new Intent(this, MultiPharmacyActivity.class);
            startActivity(i);
        }

    }
    //Reference: https://stackoverflow.com/questions/6413700/android-proper-way-to-use-onbackpressed-with-toast
    //Accessed on 12 May 2018
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage(R.string.are_you_sure_exit)
                .setNegativeButton(R.string.no, null)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        DefaultSettings.super.onBackPressed();
                    }
                }).create().show();
    }
}