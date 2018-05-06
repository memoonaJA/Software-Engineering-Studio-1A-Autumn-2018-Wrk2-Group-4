package com.example.softwaregroup4.group4_fitnessapplication;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap map;
    private Polyline polyLine;
    private TextView distanceTxt;
    private Button startBtn;
    private Button stopBtn;
    private Button pauseBtn;
    private Button resumeBtn;
    private TextView time;
    private TextView pace;
    static public final int REQUEST_CODE = 1;
    private Chronometer timer;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest mLocationRequest;
    private LocationCallback mLocationCallback;
    private LatLng myLocation;
    private long timeWhenStop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        //searches for the map and initialises
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //initialising textview and buttons
        distanceTxt = findViewById(R.id.textView1);
        startBtn = findViewById(R.id.button1);
        stopBtn = findViewById(R.id.button2);
        pauseBtn = findViewById(R.id.button3);
        resumeBtn = findViewById(R.id.button4);
        pace = findViewById(R.id.textView2);
        timer = (findViewById(R.id.chronometer));

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        createLocationRequest();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    myLocation = new LatLng(location.getLatitude(), location.getLongitude());
                    updatePolyLine(myLocation);
                    calculateDistance(polyLine.getPoints());
                    map.animateCamera(CameraUpdateFactory.newLatLng(myLocation));

                }
            }
        };
    }
    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }


    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
    }
        mFusedLocationClient.requestLocationUpdates(mLocationRequest,
    mLocationCallback,Looper.myLooper());
}

    private void stopLocationUpdates() {
        mFusedLocationClient.removeLocationUpdates(mLocationCallback);
    }


    //when the map is ready this code runs
    @Override
    public void onMapReady(final GoogleMap googleMap) {
        map = googleMap;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mFusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                // Got last known location. In some rare situations this can be null.
                if (location != null) {
                    LatLng locat = new LatLng(location.getLatitude(), location.getLongitude());
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(locat, 18.0f)); //sets initial camera position
                }
            }
        });
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        map.setMyLocationEnabled(true);
      timer.setBase(SystemClock.elapsedRealtime());

       }

    public void startButton (View view) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
            mFusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    // Got last known location. In some rare situations this can be null.
                    if (location != null) {
                        createPolyline(location);
                    }
                }
            });
        startBtn.setClickable(false);
        timer.setBase(SystemClock.elapsedRealtime() + timeWhenStop);
        timer.start();
        startLocationUpdates();
        stopBtn.setVisibility(View.INVISIBLE);
        pauseBtn.setVisibility(View.VISIBLE);

    }

    public void stopButton (View view) {
        timeWhenStop = timer.getBase() - SystemClock.elapsedRealtime();
        timer.stop();
        stopLocationUpdates();
        startBtn.setVisibility(View.VISIBLE);
        resumeBtn.setVisibility(View.INVISIBLE);
        startBtn.setClickable(true);
        polyLine.remove();
        timer.setBase(SystemClock.elapsedRealtime());
        timeWhenStop = 0;
        calculateDistance(polyLine.getPoints());
        distanceTxt.setText(getString(R.string.text, 0f));
    }

    public void pauseButton (View view) {
        timeWhenStop = timer.getBase() - SystemClock.elapsedRealtime();
        timer.stop();
        stopLocationUpdates();
        pauseBtn.setVisibility(View.INVISIBLE);
        stopBtn.setVisibility(View.VISIBLE);
        startBtn.setVisibility(View.INVISIBLE);
        resumeBtn.setVisibility(View.VISIBLE);
        resumeBtn.setClickable(true);
    }

    public void resumeButton (View view) {
        startLocationUpdates();
        timer.setBase(SystemClock.elapsedRealtime() + timeWhenStop);
        timer.start();
        pauseBtn.setVisibility(View.VISIBLE);
        stopBtn.setVisibility(View.INVISIBLE);
        resumeBtn.setClickable(false);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_CODE) {
            if (permissions.length == 1 &&
                    permissions[0] == Manifest.permission.ACCESS_FINE_LOCATION &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //map.setMyLocationEnabled(true);
            } else {
                // Permission was denied. Display an error message.
            }
        }
    }

    private void createPolyline(Location location) {
        LatLng point = new LatLng(location.getLatitude(), location.getLongitude());
        polyLine = map.addPolyline(new PolylineOptions().add(point).width(6).color(Color.RED));
    }

    private void updatePolyLine(LatLng latLng) {
        if (!polyLine.getPoints().isEmpty()) {
            List<LatLng> newPath = polyLine.getPoints(); //a list of the current points in the line
            newPath.add(latLng); //adds a new point at the end of the list
            polyLine.setPoints(newPath); //sets the line to the new path
        }
    }


    private void calculateDistance(List<LatLng> points) {
        //calculates the distance of the line in metre  s
        Location previousPoint = new Location("");
        Location point = new Location("");
        float sum = 0;
        float pace1 = 0;
        long elapsed = (SystemClock.elapsedRealtime()-timeWhenStop);
     //   long seconds = (elapsed /1000)%60;
        float minutes = (elapsed/1000)/60;
        for (int i=0; i<points.size(); ++i) {
            point.setLatitude(points.get(i).latitude);
            point.setLongitude(points.get(i).longitude);
            if(i==0) {
                previousPoint.setLatitude(point.getLatitude());
                previousPoint.setLongitude(point.getLongitude());
                continue;
            }
            sum += point.distanceTo(previousPoint);
            previousPoint.setLatitude(point.getLatitude());
            previousPoint.setLongitude(point.getLongitude());
        }
        pace1 = ((minutes)/(sum/1000))/60;
        pace.setText(getString(R.string.text1, pace1));
        distanceTxt.setText(getString(R.string.text, sum));

    }
}