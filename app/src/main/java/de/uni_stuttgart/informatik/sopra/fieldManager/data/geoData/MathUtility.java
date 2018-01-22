package de.uni_stuttgart.informatik.sopra.fieldManager.data.geoData;

/**
 * cointains some mathematical funktions and formulas
 *
 * @author Christian
 */
public class MathUtility {

	/**
	 * transforms an angle from degree to radian
	 * 
	 * @param angle the angle to transform in degree
	 * @return the transformed angle in radian
	 */
    public static double degToRad(double angle) {
        return (angle/180) *(Math.PI);
    }


    /**
	 * transforms an angle from radian to degree
	 * 
	 * @param angle the angle to transform in radian
	 * @return the transformed angle in degree
	 */
    public static double radToDeg(double angle) {
        return (angle/(Math.PI) )* 180;
    }

    /**
     * calcuates the scalarproduct of two 2D-Vectors
     * 
     * @param a the first vector
     * @param b the second vector
     * @return the scalarproduct
     */
    public static double scalarProduct(Vector a, Vector b) {
        return a.getX()*b.getX() + a.getY()*b.getY();
    }
}
