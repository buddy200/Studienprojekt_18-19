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

    private List<GeoPoint> geoPoints = new ArrayList<>();

    /**
     * the rotation of the polygon
     * true if counterclockwise
     */
    private boolean rotation = false;


    /**
     * constructor only used in custom Field classes via super()
     *
     * @param context
     * @param gPoints
     */
    public Field(Context context, List<GeoPoint> gPoints) {
        this.context = context;
        this.size = 0.0;
        this.color = R.color.fieldDefaultColor;

        if (gPoints.size() < 2) {
        } else {
            setGeoPoints(gPoints);
            calculateSize();
        }
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
    public void calculateSize() {
        size = 0;
        double firstSum = 0.0;
        double secondSum = 0.0;
        ArrayList<Vector<Double>> cordinates = new ArrayList<>();
        for (int i = 0; i < geoPoints.size(); i++) {
            cordinates.add(calculateCartesianCoordinates(geoPoints.get(i).getLatitude(), geoPoints.get(i).getLongitude()));
        }

        //area calculation with cross prduct
        for (int i = 1; i < cordinates.size(); i++) {
            firstSum = firstSum + (cordinates.get(i - 1).get(0) * cordinates.get(i).get(1));
            secondSum = secondSum + (cordinates.get(i - 1).get(1) * cordinates.get(i).get(0));
        }
        firstSum = firstSum + cordinates.get(cordinates.size() - 1).get(0) * cordinates.get(0).get(1);
        secondSum = secondSum + cordinates.get(cordinates.size() - 1).get(1) * cordinates.get(0).get(0);
        size = Math.abs((firstSum - secondSum) / 2);
        this.conSize = convertSize(this.size);
    }


    /**
     * Convert the size of the field in a readable format
     */
    public String convertSize(double size) {
        String conSize = "";
        if (size > 1000000) {
            conSize = (String.format("%.4f", size / 1000000)) + "km" + "\u00B2";
        } else if (size > 10000 && size <= 1000000) {
            conSize = (String.format("%.4f", size / 10000)) + "ha";
        } else if (size > 100 && size <= 10000) {
            conSize = (String.format("%.4f", size / 100)) + "a";
        } else {
            conSize = (String.format("%.4f", size)) + "m" + "\u00B2";
        }
        return conSize;
    }

    /**
     * calculate centroid ( = center of gravity) of polygon
     */
    public GeoPoint calculateCentroid() {
        double lowX0, lowY0, highX1, highY1;

        lowX0 = lowY0 = Double.MAX_VALUE;
        highX1 = highY1 = Double.MIN_VALUE;

        for (GeoPoint point : getGeoPoints()) {
            if (lowX0 > point.getLatitude()) {
                lowX0 = point.getLatitude();
            }
            if (lowY0 > point.getLongitude()) {
                lowY0 = point.getLongitude();
            }
            if (highX1 < point.getLatitude()) {
                highX1 = point.getLatitude();
            }
            if (highY1 < point.getLongitude()) {
                highY1 = point.getLongitude();
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

    public void setGeoPoints(List<GeoPoint> geoPoints) {
        for (GeoPoint gp : geoPoints) {
            this.geoPoints.add(gp);
            if(this.geoPoints.size() == 1)
                setAutomaticCounty();
        }
    }

    public List<GeoPoint> getGeoPoints() {
        return geoPoints;
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
    }


    /**
     * starts an async task that tries to get the SubAdminArea
     * by reverse geocoding the first corner point of this field
     * might take a bit, thats why its async -FB
     */
    public void setAutomaticCounty() {
        new AsyncReverseGeoCoding().execute(new double[]{
                this.getGeoPoints().get(0).getLatitude(),
                this.getGeoPoints().get(0).getLongitude()
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
                        if(county.equals("")){
                            county += addresses.get(0).getAddressLine(i);
                        }else{
                            county += " " + addresses.get(0).getAddressLine(i);
                        }
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