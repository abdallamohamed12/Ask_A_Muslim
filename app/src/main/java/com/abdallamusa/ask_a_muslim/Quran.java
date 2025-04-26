package com.abdallamusa.ask_a_muslim;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class Quran extends AppCompatActivity {


    TextView quranTextView ;
     private  int currentIndex = 0;
     private String[] Surah ;
    Button nextBtn , previousBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_quran);

        quranTextView = findViewById(R.id.quran_text);

        nextBtn = findViewById(R.id.next_button_quranLayout);

        previousBtn = findViewById(R.id.prevoius_button_quranLayout);

        Surah = getResources().getStringArray(R.array.short_surahs);


        quranTextView.setText(Surah[currentIndex]);

        ImageView arrowBack = findViewById(R.id.backArrow_Quran);

        arrowBack.setOnClickListener(view -> {

            Intent goToHomeActivity = new Intent(this , MainActivity.class);
            startActivity(goToHomeActivity);

        });


        nextBtn.setOnClickListener(view -> {

            currentIndex++;
        if (currentIndex >= Surah.length)
        {
            currentIndex = Surah.length-1;
        }

            quranTextView.setText(Surah[currentIndex]);

        });


        previousBtn.setOnClickListener(view -> {
            currentIndex--;
            if (currentIndex <= 0)
            {
                currentIndex =0;
            }

            quranTextView.setText(Surah[currentIndex]);

        });


    }
}