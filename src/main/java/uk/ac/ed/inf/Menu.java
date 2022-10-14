package uk.ac.ed.inf;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Class to represent a menu item.
 * @author s1808795
 * @version 1.0
 */
public class Menu {

    @JsonProperty("name")
    private String name;

    @JsonProperty("priceInPence")
    private int priceInPence;

    /**
     * @return String representing name of menu item.
     */
    public String getName() {
        return name;
    }

    /**
     * @return int representing price in pence of menu item.
     */
    public int getPriceInPence() {
        return priceInPence;
    }
}