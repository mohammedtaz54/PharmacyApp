package com.nsa.clientproject.welshpharmacy;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.support.test.filters.MediumTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.rule.GrantPermissionRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.nsa.clientproject.welshpharmacy.DefaultSettings;
import com.nsa.clientproject.welshpharmacy.KeyValueHelper;
import com.nsa.clientproject.welshpharmacy.R;
import com.nsa.clientproject.welshpharmacy.models.Pharmacy;
import com.nsa.clientproject.welshpharmacy.models.PharmacyList;
import com.nsa.clientproject.welshpharmacy.models.PharmacySearchCriteria;
import com.nsa.clientproject.welshpharmacy.models.PharmacyServices;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static android.support.test.espresso.Espresso.closeSoftKeyboard;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isChecked;
import static android.support.test.espresso.matcher.ViewMatchers.isNotChecked;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.format;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.collection.IsMapContaining.hasEntry;

@MediumTest
@RunWith(AndroidJUnit4.class)
public class EvaluateFilteringDefault {
    @Rule
    public ActivityTestRule<MultiPharmacyActivity> multiPharmacyActivity = new ActivityTestRule<>(MultiPharmacyActivity.class);


    //Reference: https://stackoverflow.com/questions/33929937/android-marshmallow-test-permissions-with-espresso
    //Accessed on 14 May 2018
    @Rule
    public GrantPermissionRule permissionRule = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION);

    @Before
    public void init() throws InterruptedException {
        EspressoHelpers.goThroughWizardIfNeeded(multiPharmacyActivity.getActivity());
        while (!multiPharmacyActivity.getActivity().isHasFinishedLoading()) {
            Thread.sleep(200);
            //wait
        }
        Thread.sleep(200);
    }

    @Test
    public void evalFilterByService() {
        PharmacySearchCriteria filters = new PharmacySearchCriteria();
        Map<PharmacyServices, Boolean> filterSearch = new HashMap<PharmacyServices, Boolean>() {{
            put(PharmacyServices.FLU_SHOT, true);
        }};
        filters.setServicesRequired(filterSearch);
        multiPharmacyActivity.getActivity().getPharmacyList().setPharmacySearchCriteria(filters);
        Intent broadcastChange = new Intent();
        broadcastChange.setAction("com.nsa.clientproject.welshpharmacy.UPDATED_LIST_PHARMACIES");
        LocalBroadcastManager.getInstance(multiPharmacyActivity.getActivity()).sendBroadcast(broadcastChange);
        ListOfPharmaciesCards cards = (ListOfPharmaciesCards) multiPharmacyActivity.getActivity().getCurrentFragment();
        ListView listPharmacies = (ListView) cards.getView().findViewById(R.id.card_list);
        Pharmacy p = (Pharmacy) listPharmacies.getAdapter().getItem(0);
        assertTrue(p.getServicesOffered().contains(PharmacyServices.FLU_SHOT));

    }

    @Test
    public void evalFilterByServiceWelsh() {
        PharmacySearchCriteria filters = new PharmacySearchCriteria();
        Map<PharmacyServices, Boolean> filterSearch = new HashMap<PharmacyServices, Boolean>() {{
            put(PharmacyServices.FLU_SHOT, true);
        }};
        filters.setServicesRequiredInWelsh(filterSearch);
        multiPharmacyActivity.getActivity().getPharmacyList().setPharmacySearchCriteria(filters);
        Intent broadcastChange = new Intent();
        broadcastChange.setAction("com.nsa.clientproject.welshpharmacy.UPDATED_LIST_PHARMACIES");
        LocalBroadcastManager.getInstance(multiPharmacyActivity.getActivity()).sendBroadcast(broadcastChange);
        ListOfPharmaciesCards cards = (ListOfPharmaciesCards) multiPharmacyActivity.getActivity().getCurrentFragment();
        ListView listPharmacies = (ListView) cards.getView().findViewById(R.id.card_list);
        Pharmacy p = (Pharmacy) listPharmacies.getAdapter().getItem(0);
        assertTrue(p.getServicesInWelsh().contains(PharmacyServices.FLU_SHOT));
    }

    @Test
    public void evalFilterByDistance() {
        PharmacySearchCriteria filters = new PharmacySearchCriteria();
        filters.setUserLat(51.5893927);
        filters.setUserLng(-3.0012496);
        filters.setMaxDistance(50);
        multiPharmacyActivity.getActivity().getPharmacyList().setPharmacySearchCriteria(filters);
        Intent broadcastChange = new Intent();
        broadcastChange.setAction("com.nsa.clientproject.welshpharmacy.UPDATED_LIST_PHARMACIES");
        LocalBroadcastManager.getInstance(multiPharmacyActivity.getActivity()).sendBroadcast(broadcastChange);
        ListOfPharmaciesCards cards = (ListOfPharmaciesCards) multiPharmacyActivity.getActivity().getCurrentFragment();
        ListView listPharmacies = (ListView) cards.getView().findViewById(R.id.card_list);
        Pharmacy p = (Pharmacy) listPharmacies.getAdapter().getItem(0);
        Log.d("UnitTest", Double.toString(p.getDistanceToUser()));
        assertTrue(p.getDistanceToUser() * PharmacyList.METRES_TO_MILE < 50);
    }

    @Test
    public void evalFiltersSettingPreferences() throws IOException {
        multiPharmacyActivity.getActivity().getSharedPreferences("DEFAULT_SETTINGS", Context.MODE_PRIVATE)
                .edit()
                .putBoolean(KeyValueHelper.KEY_DEFAULT_SERVICES_PREFIX + "FLU_SHOT", false)
                .putBoolean(KeyValueHelper.KEY_DEFAULT_SERVICES_WELSH_PREFIX + "FLU_SHOT", false)
                .commit();
        onView(withId(R.id.filter)).perform(click());
        onView(EspressoHelpers.nthChildOf(withId(R.id.services), 0)).check(matches(isNotChecked())).perform(click());
        onView(EspressoHelpers.nthChildOf(withId(R.id.services_welsh), 0)).perform(scrollTo(), click());
        onView(withId(R.id.use_postcode)).perform(scrollTo(), click());
        onView(withId(R.id.postcode_string)).perform(scrollTo(), click(), clearText(), typeText("CF103EJ"));
        closeSoftKeyboard();
        onView(withId(R.id.maximum_distance)).perform(scrollTo(), click(), clearText(), typeText("5"));
        closeSoftKeyboard();
        onView(withId(R.id.submit_filter)).perform(scrollTo(), click());
        PharmacySearchCriteria pharmacySearchCriteria = multiPharmacyActivity.getActivity().getPharmacyList().getPharmacySearchCriteria();
        Geocoder g = new Geocoder(multiPharmacyActivity.getActivity());
        Address a = g.getFromLocationName("CF103EJ", 1).get(0);
        assertEquals(5.0, pharmacySearchCriteria.getMaxDistance());
        assertEquals(a.getLatitude(), pharmacySearchCriteria.getUserLat());
        assertEquals(a.getLongitude(), pharmacySearchCriteria.getUserLng());
        assertTrue(pharmacySearchCriteria.getServicesRequired().get(PharmacyServices.FLU_SHOT));
    }

}
