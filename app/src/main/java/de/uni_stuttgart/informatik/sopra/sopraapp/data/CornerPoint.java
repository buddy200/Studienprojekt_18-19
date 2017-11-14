package de.uni_stuttgart.informatik.sopra.sopraapp.data;

import de.uni_stuttgart.informatik.sopra.sopraapp.data.geoData.UTMCoordinate;
import de.uni_stuttgart.informatik.sopra.sopraapp.data.geoData.WGS84Coordinate;
import de.uni_stuttgart.informatik.sopra.sopraapp.data.geoData.WGS84UTMConverter;

/**
 * Created by Christian on 13.11.2017.
 */

public class CornerPoint {

    private WGS84Coordinate wgs;
    private UTMCoordinate utm;
    private double angle;

    public CornerPoint(double latitude, double logitude) {
        wgs = new WGS84Coordinate(latitude, logitude);
        utm = WGS84UTMConverter.convert(wgs);
    }

    public void calculateAngle(CornerPoint before, CornerPoint after) {
        //cos a = (v_a x v_b )/ (|v_a| * |v_b)|
        //TODO find a way to get the orientation of the angle

        //maybe remove itself if angle = 180°
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

}
