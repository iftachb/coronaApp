package com.example.coronaapp;

import com.google.gson.annotations.SerializedName;

public class Results {
    @SerializedName("Country")
    private String country;

    @SerializedName("CountryCode")
    private String countryCode;

    @SerializedName("Province")
    private String province;

    @SerializedName("City")
    private String city;

    @SerializedName("Lat")
    private String lat;

    @SerializedName("Lon")
    private String lon;

    @SerializedName("Cases")
    private int cases;

    @SerializedName("Status")
    private String status;

    @SerializedName("Date")
    private String date;


    public String getStatus() {
        return status;
    }
    public int getCases() {
        return cases;
    }
}