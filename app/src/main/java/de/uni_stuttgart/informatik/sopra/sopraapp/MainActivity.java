package de.uni_stuttgart.informatik.sopra.sopraapp;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import de.uni_stuttgart.informatik.sopra.sopraapp.UI.ItemListDialogFragment;
import de.uni_stuttgart.informatik.sopra.sopraapp.UI.MapFragment;
import de.uni_stuttgart.informatik.sopra.sopraapp.UI.MenuFragment;

/**
 * sopra_priv
 * Created by Felix B on 03.11.17.
 * Mail: felix.burk@gmail.com
 */


public class MainActivity extends FragmentActivity
        implements MenuFragment.OnFragmentInteractionListener, ItemListDialogFragment.Listener {
    private static final String TAG = "MainActivity";


    MapFragment map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        map = (MapFragment) getSupportFragmentManager().findFragmentById(R.id.map_fragment);
    }

    @Override
    public void onAddButtonInteraction() {
        ItemListDialogFragment.newInstance(30).show(getSupportFragmentManager(), "dialog");
    }

    @Override
    public void onLocationButtonInteraction() {
        map.animateToPosition(GlobalConstants.SOME_POINT.getLatitude(), GlobalConstants.SOME_POINT.getLongitude());
    }

    @Override
    public void onItemClicked(int position) {

    }
}
