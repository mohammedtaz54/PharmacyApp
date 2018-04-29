package com.nsa.clientproject.welshpharmacy;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.design.widget.NavigationView;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.security.ProviderInstaller;
import com.google.android.gms.tasks.OnSuccessListener;
import com.nsa.clientproject.welshpharmacy.models.Pharmacy;
import com.nsa.clientproject.welshpharmacy.models.PharmacyList;
import com.nsa.clientproject.welshpharmacy.models.PharmacySearchCriteria;
import com.nsa.clientproject.welshpharmacy.models.PharmacyServices;

import java.security.Key;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Shows the pharmacy list interface and loads the list of cards
 * and ideally a map of them in the future
 */
public class MultiPharmacyActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        ListOfPharmaciesCards.OnFragmentInteractionListener,
        FilterDialogFragment.ContainsPharmacyList,
        OnSuccessListener<Location>,
        LoadingFragment.OnFragmentInteractionListener, ProviderInstaller.ProviderInstallListener {

    /**
     * Stores if the app has finished loading.
     */
    private boolean hasFinishedLoading = false;

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
        if (hasFinishedLoading) {
            switch (id) {
                case R.id.filter:
                    FilterDialogFragment filters = new FilterDialogFragment();
                    filters.show(getFragmentManager(), "filters");
                    break;
                case R.id.menu_refresh:
                    loadLoadingFragment();
                    break;
                case R.id.menu_language:
//                    loadLoadingFragment();
                    Intent languageIntent = new Intent(this, Language.class);
                    languageIntent.putExtra("previousActivity", "List");
                    startActivity(languageIntent);

                    this.setTitle(getString(R.string.title_activity_pharmacy_list));
                    break;

            }
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
        ProviderInstaller.installIfNeededAsync(this, this);
        Locale LocalePreference;

        setLanguage(this);


        this.setTitle(getString(R.string.title_activity_pharmacy_list));

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
        // this.pharmacyList.updatePharmacies();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //loadCardsFragment();
        loadLoadingFragment();
        this.defaultSettings = getSharedPreferences("DEFAULT_SETTINGS", MODE_PRIVATE);

        if (!defaultSettings.getBoolean(KeyValueHelper.KEY_FINISHED_WIZARD, false)) {
            Intent i = new Intent(this, DefaultSettings.class);
            startActivity(i);
            finish();
        } else {
            loadDefaultSettings();

        }
        //Reference: https://developer.android.com/training/permissions/requesting.html#java
        //Accessed 26 March 2018
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            onLocationPermissionGranted();

        } else {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, ON_LOCATION_PERMISSION_GRANTED);
        }
    }

    public static void setLanguage(Activity This) {
        Locale LocalePreference;

        SharedPreferences sp = This.getSharedPreferences("DEFAULT_SETTINGS", MODE_PRIVATE);
        sp.getString("LANGUAGE", "en");


        if (sp.getString("LANGUAGE", "en").equals("en")) {
            LocalePreference = Locale.ENGLISH;
        } else {
            LocalePreference = Locale.forLanguageTag("cy");
        }

        Log.d("myTag", sp.getString("LANGUAGE", "en"));

        //Reference:https://stackoverflow.com/questions/2900023/change-app-language-programmatically-in-android
        //Accessed on 28 April 2018
        Context context = This; // or ActivityNotification.this
        Locale language_code = LocalePreference;
        Resources res = context.getResources();
        // Change locale settings in the app.
        DisplayMetrics dm = res.getDisplayMetrics();
        android.content.res.Configuration conf = res.getConfiguration();
        conf.setLocale(language_code); // API 17+ only.
// Use conf.locale = new Locale(...) if targeting lower versions
        res.updateConfiguration(conf, dm);

    }
    /**
     * Loads the loading screen fragment
     */
    private void loadLoadingFragment() {
        LoadingFragment list = new LoadingFragment();
        Bundle data = new Bundle();
        data.putSerializable("pharmacyList", this.pharmacyList);
        list.setArguments(data);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_pharmacy_list, list, TAG_CURRENT_DISPLAY)
                .commit();
        this.setTitle(getString(R.string.title_activity_pharmacy_list));
    }

    private void loadDefaultSettings() {
        //Applies the default filters
        Map<String, ?> allDefaults = defaultSettings.getAll();
        PharmacySearchCriteria defaultSearchCriteria = new PharmacySearchCriteria();
        Map<PharmacyServices, Boolean> requiredServices = new HashMap<>();
        Map<PharmacyServices, Boolean> requiredServicesWelsh = new HashMap<>();

        for (String key : allDefaults.keySet()) {
            Log.d("HELP", key);
            if (key.startsWith(KeyValueHelper.KEY_DEFAULT_SERVICES_WELSH_PREFIX)
                    && !key.equals(KeyValueHelper.KEY_DEFAULT_SERVICES_WELSH_PREFIX + "null")) {
                boolean isActive = (Boolean) allDefaults.get(key);
                PharmacyServices currentService = PharmacyServices.valueOf(key.substring(KeyValueHelper.KEY_DEFAULT_SERVICES_WELSH_PREFIX.length()));
                requiredServicesWelsh.put(currentService, isActive);
            } else if (key.startsWith(KeyValueHelper.KEY_DEFAULT_SERVICES_PREFIX)
                    && !key.equals(KeyValueHelper.KEY_DEFAULT_SERVICES_PREFIX + "null")) {
                boolean isActive = (Boolean) allDefaults.get(key);
                PharmacyServices currentService = PharmacyServices.valueOf(key.substring(KeyValueHelper.KEY_DEFAULT_SERVICES_PREFIX.length()));
                requiredServices.put(currentService, isActive);
            }

        }
        defaultSearchCriteria.setServicesRequired(requiredServices);
        defaultSearchCriteria.setServicesRequiredInWelsh(requiredServicesWelsh);
        defaultSearchCriteria.setMaxDistance(defaultSettings.getFloat(KeyValueHelper.KEY_MAXDISTANCE_TEXT, KeyValueHelper.DEFAULT_MAXDISTANCE_TEXT));
        defaultSearchCriteria.setUserLng((double) defaultSettings.getFloat(
                KeyValueHelper.KEY_USER_LNG,
                (float) KeyValueHelper.DEFAULT_USER_LNG));
        defaultSearchCriteria.setUserLat((double) defaultSettings.getFloat(
                KeyValueHelper.KEY_USER_LAT,
                (float) KeyValueHelper.DEFAULT_USER_LAT));
        pharmacyList.setPharmacySearchCriteria(defaultSearchCriteria);
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
        FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        //This if statement isn't necessary but android doesn't let me do it anyway
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this);
        }
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
        if (hasFinishedLoading) {
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
        }
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
     * When the user swipes up to refresh, trigger this method from the fragment
     */
    @Override
    public void onRefresh() {
        loadLoadingFragment();
    }


    /**
     * Gets the pharmacyList object we use through the app
     *
     * @return the pharmacyList object
     */
    @Override
    public PharmacyList getPharmacyList() {
        return this.pharmacyList;
    }

    /**
     * What happens when we successfully get the user's location.
     */
    @Override
    public void onSuccess(Location location) {
        if (location != null) {
            //These should exist by the time this is called
            PharmacySearchCriteria pharmacySearchCriteria = this.pharmacyList.getPharmacySearchCriteria();
            if (defaultSettings.getBoolean(KeyValueHelper.KEY_USE_LOCATION_DEFAULT, KeyValueHelper.DEFAULT_USE_LOCATION_DEFAULT)) {
                Log.d("HELP", "Applying settings");
                pharmacySearchCriteria.setUserLng(location.getLongitude());
                pharmacySearchCriteria.setUserLat(location.getLatitude());
                this.pharmacyList.setPharmacySearchCriteria(pharmacySearchCriteria);
                Intent broadcastChange = new Intent();
                broadcastChange.setAction("com.nsa.clientproject.welshpharmacy.UPDATED_LIST_PHARMACIES");
                LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastChange);
            } else {
                Log.d("HELP", "Not applying settings");

            }
        }
    }

    /**
     * Load  the view fragment into the screen
     */
    @Override
    public void onFinishedLoading() {
        loadCardsFragment();
        hasFinishedLoading = true;

    }

    @Override
    public void onProviderInstalled() {
        
    }

    @Override
    public void onProviderInstallFailed(int i, Intent intent) {

    }

    /**
     * Check if the page has finished loading
     * @return if the page is loaded
     */
    public boolean isHasFinishedLoading() {
        return hasFinishedLoading;
    }

    /**
     * Returns the current fragment that is rendered
     * @return the current fragment
     */
    Fragment getCurrentFragment(){
        return getSupportFragmentManager().findFragmentByTag(TAG_CURRENT_DISPLAY);
    }
}
