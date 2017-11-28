package de.uni_stuttgart.informatik.sopra.sopraapp.data;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import de.uni_stuttgart.informatik.sopra.sopraapp.R;

/**
 * Created by larsb on 22.11.2017.
 */

public abstract class Field {

    private static final String TAG = "Field";

    //keys
    protected static final String KEY_NAME = "name";
    protected static final String KEY_COLOR = "color";
    protected static final String KEY_COUNTY = "county";
    protected static final String KEY_SIZE = "size";
    protected static final String KEY_EVALUATOR = "evaluator";


    protected Context context;

    //values for field and damage case
    public String name;
    public String county;
    protected double size;
    public int color;
    protected GeoPoint centroid;


    private List<CornerPoint> cornerPoints = new ArrayList<>();
    private boolean finished = false;


    public Field() {

    }

    public Field(Context context, List<CornerPoint> cPoints) {
        this.context = context;

        //set default attributes
        this.name = context.getResources().getString(R.string.field_default_name);
        this.county = context.getResources().getString(R.string.county_default_name);
        this.size = 0.0;
        this.color = R.color.fieldDefaultColor;


        if (cPoints.size() < 2) {
            Log.e(TAG, "not enough corner points provided for field: " + name);
        } else {
            setCornerPoints(cPoints); //TODO: does this copy work? We might need some deepCopy() stuff here
        }
        calculateCentroid();
    }

    public void addCornerPoint(CornerPoint cp) {
        cornerPoints.add(cp);
        if (cornerPoints.size() > 2) {
            cornerPoints.get(cornerPoints.size() - 2).calculateAngle(cornerPoints.get(cornerPoints.size() - 3), cp);
        }
    }

    public void finish() {
        cornerPoints.get(cornerPoints.size() - 1).calculateAngle(cornerPoints.get(cornerPoints.size() - 2), cornerPoints.get(0));
        cornerPoints.get(0).calculateAngle(cornerPoints.get(cornerPoints.size() - 1), cornerPoints.get(1));
        calculateSize();
        finished = true;
    }

    private void calculateSize() {
        Queue<CornerPoint> outwardPoints = new LinkedList<>();

        //TODO check for correct zone

        for (CornerPoint cp : cornerPoints) {
            if (cp.getAngle() > 180 /* or pi*/) {
                outwardPoints.add(cp);
            }
        }

        for (int i = 0; i < cornerPoints.size() - 2; i++) {
            if (outwardPoints.isEmpty()) {
                //simple triangulation
            } else {
                CornerPoint cp = outwardPoints.poll();
                //TODO if is not fitting
                if (false) {
                    i--;
                    outwardPoints.add(cp);
                } else {
                    //make triangle
                }
            }
        }
        //TODO calculate the field size

    }

    /**
     * calculate centroid ( = center of gravity) of polygon
     */
    public void calculateCentroid(){
        double lowX0, lowY0, highX1, highY1;

        lowX0 = lowY0 = Double.MAX_VALUE;
        highX1 = highY1 = Double.MIN_VALUE;

        for(CornerPoint point : getCornerPoints()){
            if(lowX0 > point.getWGS().getLatitude()){
                lowX0 = point.getWGS().getLatitude();
            }
            if(lowY0 > point.getWGS().getLongitude()){
                lowY0 = point.getWGS().getLongitude();
            }
            if(highX1 < point.getWGS().getLatitude()){
                highX1 = point.getWGS().getLatitude();
            }
            if(highY1 < point.getWGS().getLongitude()){
                highY1 = point.getWGS().getLongitude();
            }
        }

        centroid = new GeoPoint(lowX0 + ((highX1 - lowX0) / 2), lowY0 + ((highY1 - lowY0) / 2));
    }

    /**
     * @return the size of the field or @code{null} if the field isn't finished
     */
    public double getSize() {
        return finished ? size : null;
    }

    public void setCornerPoints(List<CornerPoint> cornerPoints) {
        this.cornerPoints = cornerPoints;
    }

    public List<CornerPoint> getCornerPoints() {
        return cornerPoints;
    }

    public GeoPoint getCentroid(){ return this.centroid;}

    public abstract Bundle getBundle();


}
