package com.nsa.clientproject.welshpharmacy;

/**
 * Stores keys and values used in SharedPreferences
 */
public class KeyValueHelper {
    /**
     * Stores the key for the default postcodoe
     */
    public static final String KEY_POSTCODE_TEXT = "edittext_postcode";
    /**
     * Stores the default value for the post code
     */
    public static final String DEFAULT_POSTCODE_TEXT = "";
    /**
     * Stores the key for the max distance,
     */
    public static final String KEY_MAXDISTANCE_TEXT = "edittext_maxdistance";
    /**
     * Stores the default value for the max distance
     */
    public static final float DEFAULT_MAXDISTANCE_TEXT = 0;
    /**
     * Stores the default user latitude's key
     */
    public static final String KEY_USER_LAT = "DEFAULT_USER_LAT";
    /**
     * Stores the default user latitude's value
     */
    public static final double DEFAULT_USER_LAT = 0;
    /**
     * Stores the default user longtitude  key
     */
    public static final String KEY_USER_LNG = "DEFAULT_USER_LNG";
    /**
     * Stores the default user lng value
     */
    public static final double DEFAULT_USER_LNG= 0;
    /**
     * Stores the key if we want to use the user's location
     */
    public static final String KEY_USE_LOCATION_DEFAULT ="DEFAULT_USE_LOCATION";
    /**
     * Stores the default value for using the user's location bt default
     */
    public static final boolean DEFAULT_USE_LOCATION_DEFAULT = false;
    /**
     * Stores they key for if the user has finished the settings wizard.
     */
    public static final String KEY_FINISHED_WIZARD = "USER_FINISHED_WIZARD";
    /**
     * Stores the default value for if the user has finished the wizard.
     */
    public static final boolean DEFAULT_USER_FINISHED_WIZARD = false;
    /**
     * Stores the prefix for default required services
     */
    public static final String KEY_DEFAULT_SERVICES_PREFIX ="REQUIRES_SERVICE_";
    /**
     * Stores the prefix for default required services in welsh.
     */
    public static final String KEY_DEFAULT_SERVICES_WELSH_PREFIX="REQUIRES_SERVICE_WELSH_";
}
