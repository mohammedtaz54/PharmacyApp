package com.nsa.clientproject.welshpharmacy;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Cache;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.nsa.clientproject.welshpharmacy.models.Pharmacy;
import com.nsa.clientproject.welshpharmacy.models.PharmacyList;
import com.nsa.clientproject.welshpharmacy.models.PharmacyServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;


public class LoadingFragment extends android.support.v4.app.Fragment {

    private OnFragmentInteractionListener mListener;
    private PharmacyList pharmacyList;

    public LoadingFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    /**
     * Loads all the json data needed for the app
     */
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        RequestQueue requestQueue = RequestQueueSingleton.getInstance(this.getContext()).getRequestQueue();
        JsonArrayRequest dataRequest = makeJsonRequest();
        requestQueue.add(dataRequest);
        this.pharmacyList = (PharmacyList) getArguments().getSerializable("pharmacyList");
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_loading, container, false);
    }

    @NonNull
    private JsonArrayRequest makeJsonRequest() {
        //Reference: http://velmm.com/volley-cache-example
        //Accessed  on 20 April 2018
        final String dataURL = "https://api.myjson.com/bins/hn6ff";
        return new JsonArrayRequest(
                Request.Method.GET,
                dataURL,
                null,
                new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        parseJsonArray(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Cache cache = RequestQueueSingleton.getInstance(getContext()).getRequestQueue().getCache();
                        Cache.Entry entry = cache.get(dataURL);
                        if (entry != null) {
                            try {
                                JSONArray resultsCached = new JSONArray(new String(entry.data));
                                parseJsonArray(resultsCached);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Snackbar.make(getView(), R.string.please_connect_internet_outdated,Snackbar.LENGTH_INDEFINITE).show();

                        }
                        else{
                            Snackbar.make(getView(), R.string.please_connect_internet_broken,Snackbar.LENGTH_INDEFINITE).show();
                        }
                    }
                });
    }

    private void parseJsonArray(JSONArray response) {
        List<Pharmacy> newPharmacyList = new ArrayList<>(800);
        for (int i = 0; i < response.length(); i++) {
            try {
                LinkedHashMap<DayOfWeek, LocalTime> openingTimesList = new LinkedHashMap<>();
                LinkedHashMap<DayOfWeek, LocalTime> closingTimesList = new LinkedHashMap<>();
                List<PharmacyServices> servicesList = new ArrayList<>(20);
                List<PharmacyServices> servicesWelshList = new ArrayList<>(20);
                JSONObject pharmacyJSON = response.getJSONObject(i);
                JSONArray services = pharmacyJSON.getJSONArray("services");
                JSONArray servicesWelsh = pharmacyJSON.getJSONArray("servicesInWelsh");

                JSONArray openingTimes = pharmacyJSON.getJSONArray("openingTimes");
                JSONArray closingTimes = pharmacyJSON.getJSONArray("closingTimes");
                //Deals with getting the opening times

                for (int j = 0; j < openingTimes.length(); j++) {
                    JSONArray currentOpeningTime = openingTimes.getJSONArray(j);
                    JSONArray currentClosingTime = closingTimes.getJSONArray(j);
                    openingTimesList.put(
                            DayOfWeek.of(j + 1),
                            LocalTime.of(currentOpeningTime.getInt(0), currentOpeningTime.getInt(1))
                    );
                    closingTimesList.put(
                            DayOfWeek.of(j + 1),
                            LocalTime.of(currentClosingTime.getInt(0), currentClosingTime.getInt(1))
                    );
                }
                for (int j = 0; j < services.length(); j++) {
                    servicesList.add(PharmacyServices.valueOf(services.getString(j)));
                }
                for (int j = 0; j < servicesWelsh.length(); j++) {
                    servicesWelshList.add(PharmacyServices.valueOf(servicesWelsh.getString(j)));
                }
                Pharmacy p = new Pharmacy(pharmacyJSON.getString("name"),
                        pharmacyJSON.getString("address")
                        , openingTimesList,
                        closingTimesList,
                        servicesList,
                        servicesWelshList,
                        pharmacyJSON.getDouble("lat"),
                        pharmacyJSON.getDouble("lng"),
                        pharmacyJSON.getString("postcode"),
                        pharmacyJSON.getString("website"),
                        pharmacyJSON.getString("email"),
                        pharmacyJSON.getString("phone")
                );
                newPharmacyList.add(p);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        pharmacyList.setPharmacies(newPharmacyList);
        mListener.onFinishedLoading();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onFinishedLoading();
    }
}
