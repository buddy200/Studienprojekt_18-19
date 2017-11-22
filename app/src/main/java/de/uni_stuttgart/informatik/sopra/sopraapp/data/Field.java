
package de.uni_stuttgart.informatik.sopra.sopraapp.data;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;
import java.util.List;

import de.uni_stuttgart.informatik.sopra.sopraapp.R;

/**
 * Created by Christian on 13.11.2017.
 */

public class Field extends Field_DamageCase {

    FieldStates state = FieldStates.NoDamage;

    //bundle keys
    private static final String KEY_NAME = "title";
    private static final String KEY_STATE = "state";



    /**
     * fields need at least 3 corner points to exist
     */
    public Field(List<CornerPoint> cPoints, Context context) {
        super(context);
        //set default values
        this.name = "Field";
        if(cPoints.size() < 2){
            Log.e(TAG, "not enough corner points provided for field: " + getName());
        }else {
            this.cornerPoints = cPoints; //TODO: does this copy work? We might need some deepCopy() stuff here
        }
    }


    public void initPolygon() {
        List<GeoPoint> polyPoints = new ArrayList<>();
        for (CornerPoint point : getCornerPoints()) {
            polyPoints.add(new GeoPoint(point.getWGS().getLatitude(), point.getWGS().getLongitude()));
        }
        // add field attributes to polygon attributes
        getFieldPolygon().setPoints(polyPoints);
        getFieldPolygon().setFillColor(stateToPolygonColor(this.state));
        // invisible borders look really cool :D
        getFieldPolygon().setTitle(getName());

    }

    /**
     * map field state to color
     * @param field
     * @return
     */
    protected int stateToPolygonColor(FieldStates field) {
        switch (field){
            case NoDamage:
                return ContextCompat.getColor(context, R.color.stateNoDamage);
            case LightDamage:
                return ContextCompat.getColor(context, R.color.stateLightDamage);
            case HighDamage:
                return ContextCompat.getColor(context, R.color.stateHighDamage);
            default:
                return ContextCompat.getColor(context, R.color.stateDefault);
        }
    }


    public void setState(FieldStates state){
        this.state = state;
    }

    public FieldStates getState(){
        return this.state;
    }


    @Override
    public Bundle getBundle(){
        Bundle bundle = new Bundle();
        bundle.putString(KEY_NAME, getName());
        bundle.putSerializable(KEY_STATE, state);
        return bundle;
    }

}
