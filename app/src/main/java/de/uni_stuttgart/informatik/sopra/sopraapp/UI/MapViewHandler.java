package de.uni_stuttgart.informatik.sopra.sopraapp.UI;

import android.content.Context;
import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polygon;

import java.util.List;

import de.uni_stuttgart.informatik.sopra.sopraapp.GlobalConstants;
import de.uni_stuttgart.informatik.sopra.sopraapp.data.ArgrarianField;
import de.uni_stuttgart.informatik.sopra.sopraapp.data.DamageField;

/**
 * sopra_priv
 * Created by Felix B on 14.11.17.
 * Mail: felix.burk@gmail.com
 */

public class MapViewHandler {

    private MapView map;
    private IMapController mapController;
    private Context context;
    private Marker currentlocmarker;

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

        mapController = map.getController();
        mapController.setZoom(GlobalConstants.DEFAULT_ZOOM);
        mapController.setCenter(GlobalConstants.START_POINT);

    }

    /**
     * add a polygon form the agrarian field to the map
     * @param polis
     */
    public void addPolygons(List<Polygon> polis){
        for(Polygon pol : polis){
            map.getOverlayManager().add(pol);
        }
    }
    public void addField(ArgrarianField argrarianField){
        map.getOverlayManager().add( argrarianField.getFieldPolygon());
    }

    public void addFields(List<ArgrarianField> argrarianFields){

        for(ArgrarianField argrarianField : argrarianFields){
            argrarianField.createPolygon();
            map.getOverlayManager().add(argrarianField.getFieldPolygon());
        }
        //TODO
    }

    /**
     * delete the agrarian field polygon from the map
     * @param argrarianField
     */
    public void deleteFieldFromOverlay(ArgrarianField argrarianField){
        map.getOverlayManager().remove(argrarianField.getFieldPolygon());
    }

    /**
     * add a polygon form the damage field to the map
     * @param damageField
     */
    public void addDamageField(DamageField damageField){
        damageField.createPolygon();
        map.getOverlayManager().add(damageField.getFieldPolygon());
    }

    /**
     * delete the damage field polygon from the map
     * @param damageField
     */
    public void deleteDamageFieldFromOverlay(DamageField damageField){
        map.getOverlayManager().remove(damageField.getFieldPolygon());
    }

    /**
     * set a Marker with the current Location on the map
     * @param point
     */
    public void setCurrlocMarker(GeoPoint point){
        map.getOverlayManager().remove(currentlocmarker);
        currentlocmarker = new Marker(map);
        currentlocmarker.setPosition(point);
        map.getOverlayManager().add(currentlocmarker);

    }

    /**
     * animate to a the given point on the map
     * @param point
     */
    public void animateTo(GeoPoint point) {
        mapController.setZoom(20);
        mapController.animateTo(point);
    }

    public MapView getMapView(){
        return map;
    }
}
