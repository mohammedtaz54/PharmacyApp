package com.nsa.clientproject.welshpharmacy;


/**
 * Describes a fragment that cares when the filters change
 */
public interface UpdatableOnFilterChange {
    /**
     * Triggers when the filters change
     */
    void onFiltersChanged();
}