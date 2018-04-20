package com.nsa.clientproject.welshpharmacy.models;


import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;


import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.Assert.*;

//Reference:https://stackoverflow.com/questions/24158766/unit-test-static-method-with-dependency
//Accessed 31 March 2018
public class PharmacyListTests {
    private PharmacyList pharmacyList;

    @Before
    public void init() {
        pharmacyList = new PharmacyList();
        final ArrayList<PharmacyServices> services = new ArrayList<PharmacyServices>() {{
            add(PharmacyServices.BLOOD_PRESSURE_MONITORING);
            add(PharmacyServices.FLU_SHOT);
        }};
        final ArrayList<PharmacyServices> FluServices = new ArrayList<PharmacyServices>() {{
            add(PharmacyServices.FLU_SHOT);
        }};
        final ArrayList<PharmacyServices> BloodServices = new ArrayList<PharmacyServices>() {{
            add(PharmacyServices.BLOOD_PRESSURE_MONITORING);
        }};
        final ArrayList<PharmacyServices> servicesEmpty = new ArrayList<PharmacyServices>();
        final LinkedHashMap<DayOfWeek, LocalTime> openingTimes = new LinkedHashMap<DayOfWeek, LocalTime>() {{
            put(DayOfWeek.MONDAY, LocalTime.of(9, 30));
            put(DayOfWeek.TUESDAY, LocalTime.of(9, 30));
            put(DayOfWeek.WEDNESDAY, LocalTime.of(9, 30));
            put(DayOfWeek.THURSDAY, LocalTime.of(9, 30));
            put(DayOfWeek.FRIDAY, LocalTime.of(9, 30));
            put(DayOfWeek.SATURDAY, LocalTime.of(0, 0));
            put(DayOfWeek.SUNDAY, LocalTime.of(0, 0));
        }};
        final LinkedHashMap<DayOfWeek, LocalTime> closingTimes = new LinkedHashMap<DayOfWeek, LocalTime>() {{
            put(DayOfWeek.MONDAY, LocalTime.of(17, 0));
            put(DayOfWeek.TUESDAY, LocalTime.of(17, 0));
            put(DayOfWeek.WEDNESDAY, LocalTime.of(17, 0));
            put(DayOfWeek.THURSDAY, LocalTime.of(17, 0));
            put(DayOfWeek.FRIDAY, LocalTime.of(17, 0));
            put(DayOfWeek.SATURDAY, LocalTime.of(0, 0));
            put(DayOfWeek.SUNDAY, LocalTime.of(0, 0));
        }};
        ArrayList<Pharmacy> pharmacies = new ArrayList<Pharmacy>() {{

            add(new Pharmacy(
                    "Pharmacy 1",
                    "CF103EP",
                    openingTimes,
                    closingTimes,
                    servicesEmpty,
                    servicesEmpty,
                    51.4927031, -3.1873809,
                    "CF103EP",
                    "http://google.com",
                    "test@test.com",
                    "7"
            ));
            add(new Pharmacy(
                    "Pharmacy 2",
                    "CF103EF",
                    closingTimes,
                    openingTimes,
                    services,
                    servicesEmpty,
                    51.49164649999999, -3.1848503,
                    "CF103EP",
                    "http://google.com",
                    "test@test.com",
                    "7"
            ));
            add(new Pharmacy(
                    "Pharmacy 3",
                    "CF243EP",
                    openingTimes,
                    closingTimes,
                    services,
                    services,
                    51.4865716, -3.1657761,
                    "CF103EP",
                    "http://google.com",
                    "test@test.com",
                    "7"
            ));
            add(new Pharmacy(
                    "Pharmacy 4",
                    "CF243EJ",
                    openingTimes,
                    closingTimes,
                    FluServices,
                    services,
                    51.48921559999999, -3.1666502,
                    "CF103EP",
                    "http://google.com",
                    "test@test.com",
                    "7"

            ));
            add(new Pharmacy(
                    "Newport Pharmacy",
                    "NP204NW",
                    openingTimes,
                    closingTimes,
                    FluServices,
                    BloodServices,
                    51.5893927, -3.0012496,
                    "CF103EP",
                    "http://google.com",
                    "test@test.com",
                    "7"
            ));
        }};
        pharmacyList.setPharmacies(pharmacies);
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
    @Test
    public void evaluateSearchByLocation(){

        PharmacySearchCriteria pharmacySearchCriteria = new PharmacySearchCriteria();
        PharmacyList.DistanceCalculator dc = mock(PharmacyList.DistanceCalculator.class);
        when(dc.distanceBetween(anyDouble(),anyDouble(),anyDouble(),anyDouble())).thenReturn((float)500000000);
        when(dc.distanceBetween(51.5893927,-3.0012496,51.5893927,-3.0012496)).thenReturn((float)50.0);
        pharmacyList.setPharmacySearchCriteria(pharmacySearchCriteria);
        assertTrue(pharmacyList.getPharmacies().size()>1);
        pharmacySearchCriteria.setUserLat(51.5893927);
        pharmacyList.setDistanceCalculator(dc);
        pharmacySearchCriteria.setUserLng(-3.0012496);
        pharmacySearchCriteria.setMaxDistance(5);
        assertEquals("Newport Pharmacy",pharmacyList.getPharmacies().get(0).getName());
        //cleanup
        pharmacyList.setDistanceCalculator(new PharmacyList.DistanceCalculator());

    }
    @Test
    public void evaluateSearchByServices(){
        Map<PharmacyServices,Boolean> pharmacyServices = new HashMap<PharmacyServices, Boolean>(){{
           put(PharmacyServices.FLU_SHOT,true);
        }};
        PharmacySearchCriteria pharmacySearchCriteria = new PharmacySearchCriteria();
        pharmacySearchCriteria.setServicesRequired(pharmacyServices);
        pharmacyList.setPharmacySearchCriteria(pharmacySearchCriteria);
        for(Pharmacy p: pharmacyList.getPharmacies()){
            assertTrue(p.getServicesOffered().contains(PharmacyServices.FLU_SHOT));
        }
    }

}
