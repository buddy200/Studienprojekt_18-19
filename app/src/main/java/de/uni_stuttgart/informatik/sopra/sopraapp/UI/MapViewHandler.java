package de.uni_stuttgart.informatik.sopra.sopraapp.UI;

import android.content.Context;

import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;

import de.uni_stuttgart.informatik.sopra.sopraapp.GlobalConstants;

/**
 * sopra_priv
 * Created by Felix B on 14.11.17.
 * Mail: felix.burk@gmail.com
 */

public class MapViewHandler {

    MapView map;
    IMapController mapController;

    public MapViewHandler(Context context){
        map = new MapView(context);
        map.setTileSource(TileSourceFactory.MAPNIK);
        //map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);
        mapController = map.getController();
        mapController.setZoom(20);
        map.setUseDataConnection(true);
        //default center is campus uni stuttgart
        mapController.setCenter(GlobalConstants.START_POINT);

    }

    public void animateTo(GeoPoint point){
        mapController.animateTo(point);
    }

    public MapView getMapView(){
        return map;
    }
}
