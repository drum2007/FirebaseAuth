package com.beam.firebaseauth;

/**
 * Created by Beam on 1/30/2018.
 */

public class Confirmation {

    public String numberOfPeople;
    public String year;
    public String month;
    public String dayOfMonth;
    public String ID;
    public String reserveTime;

    public Confirmation(String numberOfPeople, String userID, String time) {
        this.numberOfPeople = numberOfPeople;
        this.ID = userID;
        this.reserveTime = time;
    }



    public Confirmation(String numberOfPeople, String year, String month, String dayOfMonth) {
        this.numberOfPeople = numberOfPeople;
        this.year = year;
        this.month = month;
        this.dayOfMonth = dayOfMonth;
    }
}
