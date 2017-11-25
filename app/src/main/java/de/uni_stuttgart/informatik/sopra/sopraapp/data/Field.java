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

    /**
     * the rotation of the polygon
     * true if counterclockwise
     */
    private boolean rotation = false;
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
        if (cornerPoints.size() > 2) {
            cornerPoints.get(cornerPoints.size() - 1).calculateAngle(cornerPoints.get(cornerPoints.size() - 2), cornerPoints.get(0));
            cornerPoints.get(0).calculateAngle(cornerPoints.get(cornerPoints.size() - 1), cornerPoints.get(1));
            calculateSize();
            finised = true;
        }
    }

    private void calculateSize() {
        List<CornerPoint> rmCopy = new ArrayList<>(cornerPoints.size());
        Collections.copy(rmCopy, cornerPoints);
        Queue<CornerPoint> outwardPoints = new LinkedList<>();
        List<Triangle> triangleList = new ArrayList<>();

        if(angleSum()) {
            for (CornerPoint cp : cornerPoints) {
                if (angleCheck(cp.getAngle())) {
                    outwardPoints.add(cp);
                }
            }

            for (int i = 0; i < cornerPoints.size() - 2; i++) {
                if (outwardPoints.isEmpty()) {
                    triangleList.add(new Triangle(rmCopy.get(0), rmCopy.get(1), rmCopy.get(2)));
                    rmCopy.remove(1);
                } else {
                    CornerPoint cp = outwardPoints.poll();
                    int index = rmCopy.indexOf(cp);
                    int indexBefore = ((index == 0)? index-1 : rmCopy.size()-1);

                    //two outward Points following
                    if (angleCheck(rmCopy.get(indexBefore).getAngle())) {
                        i--;
                        outwardPoints.add(cp);
                    } else {
                        int indexTwoBefore = ((indexBefore == 0) ? indexBefore-1 : rmCopy.size()-1);
                        int indexThreeBefore = ((indexTwoBefore == 0) ? indexTwoBefore-1 : rmCopy.size()-1);
                        int indexAfter = ((index == rmCopy.size()-1) ? 0 : ++index);
                        //make triangle
                        triangleList.add(new Triangle(rmCopy.get(indexTwoBefore), rmCopy.get(indexBefore), rmCopy.get(index)));
                        rmCopy.remove(indexBefore);

                        //recalculate angles
                        CornerPoint cpBefore = rmCopy.get(indexTwoBefore);
                        cp.calculateAngle(cpBefore, rmCopy.get(indexAfter));
                        cpBefore.calculateAngle(rmCopy.get(indexThreeBefore), cp);
                        if(angleCheck(cp.getAngle())) {
                            outwardPoints.add(cp);
                        }
                        if(angleCheck(cpBefore.getAngle())) {
                            outwardPoints.add(cpBefore);
                        }
                    }
                }
            }

            for (Triangle t : triangleList) {
                size += t.getSize();
            }
        }
    }

    private boolean angleCheck(double angle) {
        return rotation ? angle <= Math.PI : angle >= Math.PI;
    }

    private boolean angleSum() {
        double sum = 0;
        for(int i = 0; i < cornerPoints.size();i++) {
            sum += cornerPoints.get(i).getAngle();
        }
        if (Math.abs(sum - (Math.PI * (cornerPoints.size()+2)))< 0.001) {
            rotation = true;
            return true;
        } else if(Math.abs(sum - (Math.PI * (cornerPoints.size()-2)))< 0.001) {
            return true;
        } else {
            //either wrong calculation or crossing lines in input polygon
            return false;
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
