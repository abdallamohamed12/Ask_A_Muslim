package com.abdallamusa.ask_a_muslim;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class PrayersTime extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_prayers_time);

        ImageView arrowBack = findViewById(R.id.backArrow_prayerTimes);

        arrowBack.setOnClickListener(view -> {

            Intent goToHomeActivity = new Intent(this , MainActivity.class);
            startActivity(goToHomeActivity);

        });


    }
}