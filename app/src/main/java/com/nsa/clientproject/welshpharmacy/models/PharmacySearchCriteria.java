package com.nsa.clientproject.welshpharmacy.models;

import java.io.Serializable;
import java.util.Map;

/**
 * Stores the current search criteria of the user
 */
public class PharmacySearchCriteria implements Serializable{
    /**
     * Stores the services required by the user
     */
    private Map<PharmacyServices,Boolean> servicesRequired;
    /**
     * Stores the maximum distance from a pharmacy the user can be.
     */
    private double maxDistance;

    /**
     * Stores the services required by the user in Welsh.
     */
    private Map<PharmacyServices,Boolean> servicesRequiredInWelsh;


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

    /**
     * Gets the services required in welsh.
     * @return the services required in welsh.
     */
    public Map<PharmacyServices, Boolean> getServicesRequiredInWelsh() {
        return servicesRequiredInWelsh;
    }

    /**
     * Sets the services required in welsh
     * @param servicesRequiredInWelsh the services required in welsh.
     */
    public void setServicesRequiredInWelsh(Map<PharmacyServices, Boolean> servicesRequiredInWelsh) {
        this.servicesRequiredInWelsh = servicesRequiredInWelsh;
    }

}
