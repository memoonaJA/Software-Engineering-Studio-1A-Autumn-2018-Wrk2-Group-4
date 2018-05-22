package com.example.softwaregroup4.group4_fitnessapplication;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class Summary_GPS extends AppCompatActivity {
    private String distances;
    private String time;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary__gps);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView distanceTxt = findViewById(R.id.textView7);
        TextView timeTxt = findViewById(R.id.textView4);

        distances = getIntent().getStringExtra("distances");
        time =  getIntent().getStringExtra("time");

        distanceTxt.setText(distances);
        timeTxt.setText(time);
    }

}
