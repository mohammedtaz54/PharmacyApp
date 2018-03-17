package com.nsa.clientproject.welshpharmacy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.Arrays;
import android.widget.ArrayAdapter;

import com.nsa.clientproject.welshpharmacy.models.Pharmacy;
import com.nsa.clientproject.welshpharmacy.models.PharmacyList;


public class PharmacyView extends AppCompatActivity {
    ListView lv ;
    String[] servicesConvert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pharmacy_view);
        PharmacyList pharmacyList = new PharmacyList();
        pharmacyList.updatePharmacies();
        Pharmacy pharmacy = pharmacyList.getPharmacies().get(0);

        //Change Text in name text field in single pharmacy view
        TextView singleName=(TextView)findViewById(R.id.single_name);
        singleName.setText(pharmacy.getName());

        //Change Text in location text field in single pharmacy view
        TextView singleLocation=(TextView)findViewById(R.id.single_location);
        singleLocation.setText("PostCode:" + pharmacy.getLocation());

        //Change items in services list view in single pharmacy view
        ArrayList<String> serviceList = new ArrayList();

        // Get a handle to the list view
        lv = (ListView) findViewById(R.id.single_services);

        // Convert ArrayList to array
        servicesConvert = (String[]) ArrayList.toArray();
        lv.setAdapter(new ArrayAdapter<String>(Start.this,
                android.R.layout.simple_list_item_1, lv_arr));


    }
}
