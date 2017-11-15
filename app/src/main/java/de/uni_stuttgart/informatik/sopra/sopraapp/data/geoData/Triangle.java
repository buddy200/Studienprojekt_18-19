package de.uni_stuttgart.informatik.sopra.sopraapp.data.geoData;

import de.uni_stuttgart.informatik.sopra.sopraapp.data.CornerPoint;

/**
 * Created by Christian on 15.11.2017.
 */

public class Triangle {

    private CornerPoint a;
    private CornerPoint b;
    private CornerPoint c;

    public Triangle(CornerPoint a, CornerPoint b, CornerPoint c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    public double getSize() {
        //TODO triangle size with cosinus
        //requires zone check
        return 0;
    }
}
