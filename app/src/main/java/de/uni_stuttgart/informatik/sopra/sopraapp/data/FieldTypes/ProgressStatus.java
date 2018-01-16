package de.uni_stuttgart.informatik.sopra.sopraapp.data.FieldTypes;

import de.uni_stuttgart.informatik.sopra.sopraapp.MainActivity;
import de.uni_stuttgart.informatik.sopra.sopraapp.R;

/**
 * Created by larsb on 13.01.2018.
 */

public enum ProgressStatus {
    sent(MainActivity.getmContext().getResources().getString(R.string.progress_status_sent)), inProgress(MainActivity.getmContext().getResources().getString(R.string.progress_status_inprogress)), finish(MainActivity.getmContext().getResources().getString(R.string.progress_status_finish));
    private String name;

    ProgressStatus(String name){
        this.name = name;
    }
    @Override
    public String toString(){
        return name;
    }

    public static ProgressStatus fromString(String text) {
        for (ProgressStatus status : ProgressStatus.values()) {
            if (status.toString().equalsIgnoreCase(text)) {
                return status;
            }
        }
        return null;
    }

}
