package com.example.softwaregroup4.group4_fitnessapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Summary_workout extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary_workout);
    }

    public void changeMain(View view) {
        Intent intent = new Intent(this, UserScreen.class);
        startActivity(intent);
    }
}
