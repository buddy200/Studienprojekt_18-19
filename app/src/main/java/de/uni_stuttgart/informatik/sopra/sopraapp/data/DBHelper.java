package de.uni_stuttgart.informatik.sopra.sopraapp.data;

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

    public static final String ID_COLUM = "_ID";
    public static final String SIZE_COLUM = "size";
    public static final String NAME_COLUM = "name";
    public static final String COLOR_COLUM = "color";
    public static final String COUNTY_COLUM = "county";
    public static final String OWNER_COLUM = "owner";
    public static final String EVALUATOR_COLUM = "evaluator";
    public static final String DATE_COLUM = "date";
    public static final String PARENT_COLUM = "parent_field_id";


    public static final String CREATE_AgrarianFieldTable = "CREATE TABLE " + AgrarianFieldTable_NAME + " (" +
            ID_COLUM + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            SIZE_COLUM + " REAL NOT NULL," +
            NAME_COLUM + " TEXT NOT NULL," +
            COLOR_COLUM + " INTEGER NOT NULL," +
            COUNTY_COLUM + " TEXT NOT NULL," +
            OWNER_COLUM + " TEXT NOT NULL)";
    public static final String CREATE_DamageFieldTable = "CREATE TABLE " + DamageFieldTable_NAME + " (" +
            ID_COLUM + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            SIZE_COLUM + " REAL NOT NULL," +
            NAME_COLUM + " TEXT NOT NULL," +
            COLOR_COLUM + " INTEGER NOT NULL," +
            COUNTY_COLUM + " TEXT NOT NULL," +
            EVALUATOR_COLUM + " TEXT NOT NULL," +
            DATE_COLUM + " TEXT," +
            PARENT_COLUM + "INTEGER NOT NULL)";


    public DBHelper(Context context) {
        super(context, DB_NAME, null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_AgrarianFieldTable);
        db.execSQL(CREATE_DamageFieldTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
