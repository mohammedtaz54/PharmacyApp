package com.nsa.clientproject.welshpharmacy;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.ListViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.nsa.clientproject.welshpharmacy.adapters.PharmacyListCardViewAdapter;
import com.nsa.clientproject.welshpharmacy.models.Pharmacy;
import com.nsa.clientproject.welshpharmacy.models.PharmacyList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ListOfPharmaciesCards.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class ListOfPharmaciesCards extends Fragment implements AdapterView.OnItemClickListener {

    private OnFragmentInteractionListener mListener;

    public ListOfPharmaciesCards() {
        // Required empty public constructor
    }

    /**
     * Populates the card views.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_list_of_pharmacies_cards, container, false);
        ListViewCompat cardList =  v.findViewById(R.id.card_list);
        PharmacyList pharmacyList = new PharmacyList();
        pharmacyList.updatePharmacies();
        PharmacyListCardViewAdapter adapter = new PharmacyListCardViewAdapter(getContext(),pharmacyList.getPharmacies());
        cardList.setAdapter(adapter);
        cardList.setOnItemClickListener(this);
        return v;
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
     * Listens for clicks on elements, this must be implemented by the parent activity.
      */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Pharmacy pharmacy);
    }
}
