package com.abdallamusa.ask_a_muslim;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class CourseList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_course_list);

        TextView screenTitle = findViewById(R.id.screenTitle);
                ListView listView = findViewById(R.id.coursesListView);


        ImageView arrowBack = findViewById(R.id.backButton);

        arrowBack.setOnClickListener(view -> {

            Intent goToCoursesActivity = new Intent(this , Courses.class);
            startActivity(goToCoursesActivity);

        });

        String category = getIntent().getStringExtra("category");
        if (category == null) category = "All";            // fallback

        screenTitle.setText(category + " Courses");

// 2. Prepare some dummy data (in real life you’d fetch from your API)
        List<Course> courses = new ArrayList<>();
        courses.add(new Course("Intro to Islam", "Dr. A"));
        courses.add(new Course("Quran Basics", "Dr. B"));
// … etc

// 3. Create and set your adapter
        CourseAdapter adapter = new CourseAdapter(this,
                        R.layout.activity_item_course_listview,  // <-- your item-XML filename
                        courses);
        listView.setAdapter( adapter);




    }
}