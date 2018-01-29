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
 * <p>
 * a custom FieldType class containg the different types of DamageFields
 * Strings and Colors are mapped with strings.xml and colors.xml
 */
public enum DamageFieldType implements FieldType, Serializable {

    Hail(1, 0.25),
    Snow(2, 0.2),
    Storm(3, 0.9),
    Heat(4, 0.1),
    Insects(5, 0.8);

    private double insuranceMoneyPerSquaremeter;
    private static final long serialVersionUID = 12L;
    private int id;

    DamageFieldType(int id, double insuranceMoneyPerSquaremeter) {
        this.insuranceMoneyPerSquaremeter = insuranceMoneyPerSquaremeter;
        this.id = id;
    }


    @Override
    public String toString(Context context) {
        switch (this.id) {
            case 1:
                return context.getResources().getString(R.string.hail);
            case 2:
                return context.getResources().getString(R.string.snow);
            case 3:
                return context.getResources().getString(R.string.storm);
            case 4:
                return context.getResources().getString(R.string.heat);
            case 5:
                return context.getResources().getString(R.string.insects);
            default:
                return "invalid";
        }
    }

    @Override
    public double getInsuranceMoneyPerSquareMeter() {
        return insuranceMoneyPerSquaremeter;
    }

    @Override
    public int toColor(Context context) {
        switch (this.id) {
            case 1:
                return context.getResources().getColor(R.color.hailTypeDmg);
            case 2:
                return context.getResources().getColor(R.color.snowTypeDmg);
            case 3:
                return context.getResources().getColor(R.color.stormTypeDmg);
            case 4:
                return context.getResources().getColor(R.color.heatTypeDmg);
            case 5:
                return context.getResources().getColor(R.color.insectsTypeDmg);
            default:
                return 0;
        }
    }

    @Override
    public int getPattern(Context context) {
        switch (this.id) {
            case 1:
                return R.drawable.pattern_dmg_stripes;
            case 2:
                return R.drawable.pattern_dmg_diagonal_stripes;
            case 3:
                return R.drawable.pattern_dmg_flipped_diamonds;
            case 4:
                return R.drawable.pattern_dmg_houndstooth;
            case 5:
                return R.drawable.pattern_dmg_tiny_checkers;
            default:
                return R.drawable.pattern_dmg_wiggle;
        }
    }

    public static List<String> getAllString(Context context) {
        ArrayList<String> allStrings = new ArrayList<>();
        for (DamageFieldType dmfT : DamageFieldType.values()) {
            allStrings.add(dmfT.toString(context));
        }
        return allStrings;
    }

    public static DamageFieldType fromString(String text, Context context) {
        for (DamageFieldType type : DamageFieldType.values()) {
            if (type.toString(context).equalsIgnoreCase(text)) {
                return type;
            }
        }
        return null;
    }
};
