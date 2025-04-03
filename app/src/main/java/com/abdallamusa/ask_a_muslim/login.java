package com.abdallamusa.ask_a_muslim;

import static android.view.View.AUTOFILL_HINT_EMAIL_ADDRESS;
import static android.view.View.AUTOFILL_HINT_USERNAME;

import android.content.Intent;

import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.view.autofill.AutofillManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;


public class login extends AppCompatActivity {

    Button registration_btn;
    EditText emailOrUsername_editText;

    ImageView backArrowBtn;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);


        registration_btn = findViewById(R.id.idontHaveAccount_btn);

        emailOrUsername_editText = findViewById(R.id.Email_or_Username_edit_view);


        backArrowBtn = findViewById(R.id.back_arrow_icon);


        emailOrUsername_editText.setAutofillHints( AUTOFILL_HINT_EMAIL_ADDRESS,AUTOFILL_HINT_USERNAME);

        registration_btn.setOnClickListener(view -> {


            Intent goToSignUpActivity = new Intent(login.this , signUp.class);
            startActivity(goToSignUpActivity);


        });


        backArrowBtn.setOnClickListener(view -> {

            Intent goToMainActivity = new Intent(login.this, MainActivity.class);

            startActivity(goToMainActivity);


        });

    }
}