package uk.ac.ed.inf;

import com.mapbox.geojson.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Class to generate output files.
 * @author s1808795
 * @version 1.0
 */
public class FileOutput {
    private final String date;

    /**
     * Class constructor, this stores the date so that files can be outputted with date signature.
     * @param date Date of current orders.
     */
    public FileOutput(String date) {
        this.date = date;
    }

    /**
     * Creates a geoJSON file from a flight path which consists of a list of LngLat objects.
     * @param flightPath Flight path of drone, this is a list of LngLat objects.
     */
    public void outputDroneGeoJson(ArrayList<LngLat> flightPath) {
        List<Point> points = new ArrayList<>();
        for (LngLat lngLat: flightPath) {
            Point point = Point.fromLngLat(lngLat.getLng(), lngLat.getLat());
            points.add(point);
        }

        LineString lineString = LineString.fromLngLats(points);
        FeatureCollection featureCollection = FeatureCollection.fromFeature(Feature.fromGeometry(lineString));
        featureCollection.toJson();

        generateFile("drone-", ".geojson", featureCollection.toJson());
    }

    /**
     * Creates a JSON file from a list of orders and specified properties.
     * @param orders A list of orders.
     * @param drone The drone object (this contains path calculation times).
     */
    public void outputFlightpath(Order[] orders, Drone drone) {
        JSONArray array = new JSONArray();
        long currentTicks = drone.getPathCalculationTicks();

        for (Order order : orders) {
            if (order.getOrderOutcome() == OrderOutcome.DELIVERED) {
                ArrayList<LngLat> flightPath = order.getFlightPath();
                long orderTick = order.getFlightPathTicks();
                long moveTick = orderTick / flightPath.size();
                for (int i = 0; i < flightPath.size() - 1; i++) {
                    currentTicks += moveTick;
                    JSONObject json = new JSONObject();
                    LngLat start = flightPath.get(i);
                    LngLat end = flightPath.get(i+1);
                    json.put("orderNo", order.getOrderNo());
                    json.put("fromLongitude", start.getLng());
                    json.put("fromLatitude", start.getLat());
                    json.put("angle", String.valueOf(start.directionTaken(end)));
                    json.put("toLongitude", end.getLng());
                    json.put("toLatitude", end.getLat());
                    json.put("ticksSinceStartOfCalculation", currentTicks);
                    array.add(json);
                }
            }
        }

        generateFile("flightpath-", ".json", String.valueOf(array));
    }

    /**
     * Creates a JSON file from all orders and specified properties.
     * @param orders A list of orders.
     */
    public void outputDeliveries(Order[] orders) {
        JSONArray array = new JSONArray();

        for (Order order : orders) {
            JSONObject json = new JSONObject();
            json.put("orderNo", order.getOrderNo());
            json.put("outcome", order.getOrderOutcome().toString());
            json.put("costInPence", order.getPriceTotalInPence());
            array.add(json);
        }

        generateFile("deliveries-", ".json", String.valueOf(array));
    }

    /**
     * Generates a file given file properties and content.
     * @param fileName Name of file to be generated.
     * @param fileExtension Extension of file to be generated.
     * @param fileContent Content of file to be generated.
     */
    public void generateFile(String fileName, String fileExtension, String fileContent) {
        try {
            FileWriter file = new FileWriter(fileName + date + fileExtension);
            file.write(fileContent);
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
