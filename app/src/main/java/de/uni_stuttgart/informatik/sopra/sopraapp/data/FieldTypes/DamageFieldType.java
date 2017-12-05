package de.uni_stuttgart.informatik.sopra.sopraapp.data.FieldTypes;

import android.support.v4.content.ContextCompat;

import java.io.Serializable;

import de.uni_stuttgart.informatik.sopra.sopraapp.MainActivity;
import de.uni_stuttgart.informatik.sopra.sopraapp.R;

/**
 * sopra_priv
 * Created by Felix B on 04.12.17.
 * Mail: felix.burk@gmail.com
 */

public enum DamageFieldType implements FieldType, Serializable{

    Hail(MainActivity.getmContext().getResources().getString(R.string.hail), ContextCompat.getColor(MainActivity.getmContext(), R.color.hailTypeDmg)),
    Snow(MainActivity.getmContext().getResources().getString(R.string.snow), ContextCompat.getColor(MainActivity.getmContext(), R.color.snowTypeDmg)),
    Aliens(MainActivity.getmContext().getResources().getString(R.string.aliens), ContextCompat.getColor(MainActivity.getmContext(), R.color.alienTypeDmg));


    private String friendlyName;
    private int friendlyColor;
    private static final long serialVersionUID = 12L;

    DamageFieldType(String friendlyName, int friendlyColor){
        this.friendlyName = friendlyName;
        this.friendlyColor = friendlyColor;
    }

    @Override public String toString(){
        return friendlyName;
    }

    public int toColor() {
        return friendlyColor;
    }
};
