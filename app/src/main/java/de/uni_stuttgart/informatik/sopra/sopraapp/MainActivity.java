package de.uni_stuttgart.informatik.sopra.sopraapp;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class MainActivity extends FragmentActivity implements MenuFragment.OnFragmentInteractionListener, ItemListDialogFragment.Listener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);
    }

    @Override
    public void onAddButtonInteraction() {
        ItemListDialogFragment.newInstance(30).show(getSupportFragmentManager(), "dialog");
    }

    @Override
    public void onItemClicked(int position) {

    }
}
