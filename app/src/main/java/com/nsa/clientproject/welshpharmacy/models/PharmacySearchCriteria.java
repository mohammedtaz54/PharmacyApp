package com.nsa.clientproject.welshpharmacy.models;

import java.util.Map;

/**
 * Stores the current search criteria of the user
 */
public class PharmacySearchCriteria {
    /**
     * Stores the services required by the user
     */
    private Map<PharmacyServices,Boolean> servicesRequired;
    /**
     * Stores the maximum distance from a pharmacy the user can be.
     */
    private double maxDistance;

    /**
     * Gets the services required.
     * @return the services required
     */
    public Map<PharmacyServices, Boolean> getServicesRequired() {
        return servicesRequired;
    }

    /**
     * Sets the services required.
     * @param servicesRequired the required services.
     */
    public void setServicesRequired(Map<PharmacyServices, Boolean> servicesRequired) {
        this.servicesRequired = servicesRequired;
    }

    /**
     * Gets the maximum distance
     * @return the maximum distance
     */
    public double getMaxDistance() {
        return maxDistance;
    }

    /**
     * Sets the maximum distance
     * @param maxDistance the maximum distance.
     */
    public void setMaxDistance(double maxDistance) {
        this.maxDistance = maxDistance;
    }
}
