package de.uni_stuttgart.informatik.sopra.fieldManager.data.managers;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.uni_stuttgart.informatik.sopra.fieldManager.data.AgrarianField;
import de.uni_stuttgart.informatik.sopra.fieldManager.data.DamageField;
import de.uni_stuttgart.informatik.sopra.fieldManager.data.Field;
import de.uni_stuttgart.informatik.sopra.fieldManager.data.PictureData;

/**
 * this class communicate with the App and the Database.
 */

public class AppDataManager {
    private static final String TAG = "AppDataManager";

    SharedPreferences prefs;
    private HashMap<Long, AgrarianField> agrarianFieldMap;
    private HashMap<Long, DamageField> damageFieldMap;
    private Context context;
    private DBConnection dbConnection;
    private DataChangeListener listener;
    private static AppDataManager instance;

    private AppDataManager(Context context) {
        this.context = context;
        try {
            listener = (DataChangeListener) context;
        } catch (ClassCastException e) {
            Log.e("AppDataManager", "parent must implement DataChangeListener");
        }
        dbConnection = new DBConnection(context);
        agrarianFieldMap = new HashMap<>();
        damageFieldMap = new HashMap<>();
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        dataChange();
    }

    public static AppDataManager getInstance(Context context){
        if(instance == null){
            instance = new AppDataManager(context);
        }
        return instance;
    }

    /**
     * Reads all field data from the database and write these in two Hash maps. One for DamageFields one for AgrarianFields
     */
    public void readData() {
        clearAllMaps();
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
    public ArrayList<Field> getAllFields() {
        ArrayList<Field> dataFromFields = new ArrayList<>();
        dataFromFields.addAll(agrarianFieldMap.values());
        dataFromFields.addAll(damageFieldMap.values());
        return dataFromFields;
    }

    public ArrayList<AgrarianField> getAllAgrarianFields() {
        return new ArrayList<>(agrarianFieldMap.values());
    }

    public ArrayList<DamageField> getAllDamageFields() {
        return new ArrayList<>(damageFieldMap.values());
    }

    public interface DataChangeListener {
        void onDataChange();
    }

    public void clearAllMaps() {
        agrarianFieldMap.clear();
        damageFieldMap.clear();
    }

    public void dbClose() {
        dbConnection.close();
    }

    public void openDBWhenClosed(){
        dbConnection.openDBWhenClosed();
    }

    public void addPicture(DamageField field, PictureData pd) {
        dbConnection.addPictureToField(field.getID(), pd);
    }

    /**
     * loads only the fields with the given name
     *
     * @param name
     */
    public void loadUserFields(String name) {
        clearAllMaps();
        for (Field field : this.searchOwner(name)) {
            if (field instanceof AgrarianField) {
                agrarianFieldMap.put(field.getID(), (AgrarianField) field);
            }
        }
        for (DamageField damageField : dbConnection.getAllDamgageFields()) {
            if (agrarianFieldMap.containsKey(damageField.getParentField().getID())) {
                damageFieldMap.put(damageField.getID(), damageField);
            }
        }
    }

    public ArrayList<Field> containsField(List<Field> fieldList){
        ArrayList<Field> resultList = new ArrayList<>();
        for(Field field : fieldList){
            if(field instanceof AgrarianField){
                if(agrarianFieldMap.containsKey(field.getID())){
                    resultList.add(field);
                }
            }
            else{
                if(damageFieldMap.containsKey(field.getID())){
                    resultList.add(field);
                }
            }
        }
        return resultList;
    }

    public boolean checkLogin(String usr, String pw){
        return dbConnection.checkUsr(usr,pw);
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

    public List<Field> searchEvaluator(String text) {
        return dbConnection.searchEvaluator(text);
    }

}
