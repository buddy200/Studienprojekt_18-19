
package de.uni_stuttgart.informatik.sopra.sopraapp.data;

import android.content.Context;
import android.os.Bundle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import de.uni_stuttgart.informatik.sopra.sopraapp.R;
import de.uni_stuttgart.informatik.sopra.sopraapp.data.FieldTypes.AgrarianFieldType;
import de.uni_stuttgart.informatik.sopra.sopraapp.data.FieldTypes.FieldType;
import de.uni_stuttgart.informatik.sopra.sopraapp.data.geoData.Vector;

/**
 * Created by Christian on 13.11.2017.
 *
 * A custom Field Class representing Agrarian Fields
 */

public class AgrarianField extends Field implements Serializable{

    private static final String TAG = "AgrarianField";

    //keys for the bundles
    protected static final String KEY_OWNER = "owner";
    protected static final String KEY_DMGFIELDS = "dmg";

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
    public AgrarianField(Context context, List<CornerPoint> cPoints) {
        super(context, cPoints);

        //set default values
      /*  if(context != null){
            owner = context.getResources().getString(R.string.owner_default_name);
            this.setName(context.getResources().getString(R.string.field_default_name));
            this.setCounty(context.getResources().getString(R.string.county_default_name));
        }else{
            owner = "no owner";
            this.setName("no name");
            this.setCounty("no county");
        }*/

        super.setType(AgrarianFieldType.Corn);
        this.setColor(defaultType.toColor());
        this.setContainedDamageFields(new ArrayList<DamageField>());
        containedDamageFields = new ArrayList<>();

    }

    /**
     * bundle helper function
     * TODO put damage fields in there
     * @return
     */
    @Override
    public Bundle getBundle() {
        Bundle bundle = new Bundle();
        bundle.putLong(KEY_TIMESTAMP, this.getTimestamp());
        bundle.putString(KEY_NAME, this.getName());
    //    bundle.putInt(KEY_COLOR, this.getType().toColor());
        bundle.putString(KEY_COUNTY, this.getCounty());
        bundle.putString(KEY_CONVERTEDSIZE, this.getConvertedSize());
        bundle.putDouble(KEY_SIZE, this.getSize());

        bundle.putSerializable(KEY_TYPE, (Serializable) this.getType());

        //agrarianField specific attributes
        bundle.putString(KEY_OWNER, this.owner);

        return bundle;
    }

    public void setContainedDamageFields(ArrayList<DamageField> containedDamageFields) {
        this.containedDamageFields = containedDamageFields;
    }

    public ArrayList<DamageField> getContainedDamageFields() {
        return containedDamageFields;
    }

    public void addContainedDamageField(DamageField dmgField){
        containedDamageFields.add(dmgField);
    }

    public void setOwner(String owner){this.owner = owner;}
    public String getOwner() {return owner;}



    public ArrayList<java.util.Vector<Double>> getLinesFormField() {
        return linesFormField;
    }

    public void setLinesFormField(ArrayList<java.util.Vector<Double>> linesFormField) {
        this.linesFormField = linesFormField;
    }
    public void setType(AgrarianFieldType type) {
        super.setType(type);
        if(containedDamageFields != null) {
            for (DamageField field : containedDamageFields) {
                field.calcInsuranceAmount();
            }
        }
    }
}

