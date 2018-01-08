package de.uni_stuttgart.informatik.sopra.sopraapp.data.geoData;

import de.uni_stuttgart.informatik.sopra.sopraapp.data.CornerPoint;

/**
 * represents a simple triangle
 *
 * @author Christian
 */
public class Triangle {

    private CornerPoint a;
    private CornerPoint b;
    private CornerPoint c;

    /**
     * initializes a triangle of three CornerPoints
     * 
     * @param a the first CornerPoint
     * @param b the second CornerPoint
     * @param c the  third CornerPoint
     */
    public Triangle(CornerPoint a, CornerPoint b, CornerPoint c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }
    
    /**
     * calculates the size of this triangle
     * 
     * @return the size of this trianlge
     */
    public double getSize() {
        UTMCoordinate utmA = a.getUtm();
        UTMCoordinate utmB = b.getUtm();
        UTMCoordinate utmC = c.getUtm();
        WGS84Coordinate wgsA = WGS84UTMConverter.convert(utmA);
        WGS84Coordinate wgsB = WGS84UTMConverter.convert(utmB);
        WGS84Coordinate wgsC = WGS84UTMConverter.convert(utmC);
        if(wgsA.equals(wgsB) || wgsB.equals(wgsC) || wgsA.equals(wgsC)) {
            return 0;
        }

        if(utmA.getZone() == utmB.getZone() && utmB.getZone() == utmC.getZone()) {
            Vector v_a = new Vector((utmA.getEasting() - utmB.getEasting()), (utmA.getNorthing() - utmB.getNorthing()));
            Vector v_c = new Vector((utmC.getEasting() - utmB.getEasting()), (utmC.getNorthing() - utmB.getNorthing()));

            return 0.5* Math.abs( v_a.getX()*v_c.getY() - v_c.getX()*v_a.getY());
        } else {
            double centralMerdian = 3.0 + 6.0 * (utmB.getZone() - 1.0) - 180.0;
            if (utmA.getZone() != utmB.getZone()) {
                double merdian;
                double centralmerdianOther;
                int zoneOther;
                if (utmA.getZone() < utmB.getZone()) {
                    merdian = centralMerdian - 3;
                    centralmerdianOther = centralMerdian - 6;
                    zoneOther = utmB.getZone() - 1;
                } else {
                    merdian = centralMerdian + 3;
                    centralmerdianOther = centralMerdian + 6;
                    zoneOther = utmB.getZone() + 1;
                }
                double deltaLat = a.getWGS().getLatitude() - b.getWGS().getLatitude();
                double deltaLong = a.getWGS().getLongitude() - b.getWGS().getLongitude();

                double lat = deltaLat / deltaLong * (merdian - b.getWGS().getLongitude()) + b.getWGS().getLatitude();

                UTMCoordinate utm = WGS84UTMConverter.getUtmCoordinateByMeridian(MathUtility.degToRad(lat), MathUtility.degToRad(merdian), centralMerdian);
                UTMCoordinate utmOther = WGS84UTMConverter.getUtmCoordinateByMeridian(MathUtility.degToRad(lat), MathUtility.degToRad(merdian), centralmerdianOther);
                utm.setZone(utmB.getZone());
                utmOther.setZone(zoneOther);

                CornerPoint cp1 = new CornerPoint(lat, merdian);
                CornerPoint cp2 = new CornerPoint(lat, merdian);
                cp1.setUtm(utm);
                cp2.setUtm(utmOther);

                return new Triangle(cp1, b, c).getSize() + new Triangle(a, c, cp2).getSize();
            }

            if (utmC.getZone() != utmB.getZone()) {
                double merdian;
                double centralmerdianOther;
                int zoneOther;
                if (utmC.getZone() < utmB.getZone()) {
                    merdian = centralMerdian - 3;
                    centralmerdianOther = centralMerdian - 6;
                    zoneOther = utmB.getZone() - 1;
                } else {
                    merdian = centralMerdian + 3;
                    centralmerdianOther = centralMerdian + 6;
                    zoneOther = utmB.getZone() + 1;
                }
                double deltaLat = c.getWGS().getLatitude() - b.getWGS().getLatitude();
                double deltaLong = c.getWGS().getLongitude() - b.getWGS().getLongitude();

                double lat = deltaLat / deltaLong * (merdian - b.getWGS().getLongitude()) + b.getWGS().getLatitude();

                UTMCoordinate utm = WGS84UTMConverter.getUtmCoordinateByMeridian(MathUtility.degToRad(lat), MathUtility.degToRad(merdian), centralMerdian);
                UTMCoordinate utmOther = WGS84UTMConverter.getUtmCoordinateByMeridian(MathUtility.degToRad(lat), MathUtility.degToRad(merdian), centralmerdianOther);
                utm.setZone(utmB.getZone());
                utmOther.setZone(zoneOther);

                CornerPoint cp1 = new CornerPoint(lat, merdian);
                CornerPoint cp2 = new CornerPoint(lat, merdian);
                cp1.setUtm(utm);
                cp2.setUtm(utmOther);

                return new Triangle(a, b, cp1).getSize() + new Triangle(a, c, cp2).getSize();

            }
            return 0;
        }
    }
}
