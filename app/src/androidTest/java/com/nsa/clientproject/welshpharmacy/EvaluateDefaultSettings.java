package com.nsa.clientproject.welshpharmacy;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.view.ViewGroup;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.security.Key;

import static android.support.test.espresso.Espresso.*;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withTagValue;
import static junit.framework.Assert.*;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.AllOf.allOf;

@RunWith(AndroidJUnit4.class)
public class EvaluateDefaultSettings {
    @Rule
    public ActivityTestRule<DefaultSettings> defaultSettings = new ActivityTestRule<>(DefaultSettings.class);

    //Reference:https://stackoverflow.com/questions/24748303/selecting-child-view-at-index-using-espresso
    //Accessed on 21 April 2018
    public static Matcher<View> nthChildOf(final Matcher<View> parentMatcher, final int childPosition) {
        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("with " + childPosition + " child view of type parentMatcher");
            }

            @Override
            public boolean matchesSafely(View view) {
                if (!(view.getParent() instanceof ViewGroup)) {
                    return parentMatcher.matches(view.getParent());
                }

                ViewGroup group = (ViewGroup) view.getParent();
                return parentMatcher.matches(view.getParent()) && group.getChildAt(childPosition).equals(view);
            }
        };
    }

    @Test
    public void evalSettingsSave(){
        String postcode = "CF103EJ";
        String maxdistance = "5";
        SharedPreferences sp = defaultSettings.getActivity().getSharedPreferences("DEFAULT_SETTINGS", Context.MODE_PRIVATE);
        onView(nthChildOf(withId(R.id.services_required),0)).perform(click());
        onView(withId(R.id.show_welsh_services)).perform(click());
        onView(nthChildOf(withId(R.id.services_required_welsh),0)).perform(click());
        onView(withId(R.id.show_location_options)).perform(click());
        onView(withId(R.id.postcode_given)).perform(typeText(postcode));
        closeSoftKeyboard();
        onView(withId(R.id.maximum_distance)).perform(typeText(maxdistance));
        closeSoftKeyboard();
        onView(withId(R.id.finish)).perform(click());
        assertTrue(sp.getBoolean(KeyValueHelper.KEY_DEFAULT_SERVICES_PREFIX+"FLU_SHOT",false));
        assertFalse(sp.getBoolean(KeyValueHelper.KEY_DEFAULT_SERVICES_PREFIX+"DISPENSING",false));
        assertFalse(sp.getBoolean(KeyValueHelper.KEY_USE_LOCATION_DEFAULT,false));

        assertEquals("CF103EJ",sp.getString(KeyValueHelper.KEY_POSTCODE_TEXT,"xd"));
        assertEquals(Float.parseFloat(maxdistance),sp.getFloat(KeyValueHelper.KEY_MAXDISTANCE_TEXT,13));

    }
}
