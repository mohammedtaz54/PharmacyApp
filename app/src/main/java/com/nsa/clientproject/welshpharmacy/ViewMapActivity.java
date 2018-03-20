package com.nsa.clientproject.welshpharmacy;

import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.nsa.clientproject.welshpharmacy.models.Pharmacy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ViewMapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String TAG = "ViewMapActivity";

    private GoogleMap mMap;
    private List<Pharmacy> pharmacyList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_map);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        pharmacyList = new ArrayList<>();

        Bundle getBundle = this.getIntent().getExtras();
        pharmacyList = (List<Pharmacy>) getBundle.getSerializable(MultiPharmacyActivity.PHARMACY_KEY);

        for (int i = 0; i < pharmacyList.size(); i++) {
            Log.d(TAG, pharmacyList.get(i).getLocation());
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng Pharmacy1 = new LatLng(51.492722, -3.187380);
        mMap.addMarker(new MarkerOptions().position(Pharmacy1).title("Marker in Pharmacy 1"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(Pharmacy1));

        LatLng Pharmacy2 = new LatLng(51.490875, -3.185637);
        mMap.addMarker(new MarkerOptions().position(Pharmacy2).title("Marker in Pharmacy 2"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(Pharmacy2));

        LatLng Pharmacy3 = new LatLng(51.486577, -3.165777);
        mMap.addMarker(new MarkerOptions().position(Pharmacy3).title("Marker in Pharmacy 3"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(Pharmacy3));

        LatLng Pharmacy4 = new LatLng(51.489220, -3.166650);
        mMap.addMarker(new MarkerOptions().position(Pharmacy4).title("Marker in Pharmacy 4"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(Pharmacy4));

        CameraUpdate zoom = CameraUpdateFactory.zoomTo(9);
        mMap.moveCamera(zoom);
        mMap.animateCamera(zoom);
    }


    private void gotoLocation(double lat, double lng, float zoom) {
        LatLng ll = new LatLng(lat, lng);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll, zoom);
        mMap.moveCamera(update);
    }

    public void geoLocation(View v) throws IOException {
        hideSoftKeyboard(v);

        EditText et = (EditText) findViewById(R.id.editText1);
        String location = et.getText().toString();

        Geocoder gc = new Geocoder(this);
        List<Address> list = gc.getFromLocationName(location, 1);
        Address add = list.get(0);
        String locality = add.getLocality();
        Toast.makeText(this, locality, Toast.LENGTH_LONG).show();

        double lat = add.getLatitude();
        double lng = add.getLongitude();

        gotoLocation(lat, lng, 10);


    }


    private void hideSoftKeyboard(View v) {
        InputMethodManager imm =(InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }


}


