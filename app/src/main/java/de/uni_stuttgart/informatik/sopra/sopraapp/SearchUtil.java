package de.uni_stuttgart.informatik.sopra.sopraapp;

import android.os.Bundle;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import de.uni_stuttgart.informatik.sopra.sopraapp.data.Field;

/**
 * sopra_priv
 * Created by Felix B on 04.12.17.
 * Mail: felix.burk@gmail.com
 */

public class SearchUtil {

    static boolean matchesFieldSearch(Field f, String input){
        Bundle b = f.getBundle();
        if(!f.getName().contains(input)){
            Log.e("dhwaidhioaw", (b.getSerializable("type").toString()));
            Log.e("dhwaidhioaw", String.valueOf(b.getSerializable("type").toString().contains(input)));
            if(! b.getSerializable("type").toString().contains(input)) {
                //fieldToAdd is type agrarian
                if (b.containsKey("owner")) {
                    //search for states and owners
                    if (!b.getString("owner").contains(input)) {
                        return false;
                    }
                }


                //fieldToAdd is type damage field
                if (b.containsKey("evaluator")) {
                    //search for evaluators
                    if (!b.getString("evaluator").contains(input)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

}
