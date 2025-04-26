package com.abdallamusa.ask_a_muslim;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class LessonVideo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_lesson_video);

        final WebView webView = findViewById(R.id.youtubeWebView);

        ImageView arrowBack = findViewById(R.id.backArrowIcon_CourseVideo);

        arrowBack.setOnClickListener(view -> {

            Intent goToCoursesDetailsActivity = new Intent(this , CoursesDetails.class);
            startActivity(goToCoursesDetailsActivity);

        });

        WebSettings ws = webView.getSettings();
        ws.setJavaScriptEnabled(true);
        ws.setDomStorageEnabled(true);
        ws.setJavaScriptCanOpenWindowsAutomatically(true);

        // 1) Install a WebChromeClient to catch fullscreen requests
        webView.setWebChromeClient(new WebChromeClient() {
            private View fullScreenView;
            private CustomViewCallback customViewCallback;

            @Override
            public void onShowCustomView(View view, CustomViewCallback callback) {
                // save the view and callback
                fullScreenView = view;
                customViewCallback = callback;

                // add the full-screen view to our decor
                ViewGroup decor = (ViewGroup) getWindow().getDecorView();
                decor.addView(fullScreenView,
                        new ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT));
                // hide the WebView
                webView.setVisibility(View.GONE);
                // enter immersive full-screen
                getWindow().getDecorView().setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
            }

            @Override
            public void onHideCustomView() {
                // remove the full-screen view
                ViewGroup decor = (ViewGroup) getWindow().getDecorView();
                decor.removeView(fullScreenView);
                fullScreenView = null;
                // show the WebView again
                webView.setVisibility(View.VISIBLE);
                // notify that we’re back
                customViewCallback.onCustomViewHidden();
                // clear immersive flags
                getWindow().getDecorView().setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_VISIBLE);
            }
        });

        // 2) Use loadDataWithBaseURL so iframe fullscreen is honored
        String videoId = "g1n5gTWbpSY";
        String html =
                "<html><body style=\"margin:0;padding:0;\">"
                        + "<iframe width=\"100%\" height=\"100%\" "
                        + "src=\"https://www.youtube.com/embed/" + videoId
                        + "?rel=0&autoplay=1\" "
                        + "frameborder=\"0\" "
                        + "allow=\"autoplay; encrypted-media; fullscreen\" "
                        + "allowfullscreen></iframe>"
                        + "</body></html>";

        webView.loadDataWithBaseURL(
                null,
                html,
                "text/html",
                "utf-8",
                null
        );
    }
}