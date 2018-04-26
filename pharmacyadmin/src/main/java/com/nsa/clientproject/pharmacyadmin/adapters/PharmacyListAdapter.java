package com.nsa.clientproject.pharmacyadmin.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nsa.clientproject.pharmacyadmin.R;
import com.nsa.clientproject.pharmacyadmin.models.PharmacyListItem;

import java.util.List;

public class PharmacyListAdapter extends BaseAdapter {
    private Context context;
    private List<PharmacyListItem> pharmacies;

    public PharmacyListAdapter(Context context, List<PharmacyListItem> pharmacies) {
        this.context = context;
        this.pharmacies = pharmacies;
    }

    @Override
    public int getCount() {
        return pharmacies.size();
    }

    @Override
    public PharmacyListItem getItem(int position) {
        return pharmacies.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(this.context)
                    .inflate(android.R.layout.simple_list_item_1, parent, false);
        }
        TextView inputText = convertView.findViewById(android.R.id.text1);
        inputText.setText(getItem(position).getName());
        return convertView;
    }
}
