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
import de.uni_stuttgart.informatik.sopra.fieldManager.data.DamageField;

import static org.junit.Assert.*;

/**
 * Created by larsb on 28.01.2018.
 */
@RunWith(AndroidJUnit4.class)
public class PointInfFieldTest {
    private AgrarianField agrarianField;
    private IntersectionCalculator isc;
    private PointOutOfField pointOutOfField;
    private ArrayList<GeoPoint> geoPoints;
    private Context context;
    private ArrayList<Vector<Double>> lines;

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
        agrarianField.setLinesFormField(lines);;
        pointOutOfField = new PointOutOfField();
    }

    @Test
    public void pointOutOfField() throws Exception {


        assertEquals(false, pointOutOfField.pointInField(agrarianField.getLinesFormField(), agrarianField.getCentroid(), new GeoPoint(0.0, 0.0)));
        assertEquals(false, pointOutOfField.pointInField(agrarianField.getLinesFormField(), agrarianField.getCentroid(), new GeoPoint(2.0, 0.0)));
        assertEquals(false, pointOutOfField.pointInField(agrarianField.getLinesFormField(), agrarianField.getCentroid(), new GeoPoint(0.0, 2.0)));
        assertEquals(false, pointOutOfField.pointInField(agrarianField.getLinesFormField(), agrarianField.getCentroid(), new GeoPoint(2.0, 2.0)));

    }

    @Test
    public void pointInField() {
        assertEquals(true, pointOutOfField.pointInField(agrarianField.getLinesFormField(), agrarianField.getCentroid(), new GeoPoint(0.6, 0.6)));
        assertEquals(true, pointOutOfField.pointInField(agrarianField.getLinesFormField(), agrarianField.getCentroid(), new GeoPoint(1.2, 0.6)));
        assertEquals(true, pointOutOfField.pointInField(agrarianField.getLinesFormField(), agrarianField.getCentroid(), new GeoPoint(0.6, 1.2)));
        assertEquals(true, pointOutOfField.pointInField(agrarianField.getLinesFormField(), agrarianField.getCentroid(), new GeoPoint(1.2, 1.2)));
    }

}