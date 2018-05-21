package com.example.softwaregroup4.group4_fitnessapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Workout extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);
    }

    public void changeMaps(View view) {
        Intent intent = new Intent(this, StepCounter.class);
        startActivity(intent);
    }
}
