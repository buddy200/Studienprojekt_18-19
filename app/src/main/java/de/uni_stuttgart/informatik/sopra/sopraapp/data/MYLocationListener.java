package de.uni_stuttgart.informatik.sopra.sopraapp.data;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
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

    /**
     *
     */
    public MYLocationListener() {

    }

    /**
     * Get the actual Location from GPS and Wifi. Please Null-Check the Location
     * @return actual Location
     */
    public Location getGPSPosition(Context context, MapFragment mapFragment) {
        LocationManager locationManager;
        Location location = null;
        try {
            locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER )|| locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                //Check Permission for fine location
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
                    mapFragment.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                }
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 0, this);
                if (locationManager != null) {
                }
                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                    location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    // if no GPS signal available, get location from Network Location
                    if (location == null) {
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return location;
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
}


