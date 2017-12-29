package de.uni_stuttgart.informatik.sopra.sopraapp.data.managers;

import android.content.Context;
import android.text.InputType;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import de.uni_stuttgart.informatik.sopra.sopraapp.R;
import de.uni_stuttgart.informatik.sopra.sopraapp.data.Field;

/**
 * Created by larsb on 04.12.2017.
 * This Class Write and Read Fields to/from the internal storage
 */

public class ExportImportFromFile {
    private static final String TAG = "ExportImportFromFile";

    private String filename = "Appdata";
    private FileOutputStream fos;
    private ObjectOutputStream oos;
    private FileInputStream fis;
    private ObjectInputStream ois;
    private Context context;

    public ExportImportFromFile(Context context) {
        this.context = context;
    }

    /**
     * write all fields form the list to a file in the internal storage
     * @param list
     */
    public void WriteFields(ArrayList<Field> list) {
        try {
            Log.e("DIR", context.getFilesDir().toString());
            context.deleteFile(filename);
            fos = context.openFileOutput(filename, context.MODE_PRIVATE);
            oos = new ObjectOutputStream(fos);
            for (Field field : list) {
                oos.writeObject(field);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (oos != null) {
                try {
                    oos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Read fields from the file in the internal storage
     * @return
     */
    public ArrayList<Field> readFields() {
        ArrayList<Field> list = new ArrayList<>();
        Field tempfield;
        try {
            File file = context.getFileStreamPath(filename);

            if(file == null || !file.exists()) {
                Log.e(TAG ,"file not found");
                InputStream fiis = context.getResources().openRawResource(
                        context.getResources().getIdentifier("appdata",
                                "raw", context.getPackageName()));
                ois = new ObjectInputStream(fiis);

            }else{
                fis = context.openFileInput(filename);
                ois = new ObjectInputStream(fis);
            }

            while (true) {
                tempfield = (Field) ois.readObject();
                tempfield.setContext(context);
                list.add(tempfield);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (ois != null) {
                try {
                    ois.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return list;
    }

}
