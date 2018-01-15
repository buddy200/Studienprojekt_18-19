package de.uni_stuttgart.informatik.sopra.sopraapp.data.managers;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;

import de.uni_stuttgart.informatik.sopra.sopraapp.data.AgrarianField;
import de.uni_stuttgart.informatik.sopra.sopraapp.data.DBConnection;
import de.uni_stuttgart.informatik.sopra.sopraapp.data.DamageField;
import de.uni_stuttgart.informatik.sopra.sopraapp.data.Field;

/**
 * sopra_priv
 * Created by Felix B on 15.12.17.
 * Mail: felix.burk@gmail.com
 */

public class AppDataManager {
    private static final String TAG = "AppDataManager";


    private ArrayList<Field> dataFromFields;
    private ExportImportFromFile writerReader;
    private Context context;

    private DBConnection dbConnection;

    private DataChangeListener listener;


    public AppDataManager(Context context){
        this.context = context;
        try{
            listener = (DataChangeListener) context;
        }catch(ClassCastException e){
            Log.e("AppDataManager", "parent must implement DataChangeListener");
        }
        dbConnection = new DBConnection(context);
        dataFromFields = new ArrayList<>();
        //writerReader = new ExportImportFromFile(context);
        readData();

    }

    public void readData(){
        dataFromFields.clear();
        dataFromFields.addAll(dbConnection.getAllAgrarianFields());
        dataFromFields.addAll(dbConnection.getAllDamgageFields());
        dataChange();
    }

    public void saveData(){
    //    writerReader.WriteFields(dataFromFields);

    }

    public void addAgrarianField(Field f){
        dataFromFields.add(f);
        dbConnection.addField((AgrarianField) f);
        dataChange();
    }

    /**
     * @param dmg
     */
    public void addDamageField(DamageField dmg){
        dataFromFields.add(dmg);
        dbConnection.addField(dmg);
        dataChange();
    }

    /**
     * this is broken! mostly ..
     * ich werd das mal so lassen weil hier eh alles umgebaut wird
     * @param f
     */
    public void removeField(Field f){
        for(Field field : getFields()){
            if(f.getTimestamp() == field.getTimestamp()){
                f = field;
            }
        }
        if(f instanceof DamageField){
            ((DamageField) f).getParentField().getContainedDamageFields().remove(f);
            dbConnection.updateAgrarianField(((DamageField) f).getParentField());
            dbConnection.deleteDamageField((DamageField) f);
        }else if(f instanceof AgrarianField){

            dataFromFields.removeAll(((AgrarianField)f).getContainedDamageFields());
            for (DamageField dmf : ((AgrarianField) f).getContainedDamageFields()){
                dbConnection.deleteDamageField(dmf);
            }
            dbConnection.deleteAgrarianField((AgrarianField)f);
        }
        dataFromFields.remove(f);

        Log.e("removed field", f.getName());

        dataChange();
    }

    public void dataChange(){
        if(listener != null){
            listener.onDataChange();
            saveData();
        }
    }

    public ArrayList<Field> getFields(){
        return dataFromFields;
    }


    public interface DataChangeListener{
        void onDataChange();
    }

    public void dbClose(){
        dbConnection.close();
    }
}
