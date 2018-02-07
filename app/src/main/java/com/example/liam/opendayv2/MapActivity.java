package com.example.liam.opendayv2;

import android.*;
import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.gcm.Task;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Tasks;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Toast.makeText(this, "Map is Initialized", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onMapReady: Map Ready");
        mMap = googleMap;

        if (mPermisonsGranted) {
            getloc();

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                return;
           }
            mMap.setMyLocationEnabled(true);
        }
    }
    private static final String TAG = "MapActivity";

    //Set Access as strings to save time in dev
    private static final String FINE = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LocationPermReq = 1234;
    //Set Location permissions to false to begin
    private Boolean mPermisonsGranted = false;
    //Google map variable
    private GoogleMap mMap;
    //Location
    private FusedLocationProviderClient mFusedLocationProviderClient;
    //Standard Zoom Var
    private static final float DEFAULT_ZOOM = 15;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        GetLocPermision();
    }
    //Finds Users location
    private void getloc()
    {
        Log.d(TAG, "getloc: Finding Location");

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        try
        {
            if(mPermisonsGranted)
            {
                final com.google.android.gms.tasks.Task<Location> location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull com.google.android.gms.tasks.Task task) {
                        if(task.isSuccessful())
                        {
                            Log.d(TAG, "onComplete: Location Found");
                            Location currentlocation = (Location) task.getResult();

                            movemap(new LatLng(currentlocation.getLatitude(), currentlocation.getLongitude()), DEFAULT_ZOOM);
                        }
                        else
                        {
                            Toast.makeText(MapActivity.this, "Cant Find Location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }
        catch (SecurityException e)
        {
            Log.e(TAG, "getloc: ERROR: " + e.getMessage() );
        }
    }

    //Moves map
    private void movemap(LatLng latLng,float zoom)
    {
        Log.d(TAG, "movemap: Moving to current location");
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }

    //Starts the map
    private void initMap()
    {
        Log.d(TAG, "initMap: Initializing Map");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(MapActivity.this);
    }

    //sets permissions
    private void GetLocPermision() {
        Log.d(TAG, "GetLocPermision: Getting Permissions");
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        //Checks Permissions
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                FINE) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    COARSE) == PackageManager.PERMISSION_GRANTED) {
                mPermisonsGranted = true;
                initMap();
            } else {
                ActivityCompat.requestPermissions(this, permissions,
                        LocationPermReq);
            }
        } else {
            ActivityCompat.requestPermissions(this, permissions,
                    LocationPermReq);
        }

    }

    //checks permissions
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mPermisonsGranted = false;

        switch (requestCode) {
            case LocationPermReq: {
                //Checks for permisons
                if (grantResults.length > 0)
                    for (int i = 0; i < grantResults.length; i++) {
                        //If false returns false
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            Log.d(TAG, "onRequestPermissionsResult: Permision Failed");
                            mPermisonsGranted = false;
                            return;
                        }
                    }
                //if all true start map
                Log.d(TAG, "onRequestPermissionsResult: Granted");
                mPermisonsGranted = true;
                initMap();
            }
        }
    }
}

