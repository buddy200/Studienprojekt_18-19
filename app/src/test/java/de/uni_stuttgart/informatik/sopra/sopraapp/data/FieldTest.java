package de.uni_stuttgart.informatik.sopra.sopraapp.data;

import android.content.Context;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;

import de.uni_stuttgart.informatik.sopra.sopraapp.data.geoData.UTMCoordinate;
import de.uni_stuttgart.informatik.sopra.sopraapp.data.geoData.WGS84Coordinate;
import de.uni_stuttgart.informatik.sopra.sopraapp.data.geoData.WGS84UTMConverter;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;


@RunWith(MockitoJUnitRunner.class)
public class FieldTest {

    Context context;

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
        context = mock(Context.class);
        doReturn("Sample Hello world string")
                .when(context)
                .getString(any(Integer.class));

    }

    @Test
    public void testSizeCalculation() {
        //the field of the Mercedes-Benz-Arena (size 7140m²)
        CornerPoint a = new CornerPoint(48.79277, 9.23183);
        CornerPoint b = new CornerPoint(48.79229, 9.23122);
        CornerPoint c = new CornerPoint(48.79171, 9.23231);
        CornerPoint d = new CornerPoint(48.79220, 9.23292);

        ArrayList<CornerPoint> cpS = new ArrayList<>();
        cpS.add(a);
        cpS.add(b);
        cpS.add(c);
        cpS.add(d);
        Field field = new AgrarianField(context, cpS);
        field.finish();

        assertEquals(7140, field.getSize(), 10);
        assertTrue(field.getCornerPoints().size() == 4);

        ArrayList<CornerPoint> cpSS = new ArrayList<>();
        cpSS.add(d);
        cpSS.add(c);
        cpSS.add(b);
        cpSS.add(a);
        Field field2 = new AgrarianField(context, cpS);

        field2.finish();
        assertEquals(7140, field2.getSize(), 10);
    }



    @Test
    public void testConcaveField() {
        CornerPoint a = new CornerPoint(48.79277, 9.23183);
        CornerPoint b = new CornerPoint(48.79229, 9.23122);
        CornerPoint c = new CornerPoint(48.79171, 9.23231);
        CornerPoint d = new CornerPoint(48.79220, 9.23292);
        CornerPoint e = new CornerPoint(48.79245, 9.23235);

        ArrayList<CornerPoint> cpSSS = new ArrayList<>();
        cpSSS.add(a);
        cpSSS.add(b);
        cpSSS.add(c);
        cpSSS.add(d);
        cpSSS.add(e);
        Field field = new AgrarianField(context, cpSSS);
        assertEquals(6940, field.getSize(), 10);
    }


    @Test
    public void testU() {
        ArrayList<CornerPoint> cpSSSS = new ArrayList<>();
        UTMCoordinate utm = new UTMCoordinate();
        utm.setZone(31);
        utm.setEasting(500000);
        utm.setNorthing(5400000);
        WGS84Coordinate wgs = WGS84UTMConverter.convert(utm);
        CornerPoint cp1 = new CornerPoint(wgs.getLatitude(), wgs.getLongitude());
        cpSSSS.add(cp1);

        utm.setEasting(499999);
        wgs = WGS84UTMConverter.convert(utm);
        cp1 = new CornerPoint(wgs.getLatitude(), wgs.getLongitude());
        cpSSSS.add(cp1);

        utm.setNorthing(5400001);
        wgs = WGS84UTMConverter.convert(utm);
        cp1 = new CornerPoint(wgs.getLatitude(), wgs.getLongitude());
        cpSSSS.add(cp1);

        utm.setEasting(500001);
        wgs = WGS84UTMConverter.convert(utm);
        cp1 = new CornerPoint(wgs.getLatitude(), wgs.getLongitude());
        cpSSSS.add(cp1);

        utm.setNorthing(5399998);
        wgs = WGS84UTMConverter.convert(utm);
        cp1 = new CornerPoint(wgs.getLatitude(), wgs.getLongitude());
        cpSSSS.add(cp1);

        utm.setEasting(499999);
        wgs = WGS84UTMConverter.convert(utm);
        cp1 = new CornerPoint(wgs.getLatitude(), wgs.getLongitude());
        cpSSSS.add(cp1);

        utm.setNorthing(5399999);
        wgs = WGS84UTMConverter.convert(utm);
        cp1 = new CornerPoint(wgs.getLatitude(), wgs.getLongitude());
        cpSSSS.add(cp1);

        utm.setEasting(500000);
        wgs = WGS84UTMConverter.convert(utm);
        cp1 = new CornerPoint(wgs.getLatitude(), wgs.getLongitude());
        cpSSSS.add(cp1);

        Field field = new AgrarianField(context, null);
        field.finish();
        assertEquals(5, field.getSize(),0.0001);
    }

}
