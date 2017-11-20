package de.uni_stuttgart.informatik.sopra.sopraapp.UI;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.osmdroid.util.GeoPoint;

import java.util.List;

import de.uni_stuttgart.informatik.sopra.sopraapp.R;
import de.uni_stuttgart.informatik.sopra.sopraapp.data.Field;

/**
 * sopra_priv
 * Created by Felix B on 03.11.17.
 * Mail: felix.burk@gmail.com
 */

public class MapFragment extends Fragment implements LocationListener {
    private static final String TAG = "MapFragment";

    private ConstraintLayout cl;
    private MapViewHandler mapViewHandler;

    private boolean permissionGranted = true;
    private boolean permissionGPSGranted = true;
    private LocationManager locationManager;



    //Please keep this method order!
    //Fragment lifecycle is in the same order
    //https://developer.android.com/images/fragment_lifecycle.png
    //Every Method not overriding some fragment lifecycle stuff below

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        if(ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            permissionGranted = false;
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        }

    }

    //Weird Error without overwriting this method -FB
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map, container, false);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        cl = getView().findViewById(R.id.cl);
    }

    @Override
    public void onStart(){
        super.onStart();

        if(permissionGranted){

            mapViewHandler = new MapViewHandler(getContext());
            cl.addView(mapViewHandler.getMapView());
            mListener.onMapFragmentComplete();

        }else {
            TextView v = new TextView(getContext());
            v.setText("Permission not granted - sorry");
            cl.addView(v);
        }
    }

    private OnCompleteListener mListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try{
            this.mListener = (OnCompleteListener) context;
        }catch(final ClassCastException e){
            throw new ClassCastException(context.toString() + " must implement OnCompleteListener");
        }
    }


    @Override
    public void onActivityCreated(Bundle savedInstance){
        super.onActivityCreated(savedInstance);

    }

    //Methods not for fragment lifecycle

    //Handle requested Permissions
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode){
            case 0:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    permissionGranted = true;

                    //okay this is invoking the usual fragment lifecycle, not good -FB
                    //but it works really well actually
                    // TODO change this!
                    this.onStop();
                    this.onStart();
                } else {

                    //no permission - no map
                }
                break;
            case 1:
                //GPS Permission check
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    permissionGPSGranted = true;
                } else {
                    permissionGPSGranted = false;
                }
                break;
            default:
                Log.e(TAG, "requested permission not handled");
        }

    }

    /**
     * animate to given position
     * @param lat
     * @param lon
     */
    public void animateToPosition(double lat, double lon){
        GeoPoint startPoint = new GeoPoint(lat, lon);
        if(mapViewHandler != null) {
            mapViewHandler.animateTo(startPoint);
        }
    }

    public void addData(List<Field> fields){
        mapViewHandler.addFields(fields);
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

    public interface OnCompleteListener {
        void onMapFragmentComplete();
    }


    public Location getGPSPostion() {
        Location location = null;
        try{
            locationManager = (LocationManager)getContext().getSystemService(Context.LOCATION_SERVICE);
            if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

                }
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 0, this);
                Log.e("GPSerr", "Hallo3");
                if(locationManager != null){
                    Log.e("GPSerr", "Hallo");
                    }
                    location =  locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if (location == null){
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                }


            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return location;
    }
}
