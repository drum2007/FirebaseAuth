package com.beam.firebaseauth;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Beam on 1/21/2018.
 */

public class StoreInformation {

    public String storeName;
    public String phoneNumber;
    public String openTime;
    public String closeTime;
    public String capacity;
    public String aboutStore;
    public String place;
    public String address;
    public String latlng;


    public StoreInformation(String storeName, String phoneNumber, String openTime, String closeTime, String capacity, String aboutStore, String place, String address, String latlng) {
        this.storeName = storeName;
        this.phoneNumber = phoneNumber;
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.capacity = capacity;
        this.aboutStore = aboutStore;
        this.place = place;
        this.address = address;
        this.latlng = latlng;

    }

}
