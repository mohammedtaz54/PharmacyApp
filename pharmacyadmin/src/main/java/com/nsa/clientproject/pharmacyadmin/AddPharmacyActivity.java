package com.nsa.clientproject.pharmacyadmin;

import android.content.IntentFilter;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.LinearLayoutCompat;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.nsa.clientproject.pharmacyadmin.models.PharmacyServices;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddPharmacyActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pharmacy_add_edit);
        LinearLayoutCompat weekdays = findViewById(R.id.week_day_checkboxes);
        for (DayOfWeek d : DayOfWeek.values()) {
            AppCompatCheckBox checkbox = new AppCompatCheckBox(this);
            LinearLayoutCompat line = new LinearLayoutCompat(this);
            checkbox.setTag(d);


            checkbox.setText(getString(getResources().getIdentifier(d.name(), "string", getPackageName())));
            EditText startTime = new EditText(this);
            EditText finishTime = new EditText(this);
            InputFilter[] inputFilters = new InputFilter[2];
            inputFilters[0] = new InputFilter.LengthFilter(5);
            //Reference: https://stackoverflow.com/questions/2361497/how-to-create-edittext-accepts-alphabets-only-in-android
            //Accessed on 26 April 2018
            inputFilters[1] = new InputFilter() {
                public CharSequence filter(CharSequence src, int start,
                                           int end, Spanned dst, int dstart, int dend) {
                    if (src.equals("")) { // for backspace
                        return src;
                    }
                    if (src.toString().matches("[0123456789:]+")) {
                        return src;
                    }
                    return "";
                }
            };
            startTime.setFilters(inputFilters);
            finishTime.setFilters(inputFilters);
            line.addView(checkbox);
            line.addView(startTime);
            line.addView(finishTime);
            if (d != DayOfWeek.SATURDAY && d != DayOfWeek.SUNDAY) {
                checkbox.setChecked(true);
                startTime.setText(getString(R.string.R_id_nine_thirty));
                finishTime.setText(R.string.R_id_five_pm);
            } else {
                startTime.setText(R.string.zero_zero_time);
                finishTime.setText(R.string.zero_zero_time);
            }
            weekdays.addView(line);
        }
        LinearLayoutCompat services = findViewById(R.id.services);
        LinearLayoutCompat servicesWelsh = findViewById(R.id.services_welsh);
        for (PharmacyServices service : PharmacyServices.values()) {
            Log.d("HELP", service.name());
            AppCompatCheckBox currentCheckbox = new AppCompatCheckBox(this);
            currentCheckbox.setTag(service.name());
            currentCheckbox.setText(getResources().getIdentifier(service.name(), "string", getPackageName()));
            AppCompatCheckBox currentCheckboxWelsh = new AppCompatCheckBox(this);
            currentCheckboxWelsh.setTag(service.name());
            currentCheckboxWelsh.setText(getResources().getIdentifier(service.name(), "string", getPackageName()));
            services.addView(currentCheckbox);
            servicesWelsh.addView(currentCheckboxWelsh);
        }
        Button submitButton = findViewById(R.id.submit_button);
        submitButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.submit_button) {
            final String name = ((EditText) findViewById(R.id.pharmacy_name)).getText().toString();
            final String postcode = ((EditText) findViewById(R.id.pharmacy_postcode)).getText().toString();
            final String address = ((EditText) findViewById(R.id.pharmacy_address)).getText().toString() + " " + postcode;
            final String website = ((EditText) findViewById(R.id.pharmacy_website)).getText().toString();
            final String phone = ((EditText) findViewById(R.id.pharmacy_phone)).getText().toString();
            final String email = ((EditText) findViewById(R.id.pharmacy_email)).getText().toString();
            List<Integer[]> openingClosingTimes = new ArrayList<>();


            //todo: somehow make this more clear, the parsing of weekdays is a mess.
            Address addressParsed = null;
            Geocoder geocoder = new Geocoder(this);
            try {
                List<Address> addresses = geocoder.getFromLocationName(address, 1);
                if(addresses.size()>0){
                    addressParsed = addresses.get(0);
                }
                else{
                    Toast.makeText(this, R.string.invalid_address, Toast.LENGTH_SHORT).show();
                    return;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            LinearLayoutCompat weekdays = findViewById(R.id.week_day_checkboxes);
            for (int i = 0; i < weekdays.getChildCount(); i++) {
                LinearLayoutCompat currentWeekday = (LinearLayoutCompat) weekdays.getChildAt(i);
                CheckBox currentCheckbox = (CheckBox) currentWeekday.getChildAt(0);
                if (currentCheckbox.isChecked()) {

                    String[] openingTime = ((EditText) currentWeekday.getChildAt(1))
                            .getText()
                            .toString()
                            .split(":");
                    String[] closingTime = ((EditText) currentWeekday.getChildAt(2))
                            .getText()
                            .toString()
                            .split(":");
                    if (openingTime.length != 2 || closingTime.length != 2) {
                        Log.d("HELP", "Wrong length");

                        showFailMessageInvalidTime();
                        return;
                    }
                    int openingHour;
                    int openingMinutes;
                    int closingHour;
                    int closingMinutes;
                    try {
                        openingHour = Integer.parseInt(openingTime[0]);
                        openingMinutes = Integer.parseInt(openingTime[1]);
                        closingHour = Integer.parseInt(closingTime[0]);
                        closingMinutes = Integer.parseInt(closingTime[1]);
                    } catch (NumberFormatException ex) {
                        Log.d("HELP", "NumberFormatException");
                        showFailMessageInvalidTime();
                        return;
                    }
                    if (!(intBetween(openingHour, 0, closingHour)
                            && intBetween(closingHour, 0, 24)
                            && intBetween(openingMinutes, 0, 60)
                            && intBetween(closingMinutes, 0, 60))) {
                        showFailMessageInvalidTime();
                        Log.d("HELP", "InvalidValues");
                        return;


                    }
                    Integer[] currentOpeningTimes = new Integer[]{
                            openingHour,
                            openingMinutes,
                            closingHour,
                            closingMinutes};
                    openingClosingTimes.add(currentOpeningTimes);
                } else {
                    Integer[] currentOpeningTimes = new Integer[]{
                            0,
                            0,
                            0,
                            0};
                    openingClosingTimes.add(currentOpeningTimes);
                }
            }
            LinearLayoutCompat services = findViewById(R.id.services);
            List<String> pharmacyServices = new ArrayList<>();
            for(int i=0;i<services.getChildCount();i++){
                CheckBox c = (CheckBox) services.getChildAt(i);
                if(c.isChecked()){
                    pharmacyServices.add(c.getTag().toString());
                }
            }
            LinearLayoutCompat servicesWelsh = findViewById(R.id.services_welsh);
            List<String> pharmacyServicesWelsh = new ArrayList<>();
            for(int i=0;i<servicesWelsh.getChildCount();i++){
                CheckBox c = (CheckBox) servicesWelsh.getChildAt(i);
                if(c.isChecked()){
                    pharmacyServicesWelsh.add(c.getTag().toString());
                }
            }
            Gson gson = new Gson();
            final String openingClosingTimesField = gson.toJson(openingClosingTimes);
            final String servicesField = gson.toJson(pharmacyServices);
            final String servicesWelshField = gson.toJson(pharmacyServicesWelsh);
            final Address finalAddressParsed = addressParsed;
            RequestQueue requestQueue = RequestQueueSingleton.getInstance(this).getRequestQueue();
            StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST, "https://hdimitrov.pythonanywhere.com/pharmacies", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Toast.makeText(AddPharmacyActivity.this, R.string.successfully_added_pharmacy, Toast.LENGTH_SHORT).show();

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(AddPharmacyActivity.this, R.string.no_connection, Toast.LENGTH_SHORT).show();

                    Log.d("ERROR",error.toString());
                }
            }){
                @Override
                protected Map<String, String> getParams() {
                    HashMap<String,String> params = new HashMap<>();
                    params.put("name",name);
                    params.put("email",email);
                    params.put("address",address);
                    params.put("phone",phone);
                    params.put("website",website);
                    params.put("postcode",postcode);
                    params.put("openingClosingTimes",openingClosingTimesField);
                    params.put("services",servicesField);
                    params.put("servicesInWelsh",servicesWelshField);
                    params.put("lat", Double.toString(finalAddressParsed.getLatitude()));
                    params.put("lng", Double.toString(finalAddressParsed.getLongitude()));
                    Log.d("params",openingClosingTimesField);
                    return params;
                }
            };
            requestQueue.add(jsonObjectRequest);
        }
    }

    /**
     * Shows the fail message for the invalid time to the user
     */
    private void showFailMessageInvalidTime() {
        Toast.makeText(this, R.string.invalid_time, Toast.LENGTH_SHORT).show();
    }

    /**
     * Checks if an int is between two numbers (inclusive)
     *
     * @param a     the int we're checking
     * @param lower the lower boundary
     * @param upper the upper boundary
     * @return true if it is, false if it isn't
     */
    private boolean intBetween(int a, int lower, int upper) {
        if (a >= lower && a <= upper) {
            return true;
        }
        return false;
    }
}
