package de.uni_stuttgart.informatik.sopra.sopraapp.data;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.uni_stuttgart.informatik.sopra.sopraapp.R;
import de.uni_stuttgart.informatik.sopra.sopraapp.data.FieldTypes.AgrarianFieldType;
import de.uni_stuttgart.informatik.sopra.sopraapp.data.FieldTypes.DamageFieldType;

/**
 * Created by larsb on 22.11.2017.
 *
 * a custom field class, containing fields that represent damage
 */

public class DamageField extends Field implements Serializable{
    private static final String TAG = "DamageField";

    private static final String KEY_DATE = "date";
    private static final String KEY_EVALUATOR = "evaluator";
    private static final String KEY_IMAGES = "images";

    private static final long serialVersionUID = 8L;

    private String parsedDate;
    private String evaluator;

    private ArrayList<PictureData> paths;

    private DamageFieldType type = DamageFieldType.Aliens;


    /**
     * constructor
     * @param context
     * @param cPoints
     */
    public DamageField(Context context, List<CornerPoint> cPoints) {
        super(context, cPoints);
        this.setName(context.getResources().getString(R.string.field_default_name));
        this.setType(type);
        this.setCounty(context.getResources().getString(R.string.county_default_name));
        this.setColor(damageFieldToColor());
        this.setEvaluator(context.getResources().getString(R.string.evaluator_default_name));
        this.setDate(new Date(0));
        this.paths = new ArrayList<>();

    }

    /**
     * map type of damage to color
     * TODO
     * @return
     */
    private int damageFieldToColor() {
        return R.color.fieldDefaultColor;
    }


    /**
     * bundle for passing data in the UI
     * @return
     */
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
        bundle.putSerializable(KEY_IMAGES, paths);

        return bundle;
    }

    public String getParsedDate() { return parsedDate;}

    /**
     * return the date in a readable format
     * @param date
     */
    public void setDate(Date date) {
        //store date as a string - is probably easier
        SimpleDateFormat format1 = new SimpleDateFormat("dd-MM-yyyy");
        String parsedDate =
                context.getResources().getString(R.string.date_label)
                        + " " + format1.format(date);

        this.parsedDate = parsedDate;
    }

    public void setDate(String date){
        this.parsedDate = date;
    }

    public String getEvaluator() {return evaluator;}

    public void setEvaluator(String evaluator) {this.evaluator = evaluator;}


    public ArrayList<PictureData> getpaths() {
        return paths;
    }

    public void setpaths (ArrayList<PictureData> data){
        this.paths = data;
    }

    public void setpath(String path) {
        PictureData pictureData = new PictureData((new Integer(paths.size())).toString(), path);
        paths.add(pictureData);
    }

}


