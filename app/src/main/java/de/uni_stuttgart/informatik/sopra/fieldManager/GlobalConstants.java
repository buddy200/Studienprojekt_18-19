package de.uni_stuttgart.informatik.sopra.fieldManager;

import org.osmdroid.util.GeoPoint;

import de.uni_stuttgart.informatik.sopra.fieldManager.data.DamageField;

/**
 * sopra_priv
 * Created by Felix B on 14.11.17.
 * Mail: felix.burk@gmail.com
 *
 * Class constants
 */

public class GlobalConstants {


    public static GeoPoint getLastLocationOnMap() {
        return lastLocationOnMap;
    }

    public static void setLastLocationOnMap(GeoPoint lastLocationOnMap) {
        GlobalConstants.lastLocationOnMap = lastLocationOnMap;
    }

    // Init Uni Stuttgart - compsci building else last location on map
    private static GeoPoint lastLocationOnMap = new GeoPoint( 48.745424, 9.106488 );


    //default zoom value for the mapFragment
    public static final int DEFAULT_ZOOM = 20;

    public static boolean isAdmin = false;

}
