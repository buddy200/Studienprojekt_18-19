package de.uni_stuttgart.informatik.sopra.fieldManager.data;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;

import de.uni_stuttgart.informatik.sopra.fieldManager.MainActivity;

import static org.junit.Assert.*;

/**
 * Created by larsb on 27.01.2018.
 */
@RunWith(AndroidJUnit4.class)

public class AgrarianFieldTest {
    Context context;
    AgrarianField field;
    ArrayList<GeoPoint> geoPoints;
    String s;
    @Before
    public void setup(){
        context = InstrumentationRegistry.getTargetContext();
        s = context.getResources().getString(de.uni_stuttgart.informatik.sopra.fieldManager.R.string.my_location);
        geoPoints = new ArrayList<>();
        geoPoints.add(new GeoPoint(0.0,0.0));
        geoPoints.add(new GeoPoint(1.0, 1.0));
        geoPoints.add(new GeoPoint(1.0, 0.0));
        field = new AgrarianField(context, geoPoints);
    }
    @Test
    public void setContainedDamageFields() throws Exception {
        assertEquals("Mais" , field.getType().toString(context));
    }

    @Test
    public void getContainedDamageFields() throws Exception {
    }

    @Test
    public void addContainedDamageField() throws Exception {
    }

    @Test
    public void setOwner() throws Exception {
    }

    @Test
    public void getOwner() throws Exception {
    }

    @Test
    public void getLinesFormField() throws Exception {
    }

    @Test
    public void setLinesFormField() throws Exception {
    }

    @Test
    public void setType() throws Exception {
    }

}