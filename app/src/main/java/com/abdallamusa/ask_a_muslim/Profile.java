package com.abdallamusa.ask_a_muslim;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class Profile extends AppCompatActivity {

    TextView seeAllCompletedCourses, seeAllMyCourses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);

seeAllCompletedCourses = findViewById(R.id.seeAllCompletedCourses);

seeAllMyCourses = findViewById(R.id.seeAllMyCourses);

        ImageView arrowBack = findViewById(R.id.arrowBack_ID);

        arrowBack.setOnClickListener(view -> {

            Intent goToHomeActivity = new Intent(this , MainActivity.class);
            startActivity(goToHomeActivity);

        });

        View.OnClickListener commonListener = v -> {
            TextView clickedText = (TextView)v ;// Use text as category
            String category;
            int id = v.getId();
            if (id == R.id.seeAllCompletedCourses) {
                category = "Completed";
            } else if (id == R.id.seeAllMyCourses) {
                category = "My ";
            }
            else {
                category = "All"; // fallback
            }
            Intent intent = new Intent(Profile.this, CourseList.class);
            intent.putExtra("category", category);
            startActivity(intent);
        };

        seeAllMyCourses.setOnClickListener(commonListener);
        seeAllCompletedCourses.setOnClickListener(commonListener);


    }

}