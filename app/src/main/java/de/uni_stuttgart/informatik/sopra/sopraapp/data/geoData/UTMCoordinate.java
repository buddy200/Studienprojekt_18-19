package de.uni_stuttgart.informatik.sopra.sopraapp.data.geoData;

/**
 * Created by Christian on 13.11.2017.
 */

public class UTMCoordinate {

    /**
     * the zone in longitude
     * between 1 and 60
     */
    private int zone;

    /**
     * the zonefield in latitude
     * big character between C and X (excluding I and O)
     */
    private char latitudeBand;

    /**
     * the north Value
     *
     */
    private int northValue;

    /**
     * the east value (based of the middle merdian)
     *
     */
    private int eastValue;

    public UTMCoordinate() {

    }

    public int getZone() {
        return zone;
    }

    public void setZone(int zone) {
        this.zone = zone;
    }

    public char getLatitudeBand() {
        return latitudeBand;
    }

    public void setLatitudeBand(char latitudeBand) {
        this.latitudeBand = latitudeBand;
    }

    public int getNorthValue() {
        return northValue;
    }

    public void setNorthValue(int northValue) {
        this.northValue = northValue;
    }

    public int getEastValue() {
        return eastValue;
    }

    public void setEastValue(int eastValue) {
        this.eastValue = eastValue;
    }
}
