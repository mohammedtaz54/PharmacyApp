package com.nsa.clientproject.welshpharmacy;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by c1734384 on 29-Mar-18.
 */

public class FilterDialogFragment extends DialogFragment implements View.OnClickListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View current_view = layoutInflater.inflate(R.layout.fragment_dialog_filter, null);
        builder.setView(current_view);
        Button b = current_view.findViewById(R.id.submit_filter);
        b.setOnClickListener(this);
        return builder.create();
    }


    @Override
    public void onClick(View v) {
        Toast.makeText(getContext(), "BOO", Toast.LENGTH_SHORT).show();
        dismiss();
    }
}
