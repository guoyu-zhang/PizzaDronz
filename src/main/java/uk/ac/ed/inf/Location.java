package uk.ac.ed.inf;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Class to represent a Location.
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
     *@return String representing name of location.
     * */
    public String getName() {
        return name;
    }

    /**
     *@return double representing longitude of location.
     * */
    public double getLng() {
        return lng;
    }

    /**
     *@return double representing latitude of location.
     * */
    public double getLat() {
        return lat;
    }
}
