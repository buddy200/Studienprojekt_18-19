package de.uni_stuttgart.informatik.sopra.sopraapp.data.geoData;

import org.junit.Test;

import de.uni_stuttgart.informatik.sopra.sopraapp.data.CornerPoint;

import static org.junit.Assert.assertEquals;

public class TriangleTest {

	@Test
	public void testTriangle() {
		//the field of the Mercedes-Benz-Arena (size 7140m?)
		CornerPoint a = new CornerPoint(48.79277, 9.23183);
		CornerPoint b = new CornerPoint(48.79229, 9.23122);
		CornerPoint c = new CornerPoint(48.79171, 9.23231);
		CornerPoint d = new CornerPoint(48.79220, 9.23292);
		
		Triangle t1 = new Triangle(a, b, c);
		Triangle t2 = new Triangle(a, d, c);
		
		assertEquals(3570, t1.getSize(),15);
		assertEquals(3570, t2.getSize(),15);
	}

	

}
