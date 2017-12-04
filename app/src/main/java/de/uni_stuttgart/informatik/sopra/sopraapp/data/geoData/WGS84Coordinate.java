package de.uni_stuttgart.informatik.sopra.sopraapp.data.geoData;

import java.io.Serializable;

/**
 * Created by Christian on 13.11.2017.
 */

public class WGS84Coordinate implements Serializable {

    private static final long serialVersionUID = 7L;

    /**
     * Breitengrad (N, S)
     */
    private double latitude;

    /**
     * Längengrad (E, W)
     */
    private double longitude;

    public  WGS84Coordinate() {
        this(0,0);
    }

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

    public boolean equals(WGS84Coordinate wgs) {
        return Math.abs(wgs.getLongitude() - longitude) < 0.001 && Math.abs(wgs.getLatitude() - latitude) < 0.0001;
    }
}
