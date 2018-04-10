package com.thewalkingschoolbus.thewalkingschoolbus.models;

/*
    this class is for gps location variable in the user class
    allow us to modify and update the varible such as lat and lng
 */

public class GpsLocation {
    private String lat;
    private String lng;
    private String timestamp;

    public GpsLocation(String mLat,String mLng,String mTimestamp){
        lat = mLat;
        lng = mLng;
        timestamp = mTimestamp;
    }

    public GpsLocation(){
        lat = null;
        lng = null;
        timestamp = null;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
