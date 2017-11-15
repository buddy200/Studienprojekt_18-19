package de.uni_stuttgart.informatik.sopra.sopraapp.data.geoData;

/**
 * Created by Christian on 13.11.2017.
 */

public class UTMCoordinate {

    private double middleMerdian;

    private boolean southernHemisphere;

    /**
     * the north Value
     *
     */
    private int northing;

    /**
     * the east value (based of the middle merdian)
     *
     */
    private int easting;

    public UTMCoordinate() {

    }

    public int getZone() {
        return ((int)(middleMerdian+180)/6) +1;
    }

    public int getNorthing() {
        return northing;
    }

    public void setNorthing(int northing) {
        this.northing = northing;
    }

    public int getEasting() {
        return easting;
    }

    public void setEasting(int easting) {
        this.easting = easting;
    }

    public double getMiddleMerdian() {
        return middleMerdian;
    }
    public void setMiddleMerdian(double middleMerdian) {
        this.middleMerdian = middleMerdian;
    }

    public boolean isSouth() {
        return southernHemisphere;
    }
}
