package de.uni_stuttgart.informatik.sopra.fieldManager.data.FieldTypes;

import android.support.v4.content.ContextCompat;

import java.io.Serializable;

import de.uni_stuttgart.informatik.sopra.fieldManager.MainActivity;
import de.uni_stuttgart.informatik.sopra.fieldManager.R;

/**
 * sopra_priv
 * Created by Felix B on 18.11.17.
 * Mail: felix.burk@gmail.
 *
 * a custom FieldType class containg the different types of DamageFields
 * Strings and Colors are mapped with strings.xml and colors.xml
 */
public enum DamageFieldType implements FieldType, Serializable{

    Hail(MainActivity.getmContext().getResources().getString(R.string.hail), ContextCompat.getColor(MainActivity.getmContext(), R.color.hailTypeDmg), 0.25),
    Snow(MainActivity.getmContext().getResources().getString(R.string.snow), ContextCompat.getColor(MainActivity.getmContext(), R.color.snowTypeDmg), 0.2),
    Aliens(MainActivity.getmContext().getResources().getString(R.string.aliens), ContextCompat.getColor(MainActivity.getmContext(), R.color.alienTypeDmg), 0.3);


    private String friendlyName;
    private int friendlyColor;
    private double insuranceMoneyPerSquaremeter;
    private static final long serialVersionUID = 12L;

    DamageFieldType(String friendlyName, int friendlyColor, double insuranceMoneyPerSquaremeter){
        this.friendlyName = friendlyName;
        this.friendlyColor = friendlyColor;
        this.insuranceMoneyPerSquaremeter = insuranceMoneyPerSquaremeter;
    }

    @Override public String toString(){
        return friendlyName;
    }

    public double getInsuranceMoneyPerSquareMeter(){
        return insuranceMoneyPerSquaremeter;
    }

    public int toColor() {
        return friendlyColor;
    }

    public static DamageFieldType fromString(String text) {
        for (DamageFieldType type : DamageFieldType.values()) {
            if (type.toString().equalsIgnoreCase(text)) {
                return type;
            }
        }
        return null;
    }
};
