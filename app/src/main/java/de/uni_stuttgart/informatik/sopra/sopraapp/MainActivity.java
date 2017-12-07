package de.uni_stuttgart.informatik.sopra.sopraapp;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;
import java.util.Iterator;

import de.uni_stuttgart.informatik.sopra.sopraapp.UI.BottomSheetDetailDialogFragment;
import de.uni_stuttgart.informatik.sopra.sopraapp.UI.ItemListDialogFragment;
import de.uni_stuttgart.informatik.sopra.sopraapp.UI.MapFragment;
import de.uni_stuttgart.informatik.sopra.sopraapp.Util.SearchUtil;
import de.uni_stuttgart.informatik.sopra.sopraapp.data.AgrarianField;
import de.uni_stuttgart.informatik.sopra.sopraapp.data.DamageField;
import de.uni_stuttgart.informatik.sopra.sopraapp.data.ExportImportFromFile;
import de.uni_stuttgart.informatik.sopra.sopraapp.data.Field;
import de.uni_stuttgart.informatik.sopra.sopraapp.Util.MYLocationListener;

/**
 * sopra_priv
 * Created by Felix B on 03.11.17.
 * Mail: felix.burk@gmail.com
 */


public class MainActivity extends FragmentActivity implements FragmentInteractionListener<Object> {

    private static final String TAG = "MainActivity";
    private MYLocationListener myLocationListener = new MYLocationListener();

    //i know this is bad, but there is no other way to get the context inside our AgrarianFieldType enum.. -D
    private static Context mContext;

    MapFragment mapFragment;
    ArrayList<Field> testData;
    ExportImportFromFile writerReader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;

        setContentView(R.layout.activity_main);

        mapFragment = (MapFragment) getSupportFragmentManager().findFragmentById(R.id.map_fragment);

      //  testData = GlobalConstants.fieldTest(100,4,getmContext());
        writerReader = new ExportImportFromFile(this);
        testData = writerReader.readFields();

    }

    @Override
    public void onStop(){
        super.onStop();
     //   testData.clear();
        writerReader.WriteFields(testData);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 2404) {
            if (data != null) {
                AgrarianField newData = (AgrarianField) data.getSerializableExtra("field");
                testData.add(newData);
                mapFragment.getMapViewHandler().addField(newData);

            }
        }
        if (resultCode == RESULT_OK && requestCode == 2403) {
            if (data != null){
                DamageField newDataDmg = (DamageField) data.getSerializableExtra("field");
                testData.add(newDataDmg);
                mapFragment.getMapViewHandler().addField(newDataDmg);
            }
        }
    }

    @Override
    public void onFragmentMessage(String Tag, @NonNull String action, @Nullable Object data) {
        Log.d(TAG , "MSG TAG: " + Tag + " ACTION: " + action);
        switch (Tag){
            case "MapFragment":
                switch (action){
                    case "complete":
                        mapFragment.getMapViewHandler().addFields(testData);
                        //mapFragment.getMapViewHandler().addField(GlobalConstants.damageFieldTest(this));
                        myLocationListener.initializeLocationManager(this, mapFragment);
                        break;
                }
                break;
            case "MapViewHandler":
                switch (action){
                    case "singleTabOnPoly":
                        BottomSheetDetailDialogFragment.newInstance((Field) data, false).show(this.getSupportFragmentManager(), "DetailField");
                }
            case "MenuFragment":
                switch (action){
                    case "listButton":
                        Log.d("TEST", String.valueOf(testData.size()));
                        ItemListDialogFragment.newInstance(createList(testData), false).show(getSupportFragmentManager(), "FieldList");
                        break;
                    case "locButton":
                        Location location = myLocationListener.getLocation();
                        if (location != null) {
                            mapFragment.animateToPosition(location.getLatitude(), location.getLongitude());
                            mapFragment.setCurrLocMarker(new GeoPoint(location.getLatitude(), location.getLongitude()));
                        }
                        else{
                            Toast.makeText(this, getResources().getString(R.string.toastmsg_nolocation), Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case "addButton":
                        Intent i = new Intent(this, AddFieldActivity.class);
                        startActivityForResult(i, 2404);
                        break;
                    case "infoButton":
                        //TODO
                        break;
                    case "searchButton":
                        onSearchButtonClicked((String) data);
                        break;
                }

                break;
            case "ItemListDialogFragment":
                switch (action){
                    case "itemClick":
                        //offset to show centroid of polygon completely while bottom sheet is visible
                        double offset = 0.001;

                        mapFragment.animateToPosition(((Field) data).getCentroid().getLatitude()-offset,
                                ((Field)data).getCentroid().getLongitude());
                        BottomSheetDetailDialogFragment.newInstance(((Field) data), false).show(this.getSupportFragmentManager(), "DetailField");
                        break;
                }
                break;
            case "BottomSheetDetail":
                switch (action){
                    case "finishEdit":
                        testData.add((Field) data);
                        mapFragment.getMapViewHandler().addField((Field) data);
                        mapFragment.getMapViewHandler().invalidateMap();
                        break;

                    case "startEdit":
                        mapFragment.getMapViewHandler().deleteFieldFromOverlay((Field) data);
                        testData.remove((Field) data);
                        BottomSheetDetailDialogFragment.newInstance(((Field) data), true).show(this.getSupportFragmentManager(), "EditField");
                        //TODO
                        break;
                    case "addDmgField":
                        Intent i = new Intent(this, AddFieldActivity.class);
                        i.putExtra("field", (Field) data);
                        startActivityForResult(i, 2403);
                        break;

                }

        }

    }

    public void onSearchButtonClicked(String input) {
        Log.e(TAG, "Search for: " + input);

        // copy testData in search data listGeoPoints
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
