package uk.ac.ed.inf;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Class representing a restaurant, with method to fetch restaurant data.
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
    private MenuItem[] menuItems;

    private LngLat lngLat;

    private ArrayList<LngLat> flightPath;

    /**
     * Matches orders with their corresponding restaurants.
     * @param orders List of all orders.
     * @param restaurants List of all restaurants.
     */
    public static void matchRestaurantsAndOrders(Order[] orders, Restaurant[] restaurants){
        for (Order order : orders) {
            order.searchCorrespondingRestaurant(restaurants);
        }
    }

    /**
     * Given orders and restaurants, finds only restaurants that have at least one order placed there.
     * @param orders List of all orders.
     * @param restaurants List of all restaurants.
     * @return List of restaurants that have at least one order placed there.
     */
    public static ArrayList<Restaurant> getOnlyRestaurantsWithOrders(Order[] orders, Restaurant[] restaurants) {
        Set<String> restaurantsNamesWithOrders = Arrays.stream(orders).map(o -> o.getOrderRestaurant().getName()).collect(Collectors.toSet());
        ArrayList<Restaurant> restaurantsWithOrders = new ArrayList<>();

        for (Restaurant restaurant : restaurants) {
            if (restaurantsNamesWithOrders.contains(restaurant.getName())) {
                restaurantsWithOrders.add(restaurant);
            }
        }

        return restaurantsWithOrders;
    }

    /**
     * Ser LngLat of restaurant.
     */
    public void setLngLat() {
        this.lngLat = new LngLat(lng, lat);
    }

    /**
     * @param path Flight path for restaurant (origin from specifications to and from restaurant).
     */
    public void setFlightPath(ArrayList<LngLat> path) {
        flightPath = path;
    }

    /**
     * @param restaurants Restaurants for LngLats to be assigned to.
     */
    public static void setRestaurantCoords(Restaurant[] restaurants) {
        for (Restaurant restaurant : restaurants) {
            restaurant.setLngLat();
        }
    }

    /**
     * @return LngLat of restaurant.
     */
    public LngLat getLngLat() {
        return this.lngLat;
    }

    /**
     * @return Flight path for restaurant (origin from specifications to and from restaurant).
     */
    public ArrayList<LngLat> getFlightPath() {
        return flightPath;
    }

    /**
     * @return Name of restaurant.
     */
    public String getName() {
        return name;
    }

    /**
     * @return Longitude of restaurant.
     */
    public double getLng() {
        return lng;
    }

    /**
     * @return Latitude of restaurant.
     */
    public double getLat() {
        return lat;
    }

    /**
     * @return Menu items of restaurant.
     */
    public MenuItem[] getMenuItems() {
        return menuItems;
    }
}
