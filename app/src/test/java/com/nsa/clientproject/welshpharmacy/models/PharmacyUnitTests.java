package com.nsa.clientproject.welshpharmacy.models;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Array;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class PharmacyUnitTests {
    private Pharmacy pharmacy;
    private PharmacyServices[] services;
    private Map<DayOfWeek, LocalTime> openingTimes;
    private Map<DayOfWeek, LocalTime> closingTimes;
    @Before
    public void init(){
         openingTimes = new HashMap<DayOfWeek, LocalTime>() {{
            put(DayOfWeek.MONDAY, LocalTime.of(9, 30));
            put(DayOfWeek.TUESDAY, LocalTime.of(9, 30));
            put(DayOfWeek.WEDNESDAY, LocalTime.of(9, 30));
            put(DayOfWeek.THURSDAY, LocalTime.of(9, 30));
            put(DayOfWeek.FRIDAY, LocalTime.of(9, 30));
            put(DayOfWeek.SATURDAY, LocalTime.of(0, 0));
            put(DayOfWeek.SUNDAY, LocalTime.of(0, 0));
        }};
        closingTimes = new HashMap<DayOfWeek, LocalTime>() {{
            put(DayOfWeek.MONDAY, LocalTime.of(17, 0));
            put(DayOfWeek.TUESDAY, LocalTime.of(17, 0));
            put(DayOfWeek.WEDNESDAY, LocalTime.of(17, 0));
            put(DayOfWeek.THURSDAY, LocalTime.of(17, 0));
            put(DayOfWeek.FRIDAY, LocalTime.of(17, 0));
            put(DayOfWeek.SATURDAY, LocalTime.of(0, 0));
            put(DayOfWeek.SUNDAY, LocalTime.of(0, 0));
        }};
        services = new PharmacyServices[]{PharmacyServices.BLOOD_PRESSURE_MONITORING,PharmacyServices.FLU_SHOT};
        pharmacy = new Pharmacy("Pharmacy",
                "Moon",
                openingTimes,
                closingTimes,
                Arrays.asList(services),
                Arrays.asList(services),
                30,
                30
                );
    }
    @Test
    public void evaluateGetters(){
        //todo:perhaps make this not be a single test. But is there really a point?
        assertEquals("Pharmacy",pharmacy.getName());
        assertEquals("Moon",pharmacy.getLocation());
        assertEquals(pharmacy.getOpeningTimes().get(LocalDate.now().getDayOfWeek()),pharmacy.getOpenTime());
        assertEquals(pharmacy.getClosingTimes().get(LocalDate.now().getDayOfWeek()),pharmacy.getCloseTime());
        assertEquals(Arrays.asList(services),pharmacy.getServicesOffered());
        assertEquals(Arrays.asList(services),pharmacy.getServicesInWelsh());
        assertEquals(openingTimes,pharmacy.getOpeningTimes());
        assertEquals(closingTimes,pharmacy.getClosingTimes());

    }
}
