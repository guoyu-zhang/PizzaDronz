package uk.ac.ed.inf;

import java.util.Arrays;

public class Order {

    public int getDeliveryCost(Restaurant[] restaurants, String... pizzas){
        int totalCost = 100;

//         TODO add validation for Max of 4 pizzas, min of one

//        TODO throw invalidPizzaCombinationException
        Restaurant restaurant = Arrays.stream(restaurants)
                .filter(menu -> Arrays.stream(menu.getMenu())
                        .anyMatch(item -> item.getName().equals(pizzas[0])))
                .findFirst()
                .orElse(null);  // gives null if no restaurant had the pizza, so TODO add some following validation steps

        Menu[] menuItems = restaurant.getMenu();
        for (String pizza : pizzas) {
            int itemPrice = Arrays.stream(menuItems)
                    .filter( item -> item.getName().equals(pizza))
                    .findFirst()
                    .orElse(null)
                    .getPriceInPence();
            totalCost += itemPrice;
        }

        return totalCost;
    }
}
