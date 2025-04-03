package com.abdallamusa.ask_a_muslim;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Mornings_Azkar extends AppCompatActivity {

    TextView morning_azkar_tx;
    Button nextOneBtn;
    private int  currentIndex = 0;
    private String[] azkar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_mornings_azkar);

        morning_azkar_tx = findViewById(R.id.morning_azkar_tx);

        nextOneBtn = findViewById(R.id.nextOneBtn);
         azkar = getResources().getStringArray(R.array.morning_azkar);
// Display first zikr

        morning_azkar_tx.setText(azkar[0]);

        nextOneBtn.setOnClickListener(view -> {

            showNextZikr();
        });

    }

    private void showNextZikr() {

        currentIndex++;

        if (currentIndex >= azkar.length){

            currentIndex =0;
        }
        morning_azkar_tx.setText(azkar[currentIndex]);

    }
}