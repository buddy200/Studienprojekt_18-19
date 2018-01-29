package de.uni_stuttgart.informatik.sopra.fieldManager.data.FieldTypes;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import de.uni_stuttgart.informatik.sopra.fieldManager.R;

/**
 * Created by larsb on 13.01.2018.
 */

public enum ProgressStatus {
    sent(1),
    inProgress(2),
    finish(3);
    private int id;

    ProgressStatus(int id) {
        this.id = id;
    }

    public String toString(){
        return null;
    }
    public String toString(Context context) {
        switch (this.id) {
            case 1:
                return context.getResources().getString(R.string.progress_status_sent);
            case 2:
                return context.getResources().getString(R.string.progress_status_inprogress);
            case 3:
                return context.getResources().getString(R.string.progress_status_finish);
            default:
                    return "invalid";
        }
    }
    public static List<String> getAllString(Context context){
        ArrayList<String> allStrings = new ArrayList<>();
        for(ProgressStatus ps : ProgressStatus.values()){
            allStrings.add(ps.toString(context));
        }
        return allStrings;
    }

    public static ProgressStatus fromString(String text, Context context) {
        for (ProgressStatus status : ProgressStatus.values()) {
            if (status.toString(context).equalsIgnoreCase(text)) {
                return status;
            }
        }
        return null;
    }

}
