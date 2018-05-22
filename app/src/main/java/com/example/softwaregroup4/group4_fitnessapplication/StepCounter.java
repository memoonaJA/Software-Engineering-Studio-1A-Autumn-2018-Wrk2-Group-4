package com.example.softwaregroup4.group4_fitnessapplication;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class StepCounter extends AppCompatActivity implements SensorEventListener {

    //Declaration of the required objects

    private TextView step;
    private Sensor step_counter;
    private SensorManager sensorManager;
    private int value = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout);

        //Initialising the Sensor Manager object

        sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);

        //Initialising Step Counter Sensor object

        assert sensorManager != null; //Prevent Null Pointer Exception for sensorManager

        step_counter = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

        //Register Sensor Listener for the Step Counter

        sensorManager.registerListener(this, step_counter, SensorManager.SENSOR_DELAY_FASTEST);
        //Sensor Sampling Frequency set to High ('SENSOR_DELAY_FASTEST')

        //Link/Assign Text View objects to XML

        step = (TextView)findViewById(R.id.step);




    }

    // Methods implemented from SensorEventListener class

    @Override
    public void onSensorChanged(SensorEvent event) {

        step.setText("Step Count: " + value++ );

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //Not in Use for this activity
    }

}
