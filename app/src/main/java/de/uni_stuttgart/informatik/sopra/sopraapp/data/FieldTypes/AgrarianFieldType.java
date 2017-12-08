package de.uni_stuttgart.informatik.sopra.sopraapp.data.FieldTypes;

import android.support.v4.content.ContextCompat;

import java.io.Serializable;

import de.uni_stuttgart.informatik.sopra.sopraapp.MainActivity;
import de.uni_stuttgart.informatik.sopra.sopraapp.R;

/**
 * sopra_priv
 * Created by Felix B on 18.11.17.
 * Mail: felix.burk@gmail.
 *
 * a custom FieldType class containg the different types of AgrarianFields
 * Strings and Colors are mapped with strings.xml and colors.xml
 */

public enum AgrarianFieldType implements FieldType, Serializable{

    Hemp(MainActivity.getmContext().getResources().getString(R.string.hemp), ContextCompat.getColor(MainActivity.getmContext(), R.color.hempTypeAgrarian)),
    Wheat(MainActivity.getmContext().getResources().getString(R.string.wheat), ContextCompat.getColor(MainActivity.getmContext(), R.color.wheatTypeAgrarian)),
    Rye(MainActivity.getmContext().getResources().getString(R.string.rye), ContextCompat.getColor(MainActivity.getmContext(), R.color.ryeTypeAgrarian)),
    Potatoes(MainActivity.getmContext().getResources().getString(R.string.potatoes), ContextCompat.getColor(MainActivity.getmContext(), R.color.potatoesTypeAgrarian)),
    Corn(MainActivity.getmContext().getResources().getString(R.string.corn), ContextCompat.getColor(MainActivity.getmContext(), R.color.cornTypeAgrarian));

    private String friendlyName;
    private int friendlyColor;
//    private static final long serialVersionUID = 13L;

    AgrarianFieldType(String friendlyName, int friendlyColor){
        this.friendlyName = friendlyName;
        this.friendlyColor = friendlyColor;
    }

    @Override public String toString(){
        return friendlyName;
    }

    public int toColor(){
        return friendlyColor;
    }
};
