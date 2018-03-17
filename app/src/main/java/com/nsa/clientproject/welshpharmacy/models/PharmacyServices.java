package com.nsa.clientproject.welshpharmacy.models;

/**
 * This enum stores all services that pharmacies can do
 *
 */

public enum PharmacyServices {
    FLU_SHOT("Flu Shot"),
    BLOOD_PRESSURE_MONITORING("Blood Pressure Monitoring");

    private final String textRepresentation;

    private PharmacyServices(String textRepresentation) {
        this.textRepresentation = textRepresentation;
    }

    @Override public String toString() {
        return textRepresentation;
    }
}
