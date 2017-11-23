package de.uni_stuttgart.informatik.sopra.sopraapp;

import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;

import de.uni_stuttgart.informatik.sopra.sopraapp.UI.BottomSheetDetailDialogFragment;
import de.uni_stuttgart.informatik.sopra.sopraapp.UI.ItemListDialogFragment;
import de.uni_stuttgart.informatik.sopra.sopraapp.UI.MapFragment;
import de.uni_stuttgart.informatik.sopra.sopraapp.UI.MenuFragment;
import de.uni_stuttgart.informatik.sopra.sopraapp.data.ArgrarianField;
import de.uni_stuttgart.informatik.sopra.sopraapp.data.MYLocationListener;

/**
 * sopra_priv
 * Created by Felix B on 03.11.17.
 * Mail: felix.burk@gmail.com
 */


public class MainActivity extends FragmentActivity
        implements MenuFragment.OnMenuFragmentInteractionListener, ItemListDialogFragment.Listener, MapFragment.OnCompleteListener {
    private static final String TAG = "MainActivity";
    private MYLocationListener myLocationListener = new MYLocationListener();


    MapFragment mapFragment;
    ArrayList<ArgrarianField> testData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mapFragment = (MapFragment) getSupportFragmentManager().findFragmentById(R.id.map_fragment);

        testData = GlobalConstants.fieldTest(100, 4, this);



    }

    //handle menu buttons interactions
    @Override
    public void onListButtonInteraction() {
        ItemListDialogFragment.newInstance(testData).show(getSupportFragmentManager(), "FieldList");
        //BottomSheetDetailDialogFragment.newInstance(testData.get(0)).show(this.getSupportFragmentManager(), "ArgrarianField");

    }

    @Override
    public void onLocationButtonInteraction() {
     Location location = myLocationListener.getLocation();
     if (location != null) {
         mapFragment.animateToPosition(location.getLatitude(), location.getLongitude());
         mapFragment.setCurrLocMatrker(new GeoPoint(location.getLatitude(), location.getLongitude()));
     }
     else{
         Toast.makeText(this, getResources().getString(R.string.toastmsg_nolocation), Toast.LENGTH_SHORT).show();
     }

    }

    //handle item clicked interaction from ItemListDialogFragment
    @Override
    public void onListItemClicked(int position) {
        mapFragment.animateToPosition(testData.get(position).getCornerPoints().get(0).getWGS().getLatitude(),
                testData.get(position).getCornerPoints().get(0).getWGS().getLongitude());
        BottomSheetDetailDialogFragment.newInstance(testData.get(position)).show(this.getSupportFragmentManager(), "ArgrarianField");
    }


    //add received data to the mapFragment
    @Override
    public void onMapFragmentComplete() {
        mapFragment.getMapViewHandler().addFields(testData);
        mapFragment.getMapViewHandler().addDamageField(GlobalConstants.damageFieldTest(this));
        myLocationListener.initializeLocationManager(this, mapFragment);


    }
}
