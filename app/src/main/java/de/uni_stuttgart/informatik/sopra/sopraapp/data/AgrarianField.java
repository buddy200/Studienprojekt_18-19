
package de.uni_stuttgart.informatik.sopra.sopraapp.data;

import android.content.Context;
import android.os.Bundle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import de.uni_stuttgart.informatik.sopra.sopraapp.R;
import de.uni_stuttgart.informatik.sopra.sopraapp.data.FieldTypes.AgrarianFieldType;

/**
 * Created by Christian on 13.11.2017.
 */

public class AgrarianField extends Field implements Serializable{
    private static final String TAG = "AgrarianField";

    protected static final String KEY_OWNER = "owner";
    protected static final String KEY_DMGFIELDS = "dmg";

    private static final long serialVersionUID = 9L;

    //default state
    private AgrarianFieldType state = AgrarianFieldType.Corn;
    private String owner;

    private ArrayList<DamageField> containedDamageFields;


    /**
     * fields need at least 3 corner points to exist
     */
    public AgrarianField(Context context, List<CornerPoint> cPoints) {
        super(context, cPoints);

        //set default values
        owner = context.getResources().getString(R.string.owner_default_name);
        this.setName(context.getResources().getString(R.string.field_default_name));
        this.setType(AgrarianFieldType.Corn);
        this.setCounty(context.getResources().getString(R.string.county_default_name));
        this.setColor(state.toColor());
        this.setContainedDamageFields(new ArrayList<DamageField>());

    }

    /**
     * bundle helper function
     * TODO put damage fields in there somehow
     * @return
     */
    @Override
    public Bundle getBundle() {
        Bundle bundle = new Bundle();
        bundle.putString(KEY_NAME, this.getName());
        bundle.putInt(KEY_COLOR, this.getType().toColor());
        bundle.putString(KEY_COUNTY, this.getCounty());
        if(this.getSize() != null){
            bundle.putDouble(KEY_SIZE, this.getSize());
        }
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

}
