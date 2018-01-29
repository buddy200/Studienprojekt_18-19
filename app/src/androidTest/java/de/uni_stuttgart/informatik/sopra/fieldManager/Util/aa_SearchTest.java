package de.uni_stuttgart.informatik.sopra.fieldManager.Util;


import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;
import java.util.Vector;

import de.uni_stuttgart.informatik.sopra.fieldManager.data.AgrarianField;
import de.uni_stuttgart.informatik.sopra.fieldManager.data.DamageField;
import de.uni_stuttgart.informatik.sopra.fieldManager.data.FieldTypes.AgrarianFieldType;
import de.uni_stuttgart.informatik.sopra.fieldManager.data.FieldTypes.DamageFieldType;
import de.uni_stuttgart.informatik.sopra.fieldManager.data.managers.AppDataManager;

import static junit.framework.TestCase.assertEquals;

/**
 * Created by Felix B. on 27.01.2018.
 */
@RunWith(AndroidJUnit4.class)

public class aa_SearchTest {
    Context context;
    AppDataManager manager;
    AgrarianField field1;
    DamageField dmg1;
    ArrayList<GeoPoint> geoPoints;

    String name = "test";
    String nameDmg = "dmg";
    String owner = "owner";
    String evaluator = "hans";
    String date = "1-1-1999";
    DamageFieldType typeDmg = DamageFieldType.Hail;
    AgrarianFieldType type = AgrarianFieldType.Corn;


    @Before
    public void start(){
        context = InstrumentationRegistry.getTargetContext();
        manager = AppDataManager.getInstance(context);

        geoPoints = new ArrayList<>();
        geoPoints.add(new GeoPoint(48.739905, 9.095832));
        geoPoints.add(new GeoPoint(48.744137, 9.095039));
        geoPoints.add(new GeoPoint(48.744463, 9.107699));
        field1 = new AgrarianField(context, geoPoints);
        field1.setLinesFormField(new ArrayList<Vector<Double>>());
        field1.setName(name);
        field1.setOwner(owner);
        field1.setType(type);

        dmg1 = new DamageField(context, geoPoints, field1);
        dmg1.setName(nameDmg);
        dmg1.setEvaluator(evaluator);
        dmg1.setDate(date);
        dmg1.setType(typeDmg);


    }

    @Test
    public void search(){
        manager.addAgrarianField(field1);
        manager.addDamageField(dmg1);


        assertEquals(field1.getID(), manager.searchAll("test").get(0).getID());
        assertEquals(field1.getID(), manager.searchName("test").get(0).getID());
        assertEquals(field1.getID(), manager.searchOwner("owner").get(0).getID());

        assertEquals(dmg1.getID(), manager.searchAll(nameDmg).get(0).getID());
        assertEquals(dmg1.getID(), manager.searchEvaluator(evaluator).get(0).getID());
        assertEquals(dmg1.getID(), manager.searchDate(date).get(0).getID());


        manager.dbClose();

    }
}