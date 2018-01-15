package de.uni_stuttgart.informatik.sopra.sopraapp.UI.Map;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.MotionEvent;

import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.OverlayManager;
import org.osmdroid.views.overlay.Polygon;
import org.osmdroid.views.overlay.Polyline;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uni_stuttgart.informatik.sopra.sopraapp.FragmentInteractionListener;
import de.uni_stuttgart.informatik.sopra.sopraapp.GlobalConstants;
import de.uni_stuttgart.informatik.sopra.sopraapp.R;
import de.uni_stuttgart.informatik.sopra.sopraapp.data.AgrarianField;
import de.uni_stuttgart.informatik.sopra.sopraapp.data.managers.AppDataManager;
import de.uni_stuttgart.informatik.sopra.sopraapp.data.CornerPoint;
import de.uni_stuttgart.informatik.sopra.sopraapp.data.DamageField;
import de.uni_stuttgart.informatik.sopra.sopraapp.data.Field;

/**
 * sopra_priv
 * Created by Felix B on 14.11.17.
 * Mail: felix.burk@gmail.com
 *
 * a handler class for the map view inside the MapViewFragment
 */

public class MapViewHandler implements MapContract.MapHandler {

    private static final String TAG = "MapViewHandler";

    private MapView map;
    private IMapController mapController;
    private Context context;
    private Marker currentLocMarker;

    //map fields to Polygon Overlays
    private Map<Field, FieldPolygon> fieldPolyMap;

    private FragmentInteractionListener mapInteractionListener;

    private AppDataManager mDataManager;

    @Nullable
    private MapFragment mMapFragment;


    private GeoPoint backupLocation;

    /**
     * constructor
     * @param context
     */
    public MapViewHandler(Context context, @Nullable AppDataManager dataManager, MapFragment mapFragment){
        this.context = context;
        mDataManager = dataManager;
        mMapFragment = mapFragment;
    }

    @Override
    public void start() {
        init();
    }

    /**
     * initialize the map
     */
    int counter = 0;
    public void init(){
        Log.e(TAG, "init map " + counter);
        counter++;
        map = new MapView(context);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setMultiTouchControls(true);
        map.setUseDataConnection(true);

        fieldPolyMap = new HashMap<>();

        mapController = map.getController();
        mapController.setZoom(GlobalConstants.DEFAULT_ZOOM);

        mapController.setCenter(GlobalConstants.getLastLocationOnMap());
       // mapController.setInvertedTiles(true);

        if(currentLocMarker != null){
            mapController.setCenter(currentLocMarker.getPosition());
        }
        else{
            mapController.setCenter(GlobalConstants.getLastLocationOnMap());
        }

        //setup listener for tabs on polygons
        if (context instanceof FragmentInteractionListener) {
            mapInteractionListener = (FragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement MapInteractionListener");
        }

        if(mDataManager != null){
            reloadWithData(mDataManager.getFields());
        }

    }


    /**
     * convert Objects of Type Field to Polygons on the map
     * and put both in a hashMap
     * @param mField
     * @return
     */
    public Polygon fieldToPolygon(Field mField){
        final Field field = mField;
        FieldPolygon polygon = new FieldPolygon(context, field){
            double offset = 0.00075;
            @Override
            public boolean onSingleTapConfirmed(final MotionEvent event, final MapView mapView){
                boolean tapped = contains(event);

                //only show detail if map is zoomed in enough
                if (tapped && mapView.getZoomLevel() > 13) {
                    mapInteractionListener.onFragmentMessage(TAG, "singleTabOnPoly", field);
                }
                return tapped;
            }
        };

        List<GeoPoint> polyPoints = new ArrayList<>();
        for (CornerPoint point : field.getCornerPoints()) {
            polyPoints.add(new GeoPoint(point.getWGS().getLatitude(), point.getWGS().getLongitude()));
        }

        polygon.setPoints(polyPoints);
        polygon.setFillColor(field.getColor());
        polygon.setTitle(field.getName());

        fieldPolyMap.put(field, polygon);
        return polygon;
    }

    /**
     * add a list of fields
     * first add AgrarianFields to map then DamageFields
     * because overlays are added first to last => first is "behind", last is "in front"
     * @param fields
     */
    public void addFields(List<Field> fields){
        int agrarFieldCount = 0;
        for(Field field : fields){
           //add contained damage fields if field is type agrarian
           if(field instanceof AgrarianField){
               map.getOverlayManager().add(0,fieldToPolygon(field));
               agrarFieldCount++;
           }
        }

        for(Field field : fields){
            //add contained damage fields if field is type agrarian
            if(field instanceof DamageField){
                map.getOverlayManager().add(agrarFieldCount,fieldToPolygon(field));
            }
        }
        map.invalidate();
    }

    /**
     * add a single field
     * @param field
     */
    public void addField(Field field){
        map.getOverlayManager().add(fieldToPolygon(field));
    }


    /**
     * delete field polygon from the map
     * @param field
     */
    public void deleteFieldFromOverlay(Field field){
        map.getOverlayManager().remove(fieldPolyMap.get(field));
    }

    /**
     * set a Marker with the current Location on the map
     * @param lat
     * @param lon
     */
    public void setCurrLocMarker(double lat, double lon){
        if(map == null){
            return;
        }
        map.getOverlayManager().remove(currentLocMarker);
        currentLocMarker = new Marker(map);
        currentLocMarker.setPosition(new GeoPoint(lat, lon));

        currentLocMarker.setOnMarkerClickListener(new Marker.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker, MapView mapView) {
                //do nothing
                return false;
            }
        });
        Drawable dr = context.getResources().getDrawable(R.drawable.ic_person_pin);

        currentLocMarker.setIcon(dr);
        map.getOverlayManager().add(currentLocMarker);
    }

    public void dropMarker(double lat, double lon){
        if(map == null) return;

        Marker m = new Marker(map);
        m.setPosition(new GeoPoint(lat, lon));

        m.setOnMarkerClickListener(new Marker.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker, MapView mapView) {
                //do nothing
                return false;
            }
        });

        Drawable dr = context.getResources().getDrawable(R.drawable.ic_pin_map);

        m.setIcon(dr);
        m.setAnchor(Marker.ANCHOR_BOTTOM, Marker.ANCHOR_BOTTOM);
        map.getOverlayManager().add(m);
    }

    /**
     * redraw the map
     */
    public void invalidateMap(){
        map.invalidate();
    }


    /**
     * @param lat
     * @param lon
     */
    public void animateAndZoomTo(double lat, double lon) {
        if(map != null){
            mapController.setZoom(20);
            mapController.animateTo(new GeoPoint(lat, lon));
        }else {
            backupLocation = new GeoPoint(lat, lon);
        }
    }

    @Override
    public void addPolyline(Polyline p) {
        map.getOverlayManager().add(p);
    }

    public void reload(){
        if(map != null){
            reloadWithData(mDataManager.getFields());
        }
    }

    public void reloadWithData(ArrayList<Field> fields) {
        MapEventsOverlay backup = null;

        fieldPolyMap.clear();
        for(Overlay p : map.getOverlayManager().overlays()){
            if(p instanceof Polyline){
                map.getOverlayManager().overlays().remove(p);
            }else if(p instanceof FieldPolygon){
                map.getOverlayManager().overlays().remove(p);
            }
        }

        addFields(fields);
    }

    public MapView getMap() {
        return map;
    }

    public void requestPermissions(String[] strings, int i) {
        mMapFragment.requestPermissions(strings, i);
    }

    public void destroy(){
        map.onDetach();

    }

}
