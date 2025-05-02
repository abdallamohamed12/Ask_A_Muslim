package com.abdallamusa.ask_a_muslim;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CourseAdapter extends ArrayAdapter<Course> {
    private final int resourceLayout;
    private final Context mContext;

    // our new fields:
    private final EnrollmentApi enrollmentApi;
   private final String studentId = SessionManager.get().getUserId();

    public CourseAdapter(@NonNull Context context,
                         int resource,
                         @NonNull List<Course> items) {
        super(context, resource, items);
        this.resourceLayout = resource;
        this.mContext = context;

        // 1) build a Retrofit+EnrollmentApi
        Retrofit rf = new Retrofit.Builder()
                .baseUrl("https://ask-a-muslim.runasp.net/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        this.enrollmentApi = rf.create(EnrollmentApi.class);

        // 2) load the studentId from SharedPreferences



    }

    @NonNull @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            v = LayoutInflater.from(mContext)
                    .inflate(resourceLayout, parent, false);
        }

        Course c = getItem(position);
        if (c != null) {
            TextView titleTv = v.findViewById(R.id.courseTitle);
            TextView instrTv = v.findViewById(R.id.courseInstructor);
            AppCompatButton actionBtn = v.findViewById(R.id.courseActionButton);

            titleTv.setText(c.title);
            instrTv.setText(c.instructor);

            actionBtn.setText("Enroll Now");
            actionBtn.setEnabled(true);
            actionBtn.setOnClickListener(btn -> {
                // 1) fire Enrollment POST
                enrollmentApi.enrollCourse(
                        new EnrollmentRequest(studentId, c.getId())
                ).enqueue(new Callback<Void>() {
                    @Override public void onResponse(@NonNull Call<Void> call,
                                                     @NonNull Response<Void> resp) {
                        // you could check resp.isSuccessful() here,
                        // but we'll just proceed.
                    }
                    @Override public void onFailure(@NonNull Call<Void> call,
                                                    @NonNull Throwable t) {
                        Toast.makeText(mContext,
                                "Enroll failed: " + t.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });

                // 2) save into your enrolled_ids set
                SharedPreferences prefs = mContext.getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
                Set<String> set = new HashSet<>(prefs.getStringSet("enrolled_ids", new HashSet<>()));
                set.add(c.getId());
                prefs.edit().putStringSet("enrolled_ids", set).apply();

                // 3) flip the button
                actionBtn.setText("Enrolled");
                actionBtn.setEnabled(false);

                // 4) open details
                Intent i = new Intent(mContext, CoursesDetails.class);
                i.putExtra("course_id",        c.getId());
                i.putExtra("course_title",     c.getTitle());
                i.putExtra("course_instructor",c.getInstructor());
                i.putExtra("course_level",     c.getLevel());
                i.putExtra("course_category",  c.getCategory());
                i.putExtra("course_thumbnail", c.getThumbnailUrl());
                mContext.startActivity(i);
            });
        }

        return v;
    }
}
