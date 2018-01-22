package de.uni_stuttgart.informatik.sopra.fieldManager;

import android.content.Context;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.InstrumentationTestCase;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import java.util.ArrayList;

import de.uni_stuttgart.informatik.sopra.fieldManager.data.AgrarianField;
import de.uni_stuttgart.informatik.sopra.fieldManager.data.CornerPoint;
import de.uni_stuttgart.informatik.sopra.fieldManager.data.Field;
import de.uni_stuttgart.informatik.sopra.fieldManager.data.geoData.UTMCoordinate;
import de.uni_stuttgart.informatik.sopra.fieldManager.data.geoData.WGS84Coordinate;
import de.uni_stuttgart.informatik.sopra.fieldManager.data.geoData.WGS84UTMConverter;

@RunWith(AndroidJUnit4.class)
public class FieldTest extends InstrumentationTestCase {

    @Rule
    public ActivityTestRule<MainActivity> rule = new ActivityTestRule<MainActivity>(
            MainActivity.class);

    Context context;
    @Test
    public void useAppContext() throws Exception {
        MainActivity activity = rule.getActivity();
        context = activity;
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

        assertEquals(7140, field.getSize(), 10);
        assertTrue(field.getCornerPoints().size() == 4);

        ArrayList<CornerPoint> cpSS = new ArrayList<>();
        cpSS.add(d);
        cpSS.add(c);
        cpSS.add(b);
        cpSS.add(a);
        Field field2 = new AgrarianField(context, cpS);

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

        Field field = new AgrarianField(context, cpSSSS);
        field.finish();
        assertEquals(5, field.getSize(),0.0001);
    }

}
