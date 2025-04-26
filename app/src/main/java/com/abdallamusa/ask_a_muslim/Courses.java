package com.abdallamusa.ask_a_muslim;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class Courses extends AppCompatActivity {

    TextView seeAllBeginnerCourses, seeAllIntermediateCourses, seeAllAdvancedCourses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_courses);

        seeAllBeginnerCourses = findViewById(R.id.seeAllBeginnersCourses);
        seeAllIntermediateCourses = findViewById(R.id.seeAllIntermediateCourses);
        seeAllAdvancedCourses = findViewById(R.id.seeAllAdvancedCourses);

        Button enrollNowBtn = findViewById(R.id.enrollNowBtn);
        Button startCourseBtn1 = findViewById(R.id.startcourseBtn1);
        Button startCourseBtn2 = findViewById(R.id.startcourseBtn2);
        Button intermediateStartBtn1 = findViewById(R.id.IndermediateStartCourseBtn1);
        Button intermediateStartBtn2 = findViewById(R.id.IntermediateStartCourseBtn2);
        Button advancedStartBtn1 = findViewById(R.id.advancesStartBtn1);
        Button advancedStartBtn2 = findViewById(R.id.advancesStartBtn2);


        ImageView arrowBack = findViewById(R.id.arrowBack_ID);

        arrowBack.setOnClickListener(view -> {

            Intent goToHomeActivity = new Intent(this , MainActivity.class);
            startActivity(goToHomeActivity);

        });

        View.OnClickListener commonListener = v -> {
            String category;
            int id = v.getId();
            if (id == R.id.seeAllBeginnersCourses) {
                category = "Beginners";
            } else if (id == R.id.seeAllIntermediateCourses) {
                category = "Intermediate";
            } else if (id == R.id.seeAllAdvancedCourses) {
                category = "Advanced";
            } else {
                category = "All";
            }
            Intent intent = new Intent(Courses.this, CourseList.class);
            intent.putExtra("category", category);
            startActivity(intent);
        };

        seeAllBeginnerCourses.setOnClickListener(commonListener);
        seeAllIntermediateCourses.setOnClickListener(commonListener);
        seeAllAdvancedCourses.setOnClickListener(commonListener);

        View.OnClickListener goToDetails = v -> {
            Intent intent = new Intent(Courses.this, CoursesDetails.class);
            int id = v.getId();
            String courseTitle;
            if (id == R.id.enrollNowBtn) {
                courseTitle = "Main Curriculum";
            } else if (id == R.id.startcourseBtn1) {
                courseTitle = "The Barriers Of Future Generation";
            } else if (id == R.id.startcourseBtn2) {
                courseTitle = "Let's Migrate to Allah";
            } else if (id == R.id.IndermediateStartCourseBtn1) {
                courseTitle = "Intermediate Course #1";
            } else if (id == R.id.IntermediateStartCourseBtn2) {
                courseTitle = "Intermediate Course #2";
            } else if (id == R.id.advancesStartBtn1) {
                courseTitle = "Advanced Course #1";
            } else if (id == R.id.advancesStartBtn2) {
                courseTitle = "Advanced Course #2";
            } else {
                courseTitle = "Unknown Course";
            }
            intent.putExtra("courseTitle", courseTitle);
            startActivity(intent);
        };

        enrollNowBtn.setOnClickListener(goToDetails);
        startCourseBtn1.setOnClickListener(goToDetails);
        startCourseBtn2.setOnClickListener(goToDetails);
        intermediateStartBtn1.setOnClickListener(goToDetails);
        intermediateStartBtn2.setOnClickListener(goToDetails);
        advancedStartBtn1.setOnClickListener(goToDetails);
        advancedStartBtn2.setOnClickListener(goToDetails);
    }
}
