package de.uni_stuttgart.informatik.sopra.fieldManager.data;

import java.io.Serializable;

import de.uni_stuttgart.informatik.sopra.fieldManager.data.geoData.UTMCoordinate;
import de.uni_stuttgart.informatik.sopra.fieldManager.data.geoData.WGS84Coordinate;

/**
 * Created by Christian on 13.11.2017.
 *
 * CornerPoints for Fields, these are the Points for our Polygons
 */

public class CornerPoint implements Serializable {

    private WGS84Coordinate wgs;
    private UTMCoordinate utm;

    /**
     * the angle at this cornerpoint
     * on the right side following the input path
     * as radian
     */
    private double angle;

    /**
     * constructor, we need latitude and longitude coordinates in WGS84 format
     * @param latitude
     * @param logitude
     */
    public CornerPoint(double latitude, double logitude) {
        if (latitude != Double.NaN && logitude != Double.NaN) {
            wgs = new WGS84Coordinate(latitude, logitude);
        }
    }

    public WGS84Coordinate getWGS() {
        return wgs;
    }

    public UTMCoordinate getUtm() {
        return utm;
    }

    public double getAngle() {
        return angle;
    }

    public void setUtm(UTMCoordinate utm) {
        this.utm = utm;
    }

    @Override
    public boolean equals(Object o ) {
        if (o instanceof CornerPoint) {
            return utm.equals( ((CornerPoint) o).getUtm());
        } else {
            return false;
        }
    }

}
