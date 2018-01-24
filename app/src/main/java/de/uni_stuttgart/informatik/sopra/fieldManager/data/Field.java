package de.uni_stuttgart.informatik.sopra.fieldManager.data;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;

import org.osmdroid.util.GeoPoint;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

import de.uni_stuttgart.informatik.sopra.fieldManager.R;
import de.uni_stuttgart.informatik.sopra.fieldManager.data.FieldTypes.FieldType;

/**
 * Created by larsb on 22.11.2017.
 * <p>
 * this class is used to generate custom fields containing differnt arguments
 * and several utility methods
 */

public abstract class Field implements Serializable {
    private static final String TAG = "Field";
    private static final long serialVersionUID = 11L;

    protected transient Context context;

    //values for field and damage case
    private long ID;
    private String name = "";
    private FieldType type;
    private String county = "";
    private int color;
    private double size;

    private String conSize = "";

    private List<CornerPoint> cornerPoints = new ArrayList<>();

    /**
     * the rotation of the polygon
     * true if counterclockwise
     */
    private boolean rotation = false;


    /**
     * constructor only used in custom Field classes via super()
     *
     * @param context
     * @param cPoints
     */
    public Field(Context context, List<CornerPoint> cPoints) {
        this.context = context;
        this.size = 0.0;
        this.color = R.color.fieldDefaultColor;

        if (cPoints.size() < 2) {
        } else {
            setCornerPoints(cPoints);
            calculateSize();
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
        cornerPoints.add(cp);
    }

    /**
     * calculates out of the latitude and longitude, cartesian coordinates
     *
     * @param latitude
     * @param longitude
     * @return
     */
    private Vector<Double> calculateCartesianCoordinates(double latitude, double longitude) {
        Vector<Double> coordinate = new java.util.Vector<>();
        coordinate.add(6371000 * ((longitude * Math.PI) / 180) * Math.cos((Math.PI * getCentroid().getLatitude()) / 180));
        coordinate.add(6371000 * (latitude * Math.PI) / 180);
        return coordinate;
    }

    /**
     * calculate the size of the polygon
     */
    void calculateSize() {
        size = 0;
        double firstSum = 0.0;
        double secondSum = 0.0;
        ArrayList<Vector<Double>> cordinates = new ArrayList<>();
        for (int i = 0; i < cornerPoints.size(); i++) {
            cordinates.add(calculateCartesianCoordinates(cornerPoints.get(i).getWGS().getLatitude(), cornerPoints.get(i).getWGS().getLongitude()));
        }

        //area calculation with cross prduct
        for (int i = 1; i < cordinates.size(); i++) {
            firstSum = firstSum + (cordinates.get(i - 1).get(0) * cordinates.get(i).get(1));
            secondSum = secondSum + (cordinates.get(i - 1).get(1) * cordinates.get(i).get(0));
        }
        firstSum = firstSum + cordinates.get(cordinates.size() - 1).get(0) * cordinates.get(0).get(1);
        secondSum = secondSum + cordinates.get(cordinates.size() - 1).get(1) * cordinates.get(0).get(0);
        size = Math.abs((firstSum - secondSum) / 2);
        convertSize();
    }


    /**
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
     * returns the size of this field in m^2
     *
     * @return the size of the field or 0 if the field isn't finished
     */
    public double getSize() {
        return  size;
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