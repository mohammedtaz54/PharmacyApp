package com.nsa.clientproject.welshpharmacy.models;

import android.location.Location;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * This class stores the list of pharmacies that are in our app.
 */

public class PharmacyList implements Serializable {
    /**
     * Stores all the pharmacies.
     */
    private List<Pharmacy> pharmacies;
    /**
     * Stores the pharmacy search criteria.
     */
    private PharmacySearchCriteria pharmacySearchCriteria;
    /**
     * The number we multiply to to convert from metres to miles
     */
    private static final double METRES_TO_MILE = 0.000621371;
    /**
     * Stores our instance of the distanceCalculator.
     */
    private transient DistanceCalculator distanceCalculator = new DistanceCalculator();

    /**
     * Gets the filtered pharmacies
     *
     * @return a list of pharmacies.
     */
    public List<Pharmacy> getPharmacies() {
        if (pharmacySearchCriteria == null) {
            return pharmacies;
        } else {

            List<Pharmacy> pharmacyListReturn = new ArrayList<>();
            List<PharmacyServices> servicesRequiredList = new ArrayList<>();
            //Todo: This is very repetitive. Maybe move to a function?
            //Build list of services required by pharmacy
            Map<PharmacyServices, Boolean> servicesRequired = pharmacySearchCriteria.getServicesRequired();
            if (servicesRequired != null) {
                for (PharmacyServices service : servicesRequired.keySet()) {
                    if (servicesRequired.get(service)) {
                        servicesRequiredList.add(service);
                    }
                }
            }

            Map<PharmacyServices, Boolean> servicesRequiredWelsh = pharmacySearchCriteria.getServicesRequiredInWelsh();
            List<PharmacyServices> servicesRequiredWelshList = new ArrayList<>();

            if (servicesRequiredWelsh != null) {
                for (PharmacyServices service : servicesRequiredWelsh.keySet()) {
                    if (servicesRequiredWelsh.get(service)) {
                        servicesRequiredWelshList.add(service);
                    }
                }
            }
            for (Pharmacy pharmacy : pharmacies) {
                if (pharmacy.getServicesOffered().containsAll(servicesRequiredList)
                        && pharmacy.getServicesInWelsh().containsAll(servicesRequiredWelshList)) {
                    //Location filtering
                    if (pharmacySearchCriteria.getUserLat() != null
                            && pharmacySearchCriteria.getUserLng() != null
                            && pharmacySearchCriteria.getMaxDistance() != null) {
                        float result = distanceCalculator.distanceBetween(
                                pharmacySearchCriteria.getUserLat(),
                                pharmacySearchCriteria.getUserLng(),
                                pharmacy.getPharmacyLat(),
                                pharmacy.getPharmacyLng()
                                );
                        if (result * METRES_TO_MILE <= pharmacySearchCriteria.getMaxDistance()) {
                            pharmacyListReturn.add(pharmacy);
                        }
                    } else {
                        //if we aren't filtering by location
                        pharmacyListReturn.add(pharmacy);
                    }
                }
            }
            return pharmacyListReturn;
        }
    }

    /**
     * Sets the distance calculator. This method is primarily used for unit testing
     *
     * @param distanceCalculator the distanceCalculator
     */
    public void setDistanceCalculator(DistanceCalculator distanceCalculator) {
        this.distanceCalculator = distanceCalculator;
    }

    /**
     * Calculates the distance between two locations
     * This exists because it's a wrapper needed for unit testing.
     */
    static class DistanceCalculator {
        /**
         * Calculates the distance between two points
         *
         * @param lat1 latitude of point 1
         * @param lng1 longtitude of point 1
         * @param lat2 latitude of point 2
         * @param lng2 longtitude of point 2
         */
        public float distanceBetween(double lat1, double lng1, double lat2, double lng2) {
            float[] results = new float[1];
            Location.distanceBetween(
                    lat1,
                    lng1,
                    lat2,
                    lng2,
                    results);
            return results[0];
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
        this.pharmacies = new ArrayList<Pharmacy>() {{

            add(new Pharmacy(
                    "Pharmacy 1",
                    "CF103EP",
                    openingTimes,
                    closingTimes,
                    servicesEmpty,
                    servicesEmpty,
                    51.4927031, -3.1873809
            ));
            add(new Pharmacy(
                    "Pharmacy 2",
                    "CF103EF",
                    closingTimes,
                    openingTimes,
                    services,
                    servicesEmpty,
                    51.49164649999999, -3.1848503
            ));
            add(new Pharmacy(
                    "Pharmacy 3",
                    "CF243EP",
                    openingTimes,
                    closingTimes,
                    services,
                    services,
                    51.4865716, -3.1657761
            ));
            add(new Pharmacy(
                    "Pharmacy 4",
                    "CF243EJ",
                    openingTimes,
                    closingTimes,
                    FluServices,
                    services,
                    51.48921559999999, -3.1666502

            ));
            add(new Pharmacy(
                    "Newport Pharmacy",
                    "NP204NW",
                    openingTimes,
                    closingTimes,
                    FluServices,
                    BloodServices,
                    51.5893927, -3.0012496
            ));
        }};


    }

    /**
     * Gets the pharmacies search criteria
     *
     * @return the search criteria
     */
    public PharmacySearchCriteria getPharmacySearchCriteria() {
        return pharmacySearchCriteria;
    }

    /**
     * Sets the pharmacy search criteria
     *
     * @param pharmacySearchCriteria the search criteria.
     */
    public void setPharmacySearchCriteria(PharmacySearchCriteria pharmacySearchCriteria) {
        this.pharmacySearchCriteria = pharmacySearchCriteria;
    }
}
