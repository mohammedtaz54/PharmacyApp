package com.nsa.clientproject.welshpharmacy;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.design.widget.NavigationView;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.nsa.clientproject.welshpharmacy.models.Pharmacy;
import com.nsa.clientproject.welshpharmacy.models.PharmacyList;
import com.nsa.clientproject.welshpharmacy.models.PharmacySearchCriteria;
import com.nsa.clientproject.welshpharmacy.models.PharmacyServices;

import java.security.Key;

/**
 * Shows the pharmacy list interface and loads the list of cards
 * and ideally a map of them in the future
 */
public class MultiPharmacyActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ListOfPharmaciesCards.OnFragmentInteractionListener,
        FilterDialogFragment.SearchCriteriaUpdater {
    /**
     * Code to be returned when the permission for location is granted.
     */
    private static final int ON_LOCATION_PERMISSION_GRANTED = 1;
    /**
     * Stores the fragment tag of the current display.
     */
    private static final String TAG_CURRENT_DISPLAY = "current_display";
    /**
     * Stores the default settings.
     */
    private SharedPreferences defaultSettings;
    /**
     * Stores the list of pharmacies we set filters onto
     */
    private PharmacyList pharmacyList;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.filter:
                FilterDialogFragment filters = new FilterDialogFragment();
                filters.show(getFragmentManager(), "filters");
                break;
        }
        return true;
    }

    /**
     * Shows the main menu options
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_pharmacies_list, menu);
        return true;
    }

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
        this.pharmacyList = new PharmacyList();
        this.pharmacyList.updatePharmacies();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        loadCardsFragment();
        this.defaultSettings = getSharedPreferences("DEFAULT_SETTINGS", MODE_PRIVATE);
        //This permission stuff is here for future use
        //As we'll need this for the filtering which affects the list as well
        //So we can't just load it in the map fragment.
        //Reference: https://developer.android.com/training/permissions/requesting.html#java
        //Accessed 26 March 2018
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            onLocationPermissionGranted();

        } else {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, ON_LOCATION_PERMISSION_GRANTED);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == ON_LOCATION_PERMISSION_GRANTED) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                onLocationPermissionGranted();
            } else {
                onLocationPermissionNotGranted();
            }
        }
    }

    /**
     * Executes if the location permission isn't granted.
     */
    private void onLocationPermissionNotGranted() {
        String postcode = this.defaultSettings.getString(KeyValueHelper.KEY_POSTCODE_TEXT, KeyValueHelper.DEFAULT_POSTCODE_TEXT);
        if (!postcode.equals(KeyValueHelper.DEFAULT_POSTCODE_TEXT)) {
            Toast.makeText(this, R.string.default_postcode_fallback, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, R.string.default_nopostcode_fallback, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Executes if we either have the location permission,
     * or we have just gotten it.
     */
    private void onLocationPermissionGranted() {
        Toast.makeText(this, R.string.have_location_permission, Toast.LENGTH_SHORT).show();
    }

    /**
     * Loads the fragment that displays the pharmacies as cards
     */
    private void loadCardsFragment() {
        ListOfPharmaciesCards list = new ListOfPharmaciesCards();
        Bundle data = new Bundle();
        data.putSerializable("pharmacyList", this.pharmacyList);
        list.setArguments(data);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_pharmacy_list, list, TAG_CURRENT_DISPLAY)
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

        switch (item.getItemId()) {
            case R.id.settings:
                Intent i = new Intent(this, DefaultSettings.class);
                startActivity(i);
                break;
            case R.id.map_view:
                ViewMapFragment map = new ViewMapFragment();
                Bundle data = new Bundle();
                data.putSerializable("pharmacyList", this.pharmacyList);
                map.setArguments(data);
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.content_pharmacy_list, map, TAG_CURRENT_DISPLAY)
                        .commit();
                break;
            case R.id.list_view:
                loadCardsFragment();

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

    /**
     * Saves the pharmacySearchCriteria given from the filter
     *
     * @param pharmacySearchCriteria the given criteria
     */
    @Override
    public void setPreferences(PharmacySearchCriteria pharmacySearchCriteria) {
        this.pharmacyList.setPharmacySearchCriteria(pharmacySearchCriteria);
        Intent listChanged = new Intent()
                .setAction("com.nsa.clientproject.welshpharmacy.UPDATED_LIST_PHARMACIES");
        Log.d("R","SENDING BROADCAST");

        LocalBroadcastManager.getInstance(this).sendBroadcast(listChanged);
    }


}
