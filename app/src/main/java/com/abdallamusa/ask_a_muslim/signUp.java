package com.abdallamusa.ask_a_muslim;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;


public class signUp extends AppCompatActivity {

    Button haveAccount_btn ;
     ImageView backArrowBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);


        haveAccount_btn = findViewById(R.id.iHaveAccount_btn);

        backArrowBtn = findViewById(R.id.back_arrow_icon);
        haveAccount_btn.setOnClickListener(view -> {

            Intent goToLoginActivity = new Intent(signUp.this , login.class);

            startActivity(goToLoginActivity);

        });

        backArrowBtn.setOnClickListener(view -> {

            Intent goToMainActivity = new Intent(signUp.this, MainActivity.class);

            startActivity(goToMainActivity);


        });

    }
}