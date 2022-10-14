package uk.ac.ed.inf;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.URL;
import java.net.URISyntaxException;
import java.net.MalformedURLException;
import java.io.IOException;

/**
 * Class representing a restaurant, with method to fetch restaurant data.
 *
 * @author s1808795
 * @version 1.0
 */
public class Restaurant {

    @JsonProperty("name")
    private String name;

    @JsonProperty("longitude")
    private double lng;

    @JsonProperty("latitude")
    private double lat;

    @JsonProperty("menu")
    private Menu[] menu;

    /**
     * Fetches and returns restaurants data from REST server.
     *
     * @param serverBaseAddress URL representing base address of REST server.
     * @return Restaurant array representing data for all restaurants obtained from REST server.
     */
    public static Restaurant[] getRestaurantsFromRestServer(URL serverBaseAddress) {
        URL newUrl = null;
        URI uri = null;
        ObjectMapper mapper = new ObjectMapper();
        Restaurant[] restaurants = new Restaurant[0];

        try {
            uri = serverBaseAddress.toURI();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        String newPath = uri.getPath() + "/restaurants";
        try {
            newUrl = uri.resolve(newPath).toURL();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        try {
            restaurants = mapper.readValue(newUrl, new TypeReference<>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        return restaurants;
    }

    /**
     * @return String representing name of restaurant.
     */
    public String getName() {
        return name;
    }

    /**
     * @return double representing longitude of restaurant.
     */
    public double getLng() {
        return lng;
    }

    /**
     * @return double representing longitude of restaurant.
     */
    public double getLat() {
        return lat;
    }

    /**
     * @return Menu array representing menu items of restaurant.
     */
    public Menu[] getMenu() {
        return menu;
    }
}
