package com.nsa.clientproject.welshpharmacy;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.nsa.clientproject.welshpharmacy.adapters.PharmacyListCardViewAdapter;
import com.nsa.clientproject.welshpharmacy.models.Pharmacy;
import com.nsa.clientproject.welshpharmacy.models.PharmacyList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ListOfPharmaciesCards.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class ListOfPharmaciesCards extends Fragment implements AdapterView.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {

    private OnFragmentInteractionListener mListener;
    /**
     * Stores the list of pharmacies.
     */
    private PharmacyList pharmacyList;
    /**
     * Stores the onFilterUpdateBroadcast value (so we can register and unregister
     */
    private OnFilterUpdateBroadcast onFilterUpdateBroadcast;
    public ListOfPharmaciesCards() {
        // Required empty public constructor
    }
    private SwipeRefreshLayout refreshLayout;
    /**
     * Populates the card views.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_list_of_pharmacies_cards, container, false);
        ListView cardList =  v.findViewById(R.id.card_list);
        this.pharmacyList =(PharmacyList) getArguments().getSerializable("pharmacyList");
        PharmacyListCardViewAdapter adapter = new PharmacyListCardViewAdapter(getContext(),pharmacyList.getPharmacies());
        cardList.setAdapter(adapter);
        refreshLayout = v.findViewById(R.id.swipe_to_refresh);
        refreshLayout.setOnRefreshListener(this);
//        setAdapterForList();
        cardList.setOnItemClickListener(this);
        return v;
    }

    /**
     * Unregisters ther eceiver
     */
    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(onFilterUpdateBroadcast);
    }

    /**
     * Registers the receiver.
     */
    @Override
    public void onResume() {
        onFilterUpdateBroadcast = new OnFilterUpdateBroadcast();
        IntentFilter filter = new IntentFilter("com.nsa.clientproject.welshpharmacy.UPDATED_LIST_PHARMACIES");
        Log.d("R","REGISTERING RECEIVER");
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(onFilterUpdateBroadcast,filter);
        super.onResume();
    }

    /**
     * Sets the adapter for the list
     */
    private void setAdapterForList(){
        ListView cardList =  getView().findViewById(R.id.card_list);
        PharmacyListCardViewAdapter adapter = new PharmacyListCardViewAdapter(getContext(),pharmacyList.getPharmacies());
        cardList.setAdapter(adapter);

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
     navigation_drawer_open
     * Calls the onFragmentInteraction method in the parent activity when a list item is clicked
     * @param parent the parent view
     * @param view the current view
     * @param position the position of the item
     * @param id the id of the item
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mListener.onFragmentInteraction((Pharmacy)parent.getItemAtPosition(position));
    }

    /**
     * Called when a swipe gesture triggers a refresh.
     */
    @Override
    public void onRefresh() {
        mListener.onRefresh();
        refreshLayout.setRefreshing(false);
    }


    /**
     * Class that handles what happens when the filter update broadcast triggers
     */
    private class OnFilterUpdateBroadcast extends BroadcastReceiver {
        /**
         * Describes what we do when we get the FilterUpdateBroadcast
         */
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("R","TRIGGERED BROADCAST");

            setAdapterForList();
        }
    }
    /**
     * Listens for clicks on elements, this must be implemented by the parent activity.
      */
    public interface OnFragmentInteractionListener {
        /**
         * When the user clicks a pharmacy, this method triggers
         * @param pharmacy the pharmacy the user clicked
         */
        void onFragmentInteraction(Pharmacy pharmacy);

        /**
         * When the user swipes up to refresh, this method triggers
         */
        void onRefresh();
    }
}
