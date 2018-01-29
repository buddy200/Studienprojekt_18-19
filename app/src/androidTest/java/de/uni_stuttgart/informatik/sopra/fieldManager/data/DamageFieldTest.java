package de.uni_stuttgart.informatik.sopra.fieldManager.data;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;
import java.util.Date;

import de.uni_stuttgart.informatik.sopra.fieldManager.R;
import de.uni_stuttgart.informatik.sopra.fieldManager.data.FieldTypes.DamageFieldType;
import de.uni_stuttgart.informatik.sopra.fieldManager.data.FieldTypes.ProgressStatus;

import static org.junit.Assert.*;

/**
 * Created by larsb on 28.01.2018.
 */
@RunWith(AndroidJUnit4.class)
public class DamageFieldTest {
    private AgrarianField agrarianField;
    private DamageField damageField1;
    private DamageField damageField2;
    private ArrayList<GeoPoint> geoPoints;
    private ArrayList<GeoPoint> geoPoints2;
    private Context context;

    @Before
    public void init() {
        context = InstrumentationRegistry.getTargetContext();
        geoPoints = new ArrayList<>();
        geoPoints.add(new GeoPoint(48.739905, 9.095832));
        geoPoints.add(new GeoPoint(48.744137, 9.095039));
        geoPoints.add(new GeoPoint(48.744463, 9.107699));
        agrarianField = new AgrarianField(context, geoPoints);
        damageField1 = new DamageField(context, geoPoints, agrarianField);

        geoPoints2 = new ArrayList<>();
        geoPoints2.add(new GeoPoint(0.0, 0.0));
        geoPoints2.add(new GeoPoint(2.0, 0.0));
        geoPoints2.add(new GeoPoint(2.0, 2.0));
        geoPoints2.add(new GeoPoint(0.0, 2.0));

    }


    @Test
    public void dateTest() throws Exception {
        Date date = new Date(0);
        damageField1.setDate(date);
        assertEquals(context.getResources().getString(R.string.date_label)
                + " " + "01-01-1970", damageField1.getParsedDate());
    }


    @Test
    public void evaluatorTest() throws Exception {
        damageField1.setEvaluator("Müller");
        assertEquals("Müller", damageField1.getEvaluator());
    }

    @Test
    public void photoTest() throws Exception {
        PictureData pictureData = new PictureData("Hallo", "Test.png");
        PictureData pictureData2 = new PictureData("Hallo2", "Test2.png");
        damageField1.setPath(pictureData);
        damageField1.setPath(pictureData2);
        assertEquals("Test.png", damageField1.getPaths().get(0).getImage_path());
        assertEquals("Hallo", damageField1.getPaths().get(0).getImage_title());
        damageField1.deletePhoto(0);
        assertEquals("Test2.png", damageField1.getPaths().get(0).getImage_path());
        assertEquals("Hallo2", damageField1.getPaths().get(0).getImage_title());
        damageField1.deleteAllPhotos();
        assertEquals(0, damageField1.getPaths().size());
    }


    @Test
    public void calcInsuranceAmount() throws Exception {
        assertEquals(197530.027, damageField1.getInsuranceMoney(), 0.1);
    }

    @Test
    public void getParentField() throws Exception {
        assertEquals(agrarianField, damageField1.getParentField());
    }

    @Test
    public void setType() throws Exception {
        damageField1.setType(DamageFieldType.fromString(context.getResources().getString(R.string.storm), context));
        assertEquals(damageField1.getType().toString(context), context.getResources().getString(R.string.storm));
    }

    @Test
    public void progressStatusTest() throws Exception {
        damageField1.setProgressStatus(ProgressStatus.fromString(context.getResources().getString(R.string.progress_status_finish), context));
        assertEquals(context.getResources().getString(R.string.progress_status_finish), damageField1.getProgressStatus().toString(context));
    }
}