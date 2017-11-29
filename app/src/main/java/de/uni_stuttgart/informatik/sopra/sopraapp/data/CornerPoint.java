package de.uni_stuttgart.informatik.sopra.sopraapp.data;

import de.uni_stuttgart.informatik.sopra.sopraapp.data.geoData.MathUtility;
import de.uni_stuttgart.informatik.sopra.sopraapp.data.geoData.UTMCoordinate;
import de.uni_stuttgart.informatik.sopra.sopraapp.data.geoData.Vector;
import de.uni_stuttgart.informatik.sopra.sopraapp.data.geoData.WGS84Coordinate;
import de.uni_stuttgart.informatik.sopra.sopraapp.data.geoData.WGS84UTMConverter;

/**
 * Created by Christian on 13.11.2017.
 */

public class CornerPoint {

    private WGS84Coordinate wgs;
    private UTMCoordinate utm;

    /**
     * the angle at this cornerpoint
     * on the right side following the input path
     * as radian
     */
    private double angle;

    public CornerPoint(double latitude, double logitude) {
        wgs = new WGS84Coordinate(latitude, logitude);
        utm = WGS84UTMConverter.convert(wgs);
    }

    public void calculateAngle(CornerPoint before, CornerPoint after) {

        UTMCoordinate utmBefore = before.getUtm();
        UTMCoordinate utmAfter = after.getUtm();
        if(utm.getZone() == utmBefore.getZone() && utm.getZone() == utmAfter.getZone()) {
            Vector v_before = new Vector((utmBefore.getEasting()- utm.getEasting()), (utmBefore.getNorthing()-utm.getNorthing()));
            Vector v_after = new Vector((utmAfter.getEasting()- utm.getEasting()), (utmAfter.getNorthing()-utm.getNorthing()));

            //cos a = (v_a x v_b )/ (|v_a| * |v_b)|
            angle = Math.acos(MathUtility.scalarProduct(v_before,v_after)/(v_before.getLength() * v_after.getLength()));

            Vector rotated = v_after.rotate(angle);
            if(rotated.equalDirection(v_before)) {
                angle = 2*Math.PI - angle;
            }

            //maybe remove itself if angle = 180°
        } else {
            //TODO
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

    @Override
    public boolean equals(Object o ) {
        if (o instanceof CornerPoint) {
            return wgs.equals( ((CornerPoint) o).getWGS());
        } else {
            return false;
        }
    }

}
