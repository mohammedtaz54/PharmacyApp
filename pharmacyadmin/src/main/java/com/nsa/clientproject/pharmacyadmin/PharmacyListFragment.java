package com.nsa.clientproject.pharmacyadmin;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.nsa.clientproject.pharmacyadmin.adapters.PharmacyListAdapter;
import com.nsa.clientproject.pharmacyadmin.models.PharmacyListItem;

import java.util.ArrayList;
import java.util.List;


public class PharmacyListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private OnFragmentInteractionListener mListener;
    private View currentView;
    private List<PharmacyListItem> pharmaciesFullList;
    private List<PharmacyListItem> pharmaciesFilteredList;



    private PharmacyListAdapter pharmacyListAdapter;

    private ListView pharmaciesListView;
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
        //Design pattern taken from
        //Reference: https://devreadwrite.com/posts/android-live-search-using-listview
        //Accessed 27 April 2018
        pharmaciesFullList = (List<PharmacyListItem>) getArguments().getSerializable("pharmacies");
        pharmaciesFilteredList = new ArrayList<>(pharmaciesFullList);


        pharmaciesListView  = currentView.findViewById(R.id.pharmacies_list_view);
        pharmacyListAdapter = new PharmacyListAdapter(getContext(),pharmaciesFilteredList);


        pharmaciesListView.setAdapter(pharmacyListAdapter);


        pharmaciesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PharmacyListItem item = (PharmacyListItem)parent.getAdapter().getItem(position);
                DeleteOrEditDialog dialog = new DeleteOrEditDialog();
                Bundle data = new Bundle();
                data.putInt("id",item.getId());
                dialog.setArguments(data);
                //I've no idea why but the ChildFragmentManager doesn't work here
                //There's a type mismatch because it's part of the support library
                dialog.show(getActivity().getFragmentManager(),"POPUP");
            }
        });
        SwipeRefreshLayout swipeRefreshLayout = currentView.findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(this);
        EditText searchBar = currentView.findViewById(R.id.search);

        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                pharmaciesFilteredList.clear();
                for(PharmacyListItem p : pharmaciesFullList){
                    if(p.getName().toLowerCase().replace(" ","").contains(s.toString().toLowerCase().replace(" ",""))){
                        pharmaciesFilteredList.add(p);
                    }
                }
                Log.d("HELP",Integer.toString(pharmaciesFilteredList.size()));
                pharmaciesListView.invalidateViews();

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
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

    @Override
    public void onRefresh() {
        mListener.onSwipeToRefresh();
    }



    public interface OnFragmentInteractionListener {
        void onSwipeToRefresh();
    }
}
