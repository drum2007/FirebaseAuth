package com.beam.firebaseauth;

/**
 * Created by Beam on 1/21/2018.
 */

public class StoreInformation {

    public String storeName;
    public String phoneNumber;
    public String openTime;
    public String closeTime;
    public String capacity;

    public StoreInformation(String storeName, String phoneNumber, String openTime, String closeTime, String capacity){
        this.storeName = storeName;
        this.phoneNumber = phoneNumber;
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.capacity = capacity;
    }
}
