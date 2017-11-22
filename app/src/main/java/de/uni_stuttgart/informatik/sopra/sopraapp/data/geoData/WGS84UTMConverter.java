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

    // Exzentrität e
    private final static double e = Math.sqrt(1 - b/a*b/a);

    // e prime
    private final static double e0 = e / Math.sqrt(1.0 - e*e);

    // e squared (~ 0.006694379989312105)
    private final static double eSquared = (1.0 - (b/a)*(b/a));

    // e prime squared (~ 0.006739496741436008)
    private final static double e0Squared = e*e / (1.0 - e*e);


    public static UTMCoordinate convert(WGS84Coordinate wgs) {
        UTMCoordinate utm = new UTMCoordinate();
        final double utmZone = 1 + Math.floor((wgs.getLongitude() + 180) / 6);
        utm.setZone((int)Math.round(utmZone)); // ugly i konw  ~CB

        // Ellipsoidische Länge lambda - wird nicht benötigt
        // Winkel zwischen der ellipsoidischen Meridianebene durch den Punkt auf der Kugel und der ellipsoidischen Nullmeridianebene
		final double lambda = MathUtility.degToRad(wgs.getLongitude());

        // Ellipsoidische Breite phi
        // Winkel zwischen der Ellispoidnormalen durch den Punkt auf der Kugel und dem ellipsoidischen Äquator (Ebene)
        final double phi = MathUtility.degToRad(wgs.getLatitude());

        //central merdian
        final double centralMerdian = 3.0 + 6.0 * (utm.getZone() - 1.0) - 180.0;

        final double n = a / Math.sqrt(1.0 - Math.pow(e * Math.sin(phi), 2));
        final double t = Math.pow(Math.tan(phi), 2);
        final double c = e0Squared * Math.pow(Math.cos(phi), 2);
        final double aA = MathUtility.degToRad(wgs.getLongitude() - centralMerdian)  * Math.cos(phi);

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
        //TODO Test, make english, compare to 2 alg.
        return  utm;
    }

    public static WGS84Coordinate convert(UTMCoordinate utm) {
        //TODO insert code
        //just a reminder for myself need to format and make it international
        return  null;
    }
}
