package de.uni_stuttgart.informatik.sopra.sopraapp;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
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
import de.uni_stuttgart.informatik.sopra.sopraapp.data.Field;
import de.uni_stuttgart.informatik.sopra.sopraapp.Util.MYLocationListener;
import de.uni_stuttgart.informatik.sopra.sopraapp.data.FieldTypes.FieldType;

public class AddFieldActivity extends AppCompatActivity implements MapFragment.OnCompleteListener, BottomSheetDetailDialogFragment.OnButtonInteraction {
    private static final String TAG = "AddFieldActivity";

    MapFragment mapFragment;
    MYLocationListener myLocationListener;
    Field createField;
    List<CornerPoint> cornerPoints;

    AgrarianField fieldToAdd;
    AgrarianField fieldToAddFinal;
    Bundle resultBundle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_field);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.title_activity_add_field);

        cornerPoints = new ArrayList<>();

        //floating action button listener stuff
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Location location = myLocationListener.getLocation();
                if(location != null){

                    addPoint(location);

                    Snackbar.make(view, "Point at " +
                            location.getLatitude() + " " + location.getLongitude() + " added", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();

                }else {
                    Snackbar.make(view, R.string.toastmsg_nolocation, Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
            }
        });


        //back button on toolbar implementation
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

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

    BottomSheetDetailDialogFragment test;
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_menu_done:
                if(fieldToAdd != null){
                    myLocationListener.setFollow(false);
                    if(fieldToAddFinal == null) {
                        myLocationListener.setFollow(false);
                        test = (BottomSheetDetailDialogFragment) BottomSheetDetailDialogFragment.newInstance(fieldToAdd, true);
                        test.show(getSupportFragmentManager(), "EditView");
                    }else{
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

                break;

        }
        return true;
    }

    ArrayList<GeoPoint> list = new ArrayList<>();
    ArrayList<CornerPoint> listC = new ArrayList<>();
    public void addPoint(Location location){
        Polyline p = new Polyline();
        list.add(new GeoPoint(location.getLatitude(), location.getLongitude()));
        listC.add(new CornerPoint(location.getLatitude(), location.getLongitude()));
        p.setPoints(list);
        mapFragment.getMapViewHandler().getMapView().getOverlayManager().add(p);

        if(listC.size() > 2){
            fieldToAdd = new AgrarianField(this.getApplicationContext(), listC);
            fieldToAdd.setName("NOT FINISHED");
        }

    }


    @Override
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

    @Override
    public void onFinishButtonInteraction() {
        fieldToAddFinal = new AgrarianField(getApplicationContext(), listC);
        resultBundle = test.getData();
        fieldToAddFinal.setName(resultBundle.getString("name"));
        fieldToAddFinal.setType((FieldType) resultBundle.getSerializable("type"));
        if(resultBundle.getString("address") != null){
            fieldToAddFinal.setCounty(resultBundle.getString("address"));
        }else{
            fieldToAddFinal.setAutomaticCounty();
        }
        fieldToAddFinal.setOwner(resultBundle.getString("ownerOrEvaluator"));

        mapFragment.getMapViewHandler().addField(fieldToAddFinal);
        test.dismiss();

        Log.e(TAG, "button");
    }
}
