package de.uni_stuttgart.informatik.sopra.fieldManager;

import org.junit.Test;

import de.uni_stuttgart.informatik.sopra.fieldManager.data.CornerPoint;
import de.uni_stuttgart.informatik.sopra.fieldManager.data.geoData.UTMCoordinate;
import de.uni_stuttgart.informatik.sopra.fieldManager.data.geoData.WGS84Coordinate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by larsb on 16.11.2017.
 */
public class CornerPointTest {
   
	@Test
	public void testCP() {
		CornerPoint cp = new CornerPoint(48.74522, 9.10670);
		assertEquals(new WGS84Coordinate(48.74522, 9.10670), cp.getWGS());
		UTMCoordinate utm = new UTMCoordinate();
		utm.setZone(32);
		utm.setEasting(507844);
		utm.setNorthing(5399139);
		assertEquals(utm, cp.getUtm());
		assertEquals(0, cp.getAngle(),0.0001);
		
	}
	
	@Test
	public void testAngle() {
		CornerPoint cp  = new CornerPoint(0, 0);
		CornerPoint cp1  = new CornerPoint(0, 1);
		CornerPoint cp2  = new CornerPoint(1,0);
		
		cp.calculateAngle(cp1, cp2);
		assertEquals(Math.PI/2, cp.getAngle(), 0.001);
		
		cp.calculateAngle(cp2, cp1);
		assertEquals(3*Math.PI/2, cp.getAngle(), 0.001);
		
		cp.calculateAngle(cp1, cp);
		assertEquals(0, cp.getAngle(), 0.001);
		cp.calculateAngle(cp, cp);
		assertEquals(0, cp.getAngle(), 0.001);
		
		assertTrue(cp.equals(cp));
		assertFalse(cp.equals(cp2));
		assertFalse(cp.equals(cp1));
		assertFalse(cp.equals(new UTMCoordinate()));

		cp1 = new CornerPoint(-1,0);
		cp.calculateAngle(cp1, cp2);
		assertEquals(Math.PI, cp.getAngle(), 0.001);

	}
}