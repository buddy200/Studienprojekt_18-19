package de.uni_stuttgart.informatik.sopra.sopraapp.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.rtp.AudioGroup;

import java.util.ArrayList;
import java.util.List;

import de.uni_stuttgart.informatik.sopra.sopraapp.data.geoData.WGS84Coordinate;

/**
 * Created by Christian on 11.01.2018.
 */

public class DBConnection {

    private static final String GeoPointTable_Suffix = "GeoPointsOfField";
    private static final String LAT_COLUM = "latitude";
    private static final String LONG_COLUM = "longitude";

    private DBHelper dbHelper;
    private SQLiteDatabase db;
    private Context context;

    public DBConnection(Context context) {
        this.context = context;
        dbHelper = new DBHelper(context);
        db = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public void addField(AgrarianField field) {
        if (field != null) {
            ContentValues values = new ContentValues();

            double size = field.getSize() == null ? 0 : field.getSize();
            values.put(DBHelper.SIZE_COLUM, size);
            values.put(DBHelper.NAME_COLUM, field.getName());
            values.put(DBHelper.COLOR_COLUM, field.getColor());
            values.put(DBHelper.COUNTY_COLUM, field.getCounty());
            values.put(DBHelper.OWNER_COLUM, field.getOwner());

            long rowID = db.insert(DBHelper.AgrarianFieldTable_NAME, null, values);
            field.setID(rowID);

            createGeoPointTable(field);
        }
    }

    public void addField(DamageField field) {
        if (field != null) {
            ContentValues values = new ContentValues();

            double size = field.getSize() == null ? 0 : field.getSize();
            values.put(DBHelper.SIZE_COLUM, size);
            values.put(DBHelper.NAME_COLUM, field.getName());
            values.put(DBHelper.COLOR_COLUM, field.getColor());
            values.put(DBHelper.COUNTY_COLUM, field.getCounty());
            values.put(DBHelper.EVALUATOR_COLUM, field.getEvaluator());
            values.put(DBHelper.DATE_COLUM, field.getParsedDate());

            long rowID = db.insert(DBHelper.DamageFieldTable_NAME, null, values);
            field.setID(rowID);

            createGeoPointTable(field);
        }
    }

    private void createGeoPointTable(AgrarianField field) {
        String table_name = GeoPointTable_Suffix + "_Agr_" + field.getID();
        db.execSQL("CREATE TABLE " + table_name + " (" +
        DBHelper.ID_COLUM + " INTEGER PRIMARY KEY AUTOINCREMENT," +
        LAT_COLUM + " REAL NOT NULL," +
        LONG_COLUM + " REAL NOT NULL");

        for(CornerPoint cp : field.getCornerPoints()) {
            WGS84Coordinate wgs = cp.getWGS();
            ContentValues values = new ContentValues();
            values.put(LAT_COLUM, wgs.getLatitude());
            values.put(LONG_COLUM, wgs.getLongitude());
            db.insert(table_name, null, values);
        }

    }


    private void createGeoPointTable(DamageField field) {
        String table_name = GeoPointTable_Suffix + "_Dmg_" + field.getID();
        db.execSQL("CREATE TABLE " + table_name + " (" +
        DBHelper.ID_COLUM + " INTEGER PRIMARY KEY AUTOINCREMENT," +
        LAT_COLUM + " REAL NOT NULL," +
        LONG_COLUM + " REAL NOT NULL");

        for(CornerPoint cp : field.getCornerPoints()) {
            WGS84Coordinate wgs = cp.getWGS();
            ContentValues values = new ContentValues();
            values.put(LAT_COLUM, wgs.getLatitude());
            values.put(LONG_COLUM, wgs.getLongitude());
            db.insert(table_name, null, values);
        }
    }

    public List<AgrarianField> getAllAgrarianFields() {
        List<AgrarianField> fields = new ArrayList<>();

        Cursor cursor = db.query(DBHelper.AgrarianFieldTable_NAME, null,null,null,null,null,null);
        while(cursor.moveToNext()) {
            long id = cursor.getLong(cursor.getColumnIndex(DBHelper.ID_COLUM));
            List<CornerPoint> cps = new ArrayList<>();
            String table_name = GeoPointTable_Suffix + "_Agr_" + id;
            Cursor cpCursor = db.query(table_name, new String[]{LAT_COLUM, LONG_COLUM},null,null,null,null, DBHelper.ID_COLUM + "ASC");
            while(cpCursor.moveToNext()) {
                double lat = cpCursor.getDouble(cpCursor.getColumnIndex(LAT_COLUM));
                double lon = cpCursor.getDouble(cpCursor.getColumnIndex(LONG_COLUM));
                cps.add(new CornerPoint(lat,lon));
            }

            AgrarianField field = new AgrarianField(context,cps);
            field.setID(id);
            field.setName(cursor.getString(cursor.getColumnIndex(DBHelper.NAME_COLUM)));
            field.setColor(cursor.getInt(cursor.getColumnIndex(DBHelper.COLOR_COLUM)));
            field.setCounty(cursor.getString(cursor.getColumnIndex(DBHelper.COUNTY_COLUM)));
            field.setOwner(cursor.getString(cursor.getColumnIndex(DBHelper.OWNER_COLUM)));

            fields.add(field);
        }

        return fields;
    }

    public List<DamageField> getAllDamgageFields() {
        List<DamageField> fields = new ArrayList<>();

        Cursor cursor = db.query(DBHelper.DamageFieldTable_NAME, null,null,null,null,null,null);
        while(cursor.moveToNext()) {
            long id = cursor.getLong(cursor.getColumnIndex(DBHelper.ID_COLUM));
            List<CornerPoint> cps = new ArrayList<>();
            String table_name = GeoPointTable_Suffix + "_Dmg_" + id;
            Cursor cpCursor = db.query(table_name, new String[]{LAT_COLUM, LONG_COLUM},null,null,null,null, DBHelper.ID_COLUM + "ASC");
            while(cpCursor.moveToNext()) {
                double lat = cpCursor.getDouble(cpCursor.getColumnIndex(LAT_COLUM));
                double lon = cpCursor.getDouble(cpCursor.getColumnIndex(LONG_COLUM));
                cps.add(new CornerPoint(lat,lon));
            }

            DamageField field = new DamageField(context,cps);
            field.setID(id);
            field.setName(cursor.getString(cursor.getColumnIndex(DBHelper.NAME_COLUM)));
            field.setColor(cursor.getInt(cursor.getColumnIndex(DBHelper.COLOR_COLUM)));
            field.setCounty(cursor.getString(cursor.getColumnIndex(DBHelper.COUNTY_COLUM)));
            field.setEvaluator(cursor.getString(cursor.getColumnIndex(DBHelper.EVALUATOR_COLUM)));
            field.setDate(cursor.getString(cursor.getColumnIndex(DBHelper.DATE_COLUM)));

            fields.add(field);
        }

        return fields;
    }
}
