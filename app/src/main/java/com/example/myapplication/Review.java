package com.example.myapplication;

public class Review {

    private int rating;
    private boolean parkingValue;
    private boolean accessibilityValue;
    private boolean toiletValue;
    private boolean serviceValue;
    private String extraInfo;
    private String time;
    private String id;

    public Review(int rating, boolean parkingValue, boolean accessibilityValue, boolean toiletValue,
                  boolean serviceValue, String extraInfo, String time, String id) {

        this.rating = rating;
        this.parkingValue = parkingValue;
        this.accessibilityValue = accessibilityValue;
        this.toiletValue = toiletValue;
        this.serviceValue = serviceValue;
        this.extraInfo = extraInfo;
        this.time = time;
        this.id = id;
    }

    public int getRating() {
        return rating;
    }

    public boolean isParkingValue() {
        return parkingValue;
    }

    public boolean isAccessibilityValue() {
        return accessibilityValue;
    }

    public boolean isToiletValue() {
        return toiletValue;
    }

    public boolean isServiceValue() {
        return serviceValue;
    }

    public String getExtraInfo() {
        return extraInfo;
    }

    public String getTime() {
        return time;
    }

    public String getTId() {
        return id;
    }






}
