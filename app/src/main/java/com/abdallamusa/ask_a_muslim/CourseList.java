package com.abdallamusa.ask_a_muslim;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.bumptech.glide.Glide;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

public class CourseList extends AppCompatActivity {

    private ListView listView;
    private CourseAdapter adapter;
    private List<Course> courses = new ArrayList<>();
    private CourseApiService apiService;
    private final String studentId = SessionManager.get().getUserId();

    private static EnrollmentApi enrollmentApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_list);

                     // if you have description/lessons etc, pass them too


        // Initialize views
        TextView screenTitle = findViewById(R.id.screenTitle);
        listView = findViewById(R.id.coursesListView);
        ImageView arrowBack = findViewById(R.id.backButton);

        // Initialize Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://ask-a-muslim.runasp.net/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(CourseApiService.class);

        enrollmentApi = retrofit.create(EnrollmentApi.class);

        // Get category from intent
        String levelName = getIntent().getStringExtra("category");
        if (levelName == null) levelName = "Beginner"; // Default value

        // Format level name for API URL
        levelName = levelName.toLowerCase();

        // Set screen title
        screenTitle.setText(levelName + " Courses");

        // Initialize adapter
        adapter = new CourseAdapter(this, courses);
        listView.setAdapter(adapter);

        // Load courses
        loadCourses(levelName);

        arrowBack.setOnClickListener(view -> {
            startActivity(new Intent(this, Courses.class));
            finish();
        });
    }

    private void loadCourses(String levelName) {
        apiService.getCoursesByLevel(levelName).enqueue(new Callback<List<Course>>() {
            @Override
            public void onResponse(@NonNull Call<List<Course>> call,
                                   @NonNull Response<List<Course>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    courses.clear();
                    courses.addAll(response.body());
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(CourseList.this,
                            "Failed to load courses",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Course>> call,
                                  @NonNull Throwable t) {
                Toast.makeText(CourseList.this,
                        "Network error: " + t.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    // API Service Interface
    public interface CourseApiService {
        @GET("Course/GetCourseByLevelName/ByLevelName/{levelName}")
        Call<List<Course>> getCoursesByLevel(@Path("levelName") String levelName);
    }

    // Course model class
    public static class Course {
        @SerializedName("title")
        private String title;

        @SerializedName("instructorName")
        private String instructor;

        @SerializedName("id")
        private String id;

        @SerializedName("level")
        private String level;

        @SerializedName("category")
        private String category;
        @SerializedName("thumbnailUrl")
        private String thumbnailUrl;
        public Course(String id ,String title,String thumbnailUrl,String category,String level, String instructor) {
            this.id = id ;
            this.title = title;
            this.thumbnailUrl=thumbnailUrl;
            this.instructor = instructor;

            this.level = level;
            this.category = category;

        }

        public String getId() {
            return id;
        }

        public String getLevel() {
            return level;
        }
        public String getTitle() { return title; }
        public String getInstructor() { return instructor; }

        public String getCategory() { return category; }

        public String getThumbnailUrl() {
            return thumbnailUrl;
        }
    }

    // Custom Adapter
    // inside CourseList…

    private class CourseAdapter extends ArrayAdapter<Course> {
        private final List<Course> courses;

        public CourseAdapter(Context context, List<Course> courses) {
            super(context, R.layout.activity_item_course_listview, courses);
            this.courses = courses;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext())
                        .inflate(R.layout.activity_item_course_listview, parent, false);
            }

            Course course = courses.get(position);

            TextView title       = convertView.findViewById(R.id.courseTitle);
            TextView instructor  = convertView.findViewById(R.id.courseInstructor);
            AppCompatButton btn   = convertView.findViewById(R.id.courseActionButton);
            ImageView thumb      = convertView.findViewById(R.id.thumbnailUrl_ID);

            title.setText(course.getTitle());
            instructor.setText(course.getInstructor());
            Glide.with(getContext())
                    .load(course.getThumbnailUrl())
                    .into(thumb);

            btn.setText("Enroll Now");
            btn.setEnabled(true);
            btn.setOnClickListener(v -> {
                // 1) call the Enrollment API
                enrollmentApi.enrollCourse(new EnrollmentRequest(studentId, course.getId()))
                        .enqueue(new Callback<Void>() {
                            @Override public void onResponse(@NonNull Call<Void> call,
                                                             @NonNull Response<Void> resp) {
                                // you might check resp.isSuccessful() here…
                            }
                            @Override public void onFailure(@NonNull Call<Void> call,
                                                            @NonNull Throwable t) {
                                Toast.makeText(CourseList.this,
                                        "Enroll failed: " + t.getMessage(),
                                        Toast.LENGTH_SHORT).show();
                            }
                        });

                // 2) save locally
                SharedPreferences prefs = getContext()
                        .getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
                Set<String> set = new HashSet<>(prefs.getStringSet("enrolled_ids", new HashSet<>()));
                set.add(course.getId());
                prefs.edit().putStringSet("enrolled_ids", set).apply();

                // 3) flip button
                btn.setText("Enrolled");
                btn.setEnabled(false);

                // 4) launch details
                Intent i = new Intent(CourseList.this, CoursesDetails.class);
                i.putExtra("course_id",         course.getId());
                i.putExtra("course_title",      course.getTitle());
                i.putExtra("course_instructor", course.getInstructor());
                i.putExtra("course_level",      course.getLevel());
                i.putExtra("course_category",   course.getCategory());
                i.putExtra("course_thumbnail",  course.getThumbnailUrl());
                CourseList.this.startActivity(i);
            });

            return convertView;
        }
    }




}