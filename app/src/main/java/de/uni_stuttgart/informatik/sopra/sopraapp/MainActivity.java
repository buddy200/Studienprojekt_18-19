package de.uni_stuttgart.informatik.sopra.sopraapp;

import android.content.Context;
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
import de.uni_stuttgart.informatik.sopra.sopraapp.data.AgrarianField;
import de.uni_stuttgart.informatik.sopra.sopraapp.data.DamageField;
import de.uni_stuttgart.informatik.sopra.sopraapp.data.Field;
import de.uni_stuttgart.informatik.sopra.sopraapp.data.MYLocationListener;

/**
 * sopra_priv
 * Created by Felix B on 03.11.17.
 * Mail: felix.burk@gmail.com
 */


public class MainActivity extends FragmentActivity
        implements MenuFragment.OnMenuFragmentInteractionListener, ItemListDialogFragment.Listener,
        MapFragment.OnCompleteListener, BottomSheetDetailDialogFragment.OnButtonInteraction {

    private static final String TAG = "MainActivity";
    private MYLocationListener myLocationListener = new MYLocationListener();

    //i know this is bad, but there is no other way to get the context inside our AgrarianFieldType enum.. -D
    private static Context mContext;

    MapFragment mapFragment;
    ArrayList<Field> testData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;

        setContentView(R.layout.activity_main);

        mapFragment = (MapFragment) getSupportFragmentManager().findFragmentById(R.id.map_fragment);

        testData = GlobalConstants.fieldTest(100, 5, this);


    }

    //handle menu buttons interactions
    @Override
    public void onListButtonInteraction() {
        ItemListDialogFragment.newInstance(createList(testData), false).show(getSupportFragmentManager(), "FieldList");
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


    @Override
    public void onSearchButtonClicked(String input) {
        Log.e(TAG, "Search for: " + input);

        // copy testData in search data list
        // we need a deep copy - because fields contain other fields
        ArrayList<Field> searchData = new ArrayList<>(testData);
        ArrayList<Field> resultData = new ArrayList<>();

        /**
         * not optimal and dirty way of searching
         * but it's fast to implement and probably enough for our use case
         * - ah and this is case sensitive right now... TODO
         */
        Iterator<Field> iter = searchData.iterator();
        while(iter.hasNext()){
            Field f = iter.next();

            if(SearchUtil.matchesFieldSearch(f, input)){
                resultData.add(f);
            }

            if(f instanceof AgrarianField){
                for(DamageField dmg : ((AgrarianField)f).getContainedDamageFields()){
                    Log.e("DMG", dmg.getType().toString());
                    if(SearchUtil.matchesFieldSearch(dmg,input)){
                        resultData.add(dmg);
                    }
                }
            }

        }

        if(resultData.size() != 0){
            ItemListDialogFragment.newInstance(resultData, true).show(getSupportFragmentManager(), "SearchList" );
        }else{
            Toast.makeText(this, getResources().getString(R.string.toastmsg_nothing_found), Toast.LENGTH_SHORT).show();
        }

    }

    //handle item clicked interaction from ItemListDialogFragment
    @Override
    public void onListItemClicked(Field field) {
        //offset to show centroid of polygon completely while bottom sheet is visible
        double offset = 0.001;

        mapFragment.animateToPosition(field.getCentroid().getLatitude()-offset,
                field.getCentroid().getLongitude());
        BottomSheetDetailDialogFragment.newInstance(field, false).show(this.getSupportFragmentManager(), "SearchField");

    }


    //add received data to the mapFragment
    @Override
    public void onMapFragmentComplete() {
        mapFragment.getMapViewHandler().addFields(testData);
        //mapFragment.getMapViewHandler().addField(GlobalConstants.damageFieldTest(this));
        myLocationListener.initializeLocationManager(this, mapFragment);


    }

    @Override
    public void onButtonInteraction() {

    }

    private ArrayList<Field> createList(ArrayList<Field> list){
        ArrayList<Field> newList = new ArrayList<>();
        for(Field f : list){
            newList.add(f);
            if(f instanceof AgrarianField){
                for(DamageField dmg : ((AgrarianField)f).getContainedDamageFields()){
                    newList.add(dmg);
                }
            }
        }

        return newList;
    }

    public static Context getmContext(){
        return mContext;
    }
}
