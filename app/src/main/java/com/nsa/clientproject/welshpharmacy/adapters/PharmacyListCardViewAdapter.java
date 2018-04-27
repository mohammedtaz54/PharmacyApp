package com.nsa.clientproject.welshpharmacy.adapters;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.TextViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;
import com.nsa.clientproject.welshpharmacy.PharmacyView;
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
    private static final int MAX_CHARS_NAME=20;
    private View coordinatorLayout;

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
        String pharmacyNameString = getItem(position).getName();
        if (pharmacyNameString.length() > PharmacyListCardViewAdapter.MAX_CHARS_NAME) {
            pharmacyName.setText(pharmacyNameString.substring(0, MAX_CHARS_NAME));
        } else {
            pharmacyName.setText(pharmacyNameString);

        }

        if(!(getItem(position).getServicesInWelsh().size()>0)) {
            convertView.findViewById(R.id.card_item).setBackgroundColor(context.getColor(R.color.english_pharmacy_background));
        }

        pharmacyLocation.setText(getItem(position).getPostcode());

        if(currentTime.compareTo(getItem(position).getOpenTime())>0
                && currentTime.compareTo(getItem(position).getCloseTime())<0){
            String closeTime = getItem(position).getCloseTime().toString();
            openOrClosed.setText(context.getString(R.string.open) + closeTime);
            openOrClosed.setTextColor(context.getColor(R.color.colorOpen));
        }
        else{
            openOrClosed.setText(R.string.closed);
            openOrClosed.setTextColor(context.getColor(R.color.colorClosed));

        }
        return convertView;
    }
    //Reference: https://stackoverflow.com/questions/5970640/how-to-find-android-textview-number-of-characters-per-line?utm_medium=organic&utm_source=google_rich_qa&utm_campaign=google_rich_qa
    //Accessed 20 April 2018
    private boolean isTooLarge (TextView text, String newText) {
        float textWidth = text.getPaint().measureText(newText);
        return (textWidth >= text.getMeasuredWidth ());
    }
}
