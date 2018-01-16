package de.uni_stuttgart.informatik.sopra.sopraapp.data;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.util.Log;

import org.osmdroid.util.GeoPoint;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Queue;

import de.uni_stuttgart.informatik.sopra.sopraapp.R;
import de.uni_stuttgart.informatik.sopra.sopraapp.data.FieldTypes.FieldType;
import de.uni_stuttgart.informatik.sopra.sopraapp.data.geoData.Triangle;

/**
 * Created by larsb on 22.11.2017.
 * <p>
 * this class is used to generate custom fields containing differnt arguments
 * and several utility methods
 */

public abstract class Field implements Serializable {
    private static final String TAG = "Field";
    private static final long serialVersionUID = 11L;


    //keys for bundles
    static final String KEY_NAME = "name";
    static final String KEY_COLOR = "color";
    static final String KEY_COUNTY = "county";
    static final String KEY_SIZE = "size";
    static final String KEY_TYPE = "type";
    static final String KEY_CONVERTEDSIZE = "convertedSize";
    static final String KEY_TIMESTAMP = "timestamp";

    protected transient Context context;

    //values for field and damage case
    private long ID;
    private String name = "";
    FieldType type;
    private String county = "";
    private int color;
    private double size;

    private String conSize = "";

    private List<CornerPoint> cornerPoints = new ArrayList<>();


    //distinct timestamp to identify the field objects
    private long timestamp;
    /**
     * the rotation of the polygon
     * true if counterclockwise
     */
    private boolean rotation = false;

    private boolean finished = false;

    /**
     * constructor only used in custom Field classes via super()
     *
     * @param context
     * @param cPoints
     */
    public Field(Context context, List<CornerPoint> cPoints) {
        timestamp = Calendar.getInstance().getTime().getTime();

        this.context = context;

        //set default attributes
       /* if(context != null){
            this.name = context.getResources().getString(R.string.field_default_name);
            this.county = context.getResources().getString(R.string.county_default_name);
        }else {
            this.name = "no name";
            this.county = "no county";
        }*/
        this.size = 0.0;
        this.color = R.color.fieldDefaultColor;


        if (cPoints.size() < 2) {
            Log.e(TAG, "not enough corner points provided for field: " + name);
        } else {
            setCornerPoints(cPoints); //TODO: does this copy work? We might need some deepCopy() stuff here
            finish();
        }
    }

    /**
     * add a corner point to the field
     *
     * @param cp
     */
    public void addCornerPoint(CornerPoint cp) {
        if (cornerPoints.size() == 1) {
            setAutomaticCounty();
        }
        if (!finished) {
            cornerPoints.add(cp);
            if (cornerPoints.size() > 2) {
                cornerPoints.get(cornerPoints.size() - 2).calculateAngle(cornerPoints.get(cornerPoints.size() - 3), cp);
            }
        }
    }

    /**
     * only used if there will be no corner points added anymore
     */
    public void finish() {
        if (cornerPoints.size() > 2) {
            cornerPoints.get(cornerPoints.size() - 1).calculateAngle(cornerPoints.get(cornerPoints.size() - 2), cornerPoints.get(0));
            cornerPoints.get(0).calculateAngle(cornerPoints.get(cornerPoints.size() - 1), cornerPoints.get(1));
            calculateSize();
            convertSize();
            finished = true;
        }
    }

    /**
     * calculate the size of the polygon
     */
    private void calculateSize() {
        size = 0;
        List<CornerPoint> rmCopy = new ArrayList<>(cornerPoints);

        Queue<CornerPoint> outwardPoints = new LinkedList<>();
        List<Triangle> triangleList = new ArrayList<>();

        if (angleSum()) {
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
                    int indexBefore = ((index != 0) ? index - 1 : rmCopy.size() - 1);

                    //two outward Points following
                    if (angleCheck(rmCopy.get(indexBefore).getAngle())) {
                        i--;
                        outwardPoints.add(cp);
                    } else {
                        int indexTwoBefore = ((indexBefore != 0) ? indexBefore - 1 : rmCopy.size() - 1);
                        //make triangle
                        triangleList.add(new Triangle(rmCopy.get(indexTwoBefore), rmCopy.get(indexBefore), rmCopy.get(index)));
                        rmCopy.remove(indexBefore);

                        //recalculate angles
                        index = rmCopy.indexOf(cp);
                        indexBefore = ((index != 0) ? index - 1 : rmCopy.size() - 1);
                        int indexAfter = ((index == rmCopy.size() - 1) ? 0 : ++index);
                        indexTwoBefore = ((indexBefore != 0) ? indexBefore - 1 : rmCopy.size() - 1);
                        CornerPoint cpBefore = rmCopy.get(indexBefore);
                        cp.calculateAngle(cpBefore, rmCopy.get(indexAfter));
                        cpBefore.calculateAngle(rmCopy.get(indexTwoBefore), cp);
                        if (angleCheck(cp.getAngle())) {
                            outwardPoints.add(cp);
                        }
                        if (angleCheck(cpBefore.getAngle())) {
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

    /*
     * Convert the size of the field in a readable format
     */
    private void convertSize() {
        if (this.size > 1000000) {
            this.conSize = (String.valueOf(size / 1000000)) + "km" + "\u00B2";
        } else if (this.size > 10000 && this.size <= 1000000) {
            this.conSize = (String.valueOf(size / 10000)) + "ha";
        } else if (this.size > 100 && this.size <= 10000) {
            this.conSize = (String.valueOf(size / 100)) + "a";
        } else {
            this.conSize = (String.valueOf(size)) + "m" + "\u00B2";
        }
    }

    /**
     * check if angles are okay
     *
     * @param angle
     * @return
     */
    private boolean angleCheck(double angle) {
        return rotation ? angle <= Math.PI : angle >= Math.PI;
    }

    /**
     * calculate angle sum, might be wrong
     *
     * @return
     */
    private boolean angleSum() {
        double sum = 0;
        for (int i = 0; i < cornerPoints.size(); i++) {
            sum += cornerPoints.get(i).getAngle();
        }
        if (Math.abs(sum - (Math.PI * (cornerPoints.size() + 2))) < 0.001) {
            rotation = true;
            return true;
        } else if (Math.abs(sum - (Math.PI * (cornerPoints.size() - 2))) < 0.001) {
            return true;
        } else {
            //either wrong calculation or crossing lines in input polygon
            return false;
        }
    }

    /**
     * calculate centroid ( = center of gravity) of polygon
     */
    public GeoPoint calculateCentroid() {
        double lowX0, lowY0, highX1, highY1;

        lowX0 = lowY0 = Double.MAX_VALUE;
        highX1 = highY1 = Double.MIN_VALUE;

        for (CornerPoint point : getCornerPoints()) {
            if (lowX0 > point.getWGS().getLatitude()) {
                lowX0 = point.getWGS().getLatitude();
            }
            if (lowY0 > point.getWGS().getLongitude()) {
                lowY0 = point.getWGS().getLongitude();
            }
            if (highX1 < point.getWGS().getLatitude()) {
                highX1 = point.getWGS().getLatitude();
            }
            if (highY1 < point.getWGS().getLongitude()) {
                highY1 = point.getWGS().getLongitude();
            }
        }

        return new GeoPoint(lowX0 + ((highX1 - lowX0) / 2), lowY0 + ((highY1 - lowY0) / 2));
    }


    /**
     * returns Double to be able to null check this
     *
     * @return the size of the field or @code{null} if the field isn't finished
     */
    public Double getSize() {
        return finished ? size : null;
    }

    public void setCornerPoints(List<CornerPoint> cornerPoints) {
        for (CornerPoint cp : cornerPoints) {
            addCornerPoint(cp);
        }
    }

    public List<CornerPoint> getCornerPoints() {
        return cornerPoints;
    }

    public GeoPoint getCentroid() {
        return calculateCentroid();
    }

    public abstract Bundle getBundle();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public int getColor() {
        return color;
    }

    protected void setColor(int color) {
        this.color = color;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public String getConvertedSize() {
        return conSize;
    }

    public long getTimestamp() {
        return timestamp;
    }


    public FieldType getType() {
        return type;
    }

    /**
     * sets the Field type and the color
     *
     * @param type
     */
    public void setType(FieldType type) {
        this.type = type;
        this.setColor(type.toColor());
    }


    public boolean isFieldequal(Field otherField) {
        return otherField.getID() == this.getID();
    }

    /**
     * starts an async task that tries to get the SubAdminArea
     * by reverse geocoding the first corner point of this field
     * might take a bit, thats why its async -FB
     */
    public void setAutomaticCounty() {
        //   this.setCounty("Loading..");

        new AsyncReverseGeoCoding().execute(new double[]{
                this.getCornerPoints().get(0).getWGS().getLatitude(),
                this.getCornerPoints().get(0).getWGS().getLongitude()
        });
    }

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }


    /**
     * google asks its servers for reverse geo coding, this might take some time
     * especially for 100+ fields
     * try to call this only if necessary! -FB
     */
    private class AsyncReverseGeoCoding extends AsyncTask<double[], Void, String> {

        @Override
        protected String doInBackground(double[]... doubles) {
            //async task is weird.. TODO change this
            setCountyAddress(doubles[0][0], doubles[0][1]);
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }


        private void setCountyAddress(double lat, double lon) {
            Log.e(TAG, "fetching location..");
            //uses the google geocoder, might be a part of the google maps api.. or not -FB
            Geocoder geocoder = new Geocoder(context, Locale.getDefault());
            List<Address> addresses = null;
            try {
                addresses = geocoder.getFromLocation(lat, lon, 1);

                if (addresses != null && !addresses.isEmpty()) {
                    setCounty("Adress: "); //remove "loading..."
                    String county = "";
                    for (int i = 0; i <= addresses.get(0).getMaxAddressLineIndex(); i++) {
                        county += " " + addresses.get(0).getAddressLine(i);
                    }
                    setCounty(county);
                } else {
                    setCounty("No Location Set");
                }
            } catch (IOException ignored) {
                ignored.printStackTrace();
            }
        }
    }
}
