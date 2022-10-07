package uk.ac.ed.inf;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

public class App
{
    public static void main( String[] args ) {
//        LngLat lngLat = new LngLat(23, 23);
//        System.out.println(lngLat.inCentralArea());
//
//        LngLat lngLat2 = new LngLat(-3.199, 55.945);
//        System.out.println(lngLat2.inCentralArea());
//
//        LngLat lngLat3 = new LngLat(-3.199, 55.946);
//        System.out.println(lngLat2.inCentralArea());
//        System.out.println(lngLat.distanceTo(lngLat2));
//        System.out.println(lngLat2.closeTo(lngLat3));
//
//        LngLat lngLat4 = lngLat.nextPosition(Direction.N);
//        System.out.println(lngLat4);

        Restaurant restaurant = new Restaurant();
        Restaurant[] manyRestaurants = new Restaurant[0];
        try {
            manyRestaurants = restaurant.getRestaurantsFromRestServer(new URL("https://ilp-rest.azurewebsites.net"));
            System.out.println(manyRestaurants[0].getMenu()[0].getName());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Order order = new Order();
        int price = order.getDeliveryCost(manyRestaurants, "All Shrooms", "Super Cheese");
        System.out.println(price);
    }

}
