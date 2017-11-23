package de.uni_stuttgart.informatik.sopra.sopraapp.data;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;


import de.uni_stuttgart.informatik.sopra.sopraapp.UI.MapFragment;

/**
 * Created by larsb on 21.11.2017.
 */

public class MYLocationListener implements LocationListener {

    private LocationManager locationManager;
    private String locationProvider;
    private Location location;
    private Context context;
    private MapFragment mapFragment;
    private Criteria criteria;
    private Thread thread;
    private boolean locationUpdateEnable = true;

    /**
     *
     */
    public MYLocationListener() {

    }

    public void initializeLocationManager(Context context, MapFragment mapFragment) {
        this.mapFragment = mapFragment;
        this.context = context;
        //get the location manager
        this.locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);


        //define the location manager criteria
        criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);


    }


    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public Location getLocation() {

        this.locationProvider = locationManager.getBestProvider(criteria, true);

        if (locationManager.isProviderEnabled(locationProvider)) {
            //Check Permission for fine location
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                mapFragment.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
            locationManager.requestLocationUpdates(locationProvider, 100, 10, this);
            location = locationManager.getLastKnownLocation(locationProvider);
            return location;
        } else {
            return location;
        }
    }
}


