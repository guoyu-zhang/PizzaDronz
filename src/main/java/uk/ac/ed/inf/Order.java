package uk.ac.ed.inf;

import java.util.Arrays;

/**
 * Class to represent an order.
 *
 * @author s1808795
 * @version 1.0
 */
public class Order {

    /**
     * Calculates delivery cost in pence of having all pizzas delivered by drone.
     *
     * @param restaurants Restaurant array representing participating restaurants including their menus.
     * @param pizzas      variable number of String's representing names of pizzas ordered.
     * @return int representing delivery cost in pence, in the case of an
     * invalid pizza combination -1 is returned.
     * @throws InvalidPizzaCombinationException if pizzas do not all come from same restaurant.
     */
    public int getDeliveryCost(Restaurant[] restaurants, String... pizzas) {
        int totalCost = 100; // Standard delivery charge of 100 pence.

        // It is sufficient to get the restaurant that has the first pizza, as
        // if any of the following pizzas do not exist on the menu, it can
        // be concluded that the pizzas do not come from the same restaurant.
        Restaurant restaurant = Arrays.stream(restaurants)
                .filter(menu -> Arrays.stream(menu.getMenu())
                        .anyMatch(item -> item.getName().equals(pizzas[0])))
                .findFirst()
                .orElse(null);

        Menu[] menuItems = restaurant.getMenu();
        try {
            for (String pizza : pizzas) {
                Menu menuItem = Arrays.stream(menuItems)
                        .filter(item -> item.getName().equals(pizza))
                        .findFirst()
                        .orElseThrow(() -> new InvalidPizzaCombinationException());
                totalCost += menuItem.getPriceInPence();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }

        return totalCost;
    }
}
