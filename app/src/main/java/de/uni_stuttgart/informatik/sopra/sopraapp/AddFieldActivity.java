package de.uni_stuttgart.informatik.sopra.sopraapp;

import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import org.osmdroid.util.GeoPoint;

import de.uni_stuttgart.informatik.sopra.sopraapp.R;
import de.uni_stuttgart.informatik.sopra.sopraapp.UI.MapFragment;
import de.uni_stuttgart.informatik.sopra.sopraapp.data.MYLocationListener;

public class AddFieldActivity extends AppCompatActivity implements MapFragment.OnCompleteListener {

    MapFragment mapFragment;
    MYLocationListener myLocationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_field);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.title_activity_add_field);

        //floating action button listener stuff
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
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

        mapFragment = (MapFragment) getSupportFragmentManager().findFragmentById(R.id.map_fragment_add);

    }


    @Override
    public void onMapFragmentComplete() {
        myLocationListener = new MYLocationListener();
        myLocationListener.initializeLocationManager(this, mapFragment);
        Location location = myLocationListener.getLocation();
        if (location != null) {
            GeoPoint userLocation = new GeoPoint(myLocationListener.getLocation().getLatitude(),
                    myLocationListener.getLocation().getLongitude());
            mapFragment.getMapViewHandler().animateTo(userLocation);
            mapFragment.setCurrLocMarker(userLocation);
        } else {
            Toast.makeText(this, getResources().getString(R.string.toastmsg_nolocation), Toast.LENGTH_SHORT).show();
        }
    }
}
