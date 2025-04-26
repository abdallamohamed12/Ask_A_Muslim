package com.abdallamusa.ask_a_muslim;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class Walkthrough1 extends AppCompatActivity {

    private ImageSwitcher myImageSwitcher;
    private Button switchButton;
    private boolean isImage1Displayed = true;

    private int currentPage = 0;

    TextView title_tx , welcomePhrase_tx , skipBtn;

ImageView dot1 ,dot2,dot3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        setContentView(R.layout.walkthrough1);

        myImageSwitcher = findViewById(R.id.myImageSwitcher);
        switchButton = findViewById(R.id.next_button);
        title_tx = findViewById(R.id.Title);
        welcomePhrase_tx = findViewById(R.id.welcome_phrase);
        skipBtn = findViewById(R.id.skip_text);
        dot1  = findViewById(R.id.dot1);

        dot2  = findViewById(R.id.dot2);
        dot3  = findViewById(R.id.dot3);

        // Set factory for ImageSwitcher
        myImageSwitcher.setFactory(() -> {
            ImageView imageView = new ImageView(Walkthrough1.this);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setLayoutParams(new ImageSwitcher.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));
            return imageView;
        });

        // Optional: set animations
        myImageSwitcher.setInAnimation(this, android.R.anim.fade_in);
        myImageSwitcher.setOutAnimation(this, android.R.anim.fade_out);

        // Set initial image
        myImageSwitcher.setImageResource(R.drawable.picture1);

        title_tx.setText(R.string.title1);

        welcomePhrase_tx.setText(R.string.welcome_phrase1);



        skipBtn.setOnClickListener(view -> {

            Intent goTOMainActivity = new Intent(Walkthrough1.this, MainActivity.class);
            startActivity(goTOMainActivity);
            finish();

        });

        switchButton.setOnClickListener(v -> {




            switch (currentPage){


                case 1:
                    title_tx.setText(R.string.title2);

                    welcomePhrase_tx.setText(R.string.welcome_phrase2);
                    myImageSwitcher.setImageResource(R.drawable.picture2);
                    dot2.setImageResource(R.drawable.dot_active);
                    dot1.setImageResource(R.drawable.dot_inactive);
                    dot3.setImageResource(R.drawable.dot_inactive);

                    break;

                case 2:
                    title_tx.setText(R.string.title3);

                    welcomePhrase_tx.setText(R.string.welcome_phrase3);
                    myImageSwitcher.setImageResource(R.drawable.picture3);
                    dot2.setImageResource(R.drawable.dot_inactive);
                    dot1.setImageResource(R.drawable.dot_inactive);
                    dot3.setImageResource(R.drawable.dot_active);

                    break;

                default:
                    break;

            }

            if (currentPage == 2){

                switchButton.setText(R.string.get_started);
            }







            if (currentPage>2){

                Intent goTOMainActivity = new Intent(Walkthrough1.this, MainActivity.class);
                startActivity(goTOMainActivity);
                finish();
            }
            currentPage++;
        });
    }
}
