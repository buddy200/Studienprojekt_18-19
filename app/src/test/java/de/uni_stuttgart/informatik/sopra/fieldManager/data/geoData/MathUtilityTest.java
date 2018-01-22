package de.uni_stuttgart.informatik.sopra.fieldManager.data.geoData;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MathUtilityTest {

	@Test
	public void testDegToRad() {
		assertEquals(0, MathUtility.degToRad(0),0.0001);
		assertEquals(Math.PI/2, MathUtility.degToRad(90),0.0001);
		assertEquals(Math.PI, MathUtility.degToRad(180),0.0001);
		assertEquals(Math.PI*2, MathUtility.degToRad(360),0.0001);
		assertEquals(Math.PI/4, MathUtility.degToRad(45),0.0001);
	}

	@Test
	public void testRadToDeg() {
		assertEquals(0, MathUtility.radToDeg(0),0.0001);
		assertEquals(90,MathUtility.radToDeg(Math.PI/2),0.0001);
		assertEquals(180 ,MathUtility.radToDeg(Math.PI),0.0001);
		assertEquals(360,MathUtility.radToDeg(Math.PI*2),0.0001);
		assertEquals(45, MathUtility.radToDeg(Math.PI/4),0.0001);
	}

	@Test
	public void testScalarProduct() {
		Vector v1, v2;
		v1 = new Vector(2, 2);
		v2 = new Vector(2, 2);
		assertEquals(8, MathUtility.scalarProduct(v1, v2),0.0001);
		v1 = new Vector(-2, 2);
		v2 = new Vector(2, 2);
		assertEquals(0, MathUtility.scalarProduct(v1, v2),0.0001);
		v1 = new Vector(-6, 2);
		v2 = new Vector(0, 8);
		assertEquals(16, MathUtility.scalarProduct(v1, v2),0.0001);
		v1 = new Vector(-6, 2);
		v2 = new Vector(5, 2);
		assertEquals(-26, MathUtility.scalarProduct(v1, v2),0.0001);
	}

}
