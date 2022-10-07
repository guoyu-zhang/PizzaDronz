package test;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TestResponse {
    @JsonProperty("greeting")
    public String greeting;

    @JsonProperty("name")
    public String name;

    @JsonProperty("longitude")
    public String longitude;

    @JsonProperty("latitude")
    public String latitude;
}