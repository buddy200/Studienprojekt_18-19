package de.uni_stuttgart.informatik.sopra.sopraapp.UI;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Polygon;

import java.util.ArrayList;
import java.util.List;

import de.uni_stuttgart.informatik.sopra.sopraapp.GlobalConstants;
import de.uni_stuttgart.informatik.sopra.sopraapp.R;
import de.uni_stuttgart.informatik.sopra.sopraapp.data.CornerPoint;
import de.uni_stuttgart.informatik.sopra.sopraapp.data.Field;

/**
 * sopra_priv
 * Created by Felix B on 14.11.17.
 * Mail: felix.burk@gmail.com
 */

public class MapViewHandler {

    MapView map;
    IMapController mapController;
    Context context;

    public MapViewHandler(Context context){
        this.context = context;
        init();

        //addPolygons(GlobalConstants.polygonTest(100,5));
        addFields(GlobalConstants.fieldTest(20,4));
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

    public void addPolygons(List<Polygon> polis){
        for(Polygon pol : polis){
            map.getOverlayManager().add(pol);
        }
    }

    public void addFields(List<Field> fields){
        for(Field field : fields){

            Polygon poly = new Polygon();
            List<GeoPoint> polyPoints = new ArrayList<>();

            for(CornerPoint point : field.getCornerPoints()){
                polyPoints.add(new GeoPoint(point.getWGS().getLatitude(), point.getWGS().getLongitude()));
            }
            // add field attributes to polygon attributes
            poly.setPoints(polyPoints);
            poly.setFillColor(fieldToPolygonColor(field));
            // invisible borders look really cool :D
            poly.setStrokeColor(Color.argb(0,0,0,0));
            Log.d("Field state:", String.valueOf(fieldToPolygonColor(field)));
            Log.d("Real field state", String.valueOf(field.getState().name()));
            poly.setTitle(field.getName());
            map.getOverlayManager().add(poly);
        }
        //TODO
    }

    /**
     * map field state to color
     * @param field
     * @return
     */
    private int fieldToPolygonColor(Field field) {
        switch (field.getState()){
            case NoDamage:
                return ContextCompat.getColor(context, R.color.stateNoDamage);
            case LightDamage:
                return ContextCompat.getColor(context, R.color.stateLightDamage);
            case HighDamage:
                return ContextCompat.getColor(context, R.color.stateHighDamage);
            default:
                return ContextCompat.getColor(context, R.color.stateDefault);
        }
    }

    public void animateTo(GeoPoint point){
        mapController.animateTo(point);
    }

    public MapView getMapView(){
        return map;
    }
}
