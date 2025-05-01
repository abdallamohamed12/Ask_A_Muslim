package com.abdallamusa.ask_a_muslim;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
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
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_list);

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

        public Course(String id ,String title,String category,String level, String instructor) {
            this.id = id ;
            this.title = title;
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

    }

    // Custom Adapter
    private static class CourseAdapter extends ArrayAdapter<Course> {

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

            TextView title = convertView.findViewById(R.id.courseTitle);
            TextView instructor = convertView.findViewById(R.id.courseInstructor);
            AppCompatButton actionBtn = convertView.findViewById(R.id.courseActionButton);

            // 5. Handle clicks


          //  ImageView thumbnail = convertView.findViewById(R.id.courseThumbnail);

            title.setText(course.getTitle());
           instructor.setText(course.getInstructor());

            actionBtn.setOnClickListener(btn -> {
                // e.g. start the course, open detail, show a toast...
                Intent i = new Intent(getContext(), CoursesDetails.class);
                // pass whatever you need into extras:
                i.putExtra("course_id",        course.getId());
                i.putExtra("course_title",     course.getTitle());
                i.putExtra("course_instructor",course.getInstructor());
                i.putExtra("course_level",course.getLevel());
                i.putExtra("course_category",course.getCategory());


                ;


                // if you have description/lessons etc, pass them too
                getContext().startActivity(i);
            });

return convertView;
           }
    }
}