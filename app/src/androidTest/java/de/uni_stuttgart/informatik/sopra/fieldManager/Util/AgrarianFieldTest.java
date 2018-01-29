package de.uni_stuttgart.informatik.sopra.fieldManager.Util;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;
import java.util.Vector;

import de.uni_stuttgart.informatik.sopra.fieldManager.R;
import de.uni_stuttgart.informatik.sopra.fieldManager.data.AgrarianField;
import de.uni_stuttgart.informatik.sopra.fieldManager.data.DamageField;
import de.uni_stuttgart.informatik.sopra.fieldManager.data.FieldTypes.AgrarianFieldType;

import static org.junit.Assert.*;

/**
 * Created by larsb on 27.01.2018.
 */
@RunWith(AndroidJUnit4.class)

public class AgrarianFieldTest {

    Context context;
    AgrarianField field1;
    AgrarianField field2;
    ArrayList<GeoPoint> geoPoints;
    ArrayList<GeoPoint> geoPoints2;

    @Before
    public void setup() {
        context = InstrumentationRegistry.getTargetContext();
        geoPoints = new ArrayList<>();
        geoPoints.add(new GeoPoint(48.739905, 9.095832));
        geoPoints.add(new GeoPoint(48.744137, 9.095039));
        geoPoints.add(new GeoPoint(48.744463, 9.107699));
        field1 = new AgrarianField(context, geoPoints);

        geoPoints2 = new ArrayList<>();
        geoPoints2.add(new GeoPoint(0.0, 0.0));
        geoPoints2.add(new GeoPoint(2.0, 0.0));
        geoPoints2.add(new GeoPoint(2.0, 2.0));
        geoPoints2.add(new GeoPoint(0.0, 2.0));


        field2 = new AgrarianField(context, geoPoints2);


    }

    @Test
    public void containedDamageFieldTest() throws Exception {
        DamageField dmf1 = new DamageField(context, geoPoints, field1);
        DamageField dmf2 = new DamageField(context, geoPoints, field1);
        DamageField dmf3 = new DamageField(context, geoPoints, field1);
        ArrayList<DamageField> dmfl = new ArrayList<>();
        dmfl.add(dmf1);
        dmfl.add(dmf2);
        field1.setContainedDamageFields(dmfl);
        field1.addContainedDamageField(dmf3);
        assertEquals(dmf1, field1.getContainedDamageFields().get(0));
        assertEquals(dmf2, field1.getContainedDamageFields().get(1));
        assertEquals(dmf3, field1.getContainedDamageFields().get(2));

    }



    @Test
    public void ownerTest() throws Exception {
        field1.setOwner("Maier");
        assertEquals("Maier", field1.getOwner());
    }

    @Test
    public void linesFormFieldTest() throws Exception {
        ArrayList<Vector<Double>> vec1 = new ArrayList<>();
        field1.setLinesFormField(vec1);
        assertEquals(vec1, field1.getLinesFormField());
    }


    @Test
    public void calculateSize() throws Exception {
        field1.calculateSize();
        assertTrue(((100 / field1.getSize()) * 219982.1) > 99.5 && ((100 / field1.getSize()) * 219982.1) < 100.5);
    }

    @Test
    public void calculateCentroid() throws Exception {
        assertEquals(new GeoPoint(1.0, 1.0), field2.calculateCentroid());
    }

    @Test
    public void getGeoPoints() throws Exception {
        assertEquals(geoPoints, field1.getGeoPoints());
    }


    @Test
    public void nameTest() throws Exception {
        field1.setName("Test1");
        assertEquals("Test1", field1.getName());
    }

    @Test
    public void countytest() throws Exception {
        field1.setCounty("Stuttgart");
        assertEquals("Stuttgart", field1.getCounty());
    }


    @Test
    public void convertSize() throws Exception {
        assertEquals(("10.0m" + "\u00B2"), field1.convertSize(10));
        assertEquals(("50.0a"), field1.convertSize(5000));
        assertEquals("50.0ha", field1.convertSize(500000));
        assertEquals("50.0km" + "\u00B2", field1.convertSize(50000000));
    }

    @Test
    public void typeTest() throws Exception {
        field1.setType(AgrarianFieldType.Corn);
        assertEquals(context.getResources().getString(R.string.corn), field1.getType().toString(context));
//
        assertEquals(1.0, field1.getType().getInsuranceMoneyPerSquareMeter(), 0);
        field1.setType(AgrarianFieldType.Hemp);
        assertEquals(context.getResources().getString(R.string.hemp), field1.getType().toString(context));
  //      assertEquals(context.getResources().getColor(R.color.hempTypeAgrarian), field1.getColor());
        assertEquals(1.1, field1.getType().getInsuranceMoneyPerSquareMeter(), 0);
        field1.setType(AgrarianFieldType.Potatoes);
        assertEquals(context.getResources().getString(R.string.potatoes), field1.getType().toString(context));
   //     assertEquals(context.getResources().getColor(R.color.potatoesTypeAgrarian), field1.getColor());
        assertEquals(1.2, field1.getType().getInsuranceMoneyPerSquareMeter(), 0);
        field1.setType(AgrarianFieldType.Rye);
        assertEquals(context.getResources().getString(R.string.rye), field1.getType().toString(context));
     //   assertEquals(context.getResources().getColor(R.color.ryeTypeAgrarian), field1.getColor());
        assertEquals(0.7, field1.getType().getInsuranceMoneyPerSquareMeter(), 0);
        field1.setType(AgrarianFieldType.Wheat);
        assertEquals(context.getResources().getString(R.string.wheat), field1.getType().toString(context));
       // assertEquals(context.getResources().getColor(R.color.wheatTypeAgrarian), field1.getColor());
        assertEquals(0.5, field1.getType().getInsuranceMoneyPerSquareMeter(), 0);
    }


    @Test
    public void testID() throws Exception {
        field1.setID(2);
        assertEquals(2, field1.getID());
    }


}

