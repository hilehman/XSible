package com.example.myapplication;

import java.io.Serializable;
import java.util.HashMap;

public class PlaceHolder implements Serializable {


    final String placeId;
    final String name;
    final String address;
    public static HashMap<String, PlaceHolder> placesMap;

    String grade = "";

    PlaceHolder(String placeId, String name, String address){
        this.placeId = placeId;
        this.name = name;
        this.address = address;
        placesMap.put(placeId,this);
    }




    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }



    public String getPlaceId() {
        return placeId;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

}
