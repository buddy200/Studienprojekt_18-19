package de.uni_stuttgart.informatik.sopra.sopraapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.support.v7.widget.SearchView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import org.osmdroid.util.GeoPoint;


import java.io.File;

import de.uni_stuttgart.informatik.sopra.sopraapp.UI.BottomSheets.BSDetailDialogEditFragment;
import de.uni_stuttgart.informatik.sopra.sopraapp.UI.BottomSheets.BSEditHandler;
import de.uni_stuttgart.informatik.sopra.sopraapp.UI.BottomSheets.BottomSheetDetailDialogFragment;
import de.uni_stuttgart.informatik.sopra.sopraapp.UI.BottomSheets.ItemListDialogFragment;
import de.uni_stuttgart.informatik.sopra.sopraapp.UI.LoginDialog;
import de.uni_stuttgart.informatik.sopra.sopraapp.UI.Map.MapFragment;
import de.uni_stuttgart.informatik.sopra.sopraapp.UI.Map.MapViewHandler;
import de.uni_stuttgart.informatik.sopra.sopraapp.Util.PhotoManager;
import de.uni_stuttgart.informatik.sopra.sopraapp.Util.SearchUtil;
import de.uni_stuttgart.informatik.sopra.sopraapp.data.managers.AppDataManager;
import de.uni_stuttgart.informatik.sopra.sopraapp.data.DamageField;
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

public class MainActivity extends AppCompatActivity implements FragmentInteractionListener<Object>, AppDataManager.DataChangeListener {

    private static final String TAG = "MainActivity";

    //i know this is bad, but there is no other way to get the context inside our AgrarianFieldType enum.. -D
    private static Context mContext;

    private MapFragment mapFragment;
    private MapViewHandler mapHandler;

    private AppDataManager dataManager;

    private SharedPreferences prefs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;

        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Test");

        mapFragment = (MapFragment) getSupportFragmentManager().findFragmentById(R.id.map_fragment);

        dataManager = new AppDataManager(this);

        mapHandler = new MapViewHandler(this, dataManager, mapFragment);

        mapFragment.setPresenter(mapHandler);

        prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

    }

    @Override
    public void onResume(){
        super.onResume();
    }


    @Override
    public void onStart(){
        super.onStart();
        dataManager.readData();

        //check if user already used the app - if not show login dialog
        boolean previouslyStarted = prefs.getBoolean(getString(R.string.pref_previously_started), false);
        if(!previouslyStarted) {
            new LoginDialog(this).show();
            this.invalidateOptionsMenu();
        }
    }

    @Override
    public void onStop(){
        super.onStop();
        dataManager.saveData();
        mapHandler.destroy();
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
            case "MapViewHandler":
                switch (action){
                    case "singleTabOnPoly":
                        animateMapToFieldWithBS((Field) data);
                }
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
                        //TODO
                        BSDetailDialogEditFragment bsDetail = BSDetailDialogEditFragment.newInstance();
                        new BSEditHandler((Field) data, dataManager, bsDetail);
                        bsDetail.show(getSupportFragmentManager(),"test" );
                        break;
                    case "addDmgField":
                        Intent i = new Intent(this, AddFieldActivity.class);
                        i.putExtra("parentField", (Field) data);
                        startActivityForResult(i, 2403);
                        break;

                }
                break;
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
        mapHandler.animateAndZoomTo((field).getCentroid().getLatitude()-offset,
                (field).getCentroid().getLongitude());
        BottomSheetDetailDialogFragment bs = BottomSheetDetailDialogFragment.newInstance();
        new BSEditHandler(field, dataManager, bs);
        bs.show(this.getSupportFragmentManager(), "DetailField");
    }


    /**
     * create a list of fields to display in the activity
     * containing AgrarienFields and their Damage Fields
     * @param list
     * @return
     */
    /*
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
    } */

    /**
     * get the context, this is necessary for FieldState enums
     * without context it's not possible to get Enum names from strings.xml
     * @return
     */
    public static Context getmContext(){
        return mContext;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu){
        setUpSearchMenuItem(menu);
        MenuItem username = menu.findItem(R.id.action_username);
        username.setTitle("Logged in as: " + prefs.getString("usr", "not logged in"));

        return true;
    }

    private View expandSearch;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_toolbar_menu, menu);

        return true;
    }

    /**
     * sets all listeners for the SearchView implementation in the action bar
     * @param menu
     */
    private void setUpSearchMenuItem(Menu menu) {
        final MenuItem searchItem = menu.findItem(R.id.action_toolbar_search);
        expandSearch = findViewById(R.id.search_bar);
        final Spinner searchTypeSpinner = findViewById(R.id.spinner_search);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getBaseContext(),
                android.R.layout.simple_spinner_item, SearchUtil.getSearchFor());
        searchTypeSpinner.setAdapter(adapter);
        searchTypeSpinner.setSelection(0);

        adapter.notifyDataSetChanged();
        searchItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                expandSearch.setVisibility(View.VISIBLE);
                expandSearch.bringToFront();
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                expandSearch.setVisibility(View.INVISIBLE);
                return true;
            }
        });

        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                SearchUtil.searchForType(dataManager.getFields(), query, searchTypeSpinner.getSelectedItem().toString());
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //do nothing
                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id){
            case R.id.action_toolbar_add:
                GlobalConstants.setLastLocationOnMap(new GeoPoint(mapHandler.getMap().getMapCenter().getLatitude(), mapHandler.getMap().getMapCenter().getLongitude()));
                Intent i = new Intent(this, AddFieldActivity.class);
                startActivityForResult(i, 2404);
                break;
            case R.id.action_toolbar_list:
                ItemListDialogFragment.newInstance(dataManager.getFields()).show(getSupportFragmentManager(), "FieldList");
                break;
            case R.id.action_toolbar_location:
                MYLocationListener myLocationListener = new MYLocationListener();
                myLocationListener.initializeLocationManager(this, mapHandler);
                Location location = myLocationListener.getLocation();
                if (location != null) {
                    mapHandler.animateAndZoomTo(location.getLatitude(), location.getLongitude());
                    mapHandler.setCurrLocMarker(location.getLatitude(), location.getLongitude());
                    GlobalConstants.setLastLocationOnMap(new GeoPoint(location));
                }
                else{
                    Toast.makeText(this, getResources().getString(R.string.toastmsg_nolocation), Toast.LENGTH_SHORT).show();
                }
                break;
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDataChange() {
        if(mapHandler != null){
            mapHandler.reload();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        String temp = "0";
        Field field = null;
       if (resultCode != RESULT_OK &&  requestCode == PhotoManager.REQUEST_TAKE_PHOTO) {

           for (int i = 0; i < dataManager.getFields().size(); i++) {
               field = dataManager.getFields().get(i);

               if (field instanceof DamageField)  {
                   if(((DamageField) field).getpaths() != null &&((DamageField) field).getpaths().size() > 0){
                   String path = (((DamageField) field).getpaths().get(((DamageField) field).getpaths().size() - 1)).getImage_path();
                   if (temp.compareTo(path) > 0) {
                       dataManager.removeField(field);
                       temp = path;
                   }
               }}
           }
           File f = new File(temp);
           f.delete();
           ((DamageField) field).getpaths().remove(((DamageField) field).getpaths().size() - 1);
           dataManager.addAgrarianField(field);
           dataManager.saveData();
       }
    }
}
