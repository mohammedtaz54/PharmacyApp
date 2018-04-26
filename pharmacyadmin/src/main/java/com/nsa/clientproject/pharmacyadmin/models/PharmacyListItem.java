package com.nsa.clientproject.pharmacyadmin.models;

import java.io.Serializable;

public class PharmacyListItem implements Serializable{
    /**
     * Stores the name of the pharmacy.
     */
    private String name;
    /**
     * Stores the pharmacy's ID.
     */
    private Integer id;

    /**
     * Creates a PharmacyListItem
     * @param name the name of the pharmacy
     * @param id the id of the pharmacy
     */
    public PharmacyListItem(String name, Integer id) {
        this.name = name;
        this.id = id;
    }

    /**
     * Gets the name of the pharmacy
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the id of the pharmacy
     * @return the ID.
     */
    public Integer getId() {
        return id;
    }
}
