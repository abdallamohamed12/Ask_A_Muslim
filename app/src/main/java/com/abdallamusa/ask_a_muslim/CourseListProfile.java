// CourseListProfileActivity.java
package com.abdallamusa.ask_a_muslim;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
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

public class CourseListProfile extends AppCompatActivity {

    private static final String PREFS = "MyAppPrefs";
    private EnrollmentApi    enrollmentApi;
    private CourseApi        courseApi;
    private ListView         listView;
    private TextView         screenTitle;
    private String           category;
    private SharedPreferences prefs;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_course_list_profile);

        // 1) bind views
        listView     = findViewById(R.id.coursesListViewProfile);
        screenTitle  = findViewById(R.id.screenTitleProfile);
        ImageButton back = findViewById(R.id.backButton);

        back.setOnClickListener(v -> finish());

        // 2) get category (“My” or “Completed”)
        category = getIntent().getStringExtra("category");
        if ("Completed".equals(category)) {
            screenTitle.setText("Completed Courses");
        } else {
            category = "My";
            screenTitle.setText("My Courses");
        }

        // 3) setup Retrofit
        Retrofit rf = new Retrofit.Builder()
                .baseUrl("https://ask-a-muslim.runasp.net/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        enrollmentApi = rf.create(EnrollmentApi.class);
        courseApi     = rf.create(CourseApi.class);

        // 4) shared prefs
         final String studentId = SessionManager.get().getUserId();

        if ("My".equals(category)) {
            loadMyCourses(studentId);
        } else {
            loadCompletedCourses();
        }
    }

    private void loadMyCourses(String studentId) {
        enrollmentApi.getEnrolledCourses(studentId)
                .enqueue(new Callback<List<CourseSummary>>() {
                    @Override public void onResponse(@NonNull Call<List<CourseSummary>> call,
                                                     @NonNull Response<List<CourseSummary>> resp) {
                        if (resp.isSuccessful() && resp.body()!=null) {
                            showList(resp.body());
                        } else {
                            Toast.makeText(CourseListProfile.this,
                                    "Failed to load (“My”) : " + resp.code(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override public void onFailure(@NonNull Call<List<CourseSummary>> call,
                                                    @NonNull Throwable t) {
                        Toast.makeText(CourseListProfile.this,
                                "Network error: "+t.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void loadCompletedCourses() {
        Set<String> doneIds = prefs.getStringSet("completed_ids", new HashSet<>());
        if (doneIds.isEmpty()) {
            Toast.makeText(this, "You haven’t completed any courses yet.", Toast.LENGTH_SHORT).show();
            return;
        }
        List<CourseSummary> results = new ArrayList<>();
        // for each id, fetch and accumulate
        for (String cid : doneIds) {
            courseApi.getCourseById(cid)
                    .enqueue(new Callback<CourseDetail>() {
                        @Override public void onResponse(@NonNull Call<CourseDetail> call,
                                                         @NonNull Response<CourseDetail> resp) {
                            if (resp.isSuccessful() && resp.body()!=null) {
                                CourseDetail cd = resp.body();
                                // map down to summary
                                results.add(new CourseSummary(
                                        cd.id, cd.title, cd.instructorName, cd.thumbnailUrl
                                ));
                                // once all are loaded, update adapter
                                if (results.size()==doneIds.size()) {
                                    showList(results);
                                }
                            }
                        }
                        @Override public void onFailure(@NonNull Call<CourseDetail> call,
                                                        @NonNull Throwable t) {
                            // ignore single failures
                        }
                    });
        }
    }

    private void showList(List<CourseSummary> courses){
        CourseListAdapter adapter = new CourseListAdapter(this, courses);
        listView.setAdapter(adapter);
    }

    // --- Retrofit interfaces ---
    public interface EnrollmentApi {
        @GET("Enrollment/GetEnrolledCoursesByStudent")
        Call<List<CourseSummary>> getEnrolledCourses(@retrofit2.http.Query("studentId") String studentId);
    }
    public interface CourseApi {
        @GET("Course/GetCourseById/{id}")
        Call<CourseDetail> getCourseById(@Path("id") String id);
    }

    // --- Models ---
    public static class CourseSummary {
        @SerializedName("id")            public String id;
        @SerializedName("title")         public String title;
        @SerializedName("instructorName")public String instructor;
        @SerializedName("thumbnailUrl")  public String thumbnailUrl;
        public CourseSummary(String id,String t,String instr,String thumb){
            this.id=id; this.title=t; this.instructor=instr; this.thumbnailUrl=thumb;
        }
    }
    public static class CourseDetail {
        @SerializedName("id")            public String id;
        @SerializedName("title")         public String title;
        @SerializedName("instructorName")public String instructorName;
        @SerializedName("thumbnailUrl")  public String thumbnailUrl;
        // ... other fields omitted
    }

    // --- Adapter for the profile list ---
    private static class CourseListAdapter extends android.widget.ArrayAdapter<CourseSummary> {
        public CourseListAdapter(@NonNull android.content.Context ctx,
                                 @NonNull List<CourseSummary> items) {
            super(ctx, R.layout.item_listview_profile, items);
        }
        @NonNull
       @Override
        public View getView(int pos, View cv, @NonNull ViewGroup parent) {
            View v = cv!=null?cv:
                    LayoutInflater.from(getContext())
                            .inflate(R.layout.activity_item_course_listview, parent,false);

            CourseSummary c = getItem(pos);
            // views
            ImageView thumb = v.findViewById(R.id.thumbnailUrl_ID_profile);
            TextView  title = v.findViewById(R.id.courseTitle_profile);
            TextView  instr = v.findViewById(R.id.courseInstructor_profile);
            AppCompatButton continueBtn = v.findViewById(R.id.courseContinueButton_profile);

            AppCompatButton UnenrollBtn = v.findViewById(R.id.courseUnenrollButton_profile);

            assert c != null;
            title.setText(c.title);
            instr.setText(c.instructor);
            Glide.with(getContext())
                    .load(c.thumbnailUrl)
                    .into(thumb);


            UnenrollBtn.setOnClickListener(x -> {


            });

            continueBtn.setOnClickListener(x -> {
                Intent i = new Intent(getContext(), CoursesDetails.class);
                i.putExtra("course_id", c.id);
                getContext().startActivity(i);
            });
            return v;
        }
    }
}
