package uk.ac.ed.inf;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Class to represent a menu item.
 * @author s1808795
 * @version 1.0
 */
public class MenuItem {

    @JsonProperty("name")
    private String name;

    @JsonProperty("priceInPence")
    private int priceInPence;

    /**
     * @return Name of menu item.
     */
    public String getName() {
        return name;
    }

    /**
     * @return Price in pence of menu item.
     */
    public int getPriceInPence() {
        return priceInPence;
    }
}