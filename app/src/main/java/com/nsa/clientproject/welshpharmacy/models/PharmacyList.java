package com.nsa.clientproject.welshpharmacy.models;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class stores the list of pharmacies that are in our app.
 */

public class PharmacyList implements Serializable{
    /**
     * Stores all the pharmacies.
     */
    private List<Pharmacy> pharmacies;
    /**
     * Stores the pharmacy search criteria.
     */
    private PharmacySearchCriteria pharmacySearchCriteria;

    /**
     * Gets all the pharmacies
     *
     * @return a list of pharmacies.
     */
    public List<Pharmacy> getPharmacies() {
        if(pharmacySearchCriteria==null) {
            return pharmacies;
        }
        else{
            return new ArrayList<>();
        }
    }

    /**
     * Updates the list of pharmacies.
     */
    public void updatePharmacies() {
        final ArrayList<PharmacyServices> services = new ArrayList<PharmacyServices>() {{
            add(PharmacyServices.BLOOD_PRESSURE_MONITORING);
            add(PharmacyServices.FLU_SHOT);
        }};
        final Map<DayOfWeek, LocalTime> openingTimes = new HashMap<DayOfWeek, LocalTime>() {{
            put(DayOfWeek.MONDAY, LocalTime.of(9, 30));
            put(DayOfWeek.TUESDAY, LocalTime.of(9, 30));
            put(DayOfWeek.WEDNESDAY, LocalTime.of(9, 30));
            put(DayOfWeek.THURSDAY, LocalTime.of(9, 30));
            put(DayOfWeek.FRIDAY, LocalTime.of(9, 30));
            put(DayOfWeek.SATURDAY, LocalTime.of(0, 0));
            put(DayOfWeek.SUNDAY, LocalTime.of(0, 0));
        }};
        final Map<DayOfWeek, LocalTime> closingTimes = new HashMap<DayOfWeek, LocalTime>() {{
            put(DayOfWeek.MONDAY, LocalTime.of(17, 0));
            put(DayOfWeek.TUESDAY, LocalTime.of(17, 0));
            put(DayOfWeek.WEDNESDAY, LocalTime.of(17, 0));
            put(DayOfWeek.THURSDAY, LocalTime.of(17, 0));
            put(DayOfWeek.FRIDAY, LocalTime.of(17, 0));
            put(DayOfWeek.SATURDAY, LocalTime.of(0, 0));
            put(DayOfWeek.SUNDAY, LocalTime.of(0, 0));
        }};
        this.pharmacies = new ArrayList<Pharmacy>() {{

            add(new Pharmacy(
                    "Pharmacy 1",
                    "CF103EP",
                    openingTimes,
                    closingTimes,
                    services,
                    services,
                    51.4927031,-3.1873809
            ));
            add(new Pharmacy(
                    "Pharmacy 2",
                    "CF103EF",
                    closingTimes,
                    openingTimes,
                    services,
                    services,
                    51.49164649999999,-3.1848503
            ));
            add(new Pharmacy(
                    "Pharmacy 3",
                    "CF243EP",
                    openingTimes,
                    closingTimes,
                    services,
                    services,
                    51.4865716,-3.1657761
            ));
            add(new Pharmacy(
                    "Pharmacy 4",
                    "CF243EJ",
                    openingTimes,
                    closingTimes,
                    services,
                    services,
                    51.48921559999999,-3.1666502

            ));
        }};

    }

    /**
     * Gets the pharmacies search criteria
     * @return the search criteria
     */
    public PharmacySearchCriteria getPharmacySearchCriteria() {
        return pharmacySearchCriteria;
    }

    /**
     * Sets the pharmacy search criteria
     * @param pharmacySearchCriteria the search criteria.
     */
    public void setPharmacySearchCriteria(PharmacySearchCriteria pharmacySearchCriteria) {
        this.pharmacySearchCriteria = pharmacySearchCriteria;
    }
}
