package de.uni_stuttgart.informatik.sopra.sopraapp.data.geoData;

/**
 * Created by Christian on 13.11.2017.
 */

public class UTMCoordinate {

    private int zone;

    private boolean southernHemisphere;

    /**
     * the north Value
     *
     */
    private long northing;

    /**
     * the east value (based of the middle merdian)
     *
     */
    private long easting;

    public UTMCoordinate() {

    }

    public int getZone() {
        return zone;
    }

    public void setZone(int zone) {
        this.zone = zone;
    }

    public long getNorthing() {
        return northing;
    }

    public void setNorthing(long northing) {
        this.northing = northing;
    }

    public long getEasting() {
        return easting;
    }

    public void setEasting(long easting) {
        this.easting = easting;
    }

    public boolean isSouth() {
        return southernHemisphere;
    }
}
