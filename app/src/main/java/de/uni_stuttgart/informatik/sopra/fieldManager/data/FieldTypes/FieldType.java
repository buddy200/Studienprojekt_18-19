package de.uni_stuttgart.informatik.sopra.fieldManager.data.FieldTypes;


import android.content.Context;
import android.graphics.drawable.Drawable;

import java.util.List;

/**
 * sopra_priv
 * Created by Felix B on 18.11.17.
 * Mail: felix.burk@gmail.
 *
 * a FieldType class interface for different types of different fields
 */

public interface FieldType{
    String toString(Context context);
    int toColor(Context context);
    double getInsuranceMoneyPerSquareMeter();
    int getPattern(Context context);
}
