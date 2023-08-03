package uk.ac.ed.inf;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

/**
 * Class to represent a no-fly zone.
 * @author s1808795
 * @version 1.0
 */
public class NoFlyZone {
    @JsonProperty("name")
    private String name;

    @JsonProperty("coordinates")
    private ArrayList<ArrayList<Double>> coordinates;

    /**
     * @return Name of no-fly zone.
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @return Coordinates of no-fly zone.
     */
    public ArrayList<ArrayList<Double>> getCoordinates(){
        return coordinates;
    }
}
