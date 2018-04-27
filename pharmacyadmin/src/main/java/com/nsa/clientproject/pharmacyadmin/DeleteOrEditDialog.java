package com.nsa.clientproject.pharmacyadmin;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
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
    private void deletePharmacy(){
        StringRequest deletePharmacyRequest = new StringRequest(Request.Method.DELETE, "http://hdimitrov.pythonanywhere.com/deletePharmacy/"+Integer.toString(pharmacyId), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(context, R.string.deleted_success, Toast.LENGTH_SHORT).show();
                ((Activity)context).recreate();
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
}
