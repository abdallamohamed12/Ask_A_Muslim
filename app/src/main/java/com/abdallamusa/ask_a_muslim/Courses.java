package com.abdallamusa.ask_a_muslim;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;

import com.bumptech.glide.Glide;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Courses extends AppCompatActivity {

    private ImageView arrowBack;


    // Category Buttons
    private AppCompatButton allSectionBtn, quranSectionBtn, hadithSectionBtn,
            creedSectionBtn, fiqhSectionBtn, faithSectionBtn;

    // Main Course Card
    private ImageView mainCourseImage;
    private TextView mainCourseTitle, mainCourseInstructor;
    private AppCompatButton enrollNowBtn;

    // Beginners Section
    private TextView seeAllBeginners;
    private ImageView beginnerCourseImage1, beginnerCourseImage2;
    private TextView beginnerCourseTitle1, beginnerCourseTitle2;
    private TextView beginnerInstructor1, beginnerInstructor2;
    private AppCompatButton beginnerStartBtn1, beginnerStartBtn2;

    // Intermediate Section
    private TextView seeAllIntermediate;
    private ImageView intermediateCourseImage1, intermediateCourseImage2;
    private TextView intermediateCourseTitle1, intermediateCourseTitle2;
    private TextView intermediateInstructor1, intermediateInstructor2;
    private AppCompatButton intermediateStartBtn1, intermediateStartBtn2;

    // Advanced Section
    private TextView seeAllAdvanced;
    private ImageView advancedCourseImage1, advancedCourseImage2;
    private TextView advancedCourseTitle1, advancedCourseTitle2;
    private TextView advancedInstructor1, advancedInstructor2;
    private AppCompatButton advancedStartBtn1, advancedStartBtn2;

    private CoursesApi api;
    private EnrollmentApi enrollmentApi;
    private String           a1Id, a2Id;
    private String           mainCourseId;
    private String           b1Id, b2Id;
    private String           i1Id, i2Id;

    private final String studentId = SessionManager.get().getUserId();
    private AppCompatButton[] categoryButtons;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_courses);

        // Initialize Top Bar
        arrowBack = findViewById(R.id.arrowBack_ID);


        // Initialize Category Buttons
        allSectionBtn = findViewById(R.id.all_section);
        quranSectionBtn = findViewById(R.id.quran_section);
        hadithSectionBtn = findViewById(R.id.hadith_section);
        creedSectionBtn = findViewById(R.id.creed_section);
        fiqhSectionBtn = findViewById(R.id.fiqh_section);
        faithSectionBtn = findViewById(R.id.faith_section);

        // Initialize Main Course Card
        mainCourseImage = findViewById(R.id.mainCourseImage);
        mainCourseTitle = findViewById(R.id.mainCourseTitle);
        mainCourseInstructor = findViewById(R.id.mainCourseInstructor);
        enrollNowBtn = findViewById(R.id.enrollNowBtn);

        // Initialize Beginners Section
        seeAllBeginners = findViewById(R.id.seeAllBeginnersCourses);
        beginnerCourseImage1 = findViewById(R.id.beginnerCourseImage1);
        beginnerCourseImage2 = findViewById(R.id.beginnerCourseImage2);
        beginnerCourseTitle1 = findViewById(R.id.beginnerCourseTitle1);
        beginnerCourseTitle2 = findViewById(R.id.beginnerCourseTitle2);
        beginnerInstructor1 = findViewById(R.id.beginnerInstructor1);
        beginnerInstructor2 = findViewById(R.id.beginnerInstructor2);
        beginnerStartBtn1 = findViewById(R.id.beginnerStartCourseBtn1);
        beginnerStartBtn2 = findViewById(R.id.beginnerStartCourseBtn2);

        // Initialize Intermediate Section
        seeAllIntermediate = findViewById(R.id.seeAllIntermediateCourses);
        intermediateCourseImage1 = findViewById(R.id.intermediateCourseImage1);
        intermediateCourseImage2 = findViewById(R.id.intermediateCourseImage2);
        intermediateCourseTitle1 = findViewById(R.id.intermediateCourseTitle1);
        intermediateCourseTitle2 = findViewById(R.id.intermediateCourseTitle2);
        intermediateInstructor1 = findViewById(R.id.intermediateInstructor1);
        intermediateInstructor2 = findViewById(R.id.intermediateInstructor2);
        intermediateStartBtn1 = findViewById(R.id.intermediateStartCourseBtn1);
        intermediateStartBtn2 = findViewById(R.id.intermediateStartCourseBtn2);

        // Initialize Advanced Section
        seeAllAdvanced = findViewById(R.id.seeAllAdvancedCourses);
        advancedCourseImage1 = findViewById(R.id.advancedCourseImage1);
        advancedCourseImage2 = findViewById(R.id.advancedCourseImage2);
        advancedCourseTitle1 = findViewById(R.id.advancedCourseTitle1);
        advancedCourseTitle2 = findViewById(R.id.advancedCourseTitle2);
        advancedInstructor1 = findViewById(R.id.advancedInstructor1);
        advancedInstructor2 = findViewById(R.id.advancedInstructor2);
        advancedStartBtn1 = findViewById(R.id.advancedStartCourseBtn1);
        advancedStartBtn2 = findViewById(R.id.advancedStartCourseBtn2);


        arrowBack.setOnClickListener(view -> {

            Intent goToHomeActivity = new Intent(this , MainActivity.class);
            startActivity(goToHomeActivity);

        });


        View.OnClickListener commonListener = v -> {
            String category;
            int id = v.getId();
            if (id == R.id.seeAllBeginnersCourses) {
                category = "Beginner";
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

        seeAllBeginners.setOnClickListener(commonListener);
        seeAllIntermediate.setOnClickListener(commonListener);
        seeAllAdvanced.setOnClickListener(commonListener);



// 2) In onCreate(), after you bind each button, initialize the array:
        categoryButtons = new AppCompatButton[] {
                allSectionBtn,
                quranSectionBtn,
                hadithSectionBtn,
                creedSectionBtn,
                fiqhSectionBtn,
                faithSectionBtn
        };

// 3) Write a helper that resets them all to grey, then makes the one with the given id green:

        allSectionBtn.setOnClickListener(v -> {
            highlightCategory(v.getId());
            loadAll();
        });

        View.OnClickListener sectionClick = v -> {
            String cat;
            int id = v.getId();
            highlightCategory(id);
            if (id == R.id.quran_section) {
                cat = "quran";
            } else if (id == R.id.hadith_section) {

                cat = "hadith";
            } else if (id == R.id.creed_section) {

                cat = "aqeeda";
            } else if (id == R.id.fiqh_section) {

                cat = "fiqh";
            } else if (id == R.id.faith_section) {

                cat = "faith";
            } else {
                cat = "null";
            }
            loadCategory(cat);
        };



        quranSectionBtn .setOnClickListener(sectionClick);
        hadithSectionBtn.setOnClickListener(sectionClick);
        creedSectionBtn .setOnClickListener(sectionClick);
        fiqhSectionBtn  .setOnClickListener(sectionClick);
        faithSectionBtn .setOnClickListener(sectionClick);





        // Retrofit setup
        Retrofit rf = new Retrofit.Builder()
                .baseUrl("https://ask-a-muslim.runasp.net/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        api = rf.create(CoursesApi.class);

        enrollmentApi = rf.create(EnrollmentApi.class);

    }




    private void loadAll() {
        api.getAll().enqueue(new Callback<List<CourseSummary>>() {
            @Override
            public void onResponse(@NonNull Call<List<CourseSummary>> call,
                                   @NonNull Response<List<CourseSummary>> resp) {
                if (!resp.isSuccessful() || resp.body() == null || resp.body().size() < 7) {
                    Toast.makeText(Courses.this,
                            "Not enough courses returned", Toast.LENGTH_SHORT).show();
                    return;
                }
                List<CourseSummary> list = resp.body();

                // MAIN FEATURED
                CourseSummary main = list.get(0);
                mainCourseId = main.id;
                mainCourseTitle     .setText(main.title);
                mainCourseInstructor.setText(main.instructor);
                Glide.with(Courses.this)
                        .load(main.thumbnailUrl)
                        .into(mainCourseImage);
                enrollNowBtn.setText("Enroll Now");
                enrollNowBtn.setEnabled(true);

                enrollNowBtn.setOnClickListener(v->enroll(mainCourseId, enrollNowBtn));

                // then wire the six cards exactly the same as in loadCategory()…
                wireRow(list.get(1),
                        beginnerCourseImage1, (AppCompatTextView) beginnerCourseTitle1, (AppCompatTextView) beginnerInstructor1, beginnerStartBtn1,
                        id -> b1Id = id);
                wireRow(list.get(2),
                        beginnerCourseImage2, (AppCompatTextView) beginnerCourseTitle2, (AppCompatTextView) beginnerInstructor2, beginnerStartBtn2,
                        id -> b2Id = id);

                wireRow(list.get(3),
                        intermediateCourseImage1, (AppCompatTextView) intermediateCourseTitle1, (AppCompatTextView) intermediateInstructor1, intermediateStartBtn1,
                        id -> i1Id = id);
                wireRow(list.get(4),
                        intermediateCourseImage2, (AppCompatTextView) intermediateCourseTitle2, (AppCompatTextView) intermediateInstructor2, intermediateStartBtn2,
                        id -> i2Id = id);

                wireRow(list.get(5),
                        advancedCourseImage1, (AppCompatTextView) advancedCourseTitle1, (AppCompatTextView) advancedInstructor1, advancedStartBtn1,
                        id -> a1Id = id);
                wireRow(list.get(6),
                        advancedCourseImage2, (AppCompatTextView) advancedCourseTitle2, (AppCompatTextView) advancedInstructor2, advancedStartBtn2,
                        id -> a2Id = id);
            }
            @Override public void onFailure(@NonNull Call<List<CourseSummary>> call, @NonNull Throwable t) {
                Toast.makeText(Courses.this,
                        "Network error: "+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadCategory(String cat) {
        api.getByCategory(cat).enqueue(new Callback<List<CourseSummary>>() {
            @Override
            public void onResponse(@NonNull Call<List<CourseSummary>> call,
                                   @NonNull Response<List<CourseSummary>> resp) {
                if (!resp.isSuccessful() || resp.body()==null || resp.body().size()<7) {
                    Toast.makeText(Courses.this,
                            "Not enough courses in "+cat,Toast.LENGTH_SHORT).show();
                    return;
                }
                List<CourseSummary> list = resp.body();

                // MAIN FEATURED = index 0
                CourseSummary main = list.get(0);
                mainCourseId = main.id;
                mainCourseTitle     .setText(main.title);
                mainCourseInstructor.setText(main.instructor);
                Glide.with(Courses.this)
                        .load(main.thumbnailUrl)
                        .into(mainCourseImage);

                enrollNowBtn.setOnClickListener(v->enroll(mainCourseId, enrollNowBtn));

                // BEGINNER = index 1 & 2
                wireRow(list.get(1),
                        beginnerCourseImage1, (AppCompatTextView) beginnerCourseTitle1, (AppCompatTextView) beginnerInstructor1,beginnerStartBtn1,
                        id -> b1Id=id);
                wireRow(list.get(2),
                        beginnerCourseImage2, (AppCompatTextView) beginnerCourseTitle2, (AppCompatTextView) beginnerInstructor2,beginnerStartBtn2,

                        id -> b2Id=id);

                // INTERMEDIATE = index 3 & 4
                wireRow(list.get(3),
                        intermediateCourseImage1, (AppCompatTextView) intermediateCourseTitle1, (AppCompatTextView) intermediateInstructor1,intermediateStartBtn1,
                        id -> i1Id=id);
                wireRow(list.get(4),
                        intermediateCourseImage2, (AppCompatTextView) intermediateCourseTitle2, (AppCompatTextView) intermediateInstructor2,intermediateStartBtn2,
                        id -> i2Id=id);

                // ADVANCED = index 5 & 6
                wireRow(list.get(5),
                        advancedCourseImage1, (AppCompatTextView) advancedCourseTitle1, (AppCompatTextView) advancedInstructor1,advancedStartBtn1,


                        id -> a1Id=id);
                wireRow(list.get(6),
                        advancedCourseImage2, (AppCompatTextView) advancedCourseTitle2, (AppCompatTextView) advancedInstructor2,advancedStartBtn2,
                        id -> a2Id=id);
            }
            @Override public void onFailure(@NonNull Call<List<CourseSummary>> call, @NonNull Throwable t) {
                Toast.makeText(Courses.this,
                        "Network error: "+t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private interface IdSetter { void set(String id); }

    private void wireRow(CourseSummary cs,
                         ImageView img, AppCompatTextView titleTv, AppCompatTextView instrTv,
                         AppCompatButton btn, IdSetter setter) {
        setter.set(cs.id);
        titleTv .setText(cs.title);
        instrTv .setText(cs.instructor);
        Glide.with(this)
                .load(cs.thumbnailUrl)
                .into(img);
        // enroll student in course by Calling Enrollment Api

        btn.setOnClickListener(v->enroll(cs.id, btn));
    }

    @SuppressLint("SetTextI18n")
    private void enroll(String courseId, AppCompatButton btn){
        // 1) POST to server
        enrollmentApi.enrollCourse(new EnrollmentRequest(studentId, courseId))
                .enqueue(new Callback<Void>(){
                    @Override public void onResponse(@NonNull Call<Void> c, @NonNull Response<Void> r){
                        // ignore success/fail here
                    }
                    @Override public void onFailure(@NonNull Call<Void> c, @NonNull Throwable t){
                        Toast.makeText(Courses.this,"Enroll failed: "+t.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });


        // 3) update UI + navigate
        btn.setText("Enrolled");
        btn.setEnabled(false);
        openDetail(courseId);
    }
    private void openDetail(String courseId) {
        Intent i = new Intent(this, CoursesDetails.class);

        i.putExtra("course_id", courseId);

        startActivity(i);
    }

    private void highlightCategory(int clickedId) {
        for (AppCompatButton btn : categoryButtons) {
            btn.setBackgroundResource(R.drawable.grey_button);
        }
        // now find the one that matches and turn it green
        findViewById(clickedId)
                .setBackgroundResource(R.drawable.green_button);
    }
}




