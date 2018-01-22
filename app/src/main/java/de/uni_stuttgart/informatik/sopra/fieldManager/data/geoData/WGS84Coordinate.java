package de.uni_stuttgart.informatik.sopra.fieldManager.data.geoData;

import java.io.Serializable;

/**
 * a coordinate in the WGS84 coordinate system (latitude and longitude)
 *
 * @author Christian
 */
public class WGS84Coordinate implements Serializable{

    /**
     * latitude (N, S)
     */
    private double latitude; //Breitengrad

    /**
     * longitude (E, W)
     */
    private double longitude; //Laengengrad

    public  WGS84Coordinate() {
        this(0,0);
    }

    public WGS84Coordinate(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public boolean equals(WGS84Coordinate wgs) {
        return Math.abs(wgs.getLongitude() - longitude) < 0.000001 && Math.abs(wgs.getLatitude() - latitude) < 0.000001;
    }
    
    @Override
    public boolean equals(Object o) {
    	if (o instanceof WGS84Coordinate)
    		return this.equals((WGS84Coordinate)o);
    	else
    		return false;
    }
    
    @Override
    public String toString() {
    	String s = "";
    	s += Math.abs(latitude);
    	if(latitude < 0) {
    		s += "S ";
    	} else {
    		s += "N ";
    	}
    	s += Math.abs(longitude);
    	if(longitude < 0) {
    		s += "W";
    	} else {
    		s += "E";
    	}
    	return s;
    }
}
