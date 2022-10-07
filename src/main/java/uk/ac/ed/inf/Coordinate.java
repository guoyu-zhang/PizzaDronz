package uk.ac.ed.inf;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Coordinate {
    @JsonProperty("name")
    private String name;

    @JsonProperty("longitude")
    private double lng;

    @JsonProperty("latitude")
    private double lat;

    public String getName() {
        return name;
    }

    public double getLng() {
        return lng;
    }

    public double getLat() {
        return lat;
    }
}
