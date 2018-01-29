package de.uni_stuttgart.informatik.sopra.fieldManager.UI.Map;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.Log;
import android.view.MotionEvent;

import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.Polygon;
import org.osmdroid.views.overlay.Polyline;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uni_stuttgart.informatik.sopra.fieldManager.FragmentInteractionListener;
import de.uni_stuttgart.informatik.sopra.fieldManager.GlobalConstants;
import de.uni_stuttgart.informatik.sopra.fieldManager.R;
import de.uni_stuttgart.informatik.sopra.fieldManager.data.AgrarianField;
import de.uni_stuttgart.informatik.sopra.fieldManager.data.FieldTypes.AgrarianFieldType;
import de.uni_stuttgart.informatik.sopra.fieldManager.data.FieldTypes.DamageFieldType;
import de.uni_stuttgart.informatik.sopra.fieldManager.data.managers.AppDataManager;
import de.uni_stuttgart.informatik.sopra.fieldManager.data.DamageField;
import de.uni_stuttgart.informatik.sopra.fieldManager.data.Field;

/**
 * sopra_priv
 * Created by Felix B on 14.11.17.
 * Mail: felix.burk@gmail.com
 * <p>
 * a handler class for the map view inside the MapViewFragment
 */

public class MapViewHandler implements MapContract.MapHandler {

    private static final String TAG = "MapViewHandler";

    private MapView map;
    private IMapController mapController;
    private Context context;
    private Marker currentLocMarker;
    private ArrayList<Marker> fieldMarker;

    //map fields to Polygon Overlays
    private Map<Field, FieldPolygon> fieldPolyMap;

    private FragmentInteractionListener mapInteractionListener;

    private AppDataManager mDataManager;

    @Nullable
    private MapFragment mMapFragment;
    private GeoPoint backupLocation;

    /**
     * constructor
     *
     * @param context
     */
    public MapViewHandler(Context context, @Nullable AppDataManager dataManager, MapFragment mapFragment) {
        this.context = context;
        mDataManager = dataManager;
        mMapFragment = mapFragment;
        fieldMarker = new ArrayList<>();
    }

    @Override
    public void start() {
        init();
    }

    /**
     * initialize the map
     */
    int counter = 0;

    public void init() {
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

        if (currentLocMarker != null) {
            mapController.setCenter(currentLocMarker.getPosition());
        } else {
            mapController.setCenter(GlobalConstants.getLastLocationOnMap());
        }

        //setup listener for tabs on polygons
        if (context instanceof FragmentInteractionListener) {
            mapInteractionListener = (FragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement MapInteractionListener");
        }

        if (mDataManager != null) {
            reloadWithData(mDataManager.getAllFields());
        }
    }

    /**
     * convert Objects of Type Field to Polygons on the map
     * and put both in a hashMap
     *
     * @param mField
     * @return
     */
    public Polygon fieldToPolygon(Field mField) {
        final Field field = mField;
        FieldPolygon polygon = new FieldPolygon(context, field) {
            double offset = 0.00075;

            @Override
            public boolean onSingleTapConfirmed(final MotionEvent event, final MapView mapView) {
                boolean tapped = contains(event);

                //only show detail if map is zoomed in enough
                if (tapped && mapView.getZoomLevel() > 13) {
                    mapInteractionListener.onFragmentMessage(TAG, "singleTabOnPoly", field);
                }
                return tapped;
            }
        };
        List<GeoPoint> polyPoints = new ArrayList<>();
        for (GeoPoint point : field.getGeoPoints()) {
            polyPoints.add(new GeoPoint(point.getLatitude(), point.getLongitude()));
        }
        polygon.setPoints(polyPoints);
        polygon.setFillColor(field.getColor());
        polygon.setTitle(field.getName());
        polygon.setStrokeColor(Color.BLACK);
        if(field  instanceof AgrarianField){
            polygon.setPatternBMP(getBitmapFromVectorDrawable(context, ((AgrarianFieldType)field.getType()).getPattern(context), field));
            polygon.setStrokeWidth(1.0f);
        }else {
            polygon.setPatternBMP(getBitmapFromVectorDrawable(context, ((DamageFieldType)field.getType()).getPattern(context), field));
            polygon.setStrokeWidth(2.0f);
        }

        fieldPolyMap.put(field, polygon);
        return polygon;
    }

    public static Bitmap getBitmapFromVectorDrawable(Context context, int drawableId, Field field) {
        Drawable drawable = ContextCompat.getDrawable(context, drawableId);
        if(field instanceof AgrarianField){
            drawable.setTint(context.getResources().getColor(R.color.colorAccentDark));
        }else {
            drawable.setTint(context.getResources().getColor(R.color.colorPrimary));
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            drawable = (DrawableCompat.wrap(drawable)).mutate();
        }

        Bitmap bitmap = Bitmap.createBitmap(35,
                35, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    /**
     * add a list of fields
     * first add AgrarianFields to map then DamageFields
     * because overlays are added first to last => first is "behind", last is "in front"
     *
     * @param fields
     */
    public void addFields(List<Field> fields) {
        int agrarFieldCount = 0;
        for (Field field : fields) {
            //add contained damage fields if field is type agrarian
            if (field instanceof AgrarianField) {
                map.getOverlayManager().add(0, fieldToPolygon(field));
                agrarFieldCount++;
            }
        }

        for (Field field : fields) {
            //add contained damage fields if field is type agrarian
            if (field instanceof DamageField) {
                map.getOverlayManager().add(agrarFieldCount, fieldToPolygon(field));
            }
        }
        map.invalidate();
    }

    /**
     * add a single field
     *
     * @param field
     */
    public void addField(Field field) {
        map.getOverlayManager().add(fieldToPolygon(field));
    }

    /**
     * delete field polygon from the map
     *
     * @param field
     */
    public void deleteFieldFromOverlay(Field field) {
        map.getOverlayManager().remove(fieldPolyMap.get(field));
    }

    /**
     * set a Marker with the current Location on the map
     *
     * @param lat
     * @param lon
     */
    public void setCurrLocMarker(double lat, double lon) {
        if (map == null) {
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

    public void dropMarker(double lat, double lon) {
        if (map == null) return;

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
        m.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        map.getOverlayManager().add(m);
        fieldMarker.add(m);
    }

    public void deleteLastFieldMarker() {
        map.getOverlayManager().remove(fieldMarker.get(fieldMarker.size() - 1));
        fieldMarker.remove(fieldMarker.size() - 1);
    }

    /**
     * redraw the map
     */
    public void invalidateMap() {
        map.invalidate();
    }

    /**
     * @param lat
     * @param lon
     */
    public void animateAndZoomTo(double lat, double lon) {
        if (map != null) {
            mapController.setZoom(20);
            mapController.animateTo(new GeoPoint(lat, lon));
        } else {
            backupLocation = new GeoPoint(lat, lon);
        }
    }

    @Override
    public void addPolyline(Polyline p) {
        map.getOverlayManager().add(p);
    }

    public void reload() {
        if (map != null) {
            reloadWithData(mDataManager.getAllFields());
        }
    }

    public void reloadWithData(ArrayList<Field> fields) {
        MapEventsOverlay backup = null;

        fieldPolyMap.clear();
        for (Overlay p : map.getOverlayManager().overlays()) {
            if (p instanceof Polyline) {
                map.getOverlayManager().overlays().remove(p);
            } else if (p instanceof FieldPolygon) {
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

    /**
     * Save the Map Center point to shared preference
     */
    public void saveMapCenter() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit = prefs.edit();
        edit.putFloat("Longitude", (float) this.getMap().getMapCenter().getLongitude());
        edit.putFloat("Latitude", (float) this.getMap().getMapCenter().getLatitude());
        edit.apply();
    }

    public void destroy() {
        fieldMarker.clear();
        map.onDetach();
    }
}