package de.uni_stuttgart.informatik.sopra.sopraapp.data;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import org.osmdroid.util.GeoPoint;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import de.uni_stuttgart.informatik.sopra.sopraapp.R;
import de.uni_stuttgart.informatik.sopra.sopraapp.data.FieldTypes.FieldType;
import de.uni_stuttgart.informatik.sopra.sopraapp.data.geoData.Triangle;

/**
 * Created by larsb on 22.11.2017.
 */

public abstract class Field implements Serializable{


    private static final String TAG = "Field";

    //keys
    static final String KEY_NAME = "name";
    static final String KEY_COLOR = "color";
    static final String KEY_COUNTY = "county";
    static final String KEY_SIZE = "size";
    static final String KEY_TYPE = "type";

    protected transient Context context;


    //values for field and damage case
    private String name;
    private FieldType type;
    private String county;
    private int color;
    private double size;

   private static final long serialVersionUID = 11L;

    private List<CornerPoint> cornerPoints = new ArrayList<>();

    /**
     * the rotation of the polygon
     * true if counterclockwise
     */
    private boolean rotation = false;

    private boolean finished = false;



    public Field() {

    }

    public Field(Context context, List<CornerPoint> cPoints) {
        this.context = context;

        //set default attributes
        this.name = context.getResources().getString(R.string.field_default_name);
        this.county = context.getResources().getString(R.string.county_default_name);
        this.size = 0.0;
        this.color = R.color.fieldDefaultColor;


        if (cPoints.size() < 2) {
            Log.e(TAG, "not enough corner points provided for field: " + name);
        } else {
            setCornerPoints(cPoints); //TODO: does this copy work? We might need some deepCopy() stuff here
        }
    }


    public void addCornerPoint (CornerPoint cp) {
        if(!finished) {
            cornerPoints.add(cp);
            if (cornerPoints.size() > 2) {
                cornerPoints.get(cornerPoints.size() - 2).calculateAngle(cornerPoints.get(cornerPoints.size() - 3), cp);
            }
        }
    }

    public void finish() {
        if (cornerPoints.size() > 2) {
            cornerPoints.get(cornerPoints.size() - 1).calculateAngle(cornerPoints.get(cornerPoints.size() - 2), cornerPoints.get(0));
            cornerPoints.get(0).calculateAngle(cornerPoints.get(cornerPoints.size() - 1), cornerPoints.get(1));
            calculateSize();
            finished = true;
        }
    }

    private void calculateSize() {
        List<CornerPoint> rmCopy = new ArrayList<>(cornerPoints.size());
        Collections.copy(rmCopy, cornerPoints);
        Queue<CornerPoint> outwardPoints = new LinkedList<>();
        List<Triangle> triangleList = new ArrayList<>();

        if(angleSum()) {
            for (CornerPoint cp : cornerPoints) {
                if (angleCheck(cp.getAngle())) {
                    outwardPoints.add(cp);
                }
            }

            for (int i = 0; i < cornerPoints.size() - 2; i++) {
                if (outwardPoints.isEmpty()) {
                    triangleList.add(new Triangle(rmCopy.get(0), rmCopy.get(1), rmCopy.get(2)));
                    rmCopy.remove(1);
                } else {
                    CornerPoint cp = outwardPoints.poll();
                    int index = rmCopy.indexOf(cp);
                    int indexBefore = ((index == 0)? index-1 : rmCopy.size()-1);

                    //two outward Points following
                    if (angleCheck(rmCopy.get(indexBefore).getAngle())) {
                        i--;
                        outwardPoints.add(cp);
                    } else {
                        int indexTwoBefore = ((indexBefore == 0) ? indexBefore-1 : rmCopy.size()-1);
                        int indexThreeBefore = ((indexTwoBefore == 0) ? indexTwoBefore-1 : rmCopy.size()-1);
                        int indexAfter = ((index == rmCopy.size()-1) ? 0 : ++index);
                        //make triangle
                        triangleList.add(new Triangle(rmCopy.get(indexTwoBefore), rmCopy.get(indexBefore), rmCopy.get(index)));
                        rmCopy.remove(indexBefore);

                        //recalculate angles
                        CornerPoint cpBefore = rmCopy.get(indexTwoBefore);
                        cp.calculateAngle(cpBefore, rmCopy.get(indexAfter));
                        cpBefore.calculateAngle(rmCopy.get(indexThreeBefore), cp);
                        if(angleCheck(cp.getAngle())) {
                            outwardPoints.add(cp);
                        }
                        if(angleCheck(cpBefore.getAngle())) {
                            outwardPoints.add(cpBefore);
                        }
                    }
                }
            }

            for (Triangle t : triangleList) {
                size += t.getSize();
            }
        }
    }

    private boolean angleCheck(double angle) {
        return rotation ? angle <= Math.PI : angle >= Math.PI;
    }

    private boolean angleSum() {
        double sum = 0;
        for(int i = 0; i < cornerPoints.size();i++) {
            sum += cornerPoints.get(i).getAngle();
        }
        if (Math.abs(sum - (Math.PI * (cornerPoints.size()+2)))< 0.001) {
            rotation = true;
            return true;
        } else if(Math.abs(sum - (Math.PI * (cornerPoints.size()-2)))< 0.001) {
            return true;
        } else {
            //either wrong calculation or crossing lines in input polygon
            return false;
        }
    }

    /**
     * calculate centroid ( = center of gravity) of polygon
     */
    public GeoPoint calculateCentroid(){
        double lowX0, lowY0, highX1, highY1;

        lowX0 = lowY0 = Double.MAX_VALUE;
        highX1 = highY1 = Double.MIN_VALUE;

        for(CornerPoint point : getCornerPoints()){
            if(lowX0 > point.getWGS().getLatitude()){
                lowX0 = point.getWGS().getLatitude();
            }
            if(lowY0 > point.getWGS().getLongitude()){
                lowY0 = point.getWGS().getLongitude();
            }
            if(highX1 < point.getWGS().getLatitude()){
                highX1 = point.getWGS().getLatitude();
            }
            if(highY1 < point.getWGS().getLongitude()){
                highY1 = point.getWGS().getLongitude();
            }
        }

        return new GeoPoint(lowX0 + ((highX1 - lowX0) / 2), lowY0 + ((highY1 - lowY0) / 2));
    }

    /**
     * @return the size of the field or @code{null} if the field isn't finished
     */
    public Double getSize() {
        return finished ? size : null; //sorry, ich kann keinen nullcheck für getSize() machen wenns vom typ double ist, mit Double gehts - F
    }

    public void setCornerPoints(List<CornerPoint> cornerPoints) {
        this.cornerPoints = cornerPoints;
    }

    public List<CornerPoint> getCornerPoints() {
        return cornerPoints;
    }

    public GeoPoint getCentroid(){ return calculateCentroid();}

    public abstract Bundle getBundle();

    public String getName() {return name;}

    public void setName(String name) {this.name = name;}

    public String getCounty() {return county;}

    public void setCounty(String county) {this.county = county;}

    public int getColor() {return color;}

    protected void setColor(int color){this.color = color;}

    public void setContext(Context context){
        this.context = context;
    }


    public FieldType getType() {return type;}

    public void setType(FieldType type) {
        this.type = type;
        this.setColor(type.toColor());
    }
}
