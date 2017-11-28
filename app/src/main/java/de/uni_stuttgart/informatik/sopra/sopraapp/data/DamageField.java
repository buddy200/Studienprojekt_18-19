package de.uni_stuttgart.informatik.sopra.sopraapp.data;

import android.content.Context;
import android.os.Bundle;

import java.util.Date;
import java.util.List;

import de.uni_stuttgart.informatik.sopra.sopraapp.R;

/**
 * Created by larsb on 22.11.2017.
 */

public class DamageField extends Field {
    private static final String TAG = "DamageField";

    private static final String KEY_DATE = "date";
    private static final String KEY_EVALUATOR = "evaluator";


    public Date date;
    public String evaluator;


    public DamageField(Context context, List<CornerPoint> cPoints) {
        super(context, cPoints);
        this.name= context.getResources().getString(R.string.field_default_name);
        this.county = context.getResources().getString(R.string.county_default_name);
        this.color = damageFieldToColor();
        evaluator = context.getResources().getString(R.string.evaluator_default_name);
        date = new Date(0);
    }

    /**
     * map type of damage to color
     * TODO
     * @return
     */
    private int damageFieldToColor() {
        return R.color.fieldDefaultColor;
    }


    @Override
    public Bundle getBundle() {
        Bundle bundle = new Bundle();
        bundle.putString(KEY_NAME, this.name);
        bundle.putInt(KEY_COLOR, this.color);
        bundle.putString(KEY_COUNTY, this.county);
        bundle.putDouble(KEY_SIZE, this.size);

        //damageField specific attributes
        bundle.putString(KEY_EVALUATOR, this.evaluator);
        //store date as a string - is probably easier
        bundle.putString(KEY_DATE, date.toString());
        return bundle;
    }


}


