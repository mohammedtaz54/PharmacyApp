package com.nsa.clientproject.pharmacyadmin;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.nsa.clientproject.pharmacyadmin.models.PharmacyListItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LoadingFragment extends Fragment {
    private List<PharmacyListItem>  pharmaciesList;
    private OnFragmentInteractionListener mListener;
    private boolean isRunning = false;
    private boolean hasFinishedLoading = false;
    public LoadingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.fragment_loading, container, false);

        String url ="https://hdimitrov.pythonanywhere.com/pharmacies";
        pharmaciesList = new ArrayList<>();
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {

                     for(int i=0;i<response.length();i++){
                         try {
                             JSONObject singleRow = response.getJSONObject(i);
                             String name = singleRow.getString("address");
                             Integer id = singleRow.getInt("id");
                             pharmaciesList.add(new PharmacyListItem(name,id));
                         } catch (JSONException e) {
                             e.printStackTrace();
                         }
                     }
                     hasFinishedLoading = true;
                     if(isRunning) {
                         mListener.onFragmentInteraction(pharmaciesList);
                     }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Snackbar.make(v, R.string.no_connection,Snackbar.LENGTH_INDEFINITE).show();

                    }
                });
        RequestQueueSingleton.getInstance(this.getContext()).getRequestQueue().add(jsonObjectRequest);
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
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(List<PharmacyListItem> pharmacies);
    }

    @Override
    public void onResume() {
        isRunning = true;
        super.onResume();
    }

    @Override
    public void onPause() {
        isRunning = false;
        if(hasFinishedLoading){
            mListener.onFragmentInteraction(pharmaciesList);
        }
        super.onPause();
    }
}
