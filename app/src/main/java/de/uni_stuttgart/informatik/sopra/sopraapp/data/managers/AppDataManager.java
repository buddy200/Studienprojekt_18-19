package de.uni_stuttgart.informatik.sopra.sopraapp.data;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;

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
        dataFromFields = writerReader.readFields();

    }

    public void readData(){
        dataFromFields = writerReader.readFields();
    }

    public void saveData(){
        writerReader.WriteFields(dataFromFields);
    }

    public void addField(Field f){
        dataFromFields.add(f);
        dataChange();
    }

    public void removeField(Field f){
        if(f instanceof DamageField){
            for(Field field : dataFromFields){
                if(((AgrarianField)field).getContainedDamageFields().contains(f)){
                    ((AgrarianField)field).getContainedDamageFields().remove(f);
                }
            }
        }else{
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
