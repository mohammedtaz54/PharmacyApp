package com.nsa.clientproject.welshpharmacy;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.nsa.clientproject.welshpharmacy.models.Pharmacy;
import com.nsa.clientproject.welshpharmacy.models.PharmacyList;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

public class ViewMapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, OnCompleteListener, UpdatableOnFilterChange{

    private GoogleMap mMap;
    private PharmacyList pharmacyList;
    private static final int DEFAULT_ZOOM_LVL = 15;
    private static final int WALES_ZOOM_LVL = 7;
    private SharedPreferences defaultSettings;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_view_map, container, false);


        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        //this.pharmacyList =(PharmacyList) getArguments().getSerializable("pharmacyList");

        defaultSettings = this.getContext().getSharedPreferences("DEFAULT_SETTINGS", Context.MODE_PRIVATE);


        return v;

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ContextCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PermissionChecker.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            //Reference: https://developers.google.com/maps/documentation/android-api/current-place-tutorial
            //Accessed 27 March 2018
            FusedLocationProviderClient mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this.getContext());
            Task locationResult = mFusedLocationProviderClient.getLastLocation();
            locationResult.addOnCompleteListener(this);

        } else {
            Toast.makeText(this.getContext(), R.string.location_not_found, Toast.LENGTH_SHORT).show();
            moveCameraToHome();

        }
        updateMapPins();
        mMap.setOnMarkerClickListener(this);
    }

    /**
     * Updates all the pins in the map
     */
    private void updateMapPins() {
        mMap.clear();

        if (pharmacyList != null) {
            List<Pharmacy> pharmacies = pharmacyList.getPharmacies();
            for (Pharmacy p : pharmacies) {
                MarkerOptions marker = new MarkerOptions()
                        .position(new LatLng(p.getPharmacyLat(), p.getPharmacyLng()))
                        .title(p.getName());

                Marker m = mMap.addMarker(marker);
                m.setTag(p);
            }
        }
    }


//    private void gotoLocation(double lat, double lng, float zoom) {
//        LatLng ll = new LatLng(lat, lng);
//        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll, zoom);
//        mMap.moveCamera(update);
//    }

//    public void geoLocation(View v) throws IOException {
//        hideSoftKeyboard(v);
//
//        EditText et = (EditText) findViewById(R.id.editText1);
//        String location = et.getText().toString();
//
//        Geocoder gc = new Geocoder(this);
//        List<Address> list = gc.getFromLocationName(location, 1);
//        Address add = list.get(0);
//        String locality = add.getLocality();
//        Toast.makeText(this, locality, Toast.LENGTH_LONG).show();
//
//        double lat = add.getLatitude();
//        double lng = add.getLongitude();
//
//        gotoLocation(lat, lng, 10);
//
//
//    }


//    private void hideSoftKeyboard(View v) {
//        InputMethodManager imm =(InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
//        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
//    }


    @Override
    public boolean onMarkerClick(Marker marker) {
        Intent i = new Intent(this.getContext(), PharmacyView.class);
        i.putExtra("pharmacy", (Serializable) marker.getTag());
        startActivity(i);
        return true;
    }

    /**
     * Called when the user's location is found
     */
    @Override
    public void onComplete(@NonNull Task task) {
        if (task.isSuccessful() && task.getResult()!=null) {
            moveCameraToLocation((Location) task.getResult(), DEFAULT_ZOOM_LVL);
        } else {
            moveCameraToHome();
        }
    }

    private void moveCameraToHome() {
        String postcode = defaultSettings.getString(KeyValueHelper.KEY_POSTCODE_TEXT, KeyValueHelper.DEFAULT_POSTCODE_TEXT);
        if (!postcode.equals(KeyValueHelper.DEFAULT_POSTCODE_TEXT)) {
            Geocoder geocoder = new Geocoder(this.getContext());
            try {
                Address address = geocoder.getFromLocationName(postcode, 1).get(0);
                moveCameraToLocation(address, DEFAULT_ZOOM_LVL);
            } catch (IOException e) {
                Toast.makeText(this.getContext(), R.string.cannot_resolve_postcode, Toast.LENGTH_SHORT).show();
                moveCameraToWales();
            }
        } else {
            moveCameraToWales();
        }

    }

    /**
     * Moves the camera so it covers the entirety of wales.
     */
    private void moveCameraToWales() {
        moveCameraToLocation(new LatLng(52.1307, -3.7837), WALES_ZOOM_LVL);

    }

    /**
     * Moves the camera to a location
     *
     * @param l    the location
     * @param zoom the zoom level
     */
    private void moveCameraToLocation(Location l, int zoom) {
        moveCameraToLocation(new LatLng(l.getLatitude(), l.getLongitude()), zoom);
    }

    /**
     * Moves camera to an address
     *
     * @param l    the address
     * @param zoom the zoom level
     */
    private void moveCameraToLocation(Address l, int zoom) {
        moveCameraToLocation(new LatLng(l.getLatitude(), l.getLongitude()), zoom);
    }

    /**
     * Moves camera to a LatLng pair
     *
     * @param l    the LatLng pair
     * @param zoom the zoom level
     */
    private void moveCameraToLocation(LatLng l, int zoom) {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(l, zoom));

    }

    /**
     * When the filters are changed, re-render all the pins
     */
    @Override
    public void onFiltersChanged() {
        updateMapPins();
    }
}



