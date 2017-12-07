package de.uni_stuttgart.informatik.sopra.sopraapp.data.geoData;

/**
 * Created by Christian on 15.11.2017.
 */

public class MathUtility {

    public static double degToRad(double angle) {
        return (angle/180) *(Math.PI);
    }

    public static double radToDeg(double angle) {
        return (angle/(Math.PI) )* 180;
    }

    public static double scalarProduct(Vector a, Vector b) {
        return a.getX()*b.getX() + a.getY()*b.getY();
    }
}
