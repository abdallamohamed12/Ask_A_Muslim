package com.abdallamusa.ask_a_muslim;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Azkar extends AppCompatActivity {

    ImageView backArrowBtn , morning_azkarBtn;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_azkar);

        backArrowBtn = findViewById(R.id.back_arrow_icon);
morning_azkarBtn = findViewById(R.id.morning_azkar_btn);
        backArrowBtn.setOnClickListener(view -> {

            Intent goToMainActivity = new Intent(Azkar.this, MainActivity.class);

            startActivity(goToMainActivity);


        });

morning_azkarBtn.setOnClickListener(view -> {

    Intent goToMorning_AzkarActivity = new Intent(Azkar.this, Mornings_Azkar.class);

    startActivity(goToMorning_AzkarActivity);



});

    }
}