
package de.uni_stuttgart.informatik.sopra.sopraapp.data;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import de.uni_stuttgart.informatik.sopra.sopraapp.UI.FieldPolygon;

/**
 * Created by Christian on 13.11.2017.
 */

public class Field {
    private static final String TAG = "Field";

    private boolean finished = false;

    //the size of the field in m²
    private double size;

    //default values for field
    String name = "Field";
    FieldStates state = FieldStates.NoDamage;
    List<CornerPoint> cornerPoints = new ArrayList<>();

    //bundle keys
    private static final String KEY_NAME = "title";
    private static final String KEY_STATE = "state";

    private FieldPolygon poly;
    private Context context;


    /**
     * fields need at least 3 corner points to exist
     */
    public Field(List<CornerPoint> cPoints, Context context) {
        if(cPoints.size() < 2){
            Log.e(TAG, "not enough corner points provided for field: " + name);
        }else {
            this.cornerPoints = cPoints; //TODO: does this copy work? We might need some deepCopy() stuff here
        }
        this.context = context;
        poly = new FieldPolygon(this.context);
        initPolygon();
    }


    public void addCornerPoint (CornerPoint cp) {
        cornerPoints.add(cp);
        if (cornerPoints.size() > 2) {
            cornerPoints.get(cornerPoints.size() - 2).calculateAngle(cornerPoints.get(cornerPoints.size() - 3), cp);
        }
    }

    public void finish() {
        cornerPoints.get(cornerPoints.size()-1).calculateAngle(cornerPoints.get(cornerPoints.size()-2), cornerPoints.get(0));
        cornerPoints.get(0).calculateAngle(cornerPoints.get(cornerPoints.size()-1), cornerPoints.get(1));
        calculateSize();
        finished = true;
    }

    private void calculateSize() {
        Queue<CornerPoint> outwardPoints = new LinkedList<>();

        //TODO check for correct zone

        for(CornerPoint cp : cornerPoints) {
            if (cp.getAngle() > 180 /* or pi*/) {
                outwardPoints.add(cp);
            }
        }

        for (int i = 0; i < cornerPoints.size()-2; i++) {
            if(outwardPoints.isEmpty()){
                //simple triangulation
            } else {
                CornerPoint cp = outwardPoints.poll();
                //TODO if is not fitting
                if(false) {
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
     *
     * @return the size of the field or @code{null} if the field isn't finished
     */
    public double getSize() {
        return finished ? size : null;
    }

    public List<CornerPoint> getCornerPoints(){
        return cornerPoints;
    }

    public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return this.name;
    }

    public void setState(FieldStates state){
        this.state = state;
    }
    public FieldStates getState(){
        return this.state;
    }

    public Bundle getBundle(){
        Bundle bundle = new Bundle();
        bundle.putString(KEY_NAME, name);
        bundle.putSerializable(KEY_STATE, state);
        return bundle;
    }

    public void initPolygon() {
        List<GeoPoint> polyPoints = new ArrayList<>();
        for (CornerPoint point : cornerPoints) {
            polyPoints.add(new GeoPoint(point.getWGS().getLatitude(), point.getWGS().getLongitude()));
        }
        // add field attributes to polygon attributes
        poly.setPoints(polyPoints);
        poly.setState(getState());
        // invisible borders look really cool :D
        poly.setTitle(getName());

    }
    public FieldPolygon getFieldPolygon(){
        return poly;
    }

}
