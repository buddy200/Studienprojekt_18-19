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
        Log.e("added field", f.getName());
        dataFromFields.add(f);
        dataChange();
    }

    public void addDamageField(DamageField dmg, AgrarianField parent){
        parent.addContainedDamageField(dmg);
        dataChange();
    }

    public void removeField(Field f){
        Log.e("tes rm", f.getName());
        Log.e("is in?", String.valueOf(dataFromFields.contains(f)));
        dataFromFields.remove(f);

        if(f instanceof DamageField){
            for(Field field : dataFromFields){
                if(((AgrarianField)field).getContainedDamageFields().contains(f)){
                    ((AgrarianField)field).getContainedDamageFields().remove(f);
                }
            }
        }else{
            Log.e("tes rm", f.getName());
            dataFromFields.remove(f);
        }
        dataChange();
    }

    private void dataChange(){
        if(listener != null){
            listener.onDataChange();
        }
    }

    public ArrayList<Field> getFields(){
        return dataFromFields;
    }


    public interface DataChangeListener{
        void onDataChange();
    }
}
