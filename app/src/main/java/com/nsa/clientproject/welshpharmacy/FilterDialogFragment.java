package com.nsa.clientproject.welshpharmacy;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import com.nsa.clientproject.welshpharmacy.models.PharmacySearchCriteria;
import com.nsa.clientproject.welshpharmacy.models.PharmacyServices;

import java.util.HashMap;
import java.util.Map;

//Reference:https://developer.android.com/guide/topics/ui/dialogs.html
//Accessed 29 March 2018
public class FilterDialogFragment extends DialogFragment implements View.OnClickListener {
    private SearchCriteriaUpdater parent;
    private View currentView;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        try {
            this.parent = (SearchCriteriaUpdater) getActivity();
        }
        catch(ClassCastException e){
            throw new ClassCastException("Parent activity must implement SearchCriteriaUpdater");
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        currentView = layoutInflater.inflate(R.layout.fragment_dialog_filter, null);
        builder.setView(currentView);
        Button b = currentView.findViewById(R.id.submit_filter);
        b.setOnClickListener(this);
        return builder.create();
    }

    @Override
    public void onClick(View v) {
        PharmacySearchCriteria searchCriteria = new PharmacySearchCriteria();
        Map<PharmacyServices,Boolean> servicesRequired = new HashMap<>();
        //todo: add any future search criteria here.
        if(((CheckBox) currentView.findViewById(R.id.has_bp_monitoring)).isChecked()){
            servicesRequired.put(PharmacyServices.BLOOD_PRESSURE_MONITORING,true);
        }
        searchCriteria.setServicesRequired(servicesRequired);
        this.parent.setPreferences(searchCriteria);
        dismiss();
    }
    public interface SearchCriteriaUpdater{
        void setPreferences(PharmacySearchCriteria pharmacySearchCriteria);
    }
}
