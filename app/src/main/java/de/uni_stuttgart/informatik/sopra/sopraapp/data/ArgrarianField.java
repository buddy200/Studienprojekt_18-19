
package de.uni_stuttgart.informatik.sopra.sopraapp.data;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import de.uni_stuttgart.informatik.sopra.sopraapp.R;

/**
 * Created by Christian on 13.11.2017.
 */

public class ArgrarianField extends Field {
    private static final String TAG = "ArgrarianField";

    //default state
    private FieldStates state = FieldStates.NoDamage;

    private String owner;
    private String county;
    private int color;

    //bundle keys
    private static final String KEY_NAME = "name";
    private static final String KEY_STATE = "state";
    private static final String KEY_COLOR = "color";
    private static final String KEY_OWNER = "owner";
    private static final String KEY_COUNTY = "county";


    /**
     * fields need at least 3 corner points to exist
     */
    public ArgrarianField(List<CornerPoint> cPoints, Context context) {
        super(context, cPoints);
        //set default values
        owner = context.getResources().getString(R.string.owner_default_name);
        county = context.getResources().getString(R.string.county_default_name);
        color = stateToColor(state);
        setName(context.getResources().getString(R.string.field_default_name));
    }

    /**
     * map field state to color
     *
     * @param field
     * @return
     */
    protected int stateToColor(FieldStates field) {
        switch (field) {
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

    /**
     * starts an async task that tries to get the SubAdminArea
     * by reverse geocoding the first corner point of this field
     * might take a bit, thats why its async -FB
     */
    public void setAutomaticCounty(){
        county = "Loading..";

        new AsyncReverseGeoCoding().execute(new double[]{
                this.getCornerPoints().get(0).getWGS().getLatitude(),
                this.getCornerPoints().get(0).getWGS().getLongitude()
        });
    }

    /**
     * bundle helper function
     * @return
     */
    @Override
    public Bundle getBundle() {
        Bundle bundle = new Bundle();
        bundle.putString(KEY_NAME, this.getName());
        bundle.putSerializable(KEY_STATE, this.state);
        bundle.putInt(KEY_COLOR, stateToColor(this.state));
        bundle.putString(KEY_OWNER, this.getOwner());
        bundle.putString(KEY_COUNTY, this.getCounty());
        return bundle;
    }

    public void setState(FieldStates state) {
        this.state = state;
        color = stateToColor(state);
    }

    public FieldStates getState() {
        return this.state;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public int getColor(){
        return color;
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
            setCounty(doubles[0][0], doubles[0][1]);
            return null;
        }

        @Override
        protected void onPostExecute(String result) {

        }


        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}


        private void setCounty(double lat, double lon){
            Log.d(TAG, "fetching location..");
            //uses the google geocoder, might be a part of the google maps api.. or not -FB
            Geocoder geocoder = new Geocoder(context, Locale.getDefault());
            List<Address> addresses = null;
            try {
                addresses = geocoder.getFromLocation(lat, lon, 1);
                Address result;

                if (addresses != null && !addresses.isEmpty()) {
                    county = ""; //remove "loading..."
                    for(int i=0; i<= addresses.get(0).getMaxAddressLineIndex(); i++){
                        county += " " +  addresses.get(0).getAddressLine(i);
                    }
                }else {
                    county = "No Location Set";
                }
            } catch (IOException ignored) {
                //do something
            }
        }
    }

}

