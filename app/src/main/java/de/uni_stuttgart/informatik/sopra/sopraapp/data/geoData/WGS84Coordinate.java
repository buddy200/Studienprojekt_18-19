package de.uni_stuttgart.informatik.sopra.sopraapp.data.geoData;

/**
 * Created by Christian on 13.11.2017.
 */

public class WGS84Coordinate {

    /**
     * Breitengrad (N, S)
     */
    private double latitude;

    /**
     * Längengrad (E, W)
     */
    private double longitude;

    public WGS84Coordinate(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
