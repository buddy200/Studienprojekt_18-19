package de.uni_stuttgart.informatik.sopra.fieldManager.data.geoData;

import java.io.Serializable;

/**
 * a coordinate in the UTM (Universal Transverse Mercator) coordinate system
 *
 * @author Christian
 */
public class UTMCoordinate implements Serializable{

	/**
	 * the zone of this UTMCoordinate
	 * if not set, the default value is -100
	 */
    private int zone = -100;

    /**
     * the northing of this UTMCoordinate
     */
    private long northing; //Hochwert

    /**
     * the easting of this UTMCoordinate
     */
    private long easting; //Rechtswert

    public UTMCoordinate() {

    }

    public int getZone() {
        return zone;
    }

    public void setZone(int zone) {
    		this.zone = zone;
    }

    public long getNorthing() {
        return northing;
    }

    public void setNorthing(long northing) {
        this.northing = northing;
    }

    public long getEasting() {
        return easting;
    }

    public void setEasting(long easting) {
        this.easting = easting;
    }

    /**
     * returns if this Coordinate is on the southern hemisphere
     * 
     * @return true if the coordinate is on the southern hemisphere, else false
     */
    public boolean isSouth() {
        return northing < 0;
    }
    
    @Override
    public boolean equals(Object o ) {
        if (o instanceof UTMCoordinate) {
            return this.equals((UTMCoordinate)o);
        } else {
            return false;
        }
    }
    
    public boolean equals(UTMCoordinate utm) {
    	return this.zone == utm.zone && this.northing == utm.northing && this.easting == utm.easting;
    }
}
