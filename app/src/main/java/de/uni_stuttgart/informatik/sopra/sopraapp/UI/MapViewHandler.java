package de.uni_stuttgart.informatik.sopra.sopraapp.UI;

import android.content.Context;

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

import de.uni_stuttgart.informatik.sopra.sopraapp.GlobalConstants;
import de.uni_stuttgart.informatik.sopra.sopraapp.data.AgrarianField;
import de.uni_stuttgart.informatik.sopra.sopraapp.data.CornerPoint;
import de.uni_stuttgart.informatik.sopra.sopraapp.data.Field;

/**
 * sopra_priv
 * Created by Felix B on 14.11.17.
 * Mail: felix.burk@gmail.com
 */

public class MapViewHandler {

    private MapView map;
    private IMapController mapController;
    private Context context;
    private Marker currentLocMarker;

    //map fields to Polygon Overlays
    private Map<Field, FieldPolygon> fieldPolyMap;

    public MapViewHandler(Context context){
        this.context = context;
        init();

    }

    public void init(){
        map = new MapView(context);
        map.setTileSource(TileSourceFactory.MAPNIK);
        //map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);
        map.setUseDataConnection(true);

        fieldPolyMap = new HashMap<>();

        mapController = map.getController();
        mapController.setZoom(GlobalConstants.DEFAULT_ZOOM);
        mapController.setCenter(GlobalConstants.START_POINT);

    }

    protected Polygon fieldToPolygon(Field field){
        FieldPolygon polygon = new FieldPolygon(context, field);

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
     * @param fields
     */
    public void addFields(List<Field> fields){
        for(Field field : fields){
           map.getOverlayManager().add(fieldToPolygon(field));

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
        map.getOverlayManager().add(currentLocMarker);

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
