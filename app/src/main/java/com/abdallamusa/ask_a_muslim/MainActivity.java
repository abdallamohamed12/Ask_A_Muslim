package com.abdallamusa.ask_a_muslim;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {

    LinearLayout courses_btn , prayers_time_btn , azkar_btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_main);

        courses_btn = findViewById(R.id.Courses_button);

        azkar_btn = findViewById(R.id.Azkar);

        prayers_time_btn = findViewById(R.id.prayers_button);




        courses_btn.setOnClickListener(view -> {



            Intent goToSignUPActivity = new Intent(MainActivity.this, signUp.class);

            startActivity(goToSignUPActivity);


        });

        azkar_btn.setOnClickListener(view -> {

            Intent goToAzkarActivity = new Intent(MainActivity.this, Azkar.class);

            startActivity(goToAzkarActivity);


        });


    }

    }
