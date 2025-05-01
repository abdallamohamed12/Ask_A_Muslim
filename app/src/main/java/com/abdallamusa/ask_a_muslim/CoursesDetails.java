
package com.abdallamusa.ask_a_muslim;
;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.bumptech.glide.Glide;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

public class CoursesDetails extends AppCompatActivity {
    TextView tabOverview, tabSyllabus, courseTitleTv,
            categoryLabel , numOfStudents , instructorName , courseLevel;
    FrameLayout contentContainer;
    ProgressBar progressBar;
    AppCompatButton continueLearningBtn;

ImageView headerImage ;
    private CourseApi courseApi;
    private CourseDetail course;     // will hold the fetched data
    private String courseId;
    private String level;
    private String title;

    enum Tab { OVERVIEW, SYLLABUS }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_courses_details);

        // 1) find views
        headerImage = findViewById(R.id.headerImage);
        categoryLabel        = findViewById(R.id.categoryLabel);
        courseTitleTv        = findViewById(R.id.courseTitle);
        tabOverview          = findViewById(R.id.tabOverview);
        tabSyllabus          = findViewById(R.id.tabSyllabus);
        contentContainer     = findViewById(R.id.content_container);
        progressBar          = findViewById(R.id.courseProgressBar);
        continueLearningBtn  = findViewById(R.id.btnContinueLearning);
        numOfStudents = findViewById(R.id.numOfStudents_ID);
        instructorName = findViewById(R.id.instructorName);

courseLevel = findViewById(R.id.courseLevel_ID);

        // 2) pull Intent extras
        courseId     = getIntent().getStringExtra("course_id");
        title        = getIntent().getStringExtra("course_title");
        String instructor = getIntent().getStringExtra("course_instructor");
        level        = getIntent().getStringExtra("course_level");
        String category        = getIntent().getStringExtra("course_category");



        // 3) set basic UI immediately
        courseTitleTv  .setText(title);
       courseLevel.setText(level);
        instructorName.setText(instructor);
        categoryLabel.setText(category);


        // 4) init Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://ask-a-muslim.runasp.net/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        courseApi = retrofit.create(CourseApi.class);

        // 5) fetch the full detail
        courseApi.getCourseById(courseId).enqueue(new Callback<CourseDetail>() {
            @Override
            public void onResponse(@NonNull Call<CourseDetail> call, @NonNull Response<CourseDetail> resp) {
                if (resp.isSuccessful() && resp.body() != null) {
                    course = resp.body();
                    // now we can switch tabs and populate them
                    selectTab(Tab.OVERVIEW);
                    int numOfStudentResponse =resp.body().numberOfStudentsEnrolled;
                    numOfStudents.setText(""+numOfStudentResponse);

                    Glide.with(CoursesDetails.this)
                            .load(course.thumbnailUrl)
                            /* optional */
                            .into(headerImage);

                            // in case of calling categories

                    courseTitleTv  .setText(course.title);
                    courseLevel.setText(course.level);
                    instructorName.setText(course.instructorName);
                    categoryLabel.setText(course.category);
                    ;
                } else {
                    Toast.makeText(CoursesDetails.this,
                            "Failed to load course: " + resp.code(),
                            Toast.LENGTH_SHORT).show();

                }
            }
            @Override
            public void onFailure(@NonNull Call<CourseDetail> call, @NonNull Throwable t) {
                Toast.makeText(CoursesDetails.this,
                        "Network error: " + t.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });

        // tab clicks
        tabOverview.setOnClickListener(v -> selectTab(Tab.OVERVIEW));
        tabSyllabus.setOnClickListener(v -> selectTab(Tab.SYLLABUS));

        continueLearningBtn.setOnClickListener(v -> {
            // If you want to start the first lesson, e.g.:
            if (course != null && !course.lessons.isEmpty()) {
                Intent i = new Intent(this, LessonVideo.class);
                i.putExtra("lesson_id", course.lessons.get(0).id);
                startActivity(i);
            }
        });



    }

    private void selectTab(Tab which) {
        tabOverview.setSelected(which == Tab.OVERVIEW);
        tabSyllabus.setSelected(which == Tab.SYLLABUS);

        contentContainer.removeAllViews();
        View pane = getLayoutInflater()
                .inflate(which==Tab.OVERVIEW
                                ? R.layout.overview_content
                                : R.layout.syllabus_content,
                        contentContainer, false);

        TextView body = pane.findViewById(
                which==Tab.OVERVIEW
                        ? R.id.overviewBody
                        : R.id.syllabusBody);

        if (course == null) {
            body.setText("Loading…");
        }
        else if (which == Tab.OVERVIEW) {
            // show description + enrolled count
            body.setText(
                    course.description+"\n");
        }
        else {
            // syllabus = list out each lesson
            StringBuilder sb = new StringBuilder();
            for (CourseDetail.Lesson L : course.lessons) {
                sb.append("• ").append(L.title).append("\n============\n")
                        .append(L.content).append("\n===============\n")
                        .append("\n\n");
            }
            body.setText(sb.toString());
        }

        contentContainer.addView(pane);
    }
}
