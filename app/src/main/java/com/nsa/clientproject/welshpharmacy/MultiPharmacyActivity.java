package com.nsa.clientproject.welshpharmacy;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.nsa.clientproject.welshpharmacy.models.Pharmacy;
import com.nsa.clientproject.welshpharmacy.models.PharmacyList;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Shows the pharmacy list interface and loads the list of cards
 * and ideally a map of them in the future
 */
public class MultiPharmacyActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ListOfPharmaciesCards.OnFragmentInteractionListener {

    public static final String PHARMACY_KEY = "PHARMACY_KEY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharmacy_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_pharmacy_list, new ListOfPharmaciesCards())
                .commit();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }




    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (item.getItemId())   {
            case R.id.settings:
                Intent i = new Intent(this, DefaultSettings.class);
                startActivity(i);
                break;
            case R.id.map_view:
                Intent mapIntent = new Intent(this, ViewMapActivity.class);
                startActivity(mapIntent);
                break;

        }
        // Changed if function to switch statement
//        if (id == R.id.settings) {
//            Intent i = new Intent(this, DefaultSettings.class);
//            startActivity(i);
//        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    /**
     * Takes care of a pharmacy click, so it goes to its own view.
     *
     * @param pharmacy the pharmacy the user clicked.
     */
    @Override
    public void onFragmentInteraction(Pharmacy pharmacy) {
        //Reference:https://stackoverflow.com/questions/2736389/how-to-pass-an-object-from-one-activity-to-another-on-android
        //Accessed 17 March 2018
        Intent i = new Intent(this, PharmacyView.class);
        i.putExtra("pharmacy", pharmacy);
        startActivity(i);
    }
}
