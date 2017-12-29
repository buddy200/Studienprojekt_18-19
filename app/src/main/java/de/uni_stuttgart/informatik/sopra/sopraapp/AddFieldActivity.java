package de.uni_stuttgart.informatik.sopra.sopraapp;

import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.Polyline;

import java.util.ArrayList;
import java.util.List;

import de.uni_stuttgart.informatik.sopra.sopraapp.UI.BottomSheets.BSDetailDialogEditFragment;
import de.uni_stuttgart.informatik.sopra.sopraapp.UI.BottomSheets.BottomSheetDetailDialogFragment;
import de.uni_stuttgart.informatik.sopra.sopraapp.UI.Map.MapFragment;
import de.uni_stuttgart.informatik.sopra.sopraapp.UI.Map.MapViewHandler;
import de.uni_stuttgart.informatik.sopra.sopraapp.Util.MYLocationListener;
import de.uni_stuttgart.informatik.sopra.sopraapp.data.CornerPoint;
import de.uni_stuttgart.informatik.sopra.sopraapp.data.Field;

/**
 * sopra_priv
 * Created by Felix B on 10.11.17.
 * Mail: felix.burk@gmail.com
 *
 * this activity lets users add fields depending
 * on their position
 */
public class AddFieldActivity extends AppCompatActivity implements FragmentInteractionListener<Object> {
    private static final String TAG = "AddFieldActivity";

    MapFragment mapFragment;
    MYLocationListener myLocationListener;
    List<CornerPoint> cornerPoints;

    Field fieldToAddFinal;

    BottomSheetDetailDialogFragment bottomSheetDialog;

    ArrayList<GeoPoint> listGeoPoints = new ArrayList<>();
    ArrayList<CornerPoint> listCornerPoints = new ArrayList<>();

    boolean isDmgField = false;
    Field parentField;

    private MapViewHandler mMapViewHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.activity_add_field);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.title_activity_add_field);

        cornerPoints = new ArrayList<>();

        //back button on toolbar implementation
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //floating action button listener stuff
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onFabClick(view);
            }
        });


        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        onBackPressed();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        dialog.dismiss();
                        break;
                }
            }
        };
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to go back and loose the created data?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener);

        //override onClick for toolbar arrow button,
        //to trigger going back to parent activty
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder.show();
            }
        });

        final MenuItem menuItem = toolbar.getMenu().add(Menu.NONE, 1000, Menu.NONE, R.string.done);
        menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        mapFragment = (MapFragment) getSupportFragmentManager().findFragmentById(R.id.map_fragment_add);

        mMapViewHandler = new MapViewHandler(this, null, mapFragment);

        mapFragment.setPresenter(mMapViewHandler);

    }

    @Override
    public void onStart() {
        super.onStart();

        parentField = (Field) getIntent().getSerializableExtra("parentField");
        if (parentField != null) {
            //we add a dmg field!
            isDmgField = true;
            mMapViewHandler.addField(parentField);
            getSupportActionBar().setTitle(R.string.title_activity_add_fieldDmg);
        }


        myLocationListener = new MYLocationListener();
        myLocationListener.initializeLocationManager(this, mMapViewHandler);
        myLocationListener.setFollow(true);
        Location location = myLocationListener.getLocation();
        if (location != null) {
            mMapViewHandler.animateAndZoomTo(myLocationListener.getLocation().getLatitude(),
                    myLocationListener.getLocation().getLongitude());
            mMapViewHandler.setCurrLocMarker(myLocationListener.getLocation().getLatitude(),
                    myLocationListener.getLocation().getLongitude());
        } else {
            Toast.makeText(this, getResources().getString(R.string.toastmsg_nolocation), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onStop(){
        super.onStop();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_add_field, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_menu_done:
                onDoneButtonClick();
                break;
        }
        return true;
    }


    private boolean enoughPoints = false;

    /**
     * adds a new Point to the Field
     * @param location
     */
    public void addPoint(Location location) {
        Polyline p = new Polyline();
        GeoPoint g = new GeoPoint(location.getLatitude(), location.getLongitude());
        listGeoPoints.add(g);
        listCornerPoints.add(new CornerPoint(g.getLatitude(), g.getLongitude()));
        p.setPoints(listGeoPoints);
        mMapViewHandler.addPolyline(p);

        if (listCornerPoints.size() > 2) {
            enoughPoints = true;
        }

    }

    /**
     * Handle clicks for the done button on top
     * depending on the current state of our field to add
     */
    private void onDoneButtonClick() {
        /*if (enoughPoints) {
            myLocationListener.setFollow(false);

            //add the new field
            if (fieldToAddFinal == null) {
                myLocationListener.setFollow(false);
                Field fieldToAdd = new AgrarianField(getApplicationContext(), listCornerPoints);
                if(isDmgField) {
                    fieldToAdd = new DamageField(getApplicationContext(), listCornerPoints);
                }
                bottomSheetDialog = (BottomSheetDetailDialogFragment) BSDetailDialogEditFragment.newInstance(fieldToAdd);
                bottomSheetDialog.show(getSupportFragmentManager(), "EditView");

             //done with editing - back to main
            } else {
                myLocationListener.setFollow(false);
                Intent dataBack = new Intent();

                if(parentField != null){
                    dataBack.putExtra("parentField", parentField);
                }
                dataBack.putExtra("field", fieldToAddFinal);
                setResult(RESULT_OK, dataBack);
                this.finish();
            }
        }else {
            Toast.makeText(getApplicationContext(), R.string.toastmsg_not_enough_points, Toast.LENGTH_LONG).show();
        }*/
    }

    /**
     * handle Floating Action Button clicks
     * depending on the current state of our field to add
     * @param view
     */
    private void onFabClick(View view) {
        if(fieldToAddFinal == null){
            Location location = myLocationListener.getLocation();
            if(location != null){
                addPoint(location);

                Snackbar.make(view, "Point at " +
                        location.getLatitude() + " " + location.getLongitude() + " added", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
            }else {
                Snackbar.make(view, R.string.toastmsg_nolocation, Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        }else {
            Snackbar.make(view, R.string.toastmsg_points_already_added, Snackbar.LENGTH_LONG).setAction("Action", null).show();
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
                        onMapFragmentComplete();
                        break;
                }
                break;
            case "BottomSheetDetail":
                switch (action){
                    case "finishEdit":
                        fieldToAddFinal = (Field) data;
                        mMapViewHandler.addField(fieldToAddFinal);
                        mMapViewHandler.invalidateMap();
                        break;

                    case "startEdit":
                        mMapViewHandler.deleteFieldFromOverlay((Field) data);
                        BSDetailDialogEditFragment.newInstance().show(this.getSupportFragmentManager(), "EditField");
                        break;
                }
                break;
            case "BSDetailDialogEditFragment":
                switch (action) {
                    case "finishEdit":
                        fieldToAddFinal = (Field) data;
                        mMapViewHandler.addField(fieldToAddFinal);
                        mMapViewHandler.invalidateMap();
                        break;
                }
                break;
        }
    }

    /**
     * as soon as the map fragment is completely initialized and displayed
     * try to get the current user location
     */
    public void onMapFragmentComplete() {

    }

}
