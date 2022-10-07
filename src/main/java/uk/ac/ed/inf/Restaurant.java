package uk.ac.ed.inf;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URL;

public class Restaurant {

    @JsonProperty("name")
    private String name;

    @JsonProperty("longitude")
    private double lng;

    @JsonProperty("latitude")
    private double lat;

    @JsonProperty("menu")
    private Menu[] menu;

    public String getName() {
        return name;
    }

    public Menu[] getMenu() {
        return menu;
    }

    public static Restaurant[] getRestaurantsFromRestServer (URL serverBaseAddress){
        try {
            ObjectMapper mapper = new ObjectMapper();
            Restaurant[] restaurants = mapper.readValue(serverBaseAddress, new TypeReference<>() {});
            return restaurants;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
