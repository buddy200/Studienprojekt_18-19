package de.uni_stuttgart.informatik.sopra.sopraapp.UI;

import android.content.Context;
import android.view.MotionEvent;

import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polygon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uni_stuttgart.informatik.sopra.sopraapp.FragmentInteractionListener;
import de.uni_stuttgart.informatik.sopra.sopraapp.GlobalConstants;
import de.uni_stuttgart.informatik.sopra.sopraapp.data.AgrarianField;
import de.uni_stuttgart.informatik.sopra.sopraapp.data.CornerPoint;
import de.uni_stuttgart.informatik.sopra.sopraapp.data.DamageField;
import de.uni_stuttgart.informatik.sopra.sopraapp.data.Field;

/**
 * sopra_priv
 * Created by Felix B on 14.11.17.
 * Mail: felix.burk@gmail.com
 */

public class MapViewHandler {

    private static final String TAG = "MapViewHandler";

    private MapView map;
    private IMapController mapController;
    private Context context;
    private Marker currentLocMarker;

    //map fields to Polygon Overlays
    private Map<Field, FieldPolygon> fieldPolyMap;

    private FragmentInteractionListener mapInteractionListener;


    public MapViewHandler(Context context){
        this.context = context;
        init();

    }

    public void init(){
        map = new MapView(context);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setMultiTouchControls(true);
        map.setUseDataConnection(true);

        fieldPolyMap = new HashMap<>();

        mapController = map.getController();
        mapController.setZoom(GlobalConstants.DEFAULT_ZOOM);
        mapController.setCenter(GlobalConstants.START_POINT);

        //setup listener for tabs on polygons
        if (context instanceof FragmentInteractionListener) {
            mapInteractionListener = (FragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement MapInteractionListener");
        }

    }

    protected Polygon fieldToPolygon(Field mfield){
        final Field field = mfield;
        FieldPolygon polygon = new FieldPolygon(context, field){
            double offset = 0.00075;
            @Override
            public boolean onSingleTapConfirmed(final MotionEvent event, final MapView mapView){
                boolean tapped = contains(event);

                //only show detail if map is zoomed in enough
                if (tapped && mapView.getZoomLevel() > 13){
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
     * first add damage fields to map, then the agrarian field
     * because overlays are added first to last => first is "behind", last is "in front"
     * @param fields
     */
    public void addFields(List<Field> fields){
        for(Field field : fields){
            map.getOverlayManager().add(fieldToPolygon(field));

           //add contained damage fields if field is type agrarian
           if(field instanceof AgrarianField){
               for(DamageField dmg : ((AgrarianField) field).getContainedDamageFields()){
                   map.getOverlayManager().add(fieldToPolygon(dmg));

               }
           }


        }
    }

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
     * @param point
     */
    public void setCurrLocMarker(GeoPoint point){
        map.getOverlayManager().remove(currentLocMarker);
        currentLocMarker = new Marker(map);
        currentLocMarker.setPosition(point);

        currentLocMarker.setOnMarkerClickListener(new Marker.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker, MapView mapView) {
                //do nothing
                return false;
            }
        });

        map.getOverlayManager().add(currentLocMarker);

    }

    public void invalidateMap(){
        map.invalidate();
    }

    /**
     * animate to a the given point on the map
     * @param point
     */
    public void animateAndZoomTo(GeoPoint point) {
        mapController.setZoom(20);
        mapController.animateTo(point);
    }

    public MapView getMapView(){
        return map;
    }

}
