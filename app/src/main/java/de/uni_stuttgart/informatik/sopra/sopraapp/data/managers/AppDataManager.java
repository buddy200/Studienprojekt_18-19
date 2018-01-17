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
 * sopra_priv
 * Created by Felix B on 15.12.17.
 * Mail: felix.burk@gmail.com
 */

public class AppDataManager {
    private static final String TAG = "AppDataManager";




    //   private ArrayList<Field> dataFromFields;
    private HashMap<Long, AgrarianField> agrarianFieldMap;



    private HashMap<Long, DamageField> damageFieldMap;

    private ExportImportFromFile writerReader;
    private Context context;

    private DBConnection dbConnection;

    private DataChangeListener listener;


    public AppDataManager(Context context) {
        this.context = context;
        try {
            listener = (DataChangeListener) context;
        } catch (ClassCastException e) {
            Log.e("AppDataManager", "parent must implement DataChangeListener");
        }
        dbConnection = new DBConnection(context);


        agrarianFieldMap = new HashMap<>();
        damageFieldMap = new HashMap<>();
        readData();
        //writerReader = new ExportImportFromFile(context);
        dataChange();

    }


    public void readData() {
        agrarianFieldMap.clear();
        damageFieldMap.clear();

        for (AgrarianField field : dbConnection.getAllAgrarianFields()){
            agrarianFieldMap.put(field.getID(), field);
        }
        for (DamageField field : dbConnection.getAllDamgageFields()){
            damageFieldMap.put(field.getID(), field);
        }

        for(DamageField field : damageFieldMap.values()){
          agrarianFieldMap.get(field.getParentField().getID()).addContainedDamageField(field);
        }


      //  dataChange();
    }


    public void addAgrarianField(AgrarianField f) {
        dbConnection.addField(f);
        readData();
        dataChange();
    }

    /**
     * @param dmg
     */
    public void addDamageField(DamageField dmg) {
        dbConnection.addField(dmg);
        readData();
        dataChange();
    }

    /**
     * this is broken! mostly ..
     * ich werd das mal so lassen weil hier eh alles umgebaut wird
     *
     * @param f
     */
    public void removeField(Field f) {
   /*     for (Field field : getFields()) {
            if (f.isFieldequal(field)) {
                f = field;
            }
        }*/
        if (f instanceof DamageField) {
            ((DamageField) f).getParentField().getContainedDamageFields().remove(f);
            dbConnection.updateAgrarianField(((DamageField) f).getParentField());
            dbConnection.deleteDamageField((DamageField) f);
            damageFieldMap.remove(f.getID());
            agrarianFieldMap.put(((DamageField) f).getParentField().getID(), ((DamageField) f).getParentField() );
        } else if (f instanceof AgrarianField) {
            for (DamageField dmf : ((AgrarianField) f).getContainedDamageFields()) {
                dbConnection.deleteDamageField(dmf);
                damageFieldMap.remove(dmf.getID());
            }
            for(DamageField field : ((AgrarianField) f).getContainedDamageFields()) {
                damageFieldMap.remove(field.getID());
                dbConnection.deleteDamageField(field);
            }
            agrarianFieldMap.remove(f.getID());
            dbConnection.deleteAgrarianField((AgrarianField) f);
        }

       // Log.e("removed field", f.getName());
        dataChange();
    }

    public void dataChange() {
        if (listener != null) {
            listener.onDataChange();
        }
    }

    public void changeAgrarianField(AgrarianField field) {
        dbConnection.updateAgrarianField(field);
        agrarianFieldMap.put(field.getID(), field);
        dataChange();
    }

    public void changeDamageField(DamageField field){
        dbConnection.updateDamageField(field);
        damageFieldMap.put(field.getID(), field);
        dataChange();
    }

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

    public void addPicture(DamageField field, PictureData pd){
        dbConnection.addPictureToField(field.getID(), pd);
    }

    public void deletePicture(DamageField field, PictureData pd){
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
