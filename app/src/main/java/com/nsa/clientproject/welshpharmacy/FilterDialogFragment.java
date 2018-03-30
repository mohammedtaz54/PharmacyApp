package com.nsa.clientproject.welshpharmacy;

import android.Manifest;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.nsa.clientproject.welshpharmacy.models.PharmacySearchCriteria;
import com.nsa.clientproject.welshpharmacy.models.PharmacyServices;

import java.util.HashMap;
import java.util.Map;

//Reference:https://developer.android.com/guide/topics/ui/dialogs.html
//Accessed 29 March 2018
public class FilterDialogFragment extends DialogFragment implements View.OnClickListener, RadioGroup.OnCheckedChangeListener, OnSuccessListener<Location> {
    /**
     * Stores the parenent that we need to tell that the search is updated
     */
    private SearchCriteriaUpdater parent;
    /**
     * Stores the current view - getView does nothing.
     */
    private View currentView;
    /**
     * Stores the user's location if we can get it.
     */
    private Location userLocation;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //Build the layout
        try {
            this.parent = (SearchCriteriaUpdater) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException("Parent activity must implement SearchCriteriaUpdater");
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        currentView = layoutInflater.inflate(R.layout.fragment_dialog_filter, null);
        builder.setView(currentView);
        //If we have location permission, try and fetch the last known one
        if(ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED){
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
        //todo: perhaps have separate defaults for welsh vs not welsh?
        SharedPreferences defaultSettings = getContext().getSharedPreferences("DEFAULT_SETTINGS", Context.MODE_PRIVATE);
        if (defaultSettings.getBoolean(KeyValueHelper.KEY_BLOODPRESSURE_CHECKBOX, false)) {
            CheckBox bpMonitoring = currentView.findViewById(R.id.has_bp_monitoring);
            bpMonitoring.setChecked(true);
            CheckBox bpMonitoringWelsh = currentView.findViewById(R.id.has_bp_monitoring_welsh);
            bpMonitoringWelsh.setChecked(true);
        }
        if (defaultSettings.getBoolean(KeyValueHelper.KEY_FLUSHOT_CHECKBOX, false)) {
            CheckBox fluShot = currentView.findViewById(R.id.has_flu_shot);
            fluShot.setChecked(true);
            CheckBox fluShotWelsh = currentView.findViewById(R.id.has_flu_shot_welsh);
            fluShotWelsh.setChecked(true);

        }
        return builder.create();
    }

    @Override
    public void onClick(View v) {
        PharmacySearchCriteria searchCriteria = new PharmacySearchCriteria();
        Map<PharmacyServices, Boolean> servicesRequired = new HashMap<>();
        Map<PharmacyServices, Boolean> servicesRequiredWelsh = new HashMap<>();
        //todo: add any future search criteria here.
        if (((CheckBox) currentView.findViewById(R.id.has_bp_monitoring)).isChecked()) {
            servicesRequired.put(PharmacyServices.BLOOD_PRESSURE_MONITORING, true);
        }
        if (((CheckBox) currentView.findViewById(R.id.has_flu_shot)).isChecked()) {
            servicesRequired.put(PharmacyServices.FLU_SHOT, true);

        }
        if (((CheckBox) currentView.findViewById(R.id.has_bp_monitoring_welsh)).isChecked()) {
            servicesRequiredWelsh.put(PharmacyServices.BLOOD_PRESSURE_MONITORING, true);
        }
        if (((CheckBox) currentView.findViewById(R.id.has_flu_shot_welsh)).isChecked()) {
            servicesRequiredWelsh.put(PharmacyServices.FLU_SHOT, true);
        }
        searchCriteria.setServicesRequired(servicesRequired);
        searchCriteria.setServicesRequiredInWelsh(servicesRequiredWelsh);
        this.parent.setPreferences(searchCriteria);
        dismiss();
    }


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        EditText input = this.currentView.findViewById(R.id.postcode_string);

        if (checkedId == R.id.use_postcode) {
            input.setEnabled(true);
        } else {
            input.setEnabled(false);
        }
    }

    @Override
    public void onSuccess(Location location) {
        if(location!=null){
            //stupidest if statement of my life
            //why is it successful when the location is null fuck
            this.userLocation=location;
            this.currentView.findViewById(R.id.use_location).setEnabled(true);
        }
    }

    public interface SearchCriteriaUpdater {
        void setPreferences(PharmacySearchCriteria pharmacySearchCriteria);
    }
}
