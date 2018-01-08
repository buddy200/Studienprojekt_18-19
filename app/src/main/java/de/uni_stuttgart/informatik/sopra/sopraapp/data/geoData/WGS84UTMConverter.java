package de.uni_stuttgart.informatik.sopra.sopraapp.data.geoData;

/**
 * Created by Christian on 13.11.2017.
 */
public class WGS84UTMConverter {

    // Stauchungsfaktor des Zentralmeridians = UTM-Skalierungsfaktor
    private final static double k0 = 0.9996;

    // Äquatorialradius a
    private final static double a = 6378137.0;

    // Polarradius b
    private final static double b = 6356752.314;

    // Polarabflachung f
    private final static double f = 1.0 / 298.2572236;

    // Exzentritaet e
    private final static double e = Math.sqrt(1 - b/a*b/a);

    // e prime
    private final static double e0 = e / Math.sqrt(1.0 - e*e);

    // e squared (~ 0.006694379989312105)
    private final static double eSquared = (1.0 - (b/a)*(b/a));

    // e prime squared (~ 0.006739496741436008)
    private final static double e0Squared = e*e / (1.0 - e*e);


    public static UTMCoordinate convert(WGS84Coordinate wgs) {


        // Ellipsoidische Länge lambda
        // Winkel zwischen der ellipsoidischen Meridianebene durch den Punkt auf der Kugel und der ellipsoidischen Nullmeridianebene
        final double lambda = MathUtility.degToRad(wgs.getLongitude());

        final int utmZone = 1 + (int) Math.floor((MathUtility.radToDeg(lambda) + 180) / 6);

        // Ellipsoidische Breite phi
        // Winkel zwischen der Ellispoidnormalen durch den Punkt auf der Kugel und dem ellipsoidischen Äquator (Ebene)
        final double phi = MathUtility.degToRad(wgs.getLatitude());

        //central merdian
        final double centralMerdian = 3.0 + 6.0 * (utmZone - 1.0) - 180.0;

        UTMCoordinate utm = getUtmCoordinateByMeridian(phi, lambda, centralMerdian);
        utm.setZone(utmZone);
        return utm;
    }

    public static UTMCoordinate getUtmCoordinateByMeridian(double phi, double lambda, double centralMerdian) {
        UTMCoordinate utm = new UTMCoordinate();

        final double n = a / Math.sqrt(1.0 - Math.pow(e * Math.sin(phi), 2));
        final double t = Math.pow(Math.tan(phi), 2);
        final double c = e0Squared * Math.pow(Math.cos(phi), 2);
        final double aA = (lambda - MathUtility.degToRad(centralMerdian))  * Math.cos(phi);

        // Bogenlänge entlang des Zentralmeridians der Zone
        double m = phi * (1.0 - eSquared * (1.0/4.0 + eSquared * (3.0/64.0 + 5.0*eSquared/256.0)));
        m = m - Math.sin(2.0 * phi) * (eSquared * (3.0/8.0 + eSquared * (3.0/32.0 + 45.0*eSquared/1024.0)));
        m = m + Math.sin(4.0 * phi) * (eSquared * eSquared * (15.0/256.0 + eSquared*45.0/1024.0));
        m = m - Math.sin(6.0 * phi) * (eSquared * eSquared * eSquared * (35.0/3072.0));
        m = m * a;

        // UTM-Koordinaten
        // Rechtswert relativ zum Zentralmeridian der Zone (= Ostwert, easting)
        double rechtswert = k0 * n * aA * (1.0 + aA * aA * ((1.0 - t + c)/6.0 + aA * aA * (5.0 - 18.0 * t + t*t + 72.0 * c - 58.0 * e0Squared) / 120.0));
        // Standard-Rechtswert (= Ostwert, easting), Versatz 500 km
        rechtswert += 500_000.0;

        // Hochwert vom Äquator aus (= Nordwert, northing)
        double hochwert = k0 * (m + n * Math.tan(phi)
                * (aA * aA * (1.0/2.0 + aA * aA * ((5.0 - t + 9.0 * c + 4.0 * c * c) / 24.0 + aA * aA * (61.0 - 58.0 * t + t*t + 600.0 * c - 330.0 * e0Squared) / 720.0))));

        utm.setEasting(Math.round(rechtswert));
        utm.setNorthing(Math.round(hochwert));
        return  utm;
    }

    public static WGS84Coordinate convert(UTMCoordinate utm) {
        // Rechtswert der UTM-Koordinate (= Ostwert, easting)
        final double rechtswert = utm.getEasting();
        // Hochwert der UTM-Koordinate (= Nordwert, northing)
        final double hochwert = utm.getNorthing();

        // Zentralmeridian der UTM-Zone
        final double zentralmeridianDerUTMZone = 3.0 + 6.0 * (utm.getZone() - 1) - 180.0;

        // e1 "e1 in USGS PP 1395"
        final double e1 = (1.0 - Math.sqrt(1.0 - e*e)) / (1.0 + Math.sqrt(1.0 - e*e));

        // Bogenlänge entlang des Zentralmeridians der Zone
        final double m = hochwert / k0;

        // Footprint latitude
        // Lot auf den Zentralmeridian durch den Punkt auf der Kugel
        final double mue = m / (a * (1.0 - eSquared * (1.0/4.0 + eSquared * (3.0/64.0 + 5.0 * eSquared/256.0))));
        double phi1 = mue + e1 * (3.0/2.0 - 27.0 * e1 * e1/32.0) * Math.sin(2.0 * mue)
                + e1 * e1 * (21.0/16.0 - 55.0 * e1 * e1/32.0) * Math.sin(4.0 * mue);
        phi1 = phi1	+ e1 * e1 * e1 * (Math.sin(6.0 * mue) * 151.0/96.0 + e1 * Math.sin(8.0 * mue) * 1097.0/512.0);

        final double c1 = e0Squared * Math.pow(Math.cos(phi1), 2);
        final double t1 = Math.pow(Math.tan(phi1), 2.0);
        final double n1 = a / Math.sqrt(1.0 - Math.pow(e * Math.sin(phi1), 2));
        final double r1 = n1 * (1.0 - e*e) / (1.0 - Math.pow(e * Math.sin(phi1), 2));
        final double d = (rechtswert - 500000.0) / (n1 * k0);

        // Ellipsoidische Breite phi
        // Winkel zwischen der Ellispoidnormalen durch den Punkt auf der Kugel und dem ellipsoidischen Äquator (Ebene)
        double phi = (d * d) * (1.0/2.0 - d * d * (5.0 + 3.0 * t1 + 10.0 * c1 - 4.0 * c1 * c1 - 9.0 * e0Squared) / 24.0);
        phi = phi + Math.pow(d, 6) * (61.0 + 90.0 * t1 + 298.0 * c1 + 45.0 * t1 * t1 - 252.0 * e0Squared - 3.0 * c1 * c1) / 720.0;
        phi = phi1 - (n1 * Math.tan(phi1) / r1) * phi;

        // Ellipsoidische Länge lambda
        // Winkel zwischen der ellipsoidischen Meridianebene durch den Punkt auf der Kugel und der ellipsoidischen Nullmeridianebene
        final double lambda = d * (1.0 + d * d * ((-1.0 - 2.0 * t1 - c1) / 6.0
                + d * d * (5.0 - 2.0 * c1 + 28.0 * t1 - 3.0 * c1 * c1 + 8.0 * e0Squared + 24.0 * t1 * t1) / 120.0)) / Math.cos(phi1);

        // Runden auf sechs Nachkommastellen
        WGS84Coordinate result = new WGS84Coordinate();
        result.setLongitude( Math.round(1_000_000.0 * (zentralmeridianDerUTMZone + MathUtility.radToDeg(lambda))) / 1_000_000.0);
        result.setLatitude( Math.round( MathUtility.radToDeg(1_000_000.0 *phi)) / 1_000_000.0);
        return result;

    }
}
