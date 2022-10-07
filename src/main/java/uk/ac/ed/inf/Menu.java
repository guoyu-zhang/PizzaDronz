package uk.ac.ed.inf;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Menu {
    @JsonProperty("name")
    private String name;

    public String getName() {
        return name;
    }

    @JsonProperty("priceInPence")
    private int priceInPence;

    public int getPriceInPence() {
        return priceInPence;
    }
}