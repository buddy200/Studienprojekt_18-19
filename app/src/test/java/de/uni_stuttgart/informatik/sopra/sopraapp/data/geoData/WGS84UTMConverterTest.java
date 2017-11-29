package de.uni_stuttgart.informatik.sopra.sopraapp.data.geoData;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by larsb on 16.11.2017.
 */
public class WGS84UTMConverterTest {
    @Test
    public void convert() throws Exception {
        assertEquals(null, WGS84UTMConverter.convert(new UTMCoordinate()));
    }

    @Test
    public void convert1() throws Exception {
    }

}