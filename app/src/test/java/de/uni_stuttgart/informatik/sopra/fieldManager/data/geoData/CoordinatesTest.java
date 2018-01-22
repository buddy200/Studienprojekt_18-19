package de.uni_stuttgart.informatik.sopra.fieldManager.data.geoData;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CoordinatesTest {

	@Test
	public void testUTMCoordinate() {
		UTMCoordinate utm = new UTMCoordinate();
		utm.setEasting(200000);
		utm.setNorthing(500000);
		utm.setZone(31);
		assertEquals(31, utm.getZone());
		assertEquals(500000, utm.getNorthing());
		assertEquals(200000, utm.getEasting());
		assertTrue(utm.equals(utm));
		assertFalse(utm.equals(new UTMCoordinate()));
		assertFalse(utm.equals(new WGS84Coordinate()));
		assertFalse(utm.isSouth());
		utm.setNorthing(-520);
		assertTrue(utm.isSouth());
		assertEquals(-520, utm.getNorthing());
	}
	
	@Test
	public void testWGS84Coordinate() {
		WGS84Coordinate wgs = new WGS84Coordinate(-45, 60);
		assertEquals(-45, wgs.getLatitude(),0.00001);
		assertEquals(60, wgs.getLongitude(),0.00001);
		assertEquals("45.0S 60.0E", wgs.toString());
		assertTrue(wgs.equals(new WGS84Coordinate(-45,60)));
		assertFalse(wgs.equals(new WGS84Coordinate(45,60)));
		assertFalse(wgs.equals(new WGS84Coordinate(-45,50)));
		assertFalse(wgs.equals(new WGS84Coordinate()));
		assertFalse(wgs.equals(new UTMCoordinate()));
		wgs.setLatitude(45);
		wgs.setLongitude(-60);
		assertEquals(45, wgs.getLatitude(),0.00001);
		assertEquals(-60, wgs.getLongitude(),0.00001);
		assertEquals("45.0N 60.0W", wgs.toString());
	}

	@Test
	public void testConverter() {
		//Campus
		WGS84Coordinate wgs = new WGS84Coordinate(48.745218, 9.10670);
		UTMCoordinate utm = WGS84UTMConverter.convert(wgs);
		assertEquals(32, utm.getZone());
		assertEquals(507844, utm.getEasting());
		assertEquals(5399139, utm.getNorthing());
		assertEquals(wgs, WGS84UTMConverter.convert(utm));
		
		//Alice Springs, Australia
		wgs= new WGS84Coordinate(-23.728028, 133.86882);
		utm = WGS84UTMConverter.convert(wgs);
		assertEquals(53, utm.getZone());
		assertEquals(384701, utm.getEasting());
		assertEquals(-2624574, utm.getNorthing());
		assertEquals(wgs, WGS84UTMConverter.convert(utm));
		
		//White House
		wgs= new WGS84Coordinate(38.897711, -77.036561);
		utm = WGS84UTMConverter.convert(wgs);
		assertEquals(18, utm.getZone());
		assertEquals(323389, utm.getEasting());
		assertEquals(4307397, utm.getNorthing());
		assertEquals(wgs, WGS84UTMConverter.convert(utm));
		
	/*	//somewhere in the atlantic ocean , golf of guinea
		wgs= new WGS84Coordinate();
		utm = WGS84UTMConverter.convert(wgs);
		assertEquals(31, utm.getZone());
		assertEquals(166021, utm.getEasting());
		assertEquals(0, utm.getNorthing());
		assertEquals(wgs, WGS84UTMConverter.convert(utm)); */
		
		//Copacabana, Rio de Janeiro
		wgs= new WGS84Coordinate(-22.97180, -43.183043);
		utm = WGS84UTMConverter.convert(wgs);
		assertEquals(23, utm.getZone());
		assertEquals(686265, utm.getEasting());
		assertEquals(-2541551, utm.getNorthing());
		assertEquals(wgs, WGS84UTMConverter.convert(utm));

	}
}
