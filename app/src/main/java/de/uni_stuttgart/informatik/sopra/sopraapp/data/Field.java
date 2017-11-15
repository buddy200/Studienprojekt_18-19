package de.uni_stuttgart.informatik.sopra.sopraapp.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import de.uni_stuttgart.informatik.sopra.sopraapp.data.geoData.Triangle;

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
        if(!finised) {
            cornerPoints.add(cp);
            if (cornerPoints.size() > 2) {
                cornerPoints.get(cornerPoints.size() - 2).calculateAngle(cornerPoints.get(cornerPoints.size() - 3), cp);
            }
        }
    }

    public void finish() {
        cornerPoints.get(cornerPoints.size()-1).calculateAngle(cornerPoints.get(cornerPoints.size()-2), cornerPoints.get(0));
        cornerPoints.get(0).calculateAngle(cornerPoints.get(cornerPoints.size()-1), cornerPoints.get(1));
        calculateSize();
        finised = true;
    }

    private void calculateSize() {
        List<CornerPoint> rmCopy = new ArrayList<>(cornerPoints.size());
        Collections.copy(rmCopy, cornerPoints);
        Queue<CornerPoint> outwardPoints = new LinkedList<>();
        List<Triangle> triangleList = new ArrayList<>();

        for(CornerPoint cp : cornerPoints) {
            if (cp.getAngle() > Math.PI ) {
                outwardPoints.add(cp);
            }
        }

        for (int i = 0; i < cornerPoints.size()-2; i++) {
            if(outwardPoints.isEmpty()){
                triangleList.add(new Triangle(rmCopy.get(0), rmCopy.get(1), rmCopy.get(2)));
                rmCopy.remove(1);
            } else {
                CornerPoint cp = outwardPoints.poll();
                //TODO if is not fitting
                if(false) {
                    i--;
                    outwardPoints.add(cp);
                } else {
                    //make triangle
                }

                //TODO recalculate angles
            }
        }

        for (Triangle t : triangleList) {
            size += t.getSize();
        }

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
