package de.uni_stuttgart.informatik.sopra.sopraapp.data.FieldTypes;


/**
 * sopra_priv
 * Created by Felix B on 18.11.17.
 * Mail: felix.burk@gmail.
 *
 * a FieldType class interface for different types of different fields
 */

public interface FieldType{
    String toString();
    int toColor();
    double getInsuranceMoneyPerSquareMeter();
}
