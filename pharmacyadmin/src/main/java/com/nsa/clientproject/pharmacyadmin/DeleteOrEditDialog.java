package com.nsa.clientproject.pharmacyadmin;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.nsa.clientproject.pharmacyadmin.models.Pharmacy;
import com.nsa.clientproject.pharmacyadmin.models.PharmacyServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DeleteOrEditDialog extends DialogFragment {
    /**
     * Stores the pharmacy's iD
     */
    private int pharmacyId;
    private Context context;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        this.pharmacyId = getArguments().getInt("id");
        builder.setMessage(R.string.what_would_you_like_to_do)
                .setPositiveButton(R.string.edit, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        editPharmacyPopup();
                    }
                })
                .setNegativeButton(R.string.delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deletePharmacy();
                    }
                });
        return builder.create();
    }

    private void deletePharmacy() {
        StringRequest deletePharmacyRequest = new StringRequest(Request.Method.DELETE, "http://hdimitrov.pythonanywhere.com/deletePharmacy/" + Integer.toString(pharmacyId), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(context, R.string.deleted_success, Toast.LENGTH_SHORT).show();
                ((Activity) context).recreate();
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, R.string.delete_unsuccessful, Toast.LENGTH_SHORT).show();

                    }
                });
        RequestQueueSingleton.getInstance(getContext()).getRequestQueue().add(deletePharmacyRequest);
    }

    @Override
    public void onAttach(Context context) {
        //Because getContext() and getActivity() keep
        //Returning null
        //For no reason
        this.context = context;
        super.onAttach(context);
    }

    private void editPharmacyPopup() {
        JsonObjectRequest editRequest = new JsonObjectRequest(Request.Method.GET, "https://hdimitrov.pythonanywhere.com/pharmacies/"+ Integer.toString(pharmacyId), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject pharmacyJSON) {
                try {
                    LinkedHashMap<DayOfWeek, LocalTime> openingTimesList = new LinkedHashMap<>();
                    LinkedHashMap<DayOfWeek, LocalTime> closingTimesList = new LinkedHashMap<>();
                    List<PharmacyServices> servicesList = new ArrayList<>(20);
                    List<PharmacyServices> servicesWelshList = new ArrayList<>(20);
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
                    Intent i = new Intent(context,AddPharmacyActivity.class);
                    i.putExtra("pharmacy",p);
                    i.putExtra("pharmacyId",pharmacyId);
                    context.startActivity(i);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, R.string.cannot_edit_try_later, Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueueSingleton.getInstance(context).getRequestQueue().add(editRequest);

    }
}
