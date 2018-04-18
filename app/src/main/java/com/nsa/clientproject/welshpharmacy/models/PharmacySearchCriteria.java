package com.nsa.clientproject.welshpharmacy.models;

import android.location.Location;
import android.support.annotation.Nullable;

import java.io.Serializable;
import java.util.Map;

/**
 * Stores the current search criteria of the user
 */
public class PharmacySearchCriteria implements Serializable{
    /**
     * Stores the user's lat
     */
    @Nullable
    private Double userLat;
    /**
     * Stores the user's lng if needed
     */
    @Nullable
    private Double userLng;
    /**
     * Stores the services required by the user
     */
    @Nullable
    private Map<PharmacyServices,Boolean> servicesRequired;
    /**
     * Stores the maximum distance from a pharmacy the user can be.
     */
    @Nullable
    private Double maxDistance;

    /**
     * Stores the services required by the user in Welsh.
     */
    @Nullable
    private Map<PharmacyServices,Boolean> servicesRequiredInWelsh;


    /**
     * Gets the services required.
     * @return the services required
     */
    @Nullable
    public Map<PharmacyServices, Boolean> getServicesRequired() {
        return servicesRequired;
    }

    /**
     * Sets the services required.
     * @param servicesRequired the required services.
     */
    @Nullable
    public void setServicesRequired(Map<PharmacyServices, Boolean> servicesRequired) {
        this.servicesRequired = servicesRequired;
    }

    /**
     * Gets the maximum distance
     * @return the maximum distance
     * @return the maximum distance
     */
    @Nullable
    public Double getMaxDistance() {
        return maxDistance;
    }

    /**
     * Sets the maximum distance
     * @param maxDistance the maximum distance.
     */
    @Nullable
    public void setMaxDistance(double maxDistance) {
        this.maxDistance = maxDistance;
    }

    /**
     * Gets the services required in welsh.
     * @return the services required in welsh.
     */
    @Nullable
    public Map<PharmacyServices, Boolean> getServicesRequiredInWelsh() {
        return servicesRequiredInWelsh;
    }

    /**
     * Sets the services required in welsh
     * @param servicesRequiredInWelsh the services required in welsh.
     */
    @Nullable
    public void setServicesRequiredInWelsh(Map<PharmacyServices, Boolean> servicesRequiredInWelsh) {
        this.servicesRequiredInWelsh = servicesRequiredInWelsh;
    }

    /**
     * Gets the users  latitude if given
     * @return the latitude
     */
    @Nullable
    public Double getUserLat() {
        return userLat;
    }

    /**
     * Sets the user's latitude
     * @param userLat the latitude
     */
    public void setUserLat(@Nullable Double userLat) {
        this.userLat = userLat;
    }

    /**
     * Gets the user's longitude
     * @return the longitude
     */
    @Nullable
    public Double getUserLng() {
        return userLng;
    }

    /**
     * Sets  the user's longitude
     * @param userLng the user's longitude
     */
    public void setUserLng(@Nullable Double userLng) {
        this.userLng = userLng;
    }

}
