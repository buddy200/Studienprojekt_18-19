package de.uni_stuttgart.informatik.sopra.sopraapp.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import de.uni_stuttgart.informatik.sopra.sopraapp.data.FieldTypes.AgrarianFieldType;
import de.uni_stuttgart.informatik.sopra.sopraapp.data.FieldTypes.DamageFieldType;
import de.uni_stuttgart.informatik.sopra.sopraapp.data.FieldTypes.ProgressStatus;
import de.uni_stuttgart.informatik.sopra.sopraapp.data.geoData.WGS84Coordinate;

/**
 * Created by Christian on 11.01.2018.
 */

public class DBConnection {

    private static final String GeoPointTable_Suffix = "GeoPointsOfField";
    private static final String LAT_COLUM = "latitude";
    private static final String LONG_COLUM = "longitude";
    private static final String VectorTable_Suffix = "Vectors_";
    private static final String X_COLUM = "x";
    private static final String Y_COLUM = "y";

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
            values.put(DBHelper.SIZE_COLUM, field.getSize());
            values.put(DBHelper.NAME_COLUM, field.getName());
            values.put(DBHelper.TYPE_COLUM, field.getType().toString());
            values.put(DBHelper.COUNTY_COLUM, field.getCounty());
            values.put(DBHelper.OWNER_COLUM, field.getOwner());

            long rowID = db.insert(DBHelper.AgrarianFieldTable_NAME, null, values);
            field.setID(rowID);

            createVectorTable(field);
            createGeoPointTable(field);
        }
    }

    private void createVectorTable(AgrarianField field) {
        String table_name = VectorTable_Suffix + field.getID();
        db.execSQL("CREATE TABLE IF NOT EXISTS " + table_name + " (" +
        X_COLUM + " REAL NOT NULL," +
        Y_COLUM + " REAL NOT NULL)");

        for (java.util.Vector<Double> v : field.getLinesFormField()) {
            ContentValues values = new ContentValues();
            values.put(X_COLUM, v.get(0));
            values.put(Y_COLUM, v.get(1));

            db.insert(table_name, null, values);
        }
    }

    public void addField(DamageField field) {
        if (field != null) {
            ContentValues values = new ContentValues();
            values.put(DBHelper.SIZE_COLUM, field.getSize());
            values.put(DBHelper.NAME_COLUM, field.getName());
            values.put(DBHelper.TYPE_COLUM, field.getType().toString());
            values.put(DBHelper.COUNTY_COLUM, field.getCounty());
            values.put(DBHelper.EVALUATOR_COLUM, field.getEvaluator());
            values.put(DBHelper.DATE_COLUM, field.getParsedDate());
            values.put(DBHelper.PROGRESS_COLUM, field.getProgressStatus().toString());
            values.put(DBHelper.PARENT_COLUM, field.getParentField().getID());

            long rowID = db.insert(DBHelper.DamageFieldTable_NAME, null, values);
            field.setID(rowID);

            for(PictureData pd : field.getPaths()) {
                ContentValues pd_value = new ContentValues();
                values.put(DBHelper.PARENT_COLUM, field.getID());
                values.put(DBHelper.NAME_COLUM, pd.getImage_title());
                values.put(DBHelper.PATH_COLUM, pd.getImage_path());
            }

            createGeoPointTable(field);
        }
    }

    private void createGeoPointTable(AgrarianField field) {
        String table_name = GeoPointTable_Suffix + "_Agr_" + field.getID();
        createGeoPointTable(field, table_name);
    }


    private void createGeoPointTable(DamageField field) {
        String table_name = GeoPointTable_Suffix + "_Dmg_" + field.getID();
        createGeoPointTable(field, table_name);
    }

    private void createGeoPointTable(Field field, String table_name) {
        db.execSQL("CREATE TABLE " + table_name + " (" +
        DBHelper.ID_COLUM + " INTEGER PRIMARY KEY AUTOINCREMENT," +
        LAT_COLUM + " REAL NOT NULL," +
        LONG_COLUM + " REAL NOT NULL)");

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
            AgrarianField field = toAgrarianField(cursor);

            fields.add(field);
        }

        return fields;
    }

    public AgrarianField getAgrarianFieldByID(long id) {
        String[] selection_args = new String[1];
        selection_args[0] = Long.toString(id);
        Cursor cursor = db.query(DBHelper.AgrarianFieldTable_NAME,null, (DBHelper.ID_COLUM + "= ?"), selection_args, null, null,null);
        if(cursor.moveToNext()){
            return toAgrarianField(cursor);
        }
        return null;
    }

    private AgrarianField toAgrarianField(Cursor cursor) {
        long id = cursor.getLong(cursor.getColumnIndex(DBHelper.ID_COLUM));
        List<CornerPoint> cps = new ArrayList<>();
        ArrayList<java.util.Vector<Double>> vectorList = new ArrayList<>();
        String table_name = GeoPointTable_Suffix + "_Agr_" + id;
        Cursor cpCursor = db.query(table_name, new String[]{LAT_COLUM, LONG_COLUM},null,null,null,null, DBHelper.ID_COLUM + " ASC");
        while(cpCursor.moveToNext()) {
            double lat = cpCursor.getDouble(cpCursor.getColumnIndex(LAT_COLUM));
            double lon = cpCursor.getDouble(cpCursor.getColumnIndex(LONG_COLUM));
            cps.add(new CornerPoint(lat,lon));
        }

        String vectorTable = VectorTable_Suffix + id;
        Cursor vCursor = db.query(vectorTable, null, null,null,null,null,null);
        while(vCursor.moveToNext()) {
            java.util.Vector<Double> v = new Vector<>();
            v.add(0, vCursor.getDouble(vCursor.getColumnIndex(X_COLUM)));
            v.add(1, vCursor.getDouble(vCursor.getColumnIndex(Y_COLUM)));
            vectorList.add(v);
        }

        AgrarianField field = new AgrarianField(context,cps);
        field.setID(id);
        field.setName(cursor.getString(cursor.getColumnIndex(DBHelper.NAME_COLUM)));
        field.setType(AgrarianFieldType.fromString(cursor.getString(cursor.getColumnIndex(DBHelper.TYPE_COLUM))));
        field.setCounty(cursor.getString(cursor.getColumnIndex(DBHelper.COUNTY_COLUM)));
        field.setOwner(cursor.getString(cursor.getColumnIndex(DBHelper.OWNER_COLUM)));
        field.setLinesFormField(vectorList);
        return field;
    }

    public List<DamageField> getAllDamgageFields() {
        List<DamageField> fields = new ArrayList<>();

        Cursor cursor = db.query(DBHelper.DamageFieldTable_NAME, null,null,null,null,null,null);
        while(cursor.moveToNext()) {
            DamageField field = toDamageField(cursor);

            fields.add(field);
        }

        return fields;
    }

    private DamageField toDamageField(Cursor cursor) {
        long id = cursor.getLong(cursor.getColumnIndex(DBHelper.ID_COLUM));
        List<CornerPoint> cps = new ArrayList<>();
        String table_name = GeoPointTable_Suffix + "_Dmg_" + id;
        Cursor cpCursor = db.query(table_name, new String[]{LAT_COLUM, LONG_COLUM},null,null,null,null, DBHelper.ID_COLUM + " ASC");
        while(cpCursor.moveToNext()) {
            double lat = cpCursor.getDouble(cpCursor.getColumnIndex(LAT_COLUM));
            double lon = cpCursor.getDouble(cpCursor.getColumnIndex(LONG_COLUM));
            cps.add(new CornerPoint(lat,lon));
        }
        long parent_ID = cursor.getLong(cursor.getColumnIndex(DBHelper.PARENT_COLUM));
        AgrarianField parent = getAgrarianFieldByID(parent_ID);

        DamageField field = new DamageField(context,cps, parent);
        field.setID(id);
        field.setName(cursor.getString(cursor.getColumnIndex(DBHelper.NAME_COLUM)));
        field.setType(DamageFieldType.fromString(cursor.getString(cursor.getColumnIndex(DBHelper.TYPE_COLUM))));
        field.setCounty(cursor.getString(cursor.getColumnIndex(DBHelper.COUNTY_COLUM)));
        field.setEvaluator(cursor.getString(cursor.getColumnIndex(DBHelper.EVALUATOR_COLUM)));
        field.setDate(cursor.getString(cursor.getColumnIndex(DBHelper.DATE_COLUM)));
        field.setProgressStatus(ProgressStatus.fromString(cursor.getString(cursor.getColumnIndex(DBHelper.PROGRESS_COLUM))));
        List<PictureData> pictureData = getPicturesOfField(id);
        for(PictureData pd : pictureData) {
            field.setPath(pd);
        }
        return field;
    }


    public void addPictureToField(long field_id, PictureData pd) {
        ContentValues values = new ContentValues();
        values.put(DBHelper.PARENT_COLUM, field_id);
        values.put(DBHelper.NAME_COLUM, pd.getImage_title());
        values.put(DBHelper.PATH_COLUM, pd.getImage_path());
        db.insert(DBHelper.ImageTable_NAME,null,values);
    }

    public void deletePicture(PictureData pd) {
        String[] selction_args = new String[2];
        selction_args[0] = pd.getImage_title();
        selction_args[1] = pd.getImage_path();

        db.delete(DBHelper.ImageTable_NAME, DBHelper.NAME_COLUM + "= ? AND " + DBHelper.PATH_COLUM + "= ?", selction_args);
    }

    public List<PictureData> getPicturesOfField(long field_id) {
        String[] selection_args = new String[1];
        selection_args[0] = Long.toString(field_id);
        Cursor cursor = db.query(DBHelper.ImageTable_NAME,new String[] {DBHelper.NAME_COLUM,DBHelper.PATH_COLUM},(DBHelper.PARENT_COLUM + "= ?"), selection_args,null,null,null);
        List<PictureData> pictureData = new ArrayList<>();
        while(cursor.moveToNext()) {
            String title = cursor.getString(cursor.getColumnIndex(DBHelper.NAME_COLUM));
            String path = cursor.getString(cursor.getColumnIndex(DBHelper.PATH_COLUM));
            PictureData pd = new PictureData(title, path);
            pictureData.add(pd);
        }
        return pictureData;
    }

    public void updateAgrarianField(AgrarianField field) {
        if (field != null) {
            ContentValues values = new ContentValues();
            values.put(DBHelper.SIZE_COLUM, field.getSize());
            values.put(DBHelper.NAME_COLUM, field.getName());
            values.put(DBHelper.TYPE_COLUM, field.getType().toString());
            values.put(DBHelper.COUNTY_COLUM, field.getCounty());
            values.put(DBHelper.OWNER_COLUM, field.getOwner());

            String[] selection_args = new String[1];
            selection_args[0] = Long.toString(field.getID());

            db.update(DBHelper.AgrarianFieldTable_NAME, values, (DBHelper.ID_COLUM + "= ?"), selection_args);
        }
    }


    public void updateDamageField(DamageField field) {
        if (field != null) {
            ContentValues values = new ContentValues();
            values.put(DBHelper.SIZE_COLUM, field.getSize());
            values.put(DBHelper.NAME_COLUM, field.getName());
            values.put(DBHelper.TYPE_COLUM, field.getType().toString());
            values.put(DBHelper.COUNTY_COLUM, field.getCounty());
            values.put(DBHelper.EVALUATOR_COLUM, field.getEvaluator());
            values.put(DBHelper.DATE_COLUM, field.getParsedDate());
            values.put(DBHelper.PROGRESS_COLUM, field.getProgressStatus().toString());
            values.put(DBHelper.PARENT_COLUM, field.getParentField().getID());

            String[] selection_args = new String[1];
            selection_args[0] = Long.toString(field.getID());

            db.update(DBHelper.DamageFieldTable_NAME, values, (DBHelper.ID_COLUM + "= ?"), selection_args);
        }
    }

    public void deleteAgrarianField(AgrarianField field) {
        if (field !=  null) {
            long id = field.getID();
            String[] selection_args = new String[1];
            selection_args[0] = Long.toString(id);


            int rows = db.delete(DBHelper.AgrarianFieldTable_NAME, (DBHelper.ID_COLUM + "= ?"), selection_args);
            if (rows > 0 ) {
                String table_name = GeoPointTable_Suffix + "_Agr_" + id;
                String vectorTable = VectorTable_Suffix + id;
                db.execSQL("DROP TABLE IF EXISTS " + table_name);
                //db.delete(table_name, null, null);
                db.execSQL("DROP TABLE IF EXISTS " + vectorTable);
            }
        }
    }

    public void deleteDamageField(DamageField field) {
        if (field !=  null) {
            long id = field.getID();
            String[] selection_args = new String[1];
            selection_args[0] = Long.toString(id);


            int rows = db.delete(DBHelper.DamageFieldTable_NAME, (DBHelper.ID_COLUM + "= ?"), selection_args);
            if (rows > 0) {
                String table_name = GeoPointTable_Suffix + "_Dmg_" + id;
                db.execSQL("DROP TABLE IF EXISTS " + table_name);
                //db.delete(table_name, null, null);

                db.delete(DBHelper.ImageTable_NAME, (DBHelper.PARENT_COLUM + "= ?"), selection_args);
            }
        }
    }


    public List<Field> searchAll(String text) {
        List<Field> fields = new ArrayList<>();
        String[] selection_args = new String[2];
        selection_args[0] = text;
        selection_args[1] = text;

        Cursor agrCursor = db.query(DBHelper.AgrarianFieldTable_NAME,null, DBHelper.OWNER_COLUM + " LIKE ? OR " + DBHelper.NAME_COLUM + " LIKE ?", selection_args, null, null, null);
        while (agrCursor.moveToNext()) {
            fields.add(toAgrarianField(agrCursor));
        }

        selection_args = new String[4];
        selection_args[0] = text;
        selection_args[1] = text;
        selection_args[2] = text;
        selection_args[3] = text;

        Cursor dmgCursor = db.query(DBHelper.DamageFieldTable_NAME, null, DBHelper.OWNER_COLUM + " LIKE ? OR " + DBHelper.NAME_COLUM + " LIKE ? OR "+ DBHelper.DATE_COLUM + " LIKE ? OR " + DBHelper.PROGRESS_COLUM + " LIKE ?", selection_args, null,null, null);
        while (dmgCursor.moveToNext()) {
            fields.add(toDamageField(dmgCursor));
        }

        return  fields;
    }

    public List<Field> searchOwner(String text) {
        List<Field> fields = new ArrayList<>();
        String[] selection_args = new String[1];
        selection_args[0] = text;

        Cursor agrCursor = db.query(DBHelper.AgrarianFieldTable_NAME,null, DBHelper.OWNER_COLUM + " LIKE ?", selection_args, null, null, null);
        while (agrCursor.moveToNext()) {
            fields.add(toAgrarianField(agrCursor));
        }

        Cursor dmgCursor = db.query(DBHelper.DamageFieldTable_NAME, null, DBHelper.OWNER_COLUM + " LIKE ?", selection_args, null,null, null);
        while (dmgCursor.moveToNext()) {
            fields.add(toDamageField(dmgCursor));
        }

        return  fields;
    }

    public List<Field> searchDate(String text) {
        List<Field> fields = new ArrayList<>();
        String[] selection_args = new String[1];
        selection_args[0] = text;

        Cursor dmgCursor = db.query(DBHelper.DamageFieldTable_NAME, null, DBHelper.DATE_COLUM + " LIKE ?", selection_args, null,null, null);
        while (dmgCursor.moveToNext()) {
            fields.add(toDamageField(dmgCursor));
        }

        return  fields;
    }

    public List<Field> searchName(String text) {
        List<Field> fields = new ArrayList<>();
        String[] selection_args = new String[1];
        selection_args[0] = text;

        Cursor agrCursor = db.query(DBHelper.AgrarianFieldTable_NAME,null, DBHelper.NAME_COLUM + " LIKE ?", selection_args, null, null, null);
        while (agrCursor.moveToNext()) {
            fields.add(toAgrarianField(agrCursor));
        }

        Cursor dmgCursor = db.query(DBHelper.DamageFieldTable_NAME, null, DBHelper.NAME_COLUM + " LIKE ?", selection_args, null,null, null);
        while (dmgCursor.moveToNext()) {
            fields.add(toDamageField(dmgCursor));
        }

        return  fields;
    }

    public List<Field> searchState(String text) {
        List<Field> fields = new ArrayList<>();
        String[] selection_args = new String[1];
        selection_args[0] = text;


        Cursor dmgCursor = db.query(DBHelper.DamageFieldTable_NAME, null, DBHelper.PROGRESS_COLUM + " LIKE ?", selection_args, null,null, null);
        while (dmgCursor.moveToNext()) {
            fields.add(toDamageField(dmgCursor));
        }

        return  fields;
    }
}
