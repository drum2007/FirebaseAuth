package com.beam.firebaseauth;

/**
 * Created by Beam on 1/30/2018.
 */

public class Confirmation {

    public String numberOfPeople;
    public String year;
    public String month;
    public String dayOfMonth;
    public String userID;

    public Confirmation(String numberOfPeople, String userID) {
        this.numberOfPeople = numberOfPeople;
        this.userID = userID;
    }

    public Confirmation(String numberOfPeople, String year, String month, String dayOfMonth) {
        this.numberOfPeople = numberOfPeople;
        this.year = year;
        this.month = month;
        this.dayOfMonth = dayOfMonth;
    }
}
