package de.uni_stuttgart.informatik.sopra.sopraapp.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Christian on 13.11.2017.
 */

public class Field {

    private boolean finised = false;
    private List<CornerPoint> cornerPoints = new ArrayList<>();

    /**
     * the size of the field
     * in m²
     */
    private double size;

    public Field() {

    }

    public void addCornerPoint (CornerPoint cp) {
        cornerPoints.add(cp);
        cornerPoints.get(cornerPoints.size()-2).calculateAngle(cornerPoints.get(cornerPoints.size()-3), cp);
    }

    public void finish() {
        finised = true;
        calculateSize();
    }

    private void calculateSize() {
        //TODO calculate the field size
    }

    /**
     *
     * @return the size of the field or @code{null} if the field isn't finished
     */
    public double getSize() {
        return finised ? size : null;
    }

}
