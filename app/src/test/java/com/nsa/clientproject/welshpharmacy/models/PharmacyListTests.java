package com.nsa.clientproject.welshpharmacy.models;


import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;


public class PharmacyListTests {
    private PharmacyList pharmacyList;
    @Before
    public void init(){
        pharmacyList = new PharmacyList();
        pharmacyList.updatePharmacies();
    }
    @Test
    public void evaluateCreatedList(){
        //This is a very simple test
        //But more advanced ones should be created whenever the filtering is made
        assertTrue(pharmacyList.getPharmacies().size()>0);
    }

}
