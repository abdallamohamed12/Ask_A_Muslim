package com.abdallamusa.ask_a_muslim;

import com.google.gson.annotations.SerializedName;

public class PrayersTimeLocationsResponse {
    @SerializedName("country")
    private String country;

    @SerializedName("city")
    private String city;

    // Getters
    public String getCountry() { return country; }
    public String getCity() { return city; }
}