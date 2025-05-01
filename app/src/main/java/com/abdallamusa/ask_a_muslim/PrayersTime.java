package com.abdallamusa.ask_a_muslim;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class PrayersTime extends AppCompatActivity {
    private CheckBox fajrCheckBox, dhuhrCheckBox, asrCheckBox, maghribCheckBox, ishaCheckBox;
    private TextView fajrTime, dhuhrTime, asrTime, maghribTime, ishaTime,
            locationPrayerTime, prayersTVProgressBar;
    private ProgressBar prayerProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_prayers_time);

        ImageView arrowBack       = findViewById(R.id.backArrow_prayerTimes);
        prayersTVProgressBar      = findViewById(R.id.prayerTextProgressBar);
        prayerProgressBar         = findViewById(R.id.prayerProgressBar);

        fajrCheckBox              = findViewById(R.id.fajrCheckBox);
        dhuhrCheckBox             = findViewById(R.id.DhuhrCheckBox);
        asrCheckBox               = findViewById(R.id.AsrCheckBox);
        maghribCheckBox           = findViewById(R.id.MaghribCheckBox);
        ishaCheckBox              = findViewById(R.id.IshaCheckBox);

        fajrTime                  = findViewById(R.id.fajrPrayerTime);
        dhuhrTime                 = findViewById(R.id.DhuhrPrayerTime);
        asrTime                   = findViewById(R.id.AsrPrayerTime);
        maghribTime               = findViewById(R.id.MaghribPrayerTime);
        ishaTime                  = findViewById(R.id.IshaPrayerTime);
        locationPrayerTime        = findViewById(R.id.locationPrayerTime);

        // 1) Wire up a single OnCheckedChangeListener for all boxes:
        CompoundButton.OnCheckedChangeListener listener =
                (buttonView, isChecked) -> updateProgress();

        fajrCheckBox    .setOnCheckedChangeListener(listener);
        dhuhrCheckBox   .setOnCheckedChangeListener(listener);
        asrCheckBox     .setOnCheckedChangeListener(listener);
        maghribCheckBox .setOnCheckedChangeListener(listener);
        ishaCheckBox    .setOnCheckedChangeListener(listener);

        // 2) Retrofit setup & calls (no changes here)
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://ask-a-muslim.runasp.net/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        PrayersTimeAPI api = retrofit.create(PrayersTimeAPI.class);

        api.getTodayPrayerTimes().enqueue(new Callback<PrayerTimingsResponse>() {
            @Override public void onResponse(@NonNull Call<PrayerTimingsResponse> call,
                                             @NonNull Response<PrayerTimingsResponse> resp) {
                if (resp.isSuccessful() && resp.body() != null) {
                    PrayerTimingsResponse.Timings t = resp.body().data.timings;
                    fajrTime   .setText(t.Fajr);
                    dhuhrTime  .setText(t.Dhuhr);
                    asrTime    .setText(t.Asr);
                    maghribTime.setText(t.Maghrib);
                    ishaTime   .setText(t.Isha);
                }
            }
            @Override public void onFailure(@NonNull Call<PrayerTimingsResponse> call,
                                            @NonNull Throwable t) {
                Toast.makeText(PrayersTime.this,
                        "Error: " + t.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });

        api.getLocations().enqueue(new Callback<PrayersTimeLocationsResponse>() {
            @SuppressLint("SetTextI18n")
            @Override public void onResponse(@NonNull Call<PrayersTimeLocationsResponse> call,
                                             @NonNull Response<PrayersTimeLocationsResponse> resp) {
                if (resp.isSuccessful() && resp.body() != null ) {
                    String country = resp.body().getCountry();
                    String city = resp.body().getCity();
                    if (city != null && country != null) {
                        locationPrayerTime.setText(city + ", " + country);
                    } else {
                        locationPrayerTime.setText("Unknown location");
                    }
                }
                else {

                    Toast.makeText(PrayersTime.this,
                            "Error" + "Code: "+ resp.code(),
                            Toast.LENGTH_SHORT).show();
                }

            }
            @Override public void onFailure(@NonNull Call<PrayersTimeLocationsResponse> call,
                                            @NonNull Throwable t) {
                Toast.makeText(PrayersTime.this,
                        "Error: " + t.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });

        arrowBack.setOnClickListener(v -> {
            Intent goToHomeActivity = new Intent(PrayersTime.this, MainActivity.class);

            startActivity(goToHomeActivity);

        });

        // initialize the progress display
        updateProgress();
    }

    private void updateProgress() {
        // count how many are checked
        int checked = 0;
        if (fajrCheckBox.isChecked())    checked++;
        if (dhuhrCheckBox.isChecked())   checked++;
        if (asrCheckBox.isChecked())     checked++;
        if (maghribCheckBox.isChecked()) checked++;
        if (ishaCheckBox.isChecked())    checked++;

        int percent = (int)((checked / 5.0f) * 100);
        prayerProgressBar.setProgress(percent);

        // convert to string before calling setText!
        prayersTVProgressBar.setText(percent + "%");
    }
}
