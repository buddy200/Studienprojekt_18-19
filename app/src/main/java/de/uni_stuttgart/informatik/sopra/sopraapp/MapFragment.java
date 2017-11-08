package de.uni_stuttgart.informatik.sopra.sopraapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

/**
 * sopra_priv
 * Created by Felix B on 03.11.17.
 * Mail: felix.burk@gmail.com
 */

public class MapFragment extends Fragment {
    private static final String TAG = "MapFragment";

    ConstraintLayout cl;
    ItemListDialogFragment list;
    MenuFragment menu;
    MapView map;
    IMapController mapController;


    boolean permissionGranted = true;

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
            map = new MapView(getContext());
            map.setTileSource(TileSourceFactory.MAPNIK);
            //map.setBuiltInZoomControls(true);
            map.setMultiTouchControls(true);
            mapController = map.getController();
            mapController.setZoom(20);
            //default center is campus uni stuttgart
            mapController.setCenter(new GeoPoint( 48.745424, 9.106488 ));
            cl.addView(map);

        }else {
            TextView v = new TextView(getContext());
            v.setText("Permission not granted - sorry");
            cl.addView(v);
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
        mapController.animateTo(startPoint);
    }

}
