package de.uni_stuttgart.informatik.sopra.sopraapp.data.managers;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;

import de.uni_stuttgart.informatik.sopra.sopraapp.data.AgrarianField;
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

    private DataChangeListener listener;


    public AppDataManager(Context context){
        try{
            listener = (DataChangeListener) context;
        }catch(ClassCastException e){
            Log.e("AppDataManager", "parent must implement DataChangeListener");
        }

        writerReader = new ExportImportFromFile(context);
        readData();

    }

    public void readData(){
        dataFromFields = writerReader.readFields();
        dataChange();
    }

    public void saveData(){
        writerReader.WriteFields(dataFromFields);
    }

    public void addAgrarianField(Field f){
        dataFromFields.add(f);
        dataChange();
    }

    /**
     * this is broken! completly ..
     * ich werd das mal so lassen weil hier eh alles umgebaut wird
     * @param dmg
     * @param parent
     */
    public void addDamageField(DamageField dmg, AgrarianField parent){
        Log.e(TAG, "added damage field: " + dmg.getName() + " to parent; " + parent.getName());
        removeField(parent);
        parent.addContainedDamageField(dmg);
        addAgrarianField(parent);
        dataFromFields.add(dmg);
        dataChange();
    }

    /**
     * this is broken! mostly ..
     * ich werd das mal so lassen weil hier eh alles umgebaut wird
     * @param f
     */
    public void removeField(Field f){
        if(f instanceof DamageField){
            for(Field field : dataFromFields){
                if(field instanceof AgrarianField) {
                    if (((AgrarianField) field).getContainedDamageFields().contains(f)) {
                        ((AgrarianField) field).getContainedDamageFields().remove(f);
                        Log.e("removed field damage", f.getName());
                    }
                }
            }
        }else if(f instanceof AgrarianField){
            dataFromFields.removeAll(((AgrarianField)f).getContainedDamageFields());
        }
        dataFromFields.remove(f);
        Log.e("removed field", f.getName());

        dataChange();
    }

    private void dataChange(){
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
}
