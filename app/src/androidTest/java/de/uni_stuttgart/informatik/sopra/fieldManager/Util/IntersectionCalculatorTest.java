package de.uni_stuttgart.informatik.sopra.fieldManager.Util;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;
import java.util.Vector;

import de.uni_stuttgart.informatik.sopra.fieldManager.data.AgrarianField;

import static org.junit.Assert.*;

/**
 * Created by larsb on 28.01.2018.
 */
@RunWith(AndroidJUnit4.class)
public class IntersectionCalculatorTest {
    private AgrarianField agrarianField;
    private IntersectionCalculator isc;
    private IntersectionCalculator isc2;
    private ArrayList<GeoPoint> geoPoints;
    private ArrayList<GeoPoint> geoPoints2;
    private Context context;
    private ArrayList<Vector<Double>> lines;
    private ArrayList<Vector<Double>> lines2;

    @Before
    public void init() {
        context = InstrumentationRegistry.getTargetContext();
        geoPoints = new ArrayList<>();
        lines = new ArrayList<>();
        isc = new IntersectionCalculator(geoPoints, lines);

    }

    @Test
    public void calculateLine() throws Exception {
        geoPoints.add(new GeoPoint(0.0, 0.0));
        isc.calculateLine(true);
        assertEquals(0, lines.size());
        geoPoints.add(new GeoPoint(0.0, 2.0));
        isc.calculateLine(true);
        assertEquals(1, lines.size());
        assertEquals(0.0000000000000000000000000001, lines.get(0).get(0).doubleValue(), 0);
        assertEquals(2, lines.get(0).get(1).doubleValue(), 0.0001);
        geoPoints.add((new GeoPoint(2.0, 2.0)));
        isc.calculateLine(true);
        assertEquals(2, lines.size());
        assertEquals(100000000000000000000000000000.0, lines.get(1).get(0).doubleValue(), 0);
        assertEquals(-200000000000000000000000000000.0, lines.get(1).get(1).doubleValue(), 0.0001);
        geoPoints.add(new GeoPoint(0.0, 2.0));
        isc.calculateLine(true);
        assertEquals(3, lines.size());
        assertEquals(100000000000000000000000000000.0, lines.get(2).get(0).doubleValue(), 0);
        assertEquals(2, lines.get(2).get(1).doubleValue(), 0.0001);
        isc.calcLastLine();
        assertEquals(4, lines.size());
        assertEquals(0.0000000000000000000000000001, lines.get(3).get(0).doubleValue(), 0);
        assertEquals(0.0, lines.get(3).get(1).doubleValue(), 0.0001);
    }

    @Before
    public void initCalcIntersection() {
        context = InstrumentationRegistry.getTargetContext();
        geoPoints = new ArrayList<>();
        lines = new ArrayList<>();
        isc = new IntersectionCalculator(geoPoints, lines);
        geoPoints.add(new GeoPoint(1.0, 0.0));
        isc.calculateLine(true);
        geoPoints.add(new GeoPoint(0.0, 1.0));
        isc.calculateLine(true);
        geoPoints.add(new GeoPoint(1.0, 2.0));
        isc.calculateLine(true);
        geoPoints.add(new GeoPoint(2.0, 1.0));
        isc.calculateLine(true);
        isc.calcLastLine();
        agrarianField = new AgrarianField(context, geoPoints);
        agrarianField.setLinesFormField(lines);
        geoPoints2 = new ArrayList<>();
        lines2 = new ArrayList<>();
        isc2 = new IntersectionCalculator(geoPoints2, lines2);
        geoPoints2.add(new GeoPoint(1.0, 1.0));
        isc2.calculateLine(false);
    }

    @Test
    public void calcIntersectionFalse() throws Exception {
        geoPoints2.add(new GeoPoint(0.0, 0.0));
        isc2.calculateLine(false);
        assertEquals(false, isc2.calcIntersection(agrarianField));

        geoPoints2.remove(1);
        geoPoints2.add(new GeoPoint(2.0, 0.0));
        isc2.calculateLine(false);
        assertEquals(false, isc2.calcIntersection(agrarianField));

        geoPoints2.remove(1);
        geoPoints2.add(new GeoPoint(2.0, 2.0));
        isc2.calculateLine(false);
        assertEquals(false, isc2.calcIntersection(agrarianField));

        geoPoints2.remove(1);
        geoPoints2.add(new GeoPoint(0.0, 2.0));
        isc2.calculateLine(false);
        assertEquals(false, isc2.calcIntersection(agrarianField));
        geoPoints2.remove(1);
    }

    @Test
    public void clacintersectionTrue() {

        geoPoints2.add(new GeoPoint(0.6, 0.6));
        isc2.calculateLine(false);
        assertEquals(true, isc2.calcIntersection(agrarianField));

        geoPoints2.add(new GeoPoint(1.2, 1.2));
        isc2.calculateLine(false);
        assertEquals(true, isc2.calcIntersection(agrarianField));

        geoPoints2.add(new GeoPoint(0.6, 1.2));
        isc2.calculateLine(false);
        assertEquals(true, isc2.calcIntersection(agrarianField));

        geoPoints2.add(new GeoPoint(1.2, 0.6));
        isc2.calculateLine(false);
        assertEquals(true, isc2.calcIntersection(agrarianField));
    }

}