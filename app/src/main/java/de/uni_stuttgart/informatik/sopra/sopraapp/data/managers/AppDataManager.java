package de.uni_stuttgart.informatik.sopra.sopraapp.data.managers;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uni_stuttgart.informatik.sopra.sopraapp.data.AgrarianField;
import de.uni_stuttgart.informatik.sopra.sopraapp.data.DBConnection;
import de.uni_stuttgart.informatik.sopra.sopraapp.data.DamageField;
import de.uni_stuttgart.informatik.sopra.sopraapp.data.Field;
import de.uni_stuttgart.informatik.sopra.sopraapp.data.PictureData;

/**
 * this class communicate with the App and the Database.
 */

public class AppDataManager {
    private static final String TAG = "AppDataManager";

    private HashMap<Long, AgrarianField> agrarianFieldMap;
    private HashMap<Long, DamageField> damageFieldMap;

    private DBConnection dbConnection;

    private DataChangeListener listener;

    public AppDataManager(Context context) {
        try {
            listener = (DataChangeListener) context;
        } catch (ClassCastException e) {
            Log.e("AppDataManager", "parent must implement DataChangeListener");
        }
        dbConnection = new DBConnection(context);
        agrarianFieldMap = new HashMap<>();
        damageFieldMap = new HashMap<>();
        readData();
        dataChange();
    }

    /**
     * Reads all field data from the database and write these in two Hash maps. One for DamageFields one for AgrarianFields
     */
    public void readData() {
        agrarianFieldMap.clear();
        damageFieldMap.clear();
        for (AgrarianField field : dbConnection.getAllAgrarianFields()) {
            agrarianFieldMap.put(field.getID(), field);
        }
        for (DamageField field : dbConnection.getAllDamgageFields()) {
            damageFieldMap.put(field.getID(), field);
        }
        for (DamageField field : damageFieldMap.values()) {
            agrarianFieldMap.get(field.getParentField().getID()).addContainedDamageField(field);
        }
    }

    /**
     * add a AgrarianField to the DB
     *
     * @param f
     */
    public void addAgrarianField(AgrarianField f) {
        dbConnection.addField(f);
        readData();
        dataChange();
    }

    /**
     * add a DamageField to the DB
     *
     * @param dmg
     */
    public void addDamageField(DamageField dmg) {
        dbConnection.addField(dmg);
        readData();
        dataChange();
    }

    /**
     * remove a given field. regardless of the field type
     *
     * @param f
     */
    public void removeField(Field f) {
        if (f instanceof DamageField) {
            DamageField damageField = (DamageField) f;
            removeField(damageField);
        } else if (f instanceof AgrarianField) {
            AgrarianField agrarianField = (AgrarianField) f;
            removeField(agrarianField);
        }
    }

    /**
     * remove the given damageField from the DB and the hashMaps
     *
     * @param agrarianField field to delete
     */
    public void removeField(AgrarianField agrarianField) {
        for (DamageField dmf : agrarianField.getContainedDamageFields()) {
            dbConnection.deleteDamageField(dmf);
            damageFieldMap.remove(dmf.getID());
        }
        for (DamageField field : agrarianField.getContainedDamageFields()) {
            removeField(field);
        }
        agrarianFieldMap.remove(agrarianField.getID());
        dbConnection.deleteAgrarianField(agrarianField);
        dataChange();
    }

    /**
     * remove the given damageField from the DB and the hashMaps
     *
     * @param damageField field to delete
     */
    public void removeField(DamageField damageField) {
        damageField.getParentField().getContainedDamageFields().remove(damageField);
        dbConnection.updateAgrarianField(damageField.getParentField());
        for (PictureData pictureData : damageField.getPaths()) {
            dbConnection.deletePicture(pictureData);
        }
        damageField.deleteAllPhotos();
        dbConnection.deleteDamageField(damageField);
        damageFieldMap.remove(damageField.getID());
        agrarianFieldMap.put(damageField.getParentField().getID(), damageField.getParentField());
        dataChange();
    }

    public void dataChange() {
        if (listener != null) {
            listener.onDataChange();
        }
    }

    /**
     * this method get a agrarianField that was changed and update the DB and the hashMap
     *
     * @param field field to change
     */
    public void changeAgrarianField(AgrarianField field) {
        dbConnection.updateAgrarianField(field);
        agrarianFieldMap.put(field.getID(), field);
        dataChange();
    }

    /**
     * this method get a damageField that was changed and update the DB and the hashMap
     *
     * @param field field to change
     */
    public void changeDamageField(DamageField field) {
        dbConnection.updateDamageField(field);
        damageFieldMap.put(field.getID(), field);
        dataChange();
    }

    /**
     * build a ArrayList with damageFields and AgrarianFields
     *
     * @return a ArrayList of fields
     */
    public ArrayList<Field> getFields() {
        ArrayList<Field> dataFromFields = new ArrayList<>();
        dataFromFields.addAll(agrarianFieldMap.values());
        dataFromFields.addAll(damageFieldMap.values());
        return dataFromFields;
    }

    public interface DataChangeListener {
        void onDataChange();
    }

    public void dbClose() {
        dbConnection.close();
    }

    public void addPicture(DamageField field, PictureData pd) {
        dbConnection.addPictureToField(field.getID(), pd);
    }

    public void deletePicture(DamageField field, PictureData pd) {
        dbConnection.deletePicture(pd);
    }

    public HashMap<Long, AgrarianField> getAgrarianFieldMap() {
        return agrarianFieldMap;
    }

    public HashMap<Long, DamageField> getDamageFieldMap() {
        return damageFieldMap;
    }

    public List<Field> searchAll(String text) {
        return dbConnection.searchAll(text);
    }
    public List<Field> searchOwner(String text) {
        return dbConnection.searchOwner(text);
    }

    public List<Field> searchDate(String text) {
        return dbConnection.searchDate(text);
    }

    public List<Field> searchName(String text) {
        return dbConnection.searchName(text);
    }

    public List<Field> searchState(String text) {
        return dbConnection.searchState(text);
    }

}
