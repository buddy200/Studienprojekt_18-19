package de.uni_stuttgart.informatik.sopra.sopraapp;

import android.content.DialogInterface;
import android.content.Intent;
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

import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Polyline;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import de.uni_stuttgart.informatik.sopra.sopraapp.UI.BottomSheets.BSDetailDialogEditFragment;
import de.uni_stuttgart.informatik.sopra.sopraapp.UI.BottomSheets.BSEditHandler;
import de.uni_stuttgart.informatik.sopra.sopraapp.UI.Map.MapFragment;
import de.uni_stuttgart.informatik.sopra.sopraapp.UI.Map.MapViewHandler;
import de.uni_stuttgart.informatik.sopra.sopraapp.Util.MYLocationListener;
import de.uni_stuttgart.informatik.sopra.sopraapp.Util.PhotoManager;
import de.uni_stuttgart.informatik.sopra.sopraapp.data.AgrarianField;
import de.uni_stuttgart.informatik.sopra.sopraapp.data.CornerPoint;
import de.uni_stuttgart.informatik.sopra.sopraapp.data.DamageField;
import de.uni_stuttgart.informatik.sopra.sopraapp.data.Field;
import de.uni_stuttgart.informatik.sopra.sopraapp.data.managers.AppDataManager;

/**
 * sopra_priv
 * Created by Felix B on 10.11.17.
 * Mail: felix.burk@gmail.com
 * <p>
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

    private boolean enoughPoints = false;

    private Polyline p;

    private Vector<Double> lastVector;
    private Vector<Double> currentVector;
    private ArrayList<Vector<Double>> linesFromAgrarianField;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.activity_add_field);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.title_activity_add_field);

        cornerPoints = new ArrayList<>();
        linesFromAgrarianField = new ArrayList<>();
        p = new Polyline();

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
        OnMapClick();
    }

    @Override
    public void onStop() {
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
            case R.id.action_menu_toggleLoc:
                onToggleLocButtonClick();
                break;
            case R.id.action_menu_redo:
                onRedoButtonClick();
                break;
        }
        return true;
    }

    /*
     * removes the last added Point from the new field, and redraw the Polyline
     */
    private void onRedoButtonClick() {
        if (listGeoPoints.size() > 0 || listCornerPoints.size() > 0) {
            listGeoPoints.remove(listGeoPoints.size() - 1);
            listCornerPoints.remove(listCornerPoints.size() - 1);
            p.setPoints(listGeoPoints);
            mMapViewHandler.addPolyline(p);
            mMapViewHandler.invalidateMap();
        } else {
            Toast.makeText(this, getResources().getString(R.string.add_activity_NoMorePoints), Toast.LENGTH_SHORT).show();
        }
    }

    private void onToggleLocButtonClick() {
        if (myLocationListener.getFollow()) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.toastmsg_toggle_Loc_Off), Toast.LENGTH_SHORT).show();
            myLocationListener.setFollow(false);
        } else {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.toastmsg_toggle_Loc_On), Toast.LENGTH_SHORT).show();
            myLocationListener.setFollow(true);
        }
    }

    /*
     * adds a point to the field with a click on the map
     */
    public void OnMapClick() {
        MapEventsReceiver mReceive = new MapEventsReceiver() {
            @Override
            public boolean singleTapConfirmedHelper(GeoPoint point) {
                Location location = new Location("Loc");
                location.setLatitude(point.getLatitude());
                location.setLongitude(point.getLongitude());
                addPoint(location);

                Snackbar.make(mapFragment.getView(), getResources().getString(R.string.add_activity_pointAt) +
                        location.getLatitude() + " " + location.getLongitude() + getResources().getString(R.string.add_activity_added), Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
                return false;
            }

            @Override
            public boolean longPressHelper(GeoPoint p) {
                return false;
            }
        };


        MapEventsOverlay OverlayEvents = new MapEventsOverlay(getBaseContext(), mReceive);
        mMapViewHandler.getMap().getOverlays().add(OverlayEvents);
    }


    /**
     * adds a new Point to the Field
     *
     * @param location
     */

    public void addPoint(Location location) {
        GeoPoint g = new GeoPoint(location.getLatitude(), location.getLongitude());
        listGeoPoints.add(g);
        listCornerPoints.add(new CornerPoint(g.getLatitude(), g.getLongitude()));
        p.setPoints(listGeoPoints);
        mMapViewHandler.addPolyline(p);
        mMapViewHandler.invalidateMap();
        calcIntersection(location);

        fabLabel.setText(getResources().getString(R.string.add_Activity_YouNeed) + String.valueOf(3 - listCornerPoints.size()) + getResources().getString(R.string.add_activity_needMore));

        if (listCornerPoints.size() > 2) {
            enoughPoints = true;
            fabLabel.setVisibility(View.INVISIBLE);
            menuItemDone.setVisible(true);
            menuItemDone.setTitle(getResources().getString(R.string.done_Button));

        }

    }

    private AlertDialog.Builder generateSaveDialog() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
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
        builder.setMessage(getResources().getString(R.string.go_back_message)).setPositiveButton(getResources().getString(R.string.word_yes), dialogClickListener)
                .setNegativeButton(getResources().getString(R.string.word_no), dialogClickListener);

        return builder;
    }

    public void calcIntersection(Location location) {
        currentVector = new Vector<>();
        currentVector.add(location.getLatitude());
        currentVector.add(location.getLongitude());
        Vector<Double> line = new Vector<>();


        if (lastVector != null) {
            line.add(((lastVector.get(1) - currentVector.get(1)) / (lastVector.get(0) - currentVector.get(0))));
            line.add(currentVector.get(1) - line.get(0) * currentVector.get(0));

        } else {
            lastVector = currentVector;
        }
        if (parentField == null && line != null && line.size() != 0) {
            linesFromAgrarianField.add(line);
        } else {
            if (parentField instanceof AgrarianField) {
                if (line.size() != 0) {
                    for (Vector<Double> vec : ((AgrarianField) parentField).getLinesFormField()) {
                        Vector<Double> intersection = new Vector<>();
                        intersection.add((line.get(1) - vec.get(1)) / (vec.get(0) - line.get(0)));
                        intersection.add(line.get(0) * intersection.get(0) + line.get(1));

                        if (((lastVector.get(0) <= intersection.get(0) && intersection.get(0) <= currentVector.get(0)) || (currentVector.get(0) <= intersection.get(0) && intersection.get(0) <= lastVector.get(0))) && ((lastVector.get(1) <= intersection.get(1) && intersection.get(1) <= currentVector.get(1)) || (currentVector.get(1) <= intersection.get(1) && intersection.get(1) <= lastVector.get(1)))) {
                            Toast.makeText(this, getResources().getString(R.string.add_activity_outsideOffField), Toast.LENGTH_SHORT).show();
                            onRedoButtonClick();
                            return;
                        } else {

                        }
                    }
                }
            }
        }
        lastVector = currentVector;
    }

    /**
     * Handle clicks for the done button on top
     * depending on the current state of our field to add
     */
    private void onDoneButtonClick() {
        if (enoughPoints) {
            //myLocationListener.setFollow(false);
            Field fieldToAdd = new AgrarianField(getApplicationContext(), listCornerPoints);
            if (isDmgField) {
                fieldToAdd = new DamageField(getApplicationContext(), listCornerPoints);
            } else {
                if (fieldToAdd instanceof AgrarianField) {
                    Vector<Double> line = new Vector<>();
                    Vector<Double> startVector = new Vector<>();
                    if (listGeoPoints.get(0) != null) {
                        startVector.add(listGeoPoints.get(0).getLatitude());
                        startVector.add(listGeoPoints.get(0).getLongitude());
                        currentVector = startVector;
                        line.add(((lastVector.get(1) - currentVector.get(1)) / (lastVector.get(0) - currentVector.get(0))));
                        line.add(currentVector.get(1) - line.get(0) * currentVector.get(0));
                        linesFromAgrarianField.add(line);

                    } else {
                        lastVector = currentVector;
                    }

                    ((AgrarianField) fieldToAdd).setLinesFormField(linesFromAgrarianField);
                }
            }
            bottomSheetDialog = (BSDetailDialogEditFragment) BSDetailDialogEditFragment.newInstance();
            BSEditHandler handler = new BSEditHandler(fieldToAdd, dataManager, bottomSheetDialog);
            bottomSheetDialog.setPresenter(handler);
            bottomSheetDialog.show(getSupportFragmentManager(), "EditView");

            enoughPoints = false;
            listGeoPoints.clear();
            listCornerPoints.clear();
            // linesFromAgrarianField.clear();
            Log.d("", "");

        } else {
            Toast.makeText(getApplicationContext(), R.string.toastmsg_not_enough_points, Toast.LENGTH_LONG).show();
        }
    }

    /**
     * handle Floating Action Button clicks
     * depending on the current state of our field to add
     *
     * @param view
     */
    private void onFabClick(View view) {
        if (fieldToAddFinal == null) {
            Location location = myLocationListener.getLocation();
            if (location != null) {
                addPoint(location);

                Snackbar.make(view, getResources().getString(R.string.add_activity_pointAt) +
                        location.getLatitude() + " " + location.getLongitude() + getResources().getString(R.string.add_activity_added), Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
            } else {
                Snackbar.make(view, R.string.toastmsg_nolocation, Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        } else {
            Snackbar.make(view, R.string.toastmsg_points_already_added, Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }
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
            case "MapFragment":
                switch (action) {
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
        if (mMapViewHandler != null) {
            mMapViewHandler.reload();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        String temp = "0";
        Field field = null;
        if (resultCode != RESULT_OK && requestCode == PhotoManager.REQUEST_TAKE_PHOTO) {

            for (int i = 0; i < dataManager.getFields().size(); i++) {
                field = dataManager.getFields().get(i);

                if (field instanceof DamageField) {
                    if (((DamageField) field).getpaths() != null && ((DamageField) field).getpaths().size() > 0) {
                        String path = (((DamageField) field).getpaths().get(((DamageField) field).getpaths().size() - 1)).getImage_path();
                        if (temp.compareTo(path) > 0) {
                            dataManager.removeField(field);
                            temp = path;
                        }
                    }
                }
            }
            File f = new File(temp);
            f.delete();
            ((DamageField) field).getpaths().remove(((DamageField) field).getpaths().size() - 1);
            dataManager.addAgrarianField(field);
            dataManager.saveData();
        }
    }

}
