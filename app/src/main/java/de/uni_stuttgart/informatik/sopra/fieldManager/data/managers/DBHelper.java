package de.uni_stuttgart.informatik.sopra.fieldManager.data.managers;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Christian on 11.01.2018.
 */

public class DBHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "Fields.db";
    public static final String AgrarianFieldTable_NAME = "AgrarianFields";
    public static final String DamageFieldTable_NAME = "DamageFields";
    public static final String UserTable_NAME = "Usrs";

    public static final String ID_COLUM = "_ID";
    public static final String SIZE_COLUM = "size";
    public static final String NAME_COLUM = "name";
    public static final String TYPE_COLUM = "field_type";
    public static final String COUNTY_COLUM = "county";
    public static final String OWNER_COLUM = "owner";
    public static final String EVALUATOR_COLUM = "evaluator";
    public static final String DATE_COLUM = "date";
    public static final String PROGRESS_COLUM = "progress_status";
    public static final String PARENT_COLUM = "parent_field_id";

    public static final String ImageTable_NAME = "ImageTable";
    public static final String PATH_COLUM = "path";

    public static final String USR_COLUMN = "usr_nm";
    public static final String PW_COLUM = "pw_str";

    private static final String CREATE_AgrarianFieldTable = "CREATE TABLE " + AgrarianFieldTable_NAME + " (" +
            ID_COLUM + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            SIZE_COLUM + " REAL NOT NULL," +
            NAME_COLUM + " TEXT NOT NULL," +
            TYPE_COLUM + " TEXT," +
            COUNTY_COLUM + " TEXT NOT NULL," +
            OWNER_COLUM + " TEXT NOT NULL)";

    private static final String CREATE_DamageFieldTable = "CREATE TABLE " + DamageFieldTable_NAME + " (" +
            ID_COLUM + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            SIZE_COLUM + " REAL NOT NULL," +
            NAME_COLUM + " TEXT NOT NULL," +
            PARENT_COLUM + " INTEGER," +
            TYPE_COLUM + " TEXT," +
            COUNTY_COLUM + " TEXT NOT NULL," +
            EVALUATOR_COLUM + " TEXT NOT NULL," +
            DATE_COLUM + " TEXT," +
            PROGRESS_COLUM + " TEXT)";

    private static final String CREATE_ImageTable = "CREATE TABLE " + ImageTable_NAME + " (" +
            NAME_COLUM + " TEXT," +
            PATH_COLUM + " TEXT NOT NULL," +
            PARENT_COLUM + " INTEGER NOT NULL)";

    private static final String CREATE_UserTable = "CREATE TABLE " + UserTable_NAME + " (" +
            USR_COLUMN + " TEXT NOT NULL," +
            PW_COLUM + " TEXT NOT NULL)";


    public DBHelper(Context context) {
        super(context, DB_NAME, null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_AgrarianFieldTable);
        db.execSQL(CREATE_DamageFieldTable);
        db.execSQL(CREATE_ImageTable);
        db.execSQL(CREATE_UserTable);
        generateFakeLogin(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /**
     * NOT TO BE USED IN PRODUCTION
     * ONLY FOR TESTING PURPOSES
     */
    public void generateFakeLogin(SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        values.put(DBHelper.USR_COLUMN, "Adm");
        values.put(DBHelper.PW_COLUM, "292C9515973C024858D535B4DB786ACB");

        db.insert(DBHelper.UserTable_NAME, null, values);

        values = new ContentValues();
        values.put(DBHelper.USR_COLUMN, "farm");
        values.put(DBHelper.PW_COLUM, "1BA5CD8DD94A6DADD1FADF981C78EC59");

        db.insert(DBHelper.UserTable_NAME, null, values);

    }

}
