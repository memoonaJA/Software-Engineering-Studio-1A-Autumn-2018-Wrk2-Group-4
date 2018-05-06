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
    private Button clearBtn;
    private Button resetBtn;
    private TextView time;
    private TextView pace;
    private static final LatLng UTS = new LatLng(-33.884196, 151.201009);
    static public final int REQUEST_CODE = 1;
    private Chronometer timer;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest mLocationRequest;
    private LocationCallback mLocationCallback;
    private LatLng myLocation;
    private Location lastLoc;
    private GoogleApiClient googleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        //searches for the map and initialises
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //initialising textview and buttons
        distanceTxt = findViewById(R.id.textView1);
        clearBtn = findViewById(R.id.button1);
        resetBtn = findViewById(R.id.button2);
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

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();

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
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(UTS, 18.0f)); //sets initial camera position and boom
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
        timer.start();
        startLocationUpdates();
    }

    public void stopButton (View view) {
        timer.stop();
        stopLocationUpdates();
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
        //calculates the distance of the line in metres
        Location previousPoint = new Location("");
        Location point = new Location("");
        float sum = 0;
        float pace1 = 0;
        long elapsed = SystemClock.elapsedRealtime()-timer.getBase();
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
        pace1 = (elapsed)/(sum)/100;
        pace.setText(getString(R.string.text1, pace1));
        distanceTxt.setText(getString(R.string.text, sum));

    }
}