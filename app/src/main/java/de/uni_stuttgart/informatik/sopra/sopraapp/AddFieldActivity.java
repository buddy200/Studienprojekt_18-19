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
import android.widget.TextView;
import android.widget.Toast;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.Polyline;

import java.util.ArrayList;
import java.util.List;

import de.uni_stuttgart.informatik.sopra.sopraapp.UI.BottomSheets.BSDetailDialogEditFragment;
import de.uni_stuttgart.informatik.sopra.sopraapp.UI.BottomSheets.BSEditHandler;
import de.uni_stuttgart.informatik.sopra.sopraapp.UI.BottomSheets.BottomSheetDetailDialogFragment;
import de.uni_stuttgart.informatik.sopra.sopraapp.UI.Map.MapFragment;
import de.uni_stuttgart.informatik.sopra.sopraapp.UI.Map.MapViewHandler;
import de.uni_stuttgart.informatik.sopra.sopraapp.Util.MYLocationListener;
import de.uni_stuttgart.informatik.sopra.sopraapp.data.AgrarianField;
import de.uni_stuttgart.informatik.sopra.sopraapp.data.CornerPoint;
import de.uni_stuttgart.informatik.sopra.sopraapp.data.DamageField;
import de.uni_stuttgart.informatik.sopra.sopraapp.data.Field;
import de.uni_stuttgart.informatik.sopra.sopraapp.data.managers.AppDataManager;

/**
 * sopra_priv
 * Created by Felix B on 10.11.17.
 * Mail: felix.burk@gmail.com
 *
 * this activity lets users add fields depending
 * on their position
 */
public class AddFieldActivity extends AppCompatActivity implements FragmentInteractionListener<Object>, AppDataManager.DataChangeListener {
    private static final String TAG = "AddFieldActivity";

    MapFragment mapFragment;
    MYLocationListener myLocationListener;
    List<CornerPoint> cornerPoints;

    Field fieldToAddFinal;

    BSDetailDialogEditFragment bottomSheetDialog;

    ArrayList<GeoPoint> listGeoPoints = new ArrayList<>();
    ArrayList<CornerPoint> listCornerPoints = new ArrayList<>();

    boolean isDmgField = false;
    Field parentField;

    private MapViewHandler mMapViewHandler;
    private AppDataManager dataManager;

    private TextView fabLabel;
    private MenuItem menuItemDone;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.activity_add_field);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
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
        fabLabel = (TextView) findViewById(R.id.fab_label);

        //override onClick for toolbar arrow button,
        //to trigger going back to parent activty
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                generateSaveDialog().show();
            }
        });

        menuItemDone = toolbar.getMenu().add(Menu.NONE, 1000, Menu.NONE, R.string.done);
        menuItemDone.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        invalidateOptionsMenu();

        mapFragment = (MapFragment) getSupportFragmentManager().findFragmentById(R.id.map_fragment_add);

        dataManager = new AppDataManager(this);
        mMapViewHandler = new MapViewHandler(this, dataManager, mapFragment);

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
        dataManager.readData();
    }

    @Override
    public void onStop(){
        super.onStop();
        mMapViewHandler.destroy();
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

        fabLabel.setText("You need " + String.valueOf(3-listCornerPoints.size()) + " more");

        if (listCornerPoints.size() > 2) {
            enoughPoints = true;
            fabLabel.setVisibility(View.INVISIBLE);
            menuItemDone.setVisible(true);
            menuItemDone.setTitle("Enough Points");

        }

    }

    private AlertDialog.Builder generateSaveDialog() {
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
        builder.setMessage("Are you sure you want to go back? fields not visible on screen will be dismissed").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener);

        return builder;
    }

    /**
     * Handle clicks for the done button on top
     * depending on the current state of our field to add
     */
    private void onDoneButtonClick() {
        if (enoughPoints) {
            //myLocationListener.setFollow(false);
            Field fieldToAdd = new AgrarianField(getApplicationContext(), listCornerPoints);
            if(isDmgField) {
                fieldToAdd = new DamageField(getApplicationContext(), listCornerPoints);
            }
            bottomSheetDialog = (BSDetailDialogEditFragment) BSDetailDialogEditFragment.newInstance();
            BSEditHandler handler = new BSEditHandler(fieldToAdd, dataManager, bottomSheetDialog);
            bottomSheetDialog.setPresenter(handler);
            bottomSheetDialog.show(getSupportFragmentManager(), "EditView");

            enoughPoints = false;
            listGeoPoints.clear();
            listCornerPoints.clear();
            fabLabel.setVisibility(View.VISIBLE);
            fabLabel.setText("Add a Corner Point at your current position");

        }else {
            Toast.makeText(getApplicationContext(), R.string.toastmsg_not_enough_points, Toast.LENGTH_LONG).show();
        }
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
        }
    }

    /**
     * as soon as the map fragment is completely initialized and displayed
     * try to get the current user location
     */
    public void onMapFragmentComplete() {

    }

    @Override
    public void onDataChange() {
        Log.e("dATA CHANGE", "HAA");
        if(mMapViewHandler != null){
            mMapViewHandler.reload();
        }
    }

}
