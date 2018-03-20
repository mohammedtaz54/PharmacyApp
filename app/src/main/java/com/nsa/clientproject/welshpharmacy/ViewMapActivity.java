package com.nsa.clientproject.welshpharmacy;

import android.content.Intent;
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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.nsa.clientproject.welshpharmacy.models.Pharmacy;
import com.nsa.clientproject.welshpharmacy.models.PharmacyList;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewMapActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

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
        PharmacyList pl = new PharmacyList();
        pl.updatePharmacies();
        pharmacyList = pl.getPharmacies();

        for (int i = 0; i < pharmacyList.size(); i++) {
            Log.d(TAG, pharmacyList.get(i).getLocation());
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        for(Pharmacy p: pharmacyList){
            MarkerOptions marker = new MarkerOptions()
                    .position(new LatLng(p.getPharmacyLat(),p.getPharmacyLng()))
                    .title(p.getName());

            Marker m = mMap.addMarker(marker);
            m.setTag(p);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(p.getPharmacyLat(),p.getPharmacyLng())));
            mMap.moveCamera(CameraUpdateFactory.zoomTo(13));
        }
        mMap.setOnMarkerClickListener(this);
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


    private void hideSoftKeyboard(View v) {
        InputMethodManager imm =(InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }


    @Override
    public boolean onMarkerClick(Marker marker) {
        Intent i = new Intent(this,PharmacyView.class);
        i.putExtra("pharmacy",(Serializable)marker.getTag());
        startActivity(i);
        return true;
    }
}



