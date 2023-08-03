package uk.ac.ed.inf;

import java.util.ArrayList;

import static uk.ac.ed.inf.ValidationHelper.validateDate;

/**
 * App class, controls flow of system.
 * @author s1808795
 * @version 1.0
 */
public class App {
    public static void main( String[] args ){

        if (args.length != 3) {
            System.err.println("ERROR. Three arguments are required:");
            System.err.println("You must supply a date e.g. 2023-01-01, the base address of the ILP REST Service e.g. http://restservice.somewhere and a seed e.g. cabbage.");
            System.exit(1);
        }

        // Read arguments and create objects.
        String date = args[0];
        String baseUrl = args[1];
        String seed = args[2]; // Seed is not used.

        Drone drone = new Drone();
        Client client = new Client(baseUrl);
        FileOutput fileOutput = new FileOutput(date);

        if (validateDate("uuuu-MM-dd", date) == null) {
            System.err.println("ERROR. You must supply a date in the valid format: uuuu-MM-dd. Where uuuu represents year, MM represents month and dd represents day");
            System.exit(1);
        }

        // Get responses from REST service.
        Restaurant[] responseRestaurants = (Restaurant[]) client.getResponse(Subdirectory.RESTAURANTS.subdirectoryString);
        NoFlyZone[] responseNoFlyZones = (NoFlyZone[]) client.getResponse(Subdirectory.NO_FLY_ZONES.subdirectoryString);
        Order[] responseOrders = (Order[]) client.getResponse(Subdirectory.ORDERS.subdirectoryString, date);
        Location[] responseCentralArea = (Location[]) client.getResponse(Subdirectory.CENTRAL_AREA.subdirectoryString);

        // Set required object attributes from response data.
        CentralArea.getInstance().setData(responseCentralArea);
        Restaurant.setRestaurantCoords(responseRestaurants);
        Restaurant.matchRestaurantsAndOrders(responseOrders, responseRestaurants);

        // Find paths.
        Order.validateOrders(responseOrders, responseRestaurants);
        Order[] validOrders = Order.findOnlyValidOrders(responseOrders);

        ArrayList<Restaurant> restaurantsWithOrders = Restaurant.getOnlyRestaurantsWithOrders(validOrders, responseRestaurants);
        drone.findPathsForRestaurants(restaurantsWithOrders, responseNoFlyZones);
        drone.findPathForOrders(validOrders);

        ArrayList<LngLat> paths = drone.getTotalPath();

        // Output files
        fileOutput.outputDroneGeoJson(paths);
        fileOutput.outputDeliveries(responseOrders);
        fileOutput.outputFlightpath(validOrders, drone);
    }
}