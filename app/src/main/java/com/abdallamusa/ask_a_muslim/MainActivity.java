package com.abdallamusa.ask_a_muslim;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.ViewGroup;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {

    private ImageSwitcher imageSwitcher;
    private final int[] images = {
            R.drawable.islamic_image_welcome_picture,
            R.drawable.mosque_welcome_image,

    };

    private int currentIndex = 0;
    private final Handler handler = new Handler();
    private Runnable imageSwitcherRunnable;

TextView navHomeBtn , navQuranBtn , navPrayerBtn, navCoursesBtn , navMoreBtn;

LinearLayout quranIcon , prayerIcon , azkarIcon , coursesIcon ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_main);

        navHomeBtn = findViewById(R.id.nav_home_text);

        navQuranBtn =findViewById(R.id.nav_quran_text);
                 navPrayerBtn = findViewById(R.id.nav_paryer_text);
                 navCoursesBtn  = findViewById(R.id.nav_courses_text);
                 navMoreBtn = findViewById(R.id.nav_more_text);

        imageSwitcher = findViewById(R.id.featured_image);

        quranIcon = findViewById(R.id.quran_icon);

        coursesIcon = findViewById(R.id.courses_icon);
        prayerIcon = findViewById(R.id.prayer_icon);
        azkarIcon = findViewById(R.id.Azkar_icon);


        navCoursesBtn.setOnClickListener(view -> {

            Intent goToSignUpActivity = new Intent(MainActivity.this , signUp.class);

            startActivity(goToSignUpActivity);

        });
        navPrayerBtn.setOnClickListener(view -> {

            Intent goToPrayerActivity = new Intent(MainActivity.this , PrayersTime.class);

            startActivity(goToPrayerActivity);

        });

        navQuranBtn.setOnClickListener(view -> {

            Intent goToQuranActivity = new Intent(MainActivity.this , Quran.class);

            startActivity(goToQuranActivity);

        });
        navMoreBtn.setOnClickListener(view -> {

            Intent goToMoreActivity = new Intent(MainActivity.this , More.class);

            startActivity(goToMoreActivity);

        });


        quranIcon.setOnClickListener(view -> {

            Intent goToQuranActivity = new Intent(MainActivity.this , Quran.class);

            startActivity(goToQuranActivity);

        });

        prayerIcon.setOnClickListener(view -> {


            Intent goToPrayerActivity = new Intent(MainActivity.this , PrayersTime.class);

            startActivity(goToPrayerActivity);

        });

        azkarIcon.setOnClickListener(view -> {


            Intent goToAzkarActivity = new Intent(MainActivity.this , Azkar.class);

            startActivity(goToAzkarActivity);

        });

        coursesIcon.setOnClickListener(view -> {

            Intent goToCoursesActivity = new Intent(MainActivity.this , signUp.class);

            startActivity(goToCoursesActivity);

        });

        // Set how views are created inside ImageSwitcher
        imageSwitcher.setFactory(() -> {
            ImageView imageView = new ImageView(MainActivity.this);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setLayoutParams(new ImageSwitcher.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
            ));
            return imageView;
        });

        // Set animations (optional)
        imageSwitcher.setInAnimation(this, android.R.anim.fade_in);
        imageSwitcher.setOutAnimation(this, android.R.anim.fade_out);

        // Set initial image
        imageSwitcher.setImageResource(images[currentIndex]);

        // Auto-switch logic
        imageSwitcherRunnable = new Runnable() {
            @Override
            public void run() {
                currentIndex = (currentIndex + 1) % images.length;
                imageSwitcher.setImageResource(images[currentIndex]);
                handler.postDelayed(this, 3000); // 3 seconds
            }
        };

        // Start switching
        handler.postDelayed(imageSwitcherRunnable, 3000);
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(imageSwitcherRunnable); // Avoid memory leaks
    }





    }


