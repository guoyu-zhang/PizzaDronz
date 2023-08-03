package uk.ac.ed.inf;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Class to represent a location associated with the central area.
 * @author s1808795
 * @version 1.0
 */
public class Location {

    @JsonProperty("name")
    private String name;

    @JsonProperty("longitude")
    private double lng;

    @JsonProperty("latitude")
    private double lat;

    /**
     *@return Name of location.
     * */
    public String getName() {
        return name;
    }

    /**
     *@return Longitude of location.
     * */
    public double getLng() {
        return lng;
    }

    /**
     *@return Latitude of location.
     * */
    public double getLat() {
        return lat;
    }
}
