package com.example.softwaregroup4.group4_fitnessapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

public class Summary_GPS extends AppCompatActivity {
    private String distances;
    private String time;
    private String username;
    private String steps;
    private TextView distanceTxt;
    private TextView timeTxt;
    private TextView helloTxt;
    private TextView stepTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary__gps);
        distanceTxt = findViewById(R.id.textView15);
        timeTxt = findViewById(R.id.textView14);
        helloTxt = findViewById(R.id.textView7);
        stepTxt = findViewById(R.id.textView4);
        distances = getIntent().getStringExtra("distances");
        time =  getIntent().getStringExtra("time");
        username = getIntent().getStringExtra("Username");
        steps = getIntent().getStringExtra("Steps");

        helloTxt.setText("Hello, " + username);
        distanceTxt.setText("You travelled " + distances + " metres.");
        timeTxt.setText("Your activity took " + time + " minutes.");
        stepTxt.setText("You took a total of " + steps + " steps.");

    }

    public void changeMain(View view) {
        Intent intent = new Intent(this, UserScreen.class);
        intent.putExtra("Username1", username);
        startActivity(intent);
    }

}
