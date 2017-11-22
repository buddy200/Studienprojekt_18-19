package de.uni_stuttgart.informatik.sopra.sopraapp.data;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import de.uni_stuttgart.informatik.sopra.sopraapp.R;
import de.uni_stuttgart.informatik.sopra.sopraapp.UI.FieldPolygon;

/**
 * Created by larsb on 22.11.2017.
 */

public abstract class Field {


    private FieldPolygon poly;
    protected Context context;

    //values for field and damage case
    private String name;
    private String owner;
    private String evaluator;


    private List<CornerPoint> cornerPoints = new ArrayList<>();
    private boolean finished = false;
    //the size of the field in m²
    private double size;

    public Field(){

    }

    public Field(Context context) {
        this.context = context;
        poly = new FieldPolygon(this.context);
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

    public void setCornerPoints(List<CornerPoint> cornerPoints) {
        this.cornerPoints = cornerPoints;
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

    public FieldPolygon getFieldPolygon(){
        return poly;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getEvaluator() {
        return evaluator;
    }

    public void setEvaluator(String evaluator) {
        this.evaluator = evaluator;
    }

    public abstract Bundle getBundle();
}
