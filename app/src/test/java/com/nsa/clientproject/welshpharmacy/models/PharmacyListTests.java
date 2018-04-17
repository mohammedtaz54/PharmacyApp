package com.nsa.clientproject.welshpharmacy.models;


import android.location.Location;

import org.junit.Before;
import org.junit.Test;


import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

//Reference:https://stackoverflow.com/questions/24158766/unit-test-static-method-with-dependency
//Accessed 31 March 2018
public class PharmacyListTests {
    private PharmacyList pharmacyList;

    @Before
    public void init() {
        pharmacyList = new PharmacyList();
        pharmacyList.updatePharmacies();
    }

    @Test
    public void evaluateCreatedList() {
        //This is a very simple test
        //But more advanced ones should be created whenever the filtering is made
        assertTrue(pharmacyList.getPharmacies().size() > 0);
    }

    @Test
    public void evaluateFilterByService() {
        PharmacySearchCriteria pharmacySearchCriteria = new PharmacySearchCriteria();
        Map<PharmacyServices, Boolean> mapNoBloodPressure = new HashMap<PharmacyServices, Boolean>() {{
            put(PharmacyServices.BLOOD_PRESSURE_MONITORING, false);
        }};
        Map<PharmacyServices, Boolean> mapHasBloodPressure = new HashMap<PharmacyServices, Boolean>() {{
            put(PharmacyServices.BLOOD_PRESSURE_MONITORING, true);
        }};
        pharmacySearchCriteria.setServicesRequired(mapHasBloodPressure);
        pharmacyList.setPharmacySearchCriteria(pharmacySearchCriteria);
        for (Pharmacy p : pharmacyList.getPharmacies()) {
            assertNotEquals("Pharmacy 1", p.getName());
        }
        boolean hasPharmacy1 = false;
        pharmacySearchCriteria.setServicesRequired(mapNoBloodPressure);
        for (Pharmacy p : pharmacyList.getPharmacies()) {
            if (p.getName().equals("Pharmacy 1")) {
                hasPharmacy1 = true;
            }
        }
        assertTrue(hasPharmacy1);
        pharmacyList.setPharmacySearchCriteria(null);
        hasPharmacy1 = false;
        pharmacySearchCriteria.setServicesRequired(mapNoBloodPressure);
        for (Pharmacy p : pharmacyList.getPharmacies()) {
            if (p.getName().equals("Pharmacy 1")) {
                hasPharmacy1 = true;
            }
        }
        assertTrue(hasPharmacy1);


    }
    //Can't unit test this because I have no idea how to mock static dependencies
    //Especially ones that change an array that is given to them as a parameter
    //51.5893927, -3.0012496
    @Test
    public void evaluateSearchByLocation(){

//        Location locationMock = mock(Location.class);
//        PharmacySearchCriteria pharmacySearchCriteria = new PharmacySearchCriteria();
//        pharmacyList.setPharmacySearchCriteria(pharmacySearchCriteria);
//        assertTrue(pharmacyList.getPharmacies().size()>1);
//        pharmacySearchCriteria.setUserLat(51.5893927);
//        pharmacySearchCriteria.setUserLng(-3.0012496);
//        pharmacySearchCriteria.setMaxDistance(5);
//        assertEquals("Newport Pharmacy",pharmacyList.getPharmacies().get(0).getName());
    }

}
