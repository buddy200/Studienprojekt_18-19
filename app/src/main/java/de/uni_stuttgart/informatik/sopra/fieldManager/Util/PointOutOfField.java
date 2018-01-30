package de.uni_stuttgart.informatik.sopra.fieldManager.Util;

import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;
import java.util.Vector;

/**
 * Created by larsb on 24.01.2018.
 */

public class PointOutOfField {

    public PointOutOfField() {
    }

    public static boolean pointInField(ArrayList<Vector<Double>> linesFromParent, GeoPoint centroidFromParent, GeoPoint newPoint) {
        Vector<Double> line = new Vector<>();
        int countIntersection = 0;

        line.add(((newPoint.getLongitude() - centroidFromParent.getLongitude()) / (newPoint.getLatitude() - centroidFromParent.getLatitude())));
        line.add(centroidFromParent.getLongitude() - line.get(0) * centroidFromParent.getLatitude());
        //this calculates the intersection
        for (Vector<Double> lineFromParent : linesFromParent) {
            Vector<Double> intersection = new Vector<>();
            intersection.add((line.get(1) - lineFromParent.get(1)) / (lineFromParent.get(0) - line.get(0)));
            intersection.add(line.get(0) * intersection.get(0) + line.get(1));

            //check if the intersection point is inside the damage field
            if (boundaryCheck(intersection, centroidFromParent, newPoint) && boundaryCheck2(intersection, new GeoPoint(lineFromParent.get(2).doubleValue(), lineFromParent.get(3).doubleValue()), new GeoPoint(lineFromParent.get(4).doubleValue(), lineFromParent.get(5).doubleValue()))) {
                countIntersection++;
            }
        }
        if (countIntersection % 2 == 0) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * return only true from the new point over the centroid to the infinty
     * @param intersection
     * @param centroidFromParent
     * @param newPoint
     * @return
     */
    private static boolean boundaryCheck(Vector<Double> intersection, GeoPoint centroidFromParent, GeoPoint newPoint) {
        if (newPoint.getLatitude() <= centroidFromParent.getLatitude() && newPoint.getLongitude() <= centroidFromParent.getLongitude()) {
            if ((intersection.get(0).doubleValue() >= newPoint.getLatitude()
                    && (intersection.get(1).doubleValue() >= newPoint.getLongitude()))) {
                return true;
            }
        }

        if (newPoint.getLatitude() >= centroidFromParent.getLatitude() && newPoint.getLongitude() >= centroidFromParent.getLongitude()) {
            if ((intersection.get(0).doubleValue() <= newPoint.getLatitude())
                    && intersection.get(1).doubleValue() <= newPoint.getLongitude()) {
                return true;
            }
        }
        if (newPoint.getLatitude() >= centroidFromParent.getLatitude() && newPoint.getLongitude() <= centroidFromParent.getLongitude()) {
            if ((intersection.get(0).doubleValue() >= newPoint.getLatitude()) && (intersection.get(1).doubleValue() <= newPoint.getLongitude()
            )) {
                return true;
            }
        }
        if (newPoint.getLatitude() <= centroidFromParent.getLatitude() && newPoint.getLongitude() >= centroidFromParent.getLongitude()) {
            if (((intersection.get(0).doubleValue() <= newPoint.getLatitude() && intersection.get(1).doubleValue() >= newPoint.getLongitude()))) {
                return true;
            }
        }
        return false;
    }

    /**
     * chekcs if the intersection point on the agrarain field outline
     * @param intersection
     * @param lastPoint
     * @param currentPoint
     * @return
     */
    private static boolean boundaryCheck2(Vector<Double> intersection, GeoPoint lastPoint, GeoPoint currentPoint) {
        if (lastPoint.getLatitude() <= currentPoint.getLatitude() && lastPoint.getLongitude() <= currentPoint.getLongitude()) {
            if (((intersection.get(0).doubleValue() >= lastPoint.getLatitude()
                    && intersection.get(0).doubleValue() <= currentPoint.getLatitude()) && (intersection.get(1).doubleValue() >= lastPoint.getLongitude()
                    && intersection.get(1).doubleValue() <= currentPoint.getLongitude()))) {
                return true;
            }
        }

        if (lastPoint.getLatitude() >= currentPoint.getLatitude() && lastPoint.getLongitude() >= currentPoint.getLongitude()) {
            if (((intersection.get(0).doubleValue() >= currentPoint.getLatitude()
                    && intersection.get(0).doubleValue() <= lastPoint.getLatitude()) && (intersection.get(1).doubleValue() >= currentPoint.getLongitude()
                    && intersection.get(1).doubleValue() <= lastPoint.getLongitude()))) {
                return true;
            }
        }
        if (lastPoint.getLatitude() >= currentPoint.getLatitude() && lastPoint.getLongitude() <= currentPoint.getLongitude()) {
            if (((intersection.get(0).doubleValue() >= currentPoint.getLatitude()
                    && intersection.get(0).doubleValue() <= lastPoint.getLatitude()) && (intersection.get(1).doubleValue() >= lastPoint.getLongitude()
                    && intersection.get(1).doubleValue() <= currentPoint.getLongitude()))) {
                return true;
            }
        }
        if (lastPoint.getLatitude() <= currentPoint.getLatitude() && lastPoint.getLongitude() >= currentPoint.getLongitude()) {
            if (((intersection.get(0).doubleValue() >= lastPoint.getLatitude()
                    && intersection.get(0).doubleValue() <= currentPoint.getLatitude()) &&
                    (intersection.get(1).doubleValue() >= currentPoint.getLongitude()
                            && intersection.get(1).doubleValue() <= lastPoint.getLongitude()))) {
                return true;
            }
        }
        return false;
    }
}
