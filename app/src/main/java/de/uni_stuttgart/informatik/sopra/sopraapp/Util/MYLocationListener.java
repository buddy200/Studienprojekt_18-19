package de.uni_stuttgart.informatik.sopra.sopraapp.Util;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;


import org.osmdroid.util.GeoPoint;

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

    public boolean follow = false;


    public MYLocationListener() {

    }

    public void initializeLocationManager(Context context, MapFragment mapFragment) {
        this.mapFragment = mapFragment;
        this.context = context;
        //get the location manager
        this.locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        try {
            locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                //Check Permission for fine location
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
                    mapFragment.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                }
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 0, this);
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 2000, 0, this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }




    }


    @Override
    public void onLocationChanged(Location location) {
        if(location != null){
            if(follow){
                mapFragment.animateToPosition(location.getLatitude(), location.getLongitude());
            }
            mapFragment.setCurrLocMarker(new GeoPoint(location.getLatitude(), location.getLongitude()));
        }
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
        try {

            if (locationManager != null) {
            }
            //Check Permission for fine location
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                mapFragment.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                // if no GPS signal available, get location from Network Location
                if (location == null) {
                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return location;
    }

    public void setFollow(boolean b){
        follow = b;
    }
}



