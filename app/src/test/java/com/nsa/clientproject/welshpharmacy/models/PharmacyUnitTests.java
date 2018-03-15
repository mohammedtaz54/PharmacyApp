package com.nsa.clientproject.welshpharmacy.models;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Array;
import java.time.LocalTime;
import java.util.Arrays;

import static org.junit.Assert.*;

public class PharmacyUnitTests {
    private Pharmacy pharmacy;
    private PharmacyServices[] services;
    @Before
    public void init(){
        services = new PharmacyServices[]{PharmacyServices.BLOOD_PRESSURE_MONITORING,PharmacyServices.FLU_SHOT};
        pharmacy = new Pharmacy("Pharmacy",
                "Moon",
                LocalTime.of(9,0),
                LocalTime.of(17,0),
                Arrays.asList(services),
                Arrays.asList(services)
                );
    }
    @Test
    public void evaluateGetters(){
        //todo:perhaps make this not be a single test. But is there really a point?
        assertEquals("Pharmacy",pharmacy.getName());
        assertEquals("Moon",pharmacy.getLocation());
        assertEquals(LocalTime.of(9,0),pharmacy.getOpenTime());
        assertEquals(LocalTime.of(17,0),pharmacy.getCloseTime());
        assertEquals(Arrays.asList(services),pharmacy.getServicesOffered());
        assertEquals(Arrays.asList(services),pharmacy.getServicesInWelsh());

    }
}
