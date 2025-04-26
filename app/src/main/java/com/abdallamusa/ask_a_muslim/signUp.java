package com.abdallamusa.ask_a_muslim;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;


public class signUp extends AppCompatActivity {

    TextView signIn_tx;
     ImageView backArrowBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);


        backArrowBtn = findViewById(R.id.arrow_left);

        signIn_tx = findViewById(R.id.signIn_tx);

        Button createAccountButton = findViewById(R.id.Create_account_button);


        signIn_tx.setOnClickListener(view -> {

            Intent goToLoginActivity = new Intent(signUp.this, login.class);

            startActivity(goToLoginActivity);


        });

        backArrowBtn.setOnClickListener(view -> {

            Intent goToMainActivity = new Intent(signUp.this, MainActivity.class);

            startActivity(goToMainActivity);


        });

        createAccountButton.setOnClickListener(view -> {


            Intent goToCoursesActivity = new Intent(signUp.this , Courses.class);
            startActivity(goToCoursesActivity);
        });

    }
}