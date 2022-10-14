package uk.ac.ed.inf;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URL;
import java.io.IOException;

import java.util.ArrayList;

/**
 * Class to represent central area boundaries.
 * Implemented with singleton access pattern.
 * @author s1808795
 * @version 1.0
 */
public class CentralArea {

    private static ArrayList<Location> locations = new ArrayList<>();
    private static CentralArea INSTANCE;

    private CentralArea() {
    }

    /**
     * An instance of CentralArea is created if it does not already exist.
     * Data is fetched and stored in the instance of CentralArea. The
     * instance is then returned.
     * @return This returns the single instance of the class CentralArea.
     */
    public static CentralArea getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CentralArea();
            INSTANCE.fetchData();
        }
        return INSTANCE;
    }

    /**
     * Fetches and stores data for locations, which form the boundary of the
     * central area, from the endpoint (which currently has a default value).
     * @exception IOException if data unable to be read from endpoint.
     */
    public void fetchData() {
        String url = "https://ilp-rest.azurewebsites.net/centralArea";
        ObjectMapper mapper = new ObjectMapper();
        try {
            locations = mapper.readValue(new URL(url), new TypeReference<>() {
            });
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns list of Locations forming the boundary of the central area.
     * @return ArrayList of Location, locations.
     */
    public static ArrayList<Location> getData() {
        return locations;
    }
}
