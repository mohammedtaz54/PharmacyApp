package com.nsa.clientproject.welshpharmacy;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.contrib.NavigationViewActions;
import android.support.test.filters.LargeTest;
import android.support.test.filters.MediumTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.nsa.clientproject.welshpharmacy.models.Pharmacy;
import com.nsa.clientproject.welshpharmacy.models.PharmacyList;
import com.nsa.clientproject.welshpharmacy.models.PharmacyServices;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.security.Key;

import static android.support.test.espresso.Espresso.*;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withTagValue;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.Assert.*;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.AllOf.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class EvaluateFullApp {
    @Rule
    public ActivityTestRule<MultiPharmacyActivity> multiPharmacyActivity = new ActivityTestRule<>(MultiPharmacyActivity.class);


    @Test
    public void evaluateFullAppFlow() throws InterruptedException {
        while (!multiPharmacyActivity.getActivity().isHasFinishedLoading()) {
            Thread.sleep(200);
        }
        Thread.sleep(200);
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.settings));
        String postcode = "CF103EJ";
        String maxdistance = "5";
        SharedPreferences sp = multiPharmacyActivity.getActivity().getSharedPreferences("DEFAULT_SETTINGS", Context.MODE_PRIVATE);
        onView(EspressoHelpers.nthChildOf(withId(R.id.services_required), 0)).perform(click());
        onView(withId(R.id.show_welsh_services)).perform(click());
        onView(EspressoHelpers.nthChildOf(withId(R.id.services_required_welsh), 0)).perform(click());
        onView(withId(R.id.show_location_options)).perform(click());
        onView(withId(R.id.postcode_given)).perform(typeText(postcode));
        closeSoftKeyboard();
        onView(withId(R.id.maximum_distance)).perform(typeText(maxdistance));
        closeSoftKeyboard();
        onView(withId(R.id.finish)).perform(click());
        assertTrue(sp.getBoolean(KeyValueHelper.KEY_DEFAULT_SERVICES_PREFIX + "FLU_SHOT", false));
        assertFalse(sp.getBoolean(KeyValueHelper.KEY_DEFAULT_SERVICES_PREFIX + "DISPENSING", false));
        assertFalse(sp.getBoolean(KeyValueHelper.KEY_USE_LOCATION_DEFAULT, false));
        assertEquals("CF103EJ", sp.getString(KeyValueHelper.KEY_POSTCODE_TEXT, "xd"));
        assertEquals(Float.parseFloat(maxdistance), sp.getFloat(KeyValueHelper.KEY_MAXDISTANCE_TEXT, 13));
        while (!multiPharmacyActivity.getActivity().isHasFinishedLoading()) {
            Thread.sleep(200);
        }
        ListOfPharmaciesCards cards = (ListOfPharmaciesCards) multiPharmacyActivity.getActivity().getCurrentFragment();
        ListView listPharmacies = (ListView) cards.getView().findViewById(R.id.card_list);
        Pharmacy p = (Pharmacy) listPharmacies.getAdapter().getItem(0);
        assertTrue(p.getServicesOffered().contains(PharmacyServices.FLU_SHOT));
        assertTrue(p.getServicesInWelsh().contains(PharmacyServices.FLU_SHOT));
        assertTrue(p.getDistanceToUser() * PharmacyList.METRES_TO_MILE < 5);
        onView(EspressoHelpers.nthChildOf(withId(R.id.card_list),0)).perform(click());
        onView(withId(R.id.single_name)).check(matches(withText(p.getName())));
        pressBack();
        onView(withId(R.id.filter)).perform(click());
        onView(EspressoHelpers.nthChildOf(withId(R.id.services),0)).perform(click());
        onView(EspressoHelpers.nthChildOf(withId(R.id.services),1)).perform(click());
        onView(EspressoHelpers.nthChildOf(withId(R.id.services_welsh),0)).perform(scrollTo(),click());
        onView(EspressoHelpers.nthChildOf(withId(R.id.services_welsh),1)).perform(scrollTo(),click());
        onView(withId(R.id.use_postcode)).perform(scrollTo(),click());
        onView(withId(R.id.postcode_string)).perform(scrollTo(),click(),clearText(),typeText("CF103EJ"));
        closeSoftKeyboard();
        onView(withId(R.id.maximum_distance)).perform(scrollTo(),click(),clearText(),typeText("5"));
        closeSoftKeyboard();
        onView(withId(R.id.submit_filter)).perform(scrollTo(),click());
        ListView listPharmaciesUpdated = (ListView) cards.getView().findViewById(R.id.card_list);
        Pharmacy p2 = (Pharmacy) listPharmaciesUpdated.getAdapter().getItem(0);
        assertTrue(p2.getServicesOffered().contains(PharmacyServices.BLOOD_PRESSURE_MONITORING));
        assertTrue(p2.getServicesInWelsh().contains(PharmacyServices.BLOOD_PRESSURE_MONITORING));
        onView(EspressoHelpers.nthChildOf(withId(R.id.card_list),0)).perform(click());
        onView(withId(R.id.single_name)).check(matches(withText(p2.getName())));

    }
}