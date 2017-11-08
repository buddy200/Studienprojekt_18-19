package de.uni_stuttgart.informatik.sopra.sopraapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.osmdroid.views.MapView;

/**
 * Created by gin on 03.11.17.
 */

public class MapFragment extends Fragment {
    private static final String TAG = "MapFragment";

    ConstraintLayout cl;
    ItemListDialogFragment list;
    MenuFragment menu;

    boolean permissionGranted = true;

    //Please keep this method order!
    //Fragment lifecycle is in the same order
    //https://developer.android.com/images/fragment_lifecycle.png
    //Every Method not overriding some fragment lifecycle stuff below

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        list =  ItemListDialogFragment.newInstance(5);


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
            MapView m = new MapView(getContext());
            cl.addView(m);

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
                return;

        }

    }

}
