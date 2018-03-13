package com.nsa.clientproject.welshpharmacy.models;

import java.time.LocalTime;
import java.util.List;

/**
 * Stores the data of a single pharmacy.
 */
public class Pharmacy {
    /**
     * Stores the name of the pharmacy.
     */
    private String name;
    //This is probably going to be also stores as a LatLong pair.
    /**
     * Stores the location of the pharmacy
     */
    private String location;
    /**
     * Stores what time the pharmacy opens every day.
     */
    private LocalTime openTime;
    /**
     * Stores what time the pharmacy closes every day.
     */
    private LocalTime closeTime;
    //maybe have this as weekly arrays?

    /**
     * Stores the services the pharmacy offers.
     */
    private List<PharmacyServices> servicesOffered;
    /**
     * Stores the services the pharmacy offers in Welsh.
     */
    private List<PharmacyServices> servicesInWelsh;

    /**
     * Builds a new pharmacy.
     * @param name the name of the pharmacy
     * @param location the location of the pharmacy
     * @param openTime the opening time of the pharmacy
     * @param closeTime the closing time of the pharmacy
     * @param servicesOffered the services that are offered
     * @param servicesInWelsh the services that are offered in welsh.
     */
    public Pharmacy(String name, String location, LocalTime openTime, LocalTime closeTime, List<PharmacyServices> servicesOffered, List<PharmacyServices> servicesInWelsh) {
        this.name = name;
        this.location = location;
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.servicesOffered = servicesOffered;
        this.servicesInWelsh = servicesInWelsh;
    }

    /**
     * Gets the name of the pharmacy
     * @return the name.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the location of the pharmacy.
     * @return the location.
     */
    public String getLocation() {
        return location;
    }

    /**
     * Gets the opening time of the pharmacy.
     * @return the opening time.
     */
    public LocalTime getOpenTime() {
        return openTime;
    }

    /**
     * Gets the closing time of the pharmacy.
     * @return closing time
     */
    public LocalTime getCloseTime() {
        return closeTime;
    }

    /**
     * Get all the pharmacy services offered.
     * @return a list of services.
     */
    public List<PharmacyServices> getServicesOffered() {
        return servicesOffered;
    }

    /**
     * Get all the pharmacy services offered in welsh.
     * @return a list of services
     */
    public List<PharmacyServices> getServicesInWelsh() {
        return servicesInWelsh;
    }
    //Perhaps Need setters at a later stage?
}
