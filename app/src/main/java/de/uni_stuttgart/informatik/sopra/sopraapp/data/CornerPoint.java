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

    public CornerPoint(double latitude, double logitude) {
        wgs = new WGS84Coordinate(latitude, logitude);
        utm = WGS84UTMConverter.convert(wgs);
    }

    public void calculateAngle(CornerPoint before, CornerPoint after) {
    }
}
