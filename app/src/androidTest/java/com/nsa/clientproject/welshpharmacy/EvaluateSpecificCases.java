package com.nsa.clientproject.welshpharmacy;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.contrib.NavigationViewActions;
import android.support.test.filters.LargeTest;
import android.support.test.filters.MediumTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.rule.GrantPermissionRule;
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
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withTagValue;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.Assert.*;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.AllOf.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class EvaluateSpecificCases {
    @Rule
    public ActivityTestRule<MultiPharmacyActivity> multiPharmacyActivity = new ActivityTestRule<>(MultiPharmacyActivity.class);

    //Reference: https://stackoverflow.com/questions/33929937/android-marshmallow-test-permissions-with-espresso
    //Accessed on 14 May 2018
    @Rule
    public GrantPermissionRule permissionRule = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION);

    @Test
    public void evalFirstTimeSettings(){
        multiPharmacyActivity.getActivity().getSharedPreferences("DEFAULT_SETTINGS",Context.MODE_PRIVATE).edit()
        .putBoolean(KeyValueHelper.KEY_FINISHED_WIZARD,false)
        .commit();
        multiPharmacyActivity.launchActivity(new Intent(multiPharmacyActivity.getActivity(),MultiPharmacyActivity.class));
        onView(withId(R.id.app_setup_title)).check(matches(isDisplayed()));
    }
    @Test
    public void evalInvalidInputDefaultSettings() throws InterruptedException {
        EspressoHelpers.goThroughWizardIfNeeded(multiPharmacyActivity.getActivity());
        while (!multiPharmacyActivity.getActivity().isHasFinishedLoading()) {
            Thread.sleep(200);
        }
        Thread.sleep(200);
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.settings));
        onView(withId(R.id.show_welsh_services)).perform(click());
        onView(withId(R.id.show_location_options)).perform(click());
        onView(withId(R.id.postcode_given)).perform(typeText("sljdhfjasdfhkjsda"));
        closeSoftKeyboard();
        onView(withId(R.id.maximum_distance)).perform(typeText(""));
        closeSoftKeyboard();
        onView(withId(R.id.finish)).perform(click());
        //testing toasts seems very awkward - test if we're in the correct activity instead
        onView(withId(R.id.app_setup_title_location)).check(matches(isDisplayed()));


    }

}
