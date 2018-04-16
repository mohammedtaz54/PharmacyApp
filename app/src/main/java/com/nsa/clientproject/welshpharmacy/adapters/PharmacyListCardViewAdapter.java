package com.nsa.clientproject.welshpharmacy.adapters;

import android.content.Context;
import android.support.v4.widget.TextViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nsa.clientproject.welshpharmacy.R;
import com.nsa.clientproject.welshpharmacy.models.Pharmacy;

import java.time.LocalTime;
import java.util.List;

/**
 * An adapter for converting pharmacies into a listView
 */

public class PharmacyListCardViewAdapter extends BaseAdapter {
    private final Context context;
    private final List<Pharmacy> pharmacyList;

    public PharmacyListCardViewAdapter(Context context, List<Pharmacy> pharmacyList) {
        this.context = context;
        this.pharmacyList = pharmacyList;
    }

    @Override
    public int getCount() {
        return pharmacyList.size();
    }

    @Override
    public Pharmacy getItem(int position) {
        return pharmacyList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * Generates the view for a position
     *
     * @param position    the position
     * @param convertView the given view
     * @param parent      the parent view
     * @return the view created for me.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(this.context)
                    .inflate(R.layout.card_pharmacy_list, parent, false);
        }
        TextView pharmacyName = convertView.findViewById(R.id.pharmacy_name);
        TextView pharmacyLocation = convertView.findViewById(R.id.location_text);
        TextView openOrClosed = convertView.findViewById(R.id.open_closed);
        LocalTime currentTime = LocalTime.now();
        pharmacyLocation.setText(getItem(position).getLocation());
        pharmacyName.setText(getItem(position).getName());
        if(currentTime.compareTo(getItem(position).getOpenTime())>0
                && currentTime.compareTo(getItem(position).getCloseTime())<0){
            openOrClosed.setText(R.string.open);
            openOrClosed.setTextColor(context.getColor(R.color.colorOpen));
        }
        else{
            openOrClosed.setText(R.string.closed);
            openOrClosed.setTextColor(context.getColor(R.color.colorClosed));

        }



        return convertView;
    }
}
