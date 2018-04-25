package com.nsa.clientproject.welshpharmacy.models;

import android.support.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.LinkedHashMap;
import java.util.List;
//Reference:https://stackoverflow.com/questions/2736389/how-to-pass-an-object-from-one-activity-to-another-on-android
//Accessed 17 March 2018


/**
 * Stores the data of a single pharmacy.
 */
public class Pharmacy implements Serializable,Comparable<Pharmacy> {

    /**
     * Stores the distance to the user
     * (this is not a pharmacy detail and it is stored upon it being sorted)
     *
     */
    private double distanceToUser;

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
     * Stores the coordinates of the pharmacy.
     */
    private double pharmacyLat;
    /**
     * Stores the long of the pharmacy
     */
    private double pharmacyLng;
    /**
     * Stores what time the pharmacy opens every day.
     */
    private LinkedHashMap<DayOfWeek, LocalTime> openingTimes;
    /**
     * Stores what time the pharmacy closes every day.
     */
    private LinkedHashMap<DayOfWeek, LocalTime> closingTimes;
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
     * Stores the pharmacy's email
     */
    private String email;
    /**
     * Stores the pharmacy's phone
     */
    private String phone;
    /**
     * Stores the pharmacy's website
     */
    private String website;
    /**
     * Stores the pharmacy's postcode
     */
    private String postcode;
    /**
     * Builds a new pharmacy.
     *
     * @param name            the name of the pharmacy
     * @param location        the location of the pharmacy
     * @param openingTimes    the opening time of the pharmacy
     * @param closingTimes    the closing time of the pharmacy
     * @param servicesOffered the services that are offered
     * @param servicesInWelsh the services that are offered in welsh.
     */
    public Pharmacy(String name,
                    String location,
                    LinkedHashMap<DayOfWeek, LocalTime> openingTimes,
                    LinkedHashMap<DayOfWeek, LocalTime> closingTimes,
                    List<PharmacyServices> servicesOffered,
                    List<PharmacyServices> servicesInWelsh,
                    double pharmacyLat,
                    double pharmacyLng,
                    String postcode,
                    String website,
                    String email,
                    String phone) {
        this.name = name;
        this.location = location;
        this.openingTimes = openingTimes;
        this.closingTimes = closingTimes;
        this.servicesOffered = servicesOffered;
        this.servicesInWelsh = servicesInWelsh;
        this.pharmacyLat = pharmacyLat;
        this.pharmacyLng = pharmacyLng;
        this.postcode = postcode;
        this.website = website;
        this.email = email;
        this.phone = phone;

    }

    /**
     * Gets the name of the pharmacy
     *
     * @return the name.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the location of the pharmacy.
     *
     * @return the location.
     */
    public String getLocation() {
        return location;
    }

    /**
     * Gets the opening time of the pharmacy today.
     *
     * @return the opening time.
     */
    public LocalTime getOpenTime() {

        return openingTimes.get(LocalDate.now().getDayOfWeek());
    }

    /**
     * Gets all opening times of the pharmacy
     *
     * @return a map of opening times
     */
    public LinkedHashMap<DayOfWeek, LocalTime> getOpeningTimes() {
        return openingTimes;
    }

    /**
     * Gets all closing times of the pharmacy
     *
     * @return - a map of closing times
     */
    public LinkedHashMap<DayOfWeek, LocalTime> getClosingTimes() {
        return closingTimes;
    }

    /**
     * Gets the closing time of the pharmacy today.
     *
     * @return closing time
     */
    public LocalTime getCloseTime() {
        return closingTimes.get(LocalDate.now().getDayOfWeek());
    }

    /**
     * Get all the pharmacy services offered.
     *
     * @return a list of services.
     */
    public List<PharmacyServices> getServicesOffered() {
        return servicesOffered;
    }

    /**
     * Get all the pharmacy services offered in welsh.
     *
     * @return a list of services
     */
    public List<PharmacyServices> getServicesInWelsh() {
        return servicesInWelsh;
    }
    //Perhaps Need setters at a later stage?]

    /**
     * Gets the lattude of the pharmacy
     * @return the latutude
     */
    public double getPharmacyLat() {
        return pharmacyLat;
    }

    /**
     * Gets the long of the pharmacy
     * @return the long
     */
    public double getPharmacyLng() {
        return pharmacyLng;
    }

    /**
     * Gets the pharmacy's email
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Gets the pharmacy's phone number
     * @return
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Gets the pharmacy's weebsite
     * @return the website
     */
    public String getWebsite() {
        return website;
    }

    /**
     * Gets the pharmacy's postcode
     * @return the postcode
     */
    public String getPostcode() {
        return postcode;
    }

    /**
     * Gets the distance to the user
     * @return the distance to the user
     */
    public double getDistanceToUser() {
        return distanceToUser;
    }

    /**
     * Sets the distance to the user
     * @param distanceToUser the distance to the user
     */
    public void setDistanceToUser(double distanceToUser) {
        this.distanceToUser = distanceToUser;
    }

    /**
     * Compares our current pharmacy to another pharmacy by their distance from the user.
     * @param o the pharmacy we're comparing to
     * @return +1 if this one is closer, 0 if same distance, -1 if this one is further
     */
    @Override
    public int compareTo(@NonNull Pharmacy o) {
        if (getDistanceToUser()== 0.0 || o.getDistanceToUser() == 0.0){
            throw new IllegalStateException("Can't compare pharmacies without having their distance");
        }
        //The reason i do this versus casting to an integer is because
        //casting would have to round strangely to account for different use cases
        //ex: round down if below zero but up if above to account for numbers like 0.4 or -0.4
        if(this.getDistanceToUser()-o.getDistanceToUser()<0){
            return 1;
        }
        else if(this.getDistanceToUser()-o.getDistanceToUser() == 0){
            return 0;
        }
        else{
            return -1;
        }
    }
}
