package com.nsa.clientproject.welshpharmacy.models;

import android.location.Location;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
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
     * Stores the maximum number of pharmacies to return
     */
    private static final int MAX_NUMBER_OF_PHARMACIES = 50;
    /**
     * Stores the pharmacy search criteria.
     */
    private PharmacySearchCriteria pharmacySearchCriteria;
    /**
     * The number we multiply to to convert from metres to miles
     */
    public static final double METRES_TO_MILE = 0.000621371;
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
            boolean sortPharmaciesByDistance = false;
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
                            sortPharmaciesByDistance = true;
                            pharmacy.setDistanceToUser(result);
                            pharmacyListReturn.add(pharmacy);
                        }
                    } else {
                        //if we aren't filtering by location
                        pharmacyListReturn.add(pharmacy);
                    }
                }
            }
            if(sortPharmaciesByDistance){
                Collections.sort(pharmacyListReturn,Collections.<Pharmacy>reverseOrder());
            }
            List<Pharmacy> reducedList = new ArrayList<>();
            if (pharmacyListReturn.size() < PharmacyList.MAX_NUMBER_OF_PHARMACIES) {
                return pharmacyListReturn;
            } else {
                for (int i = 0; i < PharmacyList.MAX_NUMBER_OF_PHARMACIES; i++) {
                    reducedList.add(pharmacyListReturn.get(i));
                }
                return reducedList;
            }
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


    /**
     * Sets the list of pharmacies
     *
     * @param pharmacies the list of pharmacies
     */
    public void setPharmacies(List<Pharmacy> pharmacies) {
        this.pharmacies = pharmacies;
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
