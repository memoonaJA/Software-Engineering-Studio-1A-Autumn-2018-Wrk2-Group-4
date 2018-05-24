package com.example.softwaregroup4.group4_fitnessapplication;

import android.content.Intent;
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
    private String username;
    TextView distanceTxt;
    TextView timeTxt;
    TextView helloTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary__gps);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        distanceTxt = findViewById(R.id.textView7);
        timeTxt = findViewById(R.id.textView4);
        helloTxt = findViewById(R.id.textView10);

        distances = getIntent().getStringExtra("distances");
        time =  getIntent().getStringExtra("time");
        username = getIntent().getStringExtra("Username");

        distanceTxt.setText(distances + " metres");
        timeTxt.setText(time + " minutes");
        helloTxt.setText("Hello, " + username);
    }

    public void changeMain(View view) {
        Intent intent = new Intent(this, UserScreen.class);
        intent.putExtra("Username1", username);
        startActivity(intent);
    }

}
