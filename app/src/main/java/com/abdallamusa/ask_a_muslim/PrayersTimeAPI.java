// PrayersTimeAPI.java
package com.abdallamusa.ask_a_muslim;

import retrofit2.Call;
import retrofit2.http.GET;

public interface PrayersTimeAPI {
    @GET("PrayerTimes")
    Call<PrayerTimingsResponse> getTodayPrayerTimes();

    @GET("PrayerTimes/location")
    Call<PrayersTimeLocationsResponse> getLocations();
}
