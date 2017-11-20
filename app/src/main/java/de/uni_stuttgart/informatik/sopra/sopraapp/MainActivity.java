package de.uni_stuttgart.informatik.sopra.sopraapp;

import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import java.util.ArrayList;

import de.uni_stuttgart.informatik.sopra.sopraapp.UI.BottomSheetDetailDialogFragment;
import de.uni_stuttgart.informatik.sopra.sopraapp.UI.ItemListDialogFragment;
import de.uni_stuttgart.informatik.sopra.sopraapp.UI.MapFragment;
import de.uni_stuttgart.informatik.sopra.sopraapp.UI.MenuFragment;
import de.uni_stuttgart.informatik.sopra.sopraapp.data.Field;

/**
 * sopra_priv
 * Created by Felix B on 03.11.17.
 * Mail: felix.burk@gmail.com
 */


public class MainActivity extends FragmentActivity
        implements MenuFragment.OnMenuFragmentInteractionListener, ItemListDialogFragment.Listener, MapFragment.OnCompleteListener {
    private static final String TAG = "MainActivity";


    MapFragment mapFragment;
    ArrayList<Field> testData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mapFragment = (MapFragment) getSupportFragmentManager().findFragmentById(R.id.map_fragment);

        testData = GlobalConstants.fieldTest(100,4);
    }

    //handle menu buttons interactions
    @Override
    public void onListButtonInteraction() {
        ItemListDialogFragment.newInstance(testData).show(getSupportFragmentManager(), "FieldList");
        //BottomSheetDetailDialogFragment.newInstance(testData.get(0)).show(this.getSupportFragmentManager(), "Field");

    }

    @Override
    public void onLocationButtonInteraction() {
        Location loc = mapFragment.getGPSPostion();
        if(loc != null) {
            mapFragment.animateToPosition(loc.getLatitude(), loc.getLongitude());
        }
                else{
                    Log.e("GPSErr", "Keine Location");
            }

     //   mapFragment.animateToPosition(GlobalConstants.SOME_POINT.getLatitude(), GlobalConstants.SOME_POINT.getLongitude());
    }

    //handle item clicked interaction from ItemListDialogFragment
    @Override
    public void onListItemClicked(int position) {
        Log.d("FieldList", "clicked on position: " + testData.get(position).getName());
        mapFragment.animateToPosition(testData.get(position).getCornerPoints().get(0).getWGS().getLatitude(),
                testData.get(position).getCornerPoints().get(0).getWGS().getLongitude());
    }


    //add received data to the mapFragment
    @Override
    public void onMapFragmentComplete() {
        mapFragment.addData(testData);

    }
}
