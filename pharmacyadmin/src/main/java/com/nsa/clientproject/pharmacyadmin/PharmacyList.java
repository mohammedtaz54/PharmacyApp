package com.nsa.clientproject.pharmacyadmin;

import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.nsa.clientproject.pharmacyadmin.models.PharmacyListItem;

import java.io.Serializable;
import java.util.List;

public class PharmacyList extends AppCompatActivity implements LoadingFragment.OnFragmentInteractionListener, PharmacyListFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharmacy_list);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.fragment_spot, new LoadingFragment())
                .commit();
    }

    @Override
    public void onFragmentInteraction(List<PharmacyListItem> pharmacies) {
        PharmacyListFragment fragment = new PharmacyListFragment();
        Bundle b = new Bundle();
        b.putSerializable("pharmacies", (Serializable) pharmacies);
        fragment.setArguments(b);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_spot, fragment)
                .commit();
    }

    @Override
    public void onPharmacyClick(int id) {

    }
}
