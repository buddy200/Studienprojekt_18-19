package de.uni_stuttgart.informatik.sopra.sopraapp.data;

import java.io.Serializable;

import de.uni_stuttgart.informatik.sopra.sopraapp.data.geoData.MathUtility;
import de.uni_stuttgart.informatik.sopra.sopraapp.data.geoData.UTMCoordinate;
import de.uni_stuttgart.informatik.sopra.sopraapp.data.geoData.Vector;
import de.uni_stuttgart.informatik.sopra.sopraapp.data.geoData.WGS84Coordinate;
import de.uni_stuttgart.informatik.sopra.sopraapp.data.geoData.WGS84UTMConverter;

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
            utm = WGS84UTMConverter.convert(wgs);
        }
    }

    /**
     * calculate the angle of two corner points
     * @param before
     * @param after
     */
    public void calculateAngle(CornerPoint before, CornerPoint after) {

        UTMCoordinate utmBefore = before.getUtm();
        UTMCoordinate utmAfter = after.getUtm();
        Vector v_before;
        Vector v_after;
        double centralMerdian = 3.0 + 6.0 * (this.utm.getZone() - 1.0) - 180.0;
        if(utm.getZone() == utmBefore.getZone()) {
            v_before = new Vector((utmBefore.getEasting()- utm.getEasting()), (utmBefore.getNorthing()-utm.getNorthing()));
        } else {
            double merdian;
            if(utm.getZone() < utmBefore.getZone()) {
                merdian = centralMerdian + 3;
            } else {
                merdian = centralMerdian - 3;
            }
            double deltaLat = before.getWGS().getLatitude() - wgs.getLatitude();
            double deltaLong = before.getWGS().getLongitude() - wgs.getLongitude();

            double lat = deltaLat/deltaLong * (merdian - wgs.getLongitude()) + wgs.getLatitude();

            UTMCoordinate utm = WGS84UTMConverter.getUtmCoordinateByMeridian(MathUtility.degToRad(lat),MathUtility.degToRad(merdian), centralMerdian);
            v_before = new Vector((utm.getEasting()- this.utm.getEasting()), (utm.getNorthing()- this.utm.getNorthing()));
        }

        if(utm.getZone() == utmAfter.getZone()) {
            v_after = new Vector((utmAfter.getEasting()- utm.getEasting()), (utmAfter.getNorthing()-utm.getNorthing()));
        } else {
            double merdian;
            if(utm.getZone() < utmAfter.getZone()) {
                merdian = centralMerdian + 3;
            } else {
                merdian = centralMerdian - 3;
            }
            double deltaLat = after.getWGS().getLatitude() - wgs.getLatitude();
            double deltaLong = after.getWGS().getLongitude() - wgs.getLongitude();

            double lat = deltaLat/deltaLong * (merdian - wgs.getLongitude()) + wgs.getLatitude();

            UTMCoordinate utm = WGS84UTMConverter.getUtmCoordinateByMeridian(MathUtility.degToRad(lat),MathUtility.degToRad(merdian), centralMerdian);
            v_after = new Vector((utm.getEasting()- this.utm.getEasting()), (utm.getNorthing()- this.utm.getNorthing()));
        }


        if(v_before.getLength() == 0 || v_after.getLength() == 0) {
            angle = 0;
            return;
        }
        //cos a = (v_a x v_b )/ (|v_a| * |v_b)|
        angle = Math.acos(MathUtility.scalarProduct(v_before,v_after)/(v_before.getLength() * v_after.getLength()));

        Vector rotated = v_after.rotate(angle);
        if(rotated.equalDirection(v_before)) {
            angle = 2*Math.PI - angle;
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
