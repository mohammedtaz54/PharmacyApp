package com.nsa.clientproject.pharmacyadmin;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.nsa.clientproject.pharmacyadmin.models.PharmacyListItem;

import java.io.Serializable;
import java.util.List;

public class PharmacyList extends AppCompatActivity implements LoadingFragment.OnFragmentInteractionListener, PharmacyListFragment.OnFragmentInteractionListener {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
         getMenuInflater().inflate(R.menu.main_page_menu,menu);
                 return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.add:
                Intent i = new Intent(this,AddPharmacyActivity.class);
                startActivity(i);
                break;
            case R.id.refresh:
                onSwipeToRefresh();
                break;
        }
        return true;
    }

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
    public void onSwipeToRefresh() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_spot, new LoadingFragment())
                .commit();
    }
}
