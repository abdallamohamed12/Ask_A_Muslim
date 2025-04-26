package com.abdallamusa.ask_a_muslim;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class More extends AppCompatActivity implements View.OnClickListener {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_more);

        findViewById(R.id.azkarId).setOnClickListener(this);
        findViewById(R.id.profileId).setOnClickListener(this);


    }

    @Override
    public void onClick(View textView) {


        if (textView.getId() == R.id.azkarId){

            Intent goToAzkarActivity = new Intent(More.this , Azkar.class);
            startActivity(goToAzkarActivity);
        }


        else if (textView.getId() == R.id.profileId){

            Intent goToProfileActivity = new Intent(More.this , Profile.class);
            startActivity(goToProfileActivity);
        }



    }



    }
