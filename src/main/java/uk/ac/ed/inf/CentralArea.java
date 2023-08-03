package uk.ac.ed.inf;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Class to represent central area boundaries.
 * Implemented with singleton access pattern.
 * @author s1808795
 * @version 1.0
 */
public class CentralArea {
    private static ArrayList<Location> locations;
    private static CentralArea INSTANCE;

    private CentralArea() {
    }

    /**
     * An instance of CentralArea is created if it does not already exist.
     * The instance is then returned.
     * @return This returns the single instance of the class CentralArea.
     */
    public static CentralArea getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CentralArea();
        }
        return INSTANCE;
    }

    /**
     * Sets the list of Locations forming the boundaries of the central area.
     * @param locations List of central area locations.
     */
    public void setData(Location[] locations) {
        this.locations = new ArrayList(Arrays.asList(locations));
    }

    /**
     * @return List of Locations forming the boundary of the central area.
     */
    public static ArrayList<Location> getData() {
        return locations;
    }

}
