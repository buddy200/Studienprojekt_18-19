package de.uni_stuttgart.informatik.sopra.fieldManager.Util;

import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;
import java.util.Vector;

import de.uni_stuttgart.informatik.sopra.fieldManager.data.AgrarianField;
import de.uni_stuttgart.informatik.sopra.fieldManager.data.Field;

/**
 * This class helps to check whether a damage field is complete in the correct agrarian field.
 */

public class IntersectionCalculator {

    private GeoPoint lastPoint;
    private GeoPoint currentPoint;
    private ArrayList<Vector<Double>> linesFromAgrarianField;
    private ArrayList<GeoPoint> points;
    private Vector<Double> line;

    public IntersectionCalculator(ArrayList<GeoPoint> points, ArrayList<Vector<Double>> linesFromAgrarianField) {
        this.linesFromAgrarianField = linesFromAgrarianField;
        this.points = points;
    }

    /**
     * This method calculate lines from the second last und last point and save this line in the linesFromAgrarianField Array list
     *
     * @param isAgrarianField give true if new field a agrarian field
     */
    public void calculateLine(boolean isAgrarianField) {
        //set the two points for the line and checks if enough points available
        if (points.size() > 0) {
            currentPoint = points.get(points.size() - 1);
        } else {
            currentPoint = null;
        }
        if (points.size() > 1) {
            lastPoint = points.get(points.size() - 2);
        } else {
            lastPoint = null;
        }
        line = new Vector<>();

        //calculates the line form second last to the last point
        if (lastPoint != null && currentPoint != null) {
            if (currentPoint.getLatitude() - lastPoint.getLatitude() != 0 && currentPoint.getLongitude() - lastPoint.getLongitude() != 0) {
                //pitch calculation
                line.add(((lastPoint.getLongitude() - currentPoint.getLongitude()) / (lastPoint.getLatitude() - currentPoint.getLatitude())));
                //y-intersept calculation
                line.add(currentPoint.getLongitude() - line.get(0) * currentPoint.getLatitude());
            }
            //very uncommon cases. for this application is this approximation ok.
            else if (currentPoint.getLatitude() - lastPoint.getLatitude() == 0) {
                line.add(0.0000000000000000000000000001);
                line.add(currentPoint.getLongitude() - line.get(0) * currentPoint.getLatitude());
            } else if (currentPoint.getLongitude() - lastPoint.getLongitude() == 0) {
                line.add(100000000000000000000000000000.0);
                line.add(currentPoint.getLongitude() - line.get(0) * currentPoint.getLatitude());
            }
        }
        //the line must only saved if the new field a agrarian field
        if (isAgrarianField && line != null && line.size() != 0) {
            line.add(2, lastPoint.getLatitude()); //first point form line
            line.add(3, lastPoint.getLongitude());
            line.add(4, currentPoint.getLatitude());// second point from line
            line.add(5, currentPoint.getLongitude());
            linesFromAgrarianField.add(line);
        }
    }

    /**
     * this method calculate the intersection between the new line from the damage field and all lines form the parent field
     *
     * @param parentField parent field from the damage field
     * @return true if no intersection found with the new line, else falls
     */
    public boolean calcIntersection(Field parentField) {

        if (parentField instanceof AgrarianField) {
            if (line.size() != 0) {
                //this calculates the intersection
                for (Vector<Double> lineFromParent : ((AgrarianField) parentField).getLinesFormField()) {
                    Vector<Double> intersection = new Vector<>();
                    intersection.add((line.get(1) - lineFromParent.get(1)) / (lineFromParent.get(0) - line.get(0)));
                    intersection.add(line.get(0) * intersection.get(0) + line.get(1));

                    //check if the intersection point between the second last and last point and if the intersection point
                    // in the the the line from the agrarian field
                    if (boundaryCheck(intersection, lastPoint, currentPoint) && boundaryCheck(intersection, new GeoPoint(lineFromParent.get(2).doubleValue(), lineFromParent.get(3).doubleValue()), new GeoPoint(lineFromParent.get(4).doubleValue(), lineFromParent.get(5).doubleValue()))) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * checks if the intersection point on the outline of the agrrian field or on the outline form the damage field
     *
     * @param intersection
     * @return
     */
    private boolean boundaryCheck(Vector<Double> intersection, GeoPoint lastPoint, GeoPoint currentPoint) {


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

    /**
     * this method calculate the last line from the new agrarian field from the end point to the start point
     */
    public void calcLastLine() {
        Vector<Double> line = new Vector<>();
        GeoPoint startPoint;
        if (points.get(0) != null) {
            lastPoint = points.get(points.size() - 1);
            startPoint = points.get(0);
            currentPoint = startPoint;
            //edge case
            if (currentPoint.getLatitude() - lastPoint.getLatitude() == 0) {
                line.add(0.0000000000000000000000000001);
                line.add(currentPoint.getLongitude() - line.get(0) * currentPoint.getLatitude());
            } else if (currentPoint.getLongitude() - lastPoint.getLongitude() == 0) {
                line.add(100000000000000000000000000000.0);
                line.add(currentPoint.getLongitude() - line.get(0) * currentPoint.getLatitude());
            }
            //normal case
            else {
                line.add(((lastPoint.getLongitude() - currentPoint.getLongitude()) / (lastPoint.getLatitude() - currentPoint.getLatitude())));
                line.add(currentPoint.getLongitude() - line.get(0) * currentPoint.getLatitude());
            }
            line.add(2, lastPoint.getLatitude());
            line.add(3, lastPoint.getLongitude());
            line.add(4, currentPoint.getLatitude());
            line.add(5, currentPoint.getLongitude());
            linesFromAgrarianField.add(line);
        }
    }
}