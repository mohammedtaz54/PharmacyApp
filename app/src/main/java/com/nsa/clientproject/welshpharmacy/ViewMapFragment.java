package com.nsa.clientproject.welshpharmacy;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.nsa.clientproject.welshpharmacy.models.Pharmacy;
import com.nsa.clientproject.welshpharmacy.models.PharmacyList;

import java.io.Serializable;
import java.util.List;

public class ViewMapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private List<Pharmacy> pharmacyList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_view_map, container, false);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        PharmacyList pl = new PharmacyList();
        pl.updatePharmacies();
        pharmacyList = pl.getPharmacies();
        return v;

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


//    private void hideSoftKeyboard(View v) {
//        InputMethodManager imm =(InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
//        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
//    }


    @Override
    public boolean onMarkerClick(Marker marker) {
        Intent i = new Intent(this.getContext(),PharmacyView.class);
        i.putExtra("pharmacy",(Serializable)marker.getTag());
        startActivity(i);
        return true;
    }
}



