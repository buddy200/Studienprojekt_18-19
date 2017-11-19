package de.uni_stuttgart.informatik.sopra.sopraapp;

import android.gesture.GestureOverlayView;
import android.graphics.Color;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.Polygon;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import de.uni_stuttgart.informatik.sopra.sopraapp.data.CornerPoint;
import de.uni_stuttgart.informatik.sopra.sopraapp.data.Field;
import de.uni_stuttgart.informatik.sopra.sopraapp.data.FieldStates;

/**
 * sopra_priv
 * Created by Felix B on 14.11.17.
 * Mail: felix.burk@gmail.com
 */

public class GlobalConstants {

    // Uni Stuttgart - compsci building
    public static final GeoPoint START_POINT = new GeoPoint( 48.745424, 9.106488 );

    // random Geo Point in Moehringen
    public static final GeoPoint SOME_POINT = new GeoPoint(48.727504, 9.138324);

    //default zoom value for the map
    public static final int DEFAULT_ZOOM = 20;

    /**
     * Polygon Test: this is probably inefficient and unreadable as fuck buuut it works, meh
     * creates a rectangle containing n polygons with m points, close to START_POINT  - FB
     * @param numberPolygons n
     * @param numberPoints m
     * @return ArrayList of polygons
     */
    public static List<Polygon> polygonTest(int numberPolygons, int numberPoints){
        //just small numbers to keep the tester from searching the rectangle
        double Max = +0.001;
        double Min = -0.001;

        List<Polygon> polis = new ArrayList<>();
        double initialLat = GlobalConstants.START_POINT.getLatitude();
        double initialLon = GlobalConstants.START_POINT.getLongitude();

        for(int j=0; j<numberPolygons; j++) {
            Polygon p = new Polygon();
            p.setTitle(String.valueOf(String.valueOf(j)));
            p.setFillColor(Color.argb(50,0,0,0));
            List<GeoPoint> points = new ArrayList<>();
            for (int i = 0; i < numberPoints; i++) {
                points.add(new GeoPoint(initialLat +  Min + (Math.random() * ((Max - Min) )),
                        initialLon + Min + (Math.random() * ((Max - Min) ))));
            }
            if(j % (int) Math.sqrt(numberPolygons) == 0){
                initialLon += 0.003;
                initialLat = GlobalConstants.START_POINT.getLatitude();
            }
            initialLat += 0.003;

            p.setPoints(points);
            polis.add(p);
        }

        return polis;
    }

    /**
     * Same as polygonTest, only for fields, state is selected by random
     * @param numberFields
     * @param numberCornerPoints
     * @return
     */
    public static List<Field> fieldTest(int numberFields, int numberCornerPoints){
        //just small numbers to keep the tester from searching the rectangle
        double Max = +0.001;
        double Min = -0.001;

        List<Field> polis = new ArrayList<>();
        double initialLat = GlobalConstants.START_POINT.getLatitude();
        double initialLon = GlobalConstants.START_POINT.getLongitude();

        for(int j=0; j<numberFields; j++) {


            List<CornerPoint> points = new ArrayList<>();
            for (int i = 0; i < numberCornerPoints; i++) {
                points.add(new CornerPoint(initialLat +  Min + (Math.random() * ((Max - Min) )),
                        initialLon + Min + (Math.random() * ((Max - Min) ))));
            }
            if(j % (int) Math.sqrt(numberFields) == 0){
                initialLon += 0.003;
                initialLat = GlobalConstants.START_POINT.getLatitude();
            }
            initialLat += 0.003;

            Field f = new Field(points);
            f.setName(String.valueOf(j));
            f.setState(FieldStates.values()[(int)(Math.random()*FieldStates.values().length)]);

            polis.add(f);
        }

        return polis;
    }


}
