package de.uni_stuttgart.informatik.sopra.sopraapp;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;
import java.util.Iterator;

import de.uni_stuttgart.informatik.sopra.sopraapp.UI.BottomSheetDetailDialogFragment;
import de.uni_stuttgart.informatik.sopra.sopraapp.UI.ItemListDialogFragment;
import de.uni_stuttgart.informatik.sopra.sopraapp.UI.MapFragment;
import de.uni_stuttgart.informatik.sopra.sopraapp.UI.MenuFragment;
import de.uni_stuttgart.informatik.sopra.sopraapp.data.Field;
import de.uni_stuttgart.informatik.sopra.sopraapp.data.MYLocationListener;

/**
 * sopra_priv
 * Created by Felix B on 03.11.17.
 * Mail: felix.burk@gmail.com
 */


public class MainActivity extends FragmentActivity
        implements MenuFragment.OnMenuFragmentInteractionListener, ItemListDialogFragment.Listener, MapFragment.OnCompleteListener {
    private static final String TAG = "MainActivity";
    private MYLocationListener myLocationListener = new MYLocationListener();


    MapFragment mapFragment;
    ArrayList<Field> testData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mapFragment = (MapFragment) getSupportFragmentManager().findFragmentById(R.id.map_fragment);

        testData = GlobalConstants.fieldTest(100, 50, this);


    }

    //handle menu buttons interactions
    @Override
    public void onListButtonInteraction() {
        ItemListDialogFragment.newInstance(testData, false).show(getSupportFragmentManager(), "FieldList");
        //BottomSheetDetailDialogFragment.newInstance(testData.get(0)).show(this.getSupportFragmentManager(), "AgrarianField");

    }

    @Override
    public void onLocationButtonInteraction() {
     Location location = myLocationListener.getLocation();
     if (location != null) {
         mapFragment.animateToPosition(location.getLatitude(), location.getLongitude());
         mapFragment.setCurrLocMarker(new GeoPoint(location.getLatitude(), location.getLongitude()));
     }
     else{
         Toast.makeText(this, getResources().getString(R.string.toastmsg_nolocation), Toast.LENGTH_SHORT).show();
     }

    }

    @Override
    public void onAddButtonInteraction(){
        //start Add Field Activity
        Intent i = new Intent(this, AddFieldActivity.class);
        startActivity(i);
    }

    @Override
    public void onInfoButtonInteraction(){
        //Todo
    }

    int i = 0;
    ArrayList<Field> searchData;
    @Override
    public void onSearchButtonClicked(String input) {
        Log.e(TAG, "Search for: " + input);

        // copy testData in search data list
        // this copy is comparable to shallow copy in the C language
        searchData = new ArrayList<>(testData);
        //ArrayList<Field> searchList = new ArrayList<>();

        /**
         * not optimal and dirty way of searching
         * but it's fast to implement and probably enough for our use case
         * - ah and this is case sensitive right now... TODO
         */
        Iterator<Field> iter = searchData.iterator();
        while(iter.hasNext()){
            Field f = iter.next();
            Bundle b = f.getBundle();
            if(!f.name.contains(input)){
                //f is type agrarian
                if(b.containsKey("state")){
                    //search for states and owners
                    if(! b.getSerializable("state").toString().contains(input)){
                        if( ! b.getString("owner").contains(input)){
                            iter.remove();
                        }
                    }

                }
                //f is type damage field
                if(b.containsKey("evaluator")){
                    //search for evaluators
                    if(! b.getString("evaluator").contains(input)){
                        iter.remove();
                    }
                }
            }
        }

        if(searchData.size() != 0){
            ItemListDialogFragment.newInstance(searchData, true).show(getSupportFragmentManager(), "SearchList");
        }else{
            Toast.makeText(this, getResources().getString(R.string.toastmsg_nothing_found), Toast.LENGTH_SHORT).show();
        }

    }

    //handle item clicked interaction from ItemListDialogFragment
    @Override
    public void onListItemClicked(int position, boolean search) {
        //offset to show centroid of polygon completely while bottom sheet is visible
        double offset = 0.001;
        if(search && searchData != null){
            mapFragment.animateToPosition(searchData.get(position).getCentroid().getLatitude()-offset,
                    testData.get(position).getCentroid().getLongitude());
            BottomSheetDetailDialogFragment.newInstance(searchData.get(position)).show(this.getSupportFragmentManager(), "Field");
        }else{
            mapFragment.animateToPosition(testData.get(position).getCentroid().getLatitude()-offset,
                    testData.get(position).getCentroid().getLongitude());
            BottomSheetDetailDialogFragment.newInstance(testData.get(position)).show(this.getSupportFragmentManager(), "Field");
        }

    }


    //add received data to the mapFragment
    @Override
    public void onMapFragmentComplete() {
        mapFragment.getMapViewHandler().addFields(testData);
        mapFragment.getMapViewHandler().addField(GlobalConstants.damageFieldTest(this));
        myLocationListener.initializeLocationManager(this, mapFragment);


    }
}
