package com.abdallamusa.ask_a_muslim;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class Azkar extends AppCompatActivity implements View.OnClickListener {

    private static final int CATEGORY_MORNING = 0;
    private static final int CATEGORY_EVENING = 1;
    private static final int CATEGORY_SLEEPING = 2;
    private static final int CATEGORY_DEATH = 3;
    private static final int CATEGORY_WALKING = 4;
    private static final int CATEGORY_CLOTHES = 5;

    private int currentCategory = CATEGORY_MORNING;

    ImageView backBtn;
    TextView azkarText;
    private Button btnMorning, btnEvening, btnSleeping, btnDeathJanazah, btnWalkingToMosque, btnClothes;

    private int currentIndex = 0;

    String[] morningAzkar, eveningAzkar, death_JanazahAzkar, walkToMosqueAzkar, sleepingAzkar, clothesAzkar;
    Button nextBtn, previousBtn;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_azkar);

        morningAzkar = getResources().getStringArray(R.array.morning_azkar);

        eveningAzkar = getResources().getStringArray(R.array.night_azkar);

        sleepingAzkar = getResources().getStringArray(R.array.sleeping_azkar);


        walkToMosqueAzkar = getResources().getStringArray(R.array.mosque_walk_azkar);

        clothesAzkar = getResources().getStringArray(R.array.wearing_new_clothes_azkar);

        death_JanazahAzkar = getResources().getStringArray(R.array.Janazah_death_azkar);


        backBtn = findViewById(R.id.arrow_right_azkar);
        btnMorning = findViewById(R.id.morningAzkar);
        btnEvening = findViewById(R.id.eveningAzkar);
        btnSleeping = findViewById(R.id.sleepingAzkar);
        btnDeathJanazah = findViewById(R.id.death_Janazah_Azkar);
        btnWalkingToMosque = findViewById(R.id.walkingToMosqueAzkar);
        btnClothes = findViewById(R.id.wearing_New_ClothesAzkar);
        btnSleeping = findViewById(R.id.sleepingAzkar);

        azkarText = findViewById(R.id.azkar_text);

        nextBtn = findViewById(R.id.next_button_azkarLayout);
        previousBtn = findViewById(R.id.prevoius_button_azkarLayout);


        backBtn.setOnClickListener(view -> {

            Intent goToMainActivity = new Intent(Azkar.this, MainActivity.class);
            startActivity(goToMainActivity);

        });

        btnMorning.setOnClickListener(this);
        btnEvening.setOnClickListener(this);
        btnSleeping.setOnClickListener(this);
        btnDeathJanazah.setOnClickListener(this);
        btnWalkingToMosque.setOnClickListener(this);
        btnClothes.setOnClickListener(this);


        nextBtn.setOnClickListener(view -> {
            String[] currentArray = getCurrentAzkarArray();
            if (currentIndex < currentArray.length - 1) {
                currentIndex++;
            }

            // Update the displayed azkar
            azkarText.setText(currentArray[currentIndex]);
        });

        previousBtn.setOnClickListener(view -> {
            if (currentIndex > 0) {
                currentIndex--;
            }
            // Update the displayed azkar
            azkarText.setText(getCurrentAzkarArray()[currentIndex]);
        });

        // Set the default display (for morning azkar by default)
        currentCategory = CATEGORY_MORNING;
        currentIndex = 0;
        azkarText.setText(getCurrentAzkarArray()[currentIndex]);
    }


    private String[] getCurrentAzkarArray() {
        switch (currentCategory) {
            case CATEGORY_MORNING:
                return morningAzkar;
            case CATEGORY_EVENING:
                return eveningAzkar;
            case CATEGORY_SLEEPING:
                return sleepingAzkar;
            case CATEGORY_DEATH:
                return death_JanazahAzkar;
            case CATEGORY_WALKING:
                return walkToMosqueAzkar;
            case CATEGORY_CLOTHES:
                return clothesAzkar;
            default:
                return morningAzkar;
        }
    }


    private void resetButtonStyles() {


        btnClothes.setBackground(ContextCompat.getDrawable(this, R.drawable.grey_button));
        btnClothes.setTextColor(getColor(R.color.black));


        btnMorning.setBackground(ContextCompat.getDrawable(this, R.drawable.grey_button));
        btnMorning.setTextColor(getColor(R.color.black));


        btnEvening.setBackground(ContextCompat.getDrawable(this, R.drawable.grey_button));
        btnEvening.setTextColor(getColor(R.color.black));

        btnDeathJanazah.setBackground(ContextCompat.getDrawable(this, R.drawable.grey_button));
        btnDeathJanazah.setTextColor(getColor(R.color.black));

        btnWalkingToMosque.setBackground(ContextCompat.getDrawable(this, R.drawable.grey_button));
        btnWalkingToMosque.setTextColor(getColor(R.color.black));


        btnSleeping.setBackground(ContextCompat.getDrawable(this, R.drawable.grey_button));
        btnSleeping.setTextColor(getColor(R.color.black));


    }


    @Override
    public void onClick(View azkarButton) {

        resetButtonStyles();

        if (azkarButton.getId() == R.id.eveningAzkar) {



            btnEvening.setBackground(ContextCompat.getDrawable(this, R.drawable.green_button));
            btnEvening.setTextColor(getColor(R.color.white));

            currentCategory = CATEGORY_EVENING;

azkarText.setText(getCurrentAzkarArray()[0]);

        } else if (azkarButton.getId() == R.id.morningAzkar) {


            btnMorning.setBackground(ContextCompat.getDrawable(this, R.drawable.green_button));
            btnMorning.setTextColor(getColor(R.color.white));

            currentCategory = CATEGORY_MORNING;
            azkarText.setText(getCurrentAzkarArray()[0]);

        } else if (azkarButton.getId() == R.id.wearing_New_ClothesAzkar) {


            btnClothes.setBackground(ContextCompat.getDrawable(this, R.drawable.green_button));
            btnClothes.setTextColor(getColor(R.color.white));

            currentCategory = CATEGORY_CLOTHES;

            azkarText.setText(getCurrentAzkarArray()[0]);
        }
        else if (azkarButton.getId() == R.id.sleepingAzkar) {

            btnSleeping.setBackground(ContextCompat.getDrawable(this, R.drawable.green_button));
            btnSleeping.setTextColor(getColor(R.color.white));

            currentCategory = CATEGORY_SLEEPING;
            azkarText.setText(getCurrentAzkarArray()[0]);

        }
        else if (azkarButton.getId() == R.id.death_Janazah_Azkar) {

            btnDeathJanazah.setBackground(ContextCompat.getDrawable(this, R.drawable.green_button));
            btnDeathJanazah.setTextColor(getColor(R.color.white));

            currentCategory = CATEGORY_DEATH;
            azkarText.setText(getCurrentAzkarArray()[0]);


        }
        else if (azkarButton.getId() == R.id.walkingToMosqueAzkar) {

            btnWalkingToMosque.setBackground(ContextCompat.getDrawable(this, R.drawable.green_button));
            btnWalkingToMosque.setTextColor(getColor(R.color.white));


            currentCategory = CATEGORY_WALKING;

            azkarText.setText(getCurrentAzkarArray()[0]);


        }
    }
}

