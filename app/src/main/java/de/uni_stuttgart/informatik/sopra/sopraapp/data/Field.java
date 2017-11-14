package de.uni_stuttgart.informatik.sopra.sopraapp.data;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Created by Christian on 13.11.2017.
 */

public class Field {

    private boolean finised = false;
    //removed private to access corner points - FB
    List<CornerPoint> cornerPoints = new ArrayList<>();

    /**
     * the size of the field
     * in m²
     */
    private double size;

    public Field() {

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
        finised = true;
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
        return finised ? size : null;
    }

    public List<CornerPoint> getCornerPoints(){
        return cornerPoints;
    }

}
