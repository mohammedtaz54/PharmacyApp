package com.nsa.clientproject.welshpharmacy;

import android.Manifest;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.nsa.clientproject.welshpharmacy.models.PharmacyList;
import com.nsa.clientproject.welshpharmacy.models.PharmacySearchCriteria;
import com.nsa.clientproject.welshpharmacy.models.PharmacyServices;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

//Reference:https://developer.android.com/guide/topics/ui/dialogs.html
//Accessed 29 March 2018
public class FilterDialogFragment extends DialogFragment implements View.OnClickListener, RadioGroup.OnCheckedChangeListener, OnSuccessListener<Location> {

    /**
     * Stores the current view - getView does nothing.
     */
    private View currentView;
    /**
     * Stores the user's location if we can get it.
     */
    private Location userLocation;
    /**
     * Stores the parent's activity that implements the ContainsPharmacyList interface.
     */
    private ContainsPharmacyList parent;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //Build the layout
        try {
            this.parent = (ContainsPharmacyList) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException("Parent activity must implement ContainsPharmacyList");
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        currentView = layoutInflater.inflate(R.layout.fragment_dialog_filter, null);
        builder.setView(currentView);
        //If we have location permission, try and fetch the last known one
        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this);
        }
        //Set up the success button
        Button b = currentView.findViewById(R.id.submit_filter);
        b.setOnClickListener(this);
        //Set up the radio group
        RadioGroup locationSelector = currentView.findViewById(R.id.radio_select_location);
        locationSelector.setOnCheckedChangeListener(this);



        //Load default values into the system
        SharedPreferences defaultSettings = getContext().getSharedPreferences("DEFAULT_SETTINGS", Context.MODE_PRIVATE);

        EditText postcode = currentView.findViewById(R.id.postcode_string);
        postcode.setText(defaultSettings.getString(KeyValueHelper.KEY_POSTCODE_TEXT, KeyValueHelper.DEFAULT_POSTCODE_TEXT));
        EditText maximumDistance = currentView.findViewById(R.id.maximum_distance);
        maximumDistance.setText(Float.toString(defaultSettings.getFloat(KeyValueHelper.KEY_MAXDISTANCE_TEXT, KeyValueHelper.DEFAULT_MAXDISTANCE_TEXT)));


        //todo:perhaps find a way to make this better?
        if (defaultSettings.getBoolean(KeyValueHelper.KEY_DEFAULT_SERVICES_PREFIX+"BLOOD_PRESSURE_MONITORING", false)) {
            CheckBox bpMonitoring = currentView.findViewById(R.id.has_bp_monitoring);
            bpMonitoring.setChecked(true);
        }
        if (defaultSettings.getBoolean(KeyValueHelper.KEY_DEFAULT_SERVICES_WELSH_PREFIX+"BLOOD_PRESSURE_MONITORING", false)) {
            CheckBox bpMonitoringWelsh = currentView.findViewById(R.id.has_bp_monitoring_welsh);
            bpMonitoringWelsh.setChecked(true);
        }
        if (defaultSettings.getBoolean(KeyValueHelper.KEY_DEFAULT_SERVICES_PREFIX+"FLU_SHOT", false)) {
            CheckBox fluShot= currentView.findViewById(R.id.has_flu_shot);
            fluShot.setChecked(true);
        }
        if (defaultSettings.getBoolean(KeyValueHelper.KEY_DEFAULT_SERVICES_WELSH_PREFIX+"FLU_SHOT", false)) {
            CheckBox fluShotWelsh= currentView.findViewById(R.id.has_flu_shot_welsh);
            fluShotWelsh.setChecked(true);
        }

        if (defaultSettings.getBoolean(KeyValueHelper.KEY_DEFAULT_SERVICES_PREFIX+"DISPENSING", false)) {
            CheckBox dispensing= currentView.findViewById(R.id.has_dispensing);
            dispensing.setChecked(true);
        }
        if (defaultSettings.getBoolean(KeyValueHelper.KEY_DEFAULT_SERVICES_WELSH_PREFIX+"DISPENSING", false)) {
            CheckBox dispensingWelsh = currentView.findViewById(R.id.has_dispensing_welsh);
            dispensingWelsh.setChecked(true);
        }
        if (defaultSettings.getBoolean(KeyValueHelper.KEY_DEFAULT_SERVICES_PREFIX+"ALCOHOL_ADVICE", false)) {
            CheckBox alcoholAdvice = currentView.findViewById(R.id.has_alcohol_advice);
            alcoholAdvice.setChecked(true);
        }
        if (defaultSettings.getBoolean(KeyValueHelper.KEY_DEFAULT_SERVICES_WELSH_PREFIX+"ALCOHOL_ADVICE", false)) {
            CheckBox alcoholAdviceWelsh = currentView.findViewById(R.id.has_alcohol_advice_welsh);
            alcoholAdviceWelsh.setChecked(true);
        }
        if (defaultSettings.getBoolean(KeyValueHelper.KEY_DEFAULT_SERVICES_PREFIX+"EMERGENCY_SUPPLY", false)) {
            CheckBox emergencySupply = currentView.findViewById(R.id.has_emergency_supply);
            emergencySupply.setChecked(true);
        }
        if (defaultSettings.getBoolean(KeyValueHelper.KEY_DEFAULT_SERVICES_WELSH_PREFIX+"EMERGENCY_SUPPLY", false)) {
            CheckBox emergencySupplyWelsh = currentView.findViewById(R.id.has_emergency_supply_welsh);
            emergencySupplyWelsh.setChecked(true);
        }
        if (defaultSettings.getBoolean(KeyValueHelper.KEY_DEFAULT_SERVICES_PREFIX+"MEDICINE_REVIEW", false)) {
            CheckBox medicineReview = currentView.findViewById(R.id.has_medicine_review);
            medicineReview.setChecked(true);
        }
        if (defaultSettings.getBoolean(KeyValueHelper.KEY_DEFAULT_SERVICES_WELSH_PREFIX+"MEDICINE_REVIEW", false)) {
            CheckBox medicineReviewWelsh = currentView.findViewById(R.id.has_medicine_review_welsh);
            medicineReviewWelsh.setChecked(true);
        }
        if (defaultSettings.getBoolean(KeyValueHelper.KEY_DEFAULT_SERVICES_PREFIX+"MINOR_AILMENT", false)) {
            CheckBox minorAilment = currentView.findViewById(R.id.has_minor_ailment);
            minorAilment.setChecked(true);
        }
        if (defaultSettings.getBoolean(KeyValueHelper.KEY_DEFAULT_SERVICES_WELSH_PREFIX+"MINOR_AILMENT", false)) {
            CheckBox minorAilmentWelsh = currentView.findViewById(R.id.has_minor_ailment_welsh);
            minorAilmentWelsh.setChecked(true);
        }
        if (defaultSettings.getBoolean(KeyValueHelper.KEY_DEFAULT_SERVICES_PREFIX+"NEW_MEDICINE", false)) {
            CheckBox newMedicine = currentView.findViewById(R.id.has_new_medicine);
            newMedicine.setChecked(true);
        }
        if (defaultSettings.getBoolean(KeyValueHelper.KEY_DEFAULT_SERVICES_WELSH_PREFIX+"NEW_MEDICINE", false)) {
            CheckBox newMedicineWelsh = currentView.findViewById(R.id.has_new_medicine_welsh);
            newMedicineWelsh.setChecked(true);
        }
        if (defaultSettings.getBoolean(KeyValueHelper.KEY_DEFAULT_SERVICES_PREFIX+"PREGNANCY_TEST", false)) {
            CheckBox pregnancyTest = currentView.findViewById(R.id.has_pregnancy_test);
            pregnancyTest.setChecked(true);
        }
        if (defaultSettings.getBoolean(KeyValueHelper.KEY_DEFAULT_SERVICES_WELSH_PREFIX+"PREGNANCY_TEST", false)) {
            CheckBox pregnancyTestWelsh = currentView.findViewById(R.id.has_pregnancy_test_welsh);
            pregnancyTestWelsh.setChecked(true);
        }
        if (defaultSettings.getBoolean(KeyValueHelper.KEY_DEFAULT_SERVICES_PREFIX+"WEIGHT_MANAGEMENT", false)) {
            CheckBox weightManagement = currentView.findViewById(R.id.has_weight_management);
            weightManagement.setChecked(true);
        }
        if (defaultSettings.getBoolean(KeyValueHelper.KEY_DEFAULT_SERVICES_WELSH_PREFIX+"WEIGHT_MANAGEMENT", false)) {
            CheckBox weightManagementWelsh = currentView.findViewById(R.id.has_weight_management_welsh);
            weightManagementWelsh.setChecked(true);
        }

        return builder.create();
    }

    /**
     * Submits the changes to the parent and updates the model.
     */
    @Override
    public void onClick(View v) {
        //todo:perhaps split this into two methods
        PharmacySearchCriteria searchCriteria = new PharmacySearchCriteria();
        Map<PharmacyServices, Boolean> servicesRequired = new HashMap<>();
        Map<PharmacyServices, Boolean> servicesRequiredWelsh = new HashMap<>();
        //todo: add any future search criteria here.
        if (((CheckBox) currentView.findViewById(R.id.has_bp_monitoring)).isChecked()) {
            servicesRequired.put(PharmacyServices.BLOOD_PRESSURE_MONITORING, true);
        }
        if (((CheckBox) currentView.findViewById(R.id.has_bp_monitoring_welsh)).isChecked()) {
            servicesRequiredWelsh.put(PharmacyServices.BLOOD_PRESSURE_MONITORING, true);
        }
        if (((CheckBox) currentView.findViewById(R.id.has_flu_shot)).isChecked()) {
            servicesRequired.put(PharmacyServices.FLU_SHOT, true);
        }
        if (((CheckBox) currentView.findViewById(R.id.has_flu_shot_welsh)).isChecked()) {
            servicesRequiredWelsh.put(PharmacyServices.FLU_SHOT, true);
        }
        if (((CheckBox) currentView.findViewById(R.id.has_dispensing)).isChecked()) {
            servicesRequired.put(PharmacyServices.DISPENSING, true);
        }
        if (((CheckBox) currentView.findViewById(R.id.has_dispensing_welsh)).isChecked()) {
            servicesRequiredWelsh.put(PharmacyServices.DISPENSING, true);
        }
        if (((CheckBox) currentView.findViewById(R.id.has_alcohol_advice)).isChecked()) {
            servicesRequired.put(PharmacyServices.ALCOHOL_ADVICE, true);
        }
        if (((CheckBox) currentView.findViewById(R.id.has_alcohol_advice_welsh)).isChecked()) {
            servicesRequiredWelsh.put(PharmacyServices.ALCOHOL_ADVICE, true);
        }
        if (((CheckBox) currentView.findViewById(R.id.has_emergency_supply)).isChecked()) {
            servicesRequired.put(PharmacyServices.EMERGENCY_SUPPLY, true);
        }
        if (((CheckBox) currentView.findViewById(R.id.has_emergency_supply_welsh)).isChecked()) {
            servicesRequiredWelsh.put(PharmacyServices.EMERGENCY_SUPPLY, true);
        }
        if (((CheckBox) currentView.findViewById(R.id.has_medicine_review)).isChecked()) {
            servicesRequired.put(PharmacyServices.MEDICINE_REVIEW, true);
        }
        if (((CheckBox) currentView.findViewById(R.id.has_new_medicine_welsh)).isChecked()) {
            servicesRequiredWelsh.put(PharmacyServices.MEDICINE_REVIEW, true);
        }
        if (((CheckBox) currentView.findViewById(R.id.has_minor_ailment)).isChecked()) {
            servicesRequired.put(PharmacyServices.MINOR_AILMENT, true);
        }
        if (((CheckBox) currentView.findViewById(R.id.has_minor_ailment_welsh)).isChecked()) {
            servicesRequiredWelsh.put(PharmacyServices.MINOR_AILMENT, true);
        }
        if (((CheckBox) currentView.findViewById(R.id.has_new_medicine)).isChecked()) {
            servicesRequired.put(PharmacyServices.NEW_MEDICINE, true);
        }
        if (((CheckBox) currentView.findViewById(R.id.has_new_medicine_welsh)).isChecked()) {
            servicesRequiredWelsh.put(PharmacyServices.NEW_MEDICINE, true);
        }
        if (((CheckBox) currentView.findViewById(R.id.has_pregnancy_test)).isChecked()) {
            servicesRequired.put(PharmacyServices.PREGNANCY_TEST, true);
        }
        if (((CheckBox) currentView.findViewById(R.id.has_pregnancy_test_welsh)).isChecked()) {
            servicesRequiredWelsh.put(PharmacyServices.PREGNANCY_TEST, true);
        }
        if (((CheckBox) currentView.findViewById(R.id.has_weight_management)).isChecked()) {
            servicesRequired.put(PharmacyServices.WEIGHT_MANAGEMENT, true);
        }
        if (((CheckBox) currentView.findViewById(R.id.has_weight_management_welsh)).isChecked()) {
            servicesRequiredWelsh.put(PharmacyServices.WEIGHT_MANAGEMENT, true);
        }

        searchCriteria.setServicesRequired(servicesRequired);
        searchCriteria.setServicesRequiredInWelsh(servicesRequiredWelsh);


        //Does the input for the location stuff
        EditText maximumMiles = this.currentView.findViewById(R.id.maximum_distance);
        if (maximumMiles.getText().toString().length() > 0) {

            boolean success = false;

            //If the user has given us their location
            if (((RadioButton) currentView.findViewById(R.id.use_location)).isChecked()) {
                //No need to validate this - if the has selected the box, we have the location
                searchCriteria.setUserLat(userLocation.getLatitude());
                searchCriteria.setUserLng(userLocation.getLongitude());
                success = true;
                //If the user hasn't given us a location
            } else if (((RadioButton) currentView.findViewById(R.id.use_postcode)).isChecked()) {


                String postcode = ((EditText) currentView.findViewById(R.id.postcode_string)).getText().toString();
                Geocoder geocoder = new Geocoder(getContext());
                //todo: perhaps this shouldn't be an exception?
                Address a;
                try {
                    a = geocoder.getFromLocationName(postcode, 1).get(0);
                } catch (IOException | IndexOutOfBoundsException e) {
                    a = null;
                }
                if (a != null) {
                    success = true;
                    searchCriteria.setUserLat(a.getLatitude());
                    searchCriteria.setUserLng(a.getLongitude());
                } else {
                    Toast.makeText(getContext(), R.string.cannot_locate_addr_not_filtering, Toast.LENGTH_SHORT).show();
                }

            } else {
                Toast.makeText(getContext(), R.string.not_filtering_location_not_given, Toast.LENGTH_SHORT).show();
            }
            if(success){
                searchCriteria.setMaxDistance(Double.parseDouble(maximumMiles.getText().toString()));
            }
        }
        this.parent.getPharmacyList().setPharmacySearchCriteria(searchCriteria);
        Intent listChanged = new Intent()
                .setAction("com.nsa.clientproject.welshpharmacy.UPDATED_LIST_PHARMACIES");
        Log.d("R","SENDING BROADCAST");

        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(listChanged);
        dismiss();
    }

    /**
     * Takes care of disabling the postcode if checked button isn't the postcode one
     */
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        EditText input = this.currentView.findViewById(R.id.postcode_string);

        if (checkedId == R.id.use_postcode) {
            input.setEnabled(true);
        } else {
            input.setEnabled(false);
        }
    }

    /**
     * saves the user's location and enables the option to use it.
     */
    @Override
    public void onSuccess(Location location) {
        if (location != null) {
            //stupidest if statement of my life
            //why is it successful when the location is null fuck
            this.userLocation = location;
            this.currentView.findViewById(R.id.use_location).setEnabled(true);
        }
    }

    /**
     * Interface that must be implemented by the parent so we can update
     */
    public interface ContainsPharmacyList{
        /**
         * Gets the pharmacyList object we use through the app
         * @return the pharmacyList object
         */
        PharmacyList getPharmacyList();
    }



}
