package com.nsa.clientproject.welshpharmacy;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.nsa.clientproject.welshpharmacy.models.PharmacySearchCriteria;
import com.nsa.clientproject.welshpharmacy.models.PharmacyServices;

import java.util.HashMap;
import java.util.Map;

//Reference:https://developer.android.com/guide/topics/ui/dialogs.html
//Accessed 29 March 2018
public class FilterDialogFragment extends DialogFragment implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {
    private SearchCriteriaUpdater parent;
    private View currentView;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        try {
            this.parent = (SearchCriteriaUpdater) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException("Parent activity must implement SearchCriteriaUpdater");
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        currentView = layoutInflater.inflate(R.layout.fragment_dialog_filter, null);
        builder.setView(currentView);


        Button b = currentView.findViewById(R.id.submit_filter);
        b.setOnClickListener(this);

        RadioGroup locationSelector = currentView.findViewById(R.id.radio_select_location);
        locationSelector.setOnCheckedChangeListener(this);


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
        RadioButton radioButton = group.findViewById(checkedId);
        EditText input = this.currentView.findViewById(R.id.postcode_string);

        if (checkedId == R.id.use_postcode) {
            input.setEnabled(true);
        } else {
            input.setEnabled(false);
        }
    }

    public interface SearchCriteriaUpdater {
        void setPreferences(PharmacySearchCriteria pharmacySearchCriteria);
    }
}
