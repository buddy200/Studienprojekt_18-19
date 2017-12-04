package de.uni_stuttgart.informatik.sopra.sopraapp.data;

import android.content.Context;
import android.os.Bundle;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import de.uni_stuttgart.informatik.sopra.sopraapp.R;
import de.uni_stuttgart.informatik.sopra.sopraapp.data.FieldTypes.DamageFieldType;

/**
 * Created by larsb on 22.11.2017.
 */

public class DamageField extends Field {
    private static final String TAG = "DamageField";

    private static final String KEY_DATE = "date";
    private static final String KEY_EVALUATOR = "evaluator";

    private String parsedDate;
    private String evaluator;


    public DamageField(Context context, List<CornerPoint> cPoints) {
        super(context, cPoints);
        this.setName(context.getResources().getString(R.string.field_default_name));
        this.setType(DamageFieldType.Aliens);
        this.setCounty(context.getResources().getString(R.string.county_default_name));
        this.setColor(damageFieldToColor());
        this.setEvaluator(context.getResources().getString(R.string.evaluator_default_name));
        this.setDate(new Date(0));
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
        bundle.putString(KEY_NAME, this.getName());
        bundle.putInt(KEY_COLOR, this.getColor());
        bundle.putString(KEY_COUNTY, this.getCounty());
        if(this.getSize() != null){
            bundle.putDouble(KEY_SIZE, this.getSize());
        }
        bundle.putSerializable(KEY_TYPE, (Serializable) this.getType());

        //damageField specific attributes
        bundle.putString(KEY_EVALUATOR, this.evaluator);
        bundle.putString(KEY_DATE, parsedDate);

        return bundle;
    }

    public String getParsedDate() { return parsedDate;}

    public void setDate(Date date) {
        //store date as a string - is probably easier
        SimpleDateFormat format1 = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat format2 = new SimpleDateFormat("hh:mm");
        String parsedDate =
                context.getResources().getString(R.string.date_label)
                        + " " + format1.format(date) + "\n" +
                        context.getResources().getString(R.string.time_label)
                        + " " + format2.format(date);

        this.parsedDate = parsedDate;
    }

    public String getEvaluator() {return evaluator;}

    public void setEvaluator(String evaluator) {this.evaluator = evaluator;}


}


