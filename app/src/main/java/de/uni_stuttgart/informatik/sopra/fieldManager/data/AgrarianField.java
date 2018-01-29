
package de.uni_stuttgart.informatik.sopra.fieldManager.data;

import android.content.Context;

import org.osmdroid.util.GeoPoint;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import de.uni_stuttgart.informatik.sopra.fieldManager.data.FieldTypes.AgrarianFieldType;

/**
 * Created by Christian on 13.11.2017.
 * <p>
 * A custom Field Class representing Agrarian Fields
 */

public class AgrarianField extends Field implements Serializable {

    private static final String TAG = "AgrarianField";
    private static final long serialVersionUID = 9L;

    //default type
    private AgrarianFieldType defaultType = AgrarianFieldType.Corn;
    private String owner = "";

    private ArrayList<DamageField> containedDamageFields;
    private ArrayList<java.util.Vector<Double>> linesFormField;

    /**
     * constructor method
     * fields need at least 3 corner points to exist
     */
    public AgrarianField(Context context, List<GeoPoint> gPoints) {
        super(context, gPoints);
        super.setType(AgrarianFieldType.Corn);
        this.setContainedDamageFields(new ArrayList<DamageField>());
        containedDamageFields = new ArrayList<>();

    }

    public void setContainedDamageFields(ArrayList<DamageField> containedDamageFields) {
        this.containedDamageFields = containedDamageFields;
    }

    public ArrayList<DamageField> getContainedDamageFields() {
        return containedDamageFields;
    }

    public void addContainedDamageField(DamageField dmgField) {
        containedDamageFields.add(dmgField);
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getOwner() {
        return owner;
    }

    public ArrayList<java.util.Vector<Double>> getLinesFormField() {
        return linesFormField;
    }

    public void setLinesFormField(ArrayList<java.util.Vector<Double>> linesFormField) {
        this.linesFormField = linesFormField;
    }

    public void setType(AgrarianFieldType type) {
        super.setType(type);
        if (containedDamageFields != null) {
            for (DamageField field : containedDamageFields) {
                field.calcInsuranceAmount();
            }
        }
    }
}