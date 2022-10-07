package uk.ac.ed.inf;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class Location {

    public static ArrayList<Coordinate> coordinates = new ArrayList<Coordinate>();
    private static Location INSTANCE;

    private Location() {
    }

    public static Location getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Location();
            INSTANCE.fetchData();
        }
        return INSTANCE;
    }

    public void fetchData() {
        System.out.println("i keep on running");
        try {
            String baseUrl = "https://ilp-rest.azurewebsites.net/centralArea";
            ObjectMapper mapper = new ObjectMapper();

            coordinates = mapper.readValue(new URL(baseUrl), new TypeReference<>() {
            });
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<Coordinate> getData() {
        return coordinates;
    }
}
