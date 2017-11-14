package de.uni_stuttgart.informatik.sopra.sopraapp.UI;

import android.content.Context;
import android.graphics.Color;

import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Polygon;

import java.util.ArrayList;
import java.util.List;

import de.uni_stuttgart.informatik.sopra.sopraapp.GlobalConstants;

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

        addPolygons(GlobalConstants.polygonTest(100,6));
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

    public void addFields(List<GeoPoint> points){
        //TODO
    }

    public void animateTo(GeoPoint point){
        mapController.animateTo(point);
    }

    public MapView getMapView(){
        return map;
    }
}
