package de.uni_stuttgart.informatik.sopra.sopraapp.data;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;

import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.uni_stuttgart.informatik.sopra.sopraapp.R;

/**
 * Created by larsb on 22.11.2017.
 */

public class DamageField extends Field {

    private Date date = new Date(0);
    private String evaluator;

    private ArgrarianField argrarianField;

    public DamageField(Context context, List<CornerPoint> cPoints) {
        super(context, cPoints);
        this.evaluator = context.getResources().getString(R.string.evaluator_default_name);
        setName(context.getResources().getString(R.string.damage_case_defaut_name));
    }

    public ArgrarianField getArgrarianField() {
        return argrarianField;
    }

    public void setArgrarianField(ArgrarianField argrarianField) {
        this.argrarianField = argrarianField;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getEvaluator() {
        return evaluator;
    }

    public void setEvaluator(String evaluator) {
        this.evaluator = evaluator;
    }

    @Override
    public Bundle getBundle() {
        return null;
    }


}


