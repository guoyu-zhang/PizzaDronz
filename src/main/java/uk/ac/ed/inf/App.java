package uk.ac.ed.inf;

import java.net.MalformedURLException;
import java.net.URL;

public class App
{
    public static void main( String[] args ) {
        LngLat lngLat = new LngLat(23, 23);
        System.out.println(lngLat.inCentralArea());

        LngLat lngLat2 = new LngLat(-3.199, 55.945);
        System.out.println(lngLat2.inCentralArea());

        LngLat lngLat3 = new LngLat(-3.199, 55.946);
        System.out.println(lngLat2.inCentralArea());
        System.out.println(lngLat.distanceTo(lngLat2));
        System.out.println(lngLat2.closeTo(lngLat3));

        LngLat lngLat4 = lngLat.nextPosition(Direction.N);
        System.out.println(lngLat4);

        Restaurant restaurant = new Restaurant();
        Restaurant[] restaurantss = new Restaurant[0];
        try {
            restaurantss = restaurant.getRestaurantsFromRestServer(new URL("https://ilp-rest.azurewebsites.net/restaurants"));
            System.out.println(restaurantss[0].getMenu()[0].getName());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Order order = new Order();
        int price = order.getDeliveryCost(restaurantss, "Margarita", "Margarita", "Calzone");
        System.out.println(price);
    }

}
