package de.uni_stuttgart.informatik.sopra.fieldManager.data.FieldTypes;

import android.content.Context;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import de.uni_stuttgart.informatik.sopra.fieldManager.R;

/**
 * sopra_priv
 * Created by Felix B on 18.11.17.
 * Mail: felix.burk@gmail.
 *
 * a custom FieldType class containg the different types of AgrarianFields
 * Strings and Colors are mapped with strings.xml and colors.xml
 */

public enum AgrarianFieldType implements FieldType, Serializable{

    Hemp(1, 1.1),
    Wheat(2, 0.5),
    Rye(3, 0.7),
    Potatoes(4, 1.2),
    Corn(5, 1.0);

    private double insuranceValuePerSquaremeter;
    private int id;

    AgrarianFieldType(int id, double insuranceValuePerSquaremeter){
        this.insuranceValuePerSquaremeter = insuranceValuePerSquaremeter;
        this.id = id;
    }

    @Override
    public double getInsuranceMoneyPerSquareMeter(){
        return insuranceValuePerSquaremeter;
    }


    @Override
    public String toString(Context context) {
        switch (this.id) {
            case 1:
                return context.getResources().getString(R.string.hemp);
            case 2:
                return context.getResources().getString(R.string.wheat);
            case 3:
                return context.getResources().getString(R.string.rye);
            case 4:
                return context.getResources().getString(R.string.potatoes);
            case 5:
                return context.getResources().getString(R.string.corn);
            default:
                return "invalid";
        }
    }

    @Override
    public int toColor(Context context) {
        switch (this.id) {
            case 1:
                return context.getResources().getColor(R.color.hempTypeAgrarian);
            case 2:
                return context.getResources().getColor(R.color.wheatTypeAgrarian);
            case 3:
                return context.getResources().getColor(R.color.ryeTypeAgrarian);
            case 4:
                return context.getResources().getColor(R.color.potatoesTypeAgrarian);
            case 5:
                return context.getResources().getColor(R.color.cornTypeAgrarian);
            default:
                return 0;
        }
    }

    @Override
    public int getPattern(Context context) {
        switch (this.id) {
            case 1:
                return R.drawable.pattern_plus;
            case 2:
                return R.drawable.pattern_falling_triangles;
            case 3:
                return R.drawable.pattern_4_point_stars;
            case 4:
                return R.drawable.pattern_tic_tac_toe;
            case 5:
                return R.drawable.pattern_dmg_wiggle;
            default:
                return R.drawable.pattern_dmg_wiggle;
        }
    }

    public static List<String> getAllString(Context context){
        ArrayList<String> allStrings = new ArrayList<>();
        for(AgrarianFieldType aft : AgrarianFieldType.values()){
            allStrings.add(aft.toString(context));
        }
        return allStrings;
    }

    public static AgrarianFieldType fromString(String text, Context context) {
        for (AgrarianFieldType type : AgrarianFieldType.values()) {
            if (type.toString(context).equalsIgnoreCase(text)) {
                return type;
            }
        }
        return null;
    }
};
