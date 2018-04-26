package com.nsa.clientproject.pharmacyadmin;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.nsa.clientproject.pharmacyadmin.adapters.PharmacyListAdapter;
import com.nsa.clientproject.pharmacyadmin.models.PharmacyListItem;

import java.util.List;


public class PharmacyListFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private View currentView;
    public PharmacyListFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        currentView =  inflater.inflate(R.layout.fragment_pharmacy_list, container, false);
        // Inflate the layout for this fragment
        List<PharmacyListItem> pharmacyList = (List<PharmacyListItem>) getArguments().getSerializable("pharmacies");
        ListView pharmaciesListView  = currentView.findViewById(R.id.pharmacies_list_view);
        pharmaciesListView.setAdapter(new PharmacyListAdapter(getContext(),pharmacyList));
        return currentView;
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


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onPharmacyClick(int id);
    }
}