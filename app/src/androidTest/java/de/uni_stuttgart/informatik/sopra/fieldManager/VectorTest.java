package de.uni_stuttgart.informatik.sopra.fieldManager;

import org.junit.Test;

import de.uni_stuttgart.informatik.sopra.fieldManager.data.geoData.Vector;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by Christian on 29.11.2017.
 */
public class VectorTest {
    @Test
    public void getLength() throws Exception {
        Vector v;
        v = new Vector(4,3);
        assertEquals(4, v.getX(),0.00001);
        assertEquals(3, v.getY(),0.00001);
        assertEquals(5, v.getLength(), 0.0001);
        v = new Vector(-4,3);
        assertEquals(5, v.getLength(), 0.0001);
        v = new Vector(4,-3);
        assertEquals(5, v.getLength(), 0.0001);
        v = new Vector(-4,-3);
        assertEquals(5, v.getLength(), 0.0001);
        v = new Vector(0,0);
        assertEquals(0, v.getLength(), 0.0001);
        v = new Vector(1,1);
        assertEquals(1.414213, v.getLength(), 0.001);
    }

    @Test
    public void normalize() throws Exception {
        Vector v;
        v = new Vector(4,3);;
        assertEquals(1, v.normalize().getLength(),0.0001);
        assertEquals(new Vector(4/5.0, 3/5.0), v.normalize());
        v = new Vector(-4,3);
        assertEquals(new Vector(-4/5.0, 3/5.0), v.normalize());
        v = new Vector(4,-3);
        assertEquals(new Vector(4/5.0, -3/5.0), v.normalize());
        v = new Vector(-4,-3);
        assertEquals(new Vector(-4/5.0, -3/5.0), v.normalize());
        v = new Vector(0,0);
        assertEquals(new Vector(0, 0), v.normalize());
    }

    @Test
    public void equalDirection() throws Exception {
        assertTrue(new Vector(2,4).equalDirection(new Vector(1,2)));
        assertTrue(new Vector(-2,4).equalDirection(new Vector(-1,2)));
        assertTrue(new Vector(2,-4).equalDirection(new Vector(1,-2)));
        assertTrue(new Vector(-2,-4).equalDirection(new Vector(-1,-2)));
        assertFalse(new Vector(1,3).equalDirection(new Vector(1,2)));
        assertFalse(new Vector(5,3).equalDirection(new Vector(1,2)));
        assertFalse(new Vector(1.6,3).equalDirection(new Vector(1,2)));
    }

    @Test
    public void equals() throws Exception {
        Vector v = new Vector(5,5);
        assertTrue(v.equals(v));
        assertFalse(v.equals(new Integer(3)));
        assertFalse(v.equals(new Vector(1,2)));
        assertFalse(v.equals(new Vector(5,2)));
    }

    @Test
    public void rotate() throws Exception {
        Vector v = new Vector(0,1);
        assertEquals(new Vector(0,-1), v.rotate(Math.PI));
        assertEquals(new Vector(-1,0), v.rotate(Math.PI/2));
        assertEquals(new Vector(1,0), v.rotate((Math.PI/2)*3));
    }

    @Test
    public void toStringTest() throws Exception {
        Vector v = new Vector(0,3);
        assertEquals("Vector X: 0.0 Y: 3.0", v.toString());
    }

}