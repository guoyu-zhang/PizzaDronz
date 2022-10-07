package uk.ac.ed.inf;

import java.util.ArrayList;
import java.util.Arrays;

public class Order {

    public int getDeliveryCost(Restaurant[] restaurants, String... pizzas){
        int price = 1;

//         Max of 4 pizzas, min of one

//        Filter to get the menu that contains first pizza
//        then loop through to calculate price, if a pizza is not there then
//        throw exception.
        for (String pizza : pizzas) {
            for (Restaurant restaurant : restaurants) {
                Menu[] menus = restaurant.getMenu();
                for(Menu menuItem : menus) {
                    if (menuItem.getName().equals(pizza)) {
                        price+=menuItem.getPriceInPence();
                    }
                }
            }
        }


        return price;
    }
}
