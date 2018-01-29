package de.uni_stuttgart.informatik.sopra.fieldManager;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.location.Location;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
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

import de.uni_stuttgart.informatik.sopra.fieldManager.UI.BottomSheets.BSDetailDialogDmgField;
import de.uni_stuttgart.informatik.sopra.fieldManager.UI.BottomSheets.BSDetailDialogEditAgrField;
import de.uni_stuttgart.informatik.sopra.fieldManager.UI.BottomSheets.BSDetailDialogEditDmgField;
import de.uni_stuttgart.informatik.sopra.fieldManager.UI.BottomSheets.BSEditHandler;
import de.uni_stuttgart.informatik.sopra.fieldManager.UI.BottomSheets.BottomSheetAddPhoto;
import de.uni_stuttgart.informatik.sopra.fieldManager.UI.BottomSheets.BSDetailDialogAgrField;
import de.uni_stuttgart.informatik.sopra.fieldManager.UI.BottomSheets.ItemListDialogFragment;
import de.uni_stuttgart.informatik.sopra.fieldManager.UI.LoginDialog;
import de.uni_stuttgart.informatik.sopra.fieldManager.UI.Map.MapFragment;
import de.uni_stuttgart.informatik.sopra.fieldManager.UI.Map.MapViewHandler;
import de.uni_stuttgart.informatik.sopra.fieldManager.data.AgrarianField;
import de.uni_stuttgart.informatik.sopra.fieldManager.data.managers.AppDataManager;
import de.uni_stuttgart.informatik.sopra.fieldManager.data.DamageField;
import de.uni_stuttgart.informatik.sopra.fieldManager.data.Field;
import de.uni_stuttgart.informatik.sopra.fieldManager.Util.MYLocationListener;
import de.uni_stuttgart.informatik.sopra.fieldManager.UI.TutorialOverlays;

/**
 * sopra_priv
 * Created by Felix B on 03.11.17.
 * Mail: felix.burk@gmail.com
 * <p>
 * the main activity for our app, everything starts here
 * the class is listening for every Interaction of its fragments
 */

/*
Copyright (c) 2018 Lars Buttgereit, Felix Burk, Christian Bräuner

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
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

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        //builder.detectFileUriExposure();

        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Toolbar toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        mapFragment = (MapFragment) getSupportFragmentManager().findFragmentById(R.id.map_fragment);

        dataManager = AppDataManager.getInstance(this);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        if (prefs.getFloat("Longitude", 0) != 0) {
            GeoPoint geoPoint = new GeoPoint((double) prefs.getFloat("Latitude", 0), (double) prefs.getFloat("Longitude", 0));
            GlobalConstants.setLastLocationOnMap(geoPoint);
        }
        mapHandler = new MapViewHandler(this, dataManager, mapFragment);
        mapFragment.setPresenter(mapHandler);
    }

    @Override
    public void onResume() {
        super.onResume();
        mapHandler.reload();
    }


    @Override
    public void onStart() {
        super.onStart();

        loadFieldData();

        //check if user already used the app - if not show login dialog
        boolean previouslyStarted = prefs.getBoolean(this.getResources().getString(R.string.pref_previously_started), false);
        if (!previouslyStarted) {
            LoginDialog loginDialog = new LoginDialog(this, dataManager);
            loginDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    invalidateOptionsMenu();
                }
            });
            loginDialog.show();

        } else {
            boolean adm = prefs.getBoolean(this.getString(R.string.pref_admin_bool), false);
            GlobalConstants.isAdmin = adm;
            this.invalidateOptionsMenu();
        }
        mapHandler.reload();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapHandler.saveMapCenter();
        mapHandler.destroy();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dataManager.dbClose();
    }

    /**
     * receive messages from fragments
     *
     * @param Tag    of the fragment
     * @param action the fragment performs
     * @param data   data the fragment sends
     */
    @Override
    public void onFragmentMessage(String Tag, @NonNull String action, @Nullable Object data) {
        Log.d(TAG, "MSG TAG: " + Tag + " ACTION: " + action);
        switch (Tag) {
            case "MapViewHandler":
                switch (action) {
                    case "singleTabOnPoly":
                        animateMapToFieldWithBS((Field) data);
                }
            case "ItemListDialogFragment":
                switch (action) {
                    case "itemClick":
                        animateMapToFieldWithBS((Field) data);
                        break;
                }
                break;
            case "BottomSheetDetail":
                switch (action) {
                    case "startEdit":
                        //TODO
                        if (data instanceof AgrarianField) {
                            BSDetailDialogEditAgrField bsDetail = BSDetailDialogEditAgrField.newInstance();
                            new BSEditHandler((Field) data, dataManager, bsDetail);
                            bsDetail.show(getSupportFragmentManager(), "test");
                        } else {
                            BSDetailDialogEditDmgField bsDetail = BSDetailDialogEditDmgField.newInstance();
                            new BSEditHandler((Field) data, dataManager, bsDetail);
                            bsDetail.show(getSupportFragmentManager(), "test");
                        }
                        break;
                    case "addDmgField":
                        Intent i = new Intent(this, AddFieldActivity.class);
                        i.putExtra("parentField", ((Field) data).getID());
                        startActivityForResult(i, 2403);
                        break;

                }
                break;
            case "BSDetailDialogEditFragmentDamageField":
                switch (action) {
                    case "addPhoto":
                        BottomSheetAddPhoto bottomSheetAddPhoto = BottomSheetAddPhoto.newInstance();
                        new BSEditHandler((Field) data, dataManager, bottomSheetAddPhoto);
                        bottomSheetAddPhoto.show(getSupportFragmentManager(), "test");
                        break;
                }
                break;
            case "LoginDialog":
                switch (action) {
                    case "complete":
                        onStart();
                        break;
                }
                break;

        }

    }

    /**
     * animates the center of the map to the centroid of a field
     * we need some offset because of the visible bottom sheet
     *
     * @param field
     */
    private void animateMapToFieldWithBS(Field field) {
        //offset get center on top of BottomSheet
        double offset = 0.0007;
        mapHandler.animateAndZoomTo((field).getCentroid().getLatitude() - offset,
                (field).getCentroid().getLongitude());

        if (field instanceof DamageField) {
            BSDetailDialogDmgField bs = BSDetailDialogDmgField.newInstance();
            new BSEditHandler(field, dataManager, bs);
            bs.show(this.getSupportFragmentManager(), "DetailField");
        } else if (field instanceof AgrarianField) {
            BSDetailDialogAgrField bs = BSDetailDialogAgrField.newInstance();
            new BSEditHandler(field, dataManager, bs);
            bs.show(this.getSupportFragmentManager(), "DetailField");
        }

    }

    /**
     * get the context, this is necessary for FieldState enums
     * without context it's not possible to get Enum names from strings.xml
     *
     * @return
     */
    public static Context getmContext() {
        return mContext;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        setUpSearchMenuItem(menu);

        MenuItem addFieldMenuItem = menu.findItem(R.id.action_toolbar_add);
        if (!GlobalConstants.isAdmin) {
            addFieldMenuItem.setVisible(false);
        } else {
            addFieldMenuItem.setVisible(true);
        }
        MenuItem username = menu.findItem(R.id.action_toolbar_username);
        username.setTitle("Logged in as: " + prefs.getString("usr", "not logged in"));

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_toolbar_menu, menu);

        return true;
    }

    /**
     * sets all listeners for the SearchView implementation in the action bar
     *
     * @param menu
     */
    private View expandSearch;

    private void setUpSearchMenuItem(Menu menu) {
        final String searchFor[] = {
                MainActivity.getmContext().getResources().getString(R.string.search_all),
                MainActivity.getmContext().getResources().getString(R.string.dialogItem_Name),
                MainActivity.getmContext().getResources().getString(R.string.dialogItem_Owner),
                MainActivity.getmContext().getResources().getString(R.string.dialogItem_Type),
                MainActivity.getmContext().getResources().getString(R.string.dialogItem_Date),
                MainActivity.getmContext().getResources().getString(R.string.dialogItem_evaluator)
        };

        final MenuItem searchItem = menu.findItem(R.id.action_toolbar_search);
        expandSearch = findViewById(R.id.search_bar);
        final Spinner searchTypeSpinner = findViewById(R.id.spinner_search);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getBaseContext(),
                android.R.layout.simple_spinner_item, searchFor);
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
                if (searchTypeSpinner.getSelectedItem().toString().equals(searchFor[0])) {
                    ItemListDialogFragment.newInstance(dataManager.containsField(dataManager.searchAll(query))).show(getSupportFragmentManager(), "FieldList");
                } else if (searchTypeSpinner.getSelectedItem().toString().equals(searchFor[1])) {
                    ItemListDialogFragment.newInstance(dataManager.containsField(dataManager.searchName(query))).show(getSupportFragmentManager(), "FieldList");
                } else if (searchTypeSpinner.getSelectedItem().toString().equals(searchFor[2])) {
                    ItemListDialogFragment.newInstance(dataManager.containsField(dataManager.searchOwner(query))).show(getSupportFragmentManager(), "FieldList");
                } else if (searchTypeSpinner.getSelectedItem().toString().equals(searchFor[3])) {
                    ItemListDialogFragment.newInstance(dataManager.containsField(dataManager.searchState(query))).show(getSupportFragmentManager(), "FieldList");
                } else if (searchTypeSpinner.getSelectedItem().toString().equals(searchFor[4])) {
                    ItemListDialogFragment.newInstance(dataManager.containsField(dataManager.searchDate(query))).show(getSupportFragmentManager(), "FieldList");
                } else if (searchTypeSpinner.getSelectedItem().toString().equals(searchFor[5])) {
                    ItemListDialogFragment.newInstance(dataManager.containsField(dataManager.searchEvaluator(query))).show(getSupportFragmentManager(), "FieldList");
                }

                searchItem.collapseActionView();
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

        switch (id) {
            case R.id.action_toolbar_add:
                GlobalConstants.setLastLocationOnMap(new GeoPoint(mapHandler.getMap().getMapCenter().getLatitude(), mapHandler.getMap().getMapCenter().getLongitude()));
                Intent i = new Intent(this, AddFieldActivity.class);
                startActivityForResult(i, 2404);
                break;
            case R.id.action_toolbar_list:
                ItemListDialogFragment.newInstance(dataManager.getAllFields()).show(getSupportFragmentManager(), "FieldList");
                break;
            case R.id.action_toolbar_location:
                MYLocationListener myLocationListener = new MYLocationListener();
                myLocationListener.initializeLocationManager(this, mapHandler);
                Location location = myLocationListener.getLocation();
                if (location != null) {
                    mapHandler.animateAndZoomTo(location.getLatitude(), location.getLongitude());
                    mapHandler.setCurrLocMarker(location.getLatitude(), location.getLongitude());
                    GlobalConstants.setLastLocationOnMap(new GeoPoint(location));
                } else {
                    Toast.makeText(this, getResources().getString(R.string.toastmsg_nolocation), Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.action_toolbar_logout:
                //DATABASE IS NOT CHANGED AFTER LOGOUT
                generateLogoutDialog().show();
                break;
            case R.id.action_toolbar_tutorial:
                if(GlobalConstants.isAdmin){
                    new TutorialOverlays().mainTutorial(this);
                }else {
                    new TutorialOverlays().mainTutorialNoAdmin(this);
                }
                break;
        }


        return super.onOptionsItemSelected(item);
    }

    private AlertDialog.Builder generateLogoutDialog() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        //delete all shared preferences - DATABASE IS NOT CHANGED
                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getmContext());
                        SharedPreferences.Editor edit = prefs.edit();
                        edit.clear();
                        edit.apply();
                        dataManager.clearAllMaps();
                        mapHandler.reload();
                        onStart();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        dialog.dismiss();
                        break;
                }
            }
        };
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getResources().getString(R.string.logout_message)).setPositiveButton(getResources().getString(R.string.word_yes), dialogClickListener)
                .setNegativeButton(getResources().getString(R.string.word_no), dialogClickListener);

        return builder;
    }

    @Override
    public void onDataChange() {
        if (mapHandler != null) {
            mapHandler.reload();
        }
    }

    private void loadFieldData() {
        String name = prefs.getString("usr", "");
        if (!(prefs.getBoolean("adm", false))) {
            if ((!name.equals(""))) {
                dataManager.loadUserFields(name);
            } else {
                dataManager.clearAllMaps();
            }
        } else {
            dataManager.readData();
        }
        mapHandler.reload();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
