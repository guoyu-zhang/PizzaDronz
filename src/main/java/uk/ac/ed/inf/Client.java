package uk.ac.ed.inf;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Class to represent a client to get responses from a REST server.
 * @author s1808795
 * @version 1.0
 */
public class Client {
    private final String baseUrl;

    /**
     * Class constructor which ensures subdirectories can be added to a base URL by guaranteeing the
     * base URL ends with a "/". The base URL is stored so only a subdirectory is required for a request.
     * @param baseUrl Base URL of the REST server.
     */
    public Client(String baseUrl) {
        if (!baseUrl.endsWith("/")) {
            baseUrl += "/";
        }
        this.baseUrl = baseUrl;
    }


    /**
     * Client to get data from REST server. This can be used for all necessary subdirectories.
     * @param urlComponents Components making up the URL, the first element is a subdirectory,
     *                      such as "restaurant", the second element can be the date for an order.
     * @return Response of Object corresponding to the URL.
     */
    public Object getResponse(String... urlComponents) {
        String subDirectory = urlComponents[0];
        Object response = null;
        Class responseClass = null;

        // Switch determines which class to use for reading values in ObjectMapper.
        switch (subDirectory) {
            case "restaurants":
                responseClass = Restaurant[].class;
                break;
            case "noFlyZones":
                responseClass = NoFlyZone[].class;
                break;
            case "orders":
                responseClass = Order[].class;
                if (!subDirectory.endsWith("/")) {
                    subDirectory += "/";
                }
                if (urlComponents.length > 1) {
                    String date = urlComponents[1];
                    subDirectory = subDirectory + date;
                }
                break;
            case "centralArea":
                responseClass = Location[].class;
                break;
            default:
                System.err.println("ERROR. subdirectory provided is invalid");
                System.exit(1);
                break;
        }

        try {
            URL url = new URL(baseUrl + subDirectory);
            response = new ObjectMapper().readValue(url, responseClass);
        } catch (MalformedURLException e) {
            System.err.println("ERROR. Please ensure URL supplied is valid.");
            System.exit(1);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        return response;
    }
}
