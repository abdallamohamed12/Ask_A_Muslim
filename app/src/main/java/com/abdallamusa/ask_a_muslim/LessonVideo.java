package com.abdallamusa.ask_a_muslim;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public class LessonVideo extends AppCompatActivity {

    private WebView webView;
    private AppCompatButton finishBtn;
    private TextView lessonTitleTv;
    private ImageView arrowBack;

    private List<Lesson> lessons;
    private int currentIndex = 0;
    private String courseId;

    interface LessonApi {
        @GET("Lesson/GetCourseLessons/{courseId}")
        Call<List<Lesson>> getCourseLessons(@Path("courseId") String courseId);
    }

    interface ProgressApi {
        @PUT("Progress/UpdateProgress/{progressId}")
        Call<Void> updateProgress(@Path("progressId") String lessonId);

        @GET("Progress/GetProgressByStudentId/ByStudent/{studentId}")
        Call<ProgressResponse> getProgressByStudent(@Path("studentId") String studentId);
    }

    private LessonApi lessonApi;
    private ProgressApi progressApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_lesson_video);

        initializeViews();
        setupWebView();
        setupRetrofit();
        fetchLessons();
        setupButtonClickListener();
    }

    private void initializeViews() {
        webView = findViewById(R.id.youtubeWebView);
        finishBtn = findViewById(R.id.finishedVideoBtn_ID);
        lessonTitleTv = findViewById(R.id.lessonTitle);
        arrowBack = findViewById(R.id.backArrowIcon_CourseVideo);

        arrowBack.setOnClickListener(v ->
                startActivity(new Intent(LessonVideo.this, CoursesDetails.class))
        );

        courseId = getIntent().getStringExtra("course_id");
    }

    @SuppressLint({"SetJavaScriptEnabled", "ClickableViewAccessibility"})
    private void setupWebView() {
        WebSettings ws = webView.getSettings();
        ws.setJavaScriptEnabled(true);
        ws.setDomStorageEnabled(true);
        ws.setMediaPlaybackRequiresUserGesture(false);

        // Disable viewport scaling
        ws.setUseWideViewPort(false);
        ws.setLoadWithOverviewMode(false);

        // Fixed scale settings
        webView.setInitialScale(100);

        // Disable zoom controls
        ws.setBuiltInZoomControls(false);
        ws.setDisplayZoomControls(false);

        webView.setWebChromeClient(new WebChromeClient() {
            private View fullScreenView;
            private CustomViewCallback customViewCallback;

            @Override
            public void onShowCustomView(View view, CustomViewCallback callback) {
                fullScreenView = view;
                customViewCallback = callback;
                ViewGroup decor = (ViewGroup) getWindow().getDecorView();
                decor.addView(fullScreenView, new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));
                webView.setVisibility(View.GONE);
                getWindow().getDecorView().setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
            }

            @Override
            public void onHideCustomView() {
                ViewGroup decor = (ViewGroup) getWindow().getDecorView();
                if (fullScreenView != null) decor.removeView(fullScreenView);
                fullScreenView = null;
                webView.setVisibility(View.VISIBLE);
                if (customViewCallback != null) customViewCallback.onCustomViewHidden();
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
            }
        });
    }

    private void setupRetrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://ask-a-muslim.runasp.net/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        lessonApi = retrofit.create(LessonApi.class);
        progressApi = retrofit.create(ProgressApi.class);
    }

    private void fetchLessons() {
        String startLessonId = getIntent().getStringExtra("lesson_id");
        lessonApi.getCourseLessons(courseId).enqueue(new Callback<List<Lesson>>() {
            @Override
            public void onResponse(@NonNull Call<List<Lesson>> call, @NonNull Response<List<Lesson>> resp) {
                if (resp.isSuccessful() && resp.body() != null) {
                    lessons = resp.body();
                    if (startLessonId != null) {
                        for (int i = 0; i < lessons.size(); i++) {
                            if (lessons.get(i).id.equals(startLessonId)) {
                                currentIndex = i;
                                break;
                            }
                        }
                    }
                    showCurrentLesson();
                } else {

                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Lesson>> call, @NonNull Throwable t) {
                //showError("Network error: " + t.getMessage());
            }
        });
    }

    private void setupButtonClickListener() {
        finishBtn.setOnClickListener(v -> {
            if (lessons == null || lessons.isEmpty()) return;

            Lesson currentLesson = lessons.get(currentIndex);
            progressApi.updateProgress(currentLesson.id).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                    handleProgressUpdateSuccess();
                }

                @Override
                public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                    //showError("Progress update failed");
                }
            });
        });
    }

    @SuppressLint("SetTextI18n")
    private void showCurrentLesson() {
        if (lessons == null || lessons.isEmpty()) return;

        Lesson currentLesson = lessons.get(currentIndex);
        lessonTitleTv.setText(currentLesson.title);
        loadVideo(currentLesson.externalVideoUrl);
        updateButtonState();
    }

    private void loadVideo(String videoUrl) {
        String videoId = extractVideoId(videoUrl);
        String html =
                "<!DOCTYPE html>" +
                        "<html><head>" +
                        "  <style>" +
                        "    html, body { margin:0; padding:0; height:100%; }" +
                        "    iframe { position:absolute; top:0; left:0; width:100%; height:100%; border:0; }" +
                        "  </style>" +
                        "</head><body>" +
                        "  <iframe src=\"https://www.youtube.com/embed/" + videoId +
                        "?autoplay=1&rel=0\" allowfullscreen></iframe>" +
                        "</body></html>";

        webView.loadDataWithBaseURL(null, html, "text/html", "UTF-8", null);
    }


    private String extractVideoId(String url) {
        if (url.contains("youtube.com/watch")) {
            String[] parts = url.split("v=");
            if (parts.length > 1) {
                String videoId = parts[1];
                int ampPos = videoId.indexOf('&');
                if (ampPos != -1) {
                    return videoId.substring(0, ampPos);
                }
                return videoId;
            }
        } else if (url.contains("youtu.be/")) {
            return url.substring(url.lastIndexOf("/") + 1);
        }
        return url;
    }

    private void updateButtonState() {
        finishBtn.setText(currentIndex < lessons.size() - 1 ? "Next Lesson" : "Finish Course");

        finishBtn.setEnabled(true);
    }

    private void handleProgressUpdateSuccess() {
        if (currentIndex < lessons.size() - 1) {
            currentIndex++;
            showCurrentLesson();
        } else {
            handleCourseCompletion();
        }
    }

    private void handleCourseCompletion() {
        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        int completedCount = prefs.getInt("completed_courses_count", 0);
        prefs.edit()
                .putInt("completed_courses_count", completedCount + 1)
                .apply();
        finishBtn.setEnabled(false);
        Toast.makeText(this, "Course Completed!", Toast.LENGTH_SHORT).show();

        startActivity(new Intent(this, Profile.class));
    }

    // ... [Keep remaining methods unchanged] ...

    public static class Lesson {
        public String id;
        public String title;
        public String content;
        public String thumbnailUrl;
        public String videoUrl;
        public String externalVideoUrl;
        public String courseId;
    }

    public static class ProgressResponse {
        public int totalLessonsCompleted;
    }
}