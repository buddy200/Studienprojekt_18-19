package de.uni_stuttgart.informatik.sopra.sopraapp;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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

import de.uni_stuttgart.informatik.sopra.sopraapp.UI.BottomSheetDetailDialogFragment;
import de.uni_stuttgart.informatik.sopra.sopraapp.UI.MapFragment;
import de.uni_stuttgart.informatik.sopra.sopraapp.data.AgrarianField;
import de.uni_stuttgart.informatik.sopra.sopraapp.data.CornerPoint;
import de.uni_stuttgart.informatik.sopra.sopraapp.Util.MYLocationListener;
import de.uni_stuttgart.informatik.sopra.sopraapp.data.Field;
import de.uni_stuttgart.informatik.sopra.sopraapp.data.FieldTypes.FieldType;

public class AddFieldActivity extends AppCompatActivity implements FragmentInteractionListener<Object> {
    private static final String TAG = "AddFieldActivity";

    MapFragment mapFragment;
    MYLocationListener myLocationListener;
    List<CornerPoint> cornerPoints;

    Field fieldToAddFinal;

    BottomSheetDetailDialogFragment bottomSheetDialog;

    ArrayList<GeoPoint> listGeoPoints = new ArrayList<>();
    ArrayList<CornerPoint> listCornerPoints = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

        //override onClick for toolbar arrow button,
        //to trigger going back to parent activty
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        final MenuItem menuItem = toolbar.getMenu().add(Menu.NONE, 1000, Menu.NONE, R.string.done);
        menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        mapFragment = (MapFragment) getSupportFragmentManager().findFragmentById(R.id.map_fragment_add);

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
    public void addPoint(Location location) {
        Polyline p = new Polyline();
        GeoPoint g = new GeoPoint(location.getLatitude(), location.getLongitude());
        listGeoPoints.add(g);
        listCornerPoints.add(new CornerPoint(g.getLatitude(), g.getLongitude()));
        p.setPoints(listGeoPoints);
        mapFragment.getMapViewHandler().getMapView().getOverlayManager().add(p);

        if (listCornerPoints.size() > 2) {
            enoughPoints = true;
        }

    }

    private void onDoneButtonClick() {
        if (enoughPoints) {
            myLocationListener.setFollow(false);

            //add the new field
            if (fieldToAddFinal == null) {
                myLocationListener.setFollow(false);
                Field fieldToAdd = new AgrarianField(getApplicationContext(), listCornerPoints);
                bottomSheetDialog = (BottomSheetDetailDialogFragment) BottomSheetDetailDialogFragment.newInstance(fieldToAdd, true);
                bottomSheetDialog.show(getSupportFragmentManager(), "EditView");

                //done with editing - back to main
            } else {
                myLocationListener.setFollow(false);
                Intent dataBack = new Intent();

                Log.e("is Null?", String.valueOf(fieldToAddFinal.getBundle() == null));
                //getIntent().putExtra("field", fieldToAdd.getBundle());
                dataBack.putExtra("field", fieldToAddFinal);
                setResult(RESULT_OK, dataBack);
                this.finish();
            }
        }else {
            Toast.makeText(getApplicationContext(), R.string.toastmsg_not_enough_points, Toast.LENGTH_LONG).show();
        }
    }

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
                        mapFragment.getMapViewHandler().addField(fieldToAddFinal);
                        mapFragment.getMapViewHandler().invalidateMap();
                        break;

                    case "startEdit":
                        mapFragment.getMapViewHandler().deleteFieldFromOverlay((Field) data);
                        BottomSheetDetailDialogFragment.newInstance(((Field) data), true).show(this.getSupportFragmentManager(), "EditField");
                        break;
                }

        }
    }

    public void onMapFragmentComplete() {
        myLocationListener = new MYLocationListener();
        myLocationListener.setFollow(true);
        myLocationListener.initializeLocationManager(this, mapFragment);
        Location location = myLocationListener.getLocation();
        if (location != null) {
            GeoPoint userLocation = new GeoPoint(myLocationListener.getLocation().getLatitude(),
                    myLocationListener.getLocation().getLongitude());
            mapFragment.getMapViewHandler().animateAndZoomTo(userLocation);
            mapFragment.setCurrLocMarker(userLocation);
        } else {
            Toast.makeText(this, getResources().getString(R.string.toastmsg_nolocation), Toast.LENGTH_SHORT).show();
        }
    }

}
