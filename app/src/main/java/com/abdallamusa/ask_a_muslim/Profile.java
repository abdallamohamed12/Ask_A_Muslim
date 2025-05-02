package com.abdallamusa.ask_a_muslim;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Profile extends AppCompatActivity {
    private EnrollmentApi    enrollmentApi;
    private SharedPreferences prefs;
    private CourseCardAdapter adapter;
    private List<CourseSummary> enrolledCourses;
    private RecyclerView     myCoursesRv;
    private TextView         userName, userEmail , numOfEnrolledCourses , numOfCompletedCourses;

    private int count =0;

    private final String studentId = SessionManager.get().getUserId();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // bind views
        myCoursesRv = findViewById(R.id.myCoursesContainer);
        userName    = findViewById(R.id.userName_ID);
        userEmail   = findViewById(R.id.emailTextView_ID);
        ImageView arrowBack = findViewById(R.id.arrowBack_ID);
        AppCompatButton editProfileBtn = findViewById(R.id.editProfileBtn);


        numOfEnrolledCourses = findViewById(R.id.numOfEnrolledCourses_ID);

        prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);

        // … your existing finds …

        numOfCompletedCourses = findViewById(R.id.numOfCompletedCourses_ID);

        // Load and display the persisted completed‐courses count:
        int completedCount = prefs.getInt("completed_courses_count", 0);
        numOfCompletedCourses.setText(String.valueOf(completedCount));

        arrowBack.setOnClickListener(v ->
                startActivity(new Intent(Profile.this, MainActivity.class))
        );

        editProfileBtn.setOnClickListener(v ->
                startActivity(new Intent(Profile.this, EditProfile.class))
        );


        Retrofit rf = new Retrofit.Builder()
                .baseUrl("https://ask-a-muslim.runasp.net/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // 1) fetch fullName & email
        UserApi userApi = rf.create(UserApi.class);
        if (!studentId.isEmpty()) {
            userApi.getUserById(studentId).enqueue(new Callback<UserResponse>() {
                @Override public void onResponse(@NonNull Call<UserResponse> call,
                                                 @NonNull Response<UserResponse> resp) {
                    if (resp.isSuccessful() && resp.body() != null) {
                        userName .setText(resp.body().fullName);
                        userEmail.setText(resp.body().email);


                    } else {
                        Toast.makeText(Profile.this,
                                "Profile load failed: "+resp.code(),
                                Toast.LENGTH_SHORT).show();
                    }
                }
                @Override public void onFailure(@NonNull Call<UserResponse> call,
                                                @NonNull Throwable t) {
                    Toast.makeText(Profile.this,
                            "Network error: "+t.getMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            });
        }

        // 2) fetch enrolled courses & wire up RecyclerView
        myCoursesRv.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        );
        enrollmentApi = rf.create(EnrollmentApi.class);
        fetchEnrolledCourses(studentId);
    }

    private void fetchEnrolledCourses(String studentId) {
        enrollmentApi.getEnrolledCourses(studentId)
                .enqueue(new Callback<List<CourseSummary>>() {
                    @Override public void onResponse(@NonNull Call<List<CourseSummary>> call,
                                                     @NonNull Response<List<CourseSummary>> resp) {
                        if (!resp.isSuccessful() || resp.body() == null) {
                            Toast.makeText(Profile.this,
                                    "Failed to load enrolled ("+resp.code()+")",
                                    Toast.LENGTH_SHORT).show();
                            return;
                        }

                        enrolledCourses = resp.body();
                         count = resp.body().size();
                        numOfEnrolledCourses.setText(String.valueOf(count));

                        adapter = new CourseCardAdapter(
                                Profile.this,
                                enrolledCourses,
                                new CourseCardAdapter.OnCourseAction() {
                                    @Override
                                    public void onCourseContinue(String courseId) {
                                        startActivity(new Intent(Profile.this, CoursesDetails.class)
                                                .putExtra("course_id", courseId));
                                    }
                                    @Override
                                    public void onCourseUnenroll(String courseId) {
                                        unenrollCourse(studentId, courseId);


                                    }
                                }
                        );
                        myCoursesRv.setAdapter(adapter);
                    }
                    @Override public void onFailure(@NonNull Call<List<CourseSummary>> call,
                                                    @NonNull Throwable t) {
                        Toast.makeText(Profile.this,
                                "Network error: "+t.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void unenrollCourse(String studentId, String courseId) {
        enrollmentApi.unenrollCourse(studentId, courseId)
                .enqueue(new Callback<Void>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override public void onResponse(@NonNull Call<Void> call,
                                                     @NonNull Response<Void> resp) {
                        if (resp.isSuccessful()) {
                            // remove locally stored
                            Set<String> set = new HashSet<>(prefs.getStringSet("enrolled_ids", new HashSet<>()));
                            set.remove(courseId);
                            prefs.edit().putStringSet("enrolled_ids", set).apply();
                            // update UI


                            for (int i = 0; i < enrolledCourses.size(); i++) {
                                if (enrolledCourses.get(i).id.equals(courseId)) {
                                    enrolledCourses.remove(i);
                                    adapter.notifyItemRemoved(i);
                                    count--;
                                    numOfEnrolledCourses.setText(String.valueOf(count));
                                    break;
                                }



                            }
                        } else {
                            Toast.makeText(Profile.this,
                                    "Unenroll failed: "+resp.code(),
                                    Toast.LENGTH_SHORT).show();
                            adapter.notifyDataSetChanged();
                        }
                    }
                    @SuppressLint("NotifyDataSetChanged")
                    @Override public void onFailure(@NonNull Call<Void> call,
                                                    @NonNull Throwable t) {
                        Toast.makeText(Profile.this,
                                "Network error: "+t.getMessage(),
                                Toast.LENGTH_SHORT).show();
                        adapter.notifyDataSetChanged();
                    }
                });
    }
}
