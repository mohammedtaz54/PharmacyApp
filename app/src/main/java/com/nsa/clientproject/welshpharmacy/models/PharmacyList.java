package com.nsa.clientproject.welshpharmacy.models;

import java.lang.reflect.Array;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * This class stores the list of pharmacies that are in our app.
 */

public class PharmacyList {
    /**
     * Stores all the pharmacies.
     */
    private List<Pharmacy> pharmacies;

    /**
     * Gets all the pharmacies
     * @return a list of pharmacies.
     */
    public List<Pharmacy> getPharmacies() {
        return pharmacies;
    }
    /**
     * Updates the list of pharmacies.
     */
    public void updatePharmacies(){
        final ArrayList<PharmacyServices> services = new ArrayList<PharmacyServices>(){{
            add(PharmacyServices.BLOOD_PRESSURE_MONITORING);
            add(PharmacyServices.FLU_SHOT);
        }};
        this.pharmacies = new ArrayList<Pharmacy>(){{
            add(new Pharmacy(
                    "Pharmacy 1",
                    "CF103EP",
                    LocalTime.of(9,30),
                    LocalTime.of(17,0),
                    services,
                    services
            ));
            add(new Pharmacy(
                    "Pharmacy 2",
                    "CF103EF",
                    LocalTime.of(17,0),
                    LocalTime.of(9,30),
                    services,
                    services
            ));
            add(new Pharmacy(
                    "Pharmacy 3",
                    "CF243EP",
                    LocalTime.of(9,30),
                    LocalTime.of(17,0),
                    services,
                    services
            ));
            add(new Pharmacy(
                    "Pharmacy 4",
                    "CF243EJ",
                    LocalTime.of(9,30),
                    LocalTime.of(17,0),
                    services,
                    services
            ));
        }};

    }
}
