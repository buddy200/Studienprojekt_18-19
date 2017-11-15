package de.uni_stuttgart.informatik.sopra.sopraapp.data.geoData;

/**
 * Created by Christian on 15.11.2017.
 */

public class Vector {

    private int x;
    private int y;

    public Vector(int x , int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public double getLength() {
        return Math.sqrt(x*x + y*y);
    }

}
