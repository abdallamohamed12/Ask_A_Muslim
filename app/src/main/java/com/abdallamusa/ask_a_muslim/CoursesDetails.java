package com.abdallamusa.ask_a_muslim;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

public class CoursesDetails extends AppCompatActivity {

    TextView tabOverview , tabSyllabus , courseTitle;
    FrameLayout contentContainer ;
    ProgressBar progressBar ;
    AppCompatButton continueLearningBtn ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_courses_details);


        tabOverview = findViewById(R.id.tabOverview);

        tabSyllabus = findViewById(R.id.tabSyllabus);

        contentContainer  = findViewById(R.id.content_container);

         courseTitle = findViewById(R.id.courseTitle);

         progressBar = findViewById(R.id.courseProgressBar);

         continueLearningBtn = findViewById(R.id.btnContinueLearning);



         // To change the course title based Upon Which course was Selected
        String coursetitle = getIntent().getStringExtra("courseTitle");
        if (coursetitle == null) coursetitle = "All";            // fallback

        courseTitle.setText(coursetitle);


        tabOverview.setOnClickListener(v -> selectTab(Tab.OVERVIEW));
        tabSyllabus.setOnClickListener(v -> selectTab(Tab.SYLLABUS));


        // show overview by default
        selectTab(Tab.OVERVIEW);

        continueLearningBtn.setOnClickListener(view -> {

            Intent goToLessonVideo = new Intent(this , LessonVideo.class);

            startActivity(goToLessonVideo);

        });
    }

    enum Tab { OVERVIEW, SYLLABUS }

    private void selectTab(Tab which) {
        // update the tab “highlight”
        tabOverview .setSelected(which == Tab.OVERVIEW);
        tabSyllabus .setSelected(which == Tab.SYLLABUS);

        // now swap the content pane
        contentContainer.removeAllViews();
        View pane;
        if (which == Tab.OVERVIEW) {
            pane = getLayoutInflater().inflate(R.layout.overview_content, contentContainer, false);
            TextView body = pane.findViewById(R.id.overviewBody);
            fetchOverviewFromApi(body);
        } else {
            pane = getLayoutInflater().inflate(R.layout.syllabus_content, contentContainer, false);
            TextView body = pane.findViewById(R.id.syllabusBody);
            fetchSyllabusFromApi(body);
        }
        contentContainer.addView(pane);
    }

    private void fetchOverviewFromApi(TextView into) {
        // Example stub: you’d call Retrofit/OkHttp/etc. here.
        // On success:
        into.setText("This masterclass covers advanced digital photography techniques…");
    }

    private void fetchSyllabusFromApi(TextView into) {
        // Example stub: call your API, parse JSON, build a nice bullet list…
        into.setText("42h • 8 modules • 32 lessons\n\nModule 1: Camera Mastery …\nModule 2: Composition …");
    }
}