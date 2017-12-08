package de.uni_stuttgart.informatik.sopra.sopraapp;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
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

import de.uni_stuttgart.informatik.sopra.sopraapp.UI.BSDetailDialogEditFragment;
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
 *
 * the main activity for our app, everything starts here
 * the class is listening for every Interaction of its fragments
 */

public class MainActivity extends FragmentActivity implements FragmentInteractionListener<Object> {

    private static final String TAG = "MainActivity";
    private MYLocationListener myLocationListener = new MYLocationListener();

    //i know this is bad, but there is no other way to get the context inside our AgrarianFieldType enum.. -D
    private static Context mContext;

    private MapFragment mapFragment;
    private ArrayList<Field> dataFromFields;
    private ExportImportFromFile writerReader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;

        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mapFragment = (MapFragment) getSupportFragmentManager().findFragmentById(R.id.map_fragment);

        writerReader = new ExportImportFromFile(this);
        dataFromFields = writerReader.readFields();

    }

    @Override
    public void onStop(){
        super.onStop();
        writerReader.WriteFields(dataFromFields);

    }

    /**
     * receives results from other activities
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //2404 is an added agrarian field
        if (resultCode == RESULT_OK && requestCode == 2404) {
            if (data != null) {
                AgrarianField newData = (AgrarianField) data.getSerializableExtra("field");
                dataFromFields.add(newData);
                mapFragment.getMapViewHandler().addField(newData);

            }
        }
        //2403 is an added damage field
        if (resultCode == RESULT_OK && requestCode == 2403) {
            if (data != null){
                DamageField newDataDmg = (DamageField) data.getSerializableExtra("field");
                AgrarianField parent = (AgrarianField) data.getSerializableExtra("parentField");

                parent.addContainedDamageField(newDataDmg);
                dataFromFields.add(parent);
                mapFragment.getMapViewHandler().invalidateMap();
            }
        }
    }

    /**
     * receive messages from fragments
     * @param Tag of the fragment
     * @param action the fragment performs
     * @param data data the fragment sends
     */
    @Override
    public void onFragmentMessage(String Tag, @NonNull String action, @Nullable Object data) {
        Log.d(TAG , "MSG TAG: " + Tag + " ACTION: " + action);
        switch (Tag){
            case "MapFragment":
                switch (action){
                    case "complete":
                        mapFragment.getMapViewHandler().addFields(dataFromFields);
                        myLocationListener.initializeLocationManager(this, mapFragment);
                        break;
                }
                break;
            case "MapViewHandler":
                switch (action){
                    case "singleTabOnPoly":
                        animateMapToFieldWithBS((Field) data);
                }
            case "MenuFragment":
                switch (action){
                    case "listButton":
                        ItemListDialogFragment.newInstance(createList(dataFromFields)).show(getSupportFragmentManager(), "FieldList");
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
                        animateMapToFieldWithBS((Field) data);
                        break;
                }
                break;
            case "BottomSheetDetail":
                switch (action){
                    case "startEdit":
                        mapFragment.getMapViewHandler().deleteFieldFromOverlay((Field) data);
                        if(data instanceof AgrarianField){
                            dataFromFields.remove((Field) data);
                            for(DamageField d : ((AgrarianField) data).getContainedDamageFields()){
                                dataFromFields.remove(d);
                                mapFragment.getMapViewHandler().deleteFieldFromOverlay(d);
                            }
                        }else if(data instanceof DamageField){
                            for(Field field : dataFromFields){
                                if(((AgrarianField) field).getContainedDamageFields().contains((DamageField) data)){
                                    ((AgrarianField) field).getContainedDamageFields().remove((DamageField) data);
                                }
                            }
                        }
                        BSDetailDialogEditFragment.newInstance(((Field) data)).show(this.getSupportFragmentManager(), "EditField");
                        //TODO
                        break;
                    case "addDmgField":
                        dataFromFields.remove((Field) data);
                        Intent i = new Intent(this, AddFieldActivity.class);
                        i.putExtra("parentField", (Field) data);
                        startActivityForResult(i, 2403);
                        break;

                }
                break;
            case "BSDetailDialogEditFragment":
                switch (action){
                    case "finishEdit":
                        dataFromFields.add((Field) data);
                        mapFragment.getMapViewHandler().addField((Field) data);
                        mapFragment.getMapViewHandler().invalidateMap();
                        break;
                    case "delete":
                        //we don't need to delete the field in the map this already happend in start edit
                        mapFragment.getMapViewHandler().invalidateMap();
                        break;
                }

        }

    }

    /**
     * animates the center of the map to the centroid of a field
     * we need some offset because of the visible bottom sheet
     * @param field
     */
    private void animateMapToFieldWithBS(Field field){
        //offset get center on top of BottomSheet
        double offset = 0.0007;

        mapFragment.animateToPosition((field).getCentroid().getLatitude()-offset,
                (field).getCentroid().getLongitude());
        BottomSheetDetailDialogFragment.newInstance((field)).show(this.getSupportFragmentManager(), "DetailField");
    }

    /**
     * search implementation
     * @param input
     */
    public void onSearchButtonClicked(String input) {
        Log.e(TAG, "Search for: " + input);

        // copy dataFromFields in search data listGeoPoints
        // we need a deep copy - because fields contain other fields
        ArrayList<Field> searchData = new ArrayList<>(dataFromFields);
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
            ItemListDialogFragment.newInstance(resultData).show(getSupportFragmentManager(), "SearchList" );
        }else{
            Toast.makeText(this, getResources().getString(R.string.toastmsg_nothing_found), Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * create a list of fields to display in the activity
     * containing AgrarienFields and their Damage Fields
     * @param list
     * @return
     */
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

    /**
     * get the context, this is necessary for FieldState enums
     * without context it's not possible to get Enum names from strings.xml
     * @return
     */
    public static Context getmContext(){
        return mContext;
    }

}
