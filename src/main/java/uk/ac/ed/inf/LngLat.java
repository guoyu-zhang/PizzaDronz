package uk.ac.ed.inf;

import java.util.ArrayList;

/**
 * Record to represent the longitude and latitude of a Location,
 * with methods to perform calculations using locations.
 * @param lng longitude of location.
 * @param lat latitude of location.
 * @author s1808795
 * @version 1.0
 */
public record LngLat(double lng, double lat){

    /**
     * This function checks whether the current location (LngLat object) lies within
     * the boundaries of central area. The calculation is sourced from link below.
     * https://web.archive.org/web/20161108113341/https://www.ecse.rpi.edu/Homepages/wrf/Research/Short_Notes/pnpoly.html
     * @return boolean value, true if current location (LngLat object) lies within
     * the boundaries of central area, and false otherwise.
     */
    public boolean inCentralArea() {
        CentralArea centralArea = CentralArea.getInstance();
        ArrayList<Location> locations =  centralArea.getData();

        // Checks whether current location (LngLat object) lies on any of the points forming the boundaries of central area.
        if (locations.stream().anyMatch(s -> ((s.getLng() == lng) && (s.getLat()==lat)))){
            return true;
        }

        // Checks whether current location (LngLat object) lies within boundaries of central area.
        boolean odd = false;
        for (int i = 0, j = locations.size() - 1; i < locations.size(); i++) {
            if (((locations.get(i).getLat() >= lat) != (locations.get(j).getLat() >= lat))
                    && (lng <= (locations.get(j).getLng() - locations.get(i).getLng())
                    * (lat - locations.get(i).getLat()) / (locations.get(j).getLat()
                    - locations.get(i).getLat() ) + locations.get(i).getLng())) {
                odd = !odd;
            }
            j = i;
        }
        return odd;
    }

    /**
     * Calculates and returns distance between two locations.
     * @param lngLat The location to calculate distance to, from current location.
     * @return double value representing distance between two locations.
     */
    public double distanceTo(LngLat lngLat) {
        return Math.hypot(lngLat.lat - lat, lngLat.lng - lng);
    }

    /**
     * Determines whether two locations are close to each other.
     * In terms of specification, this is less than 0.00015 degrees.
     * @param lngLat The location to determine whether current location is close to.
     * @return boolean value, true if locations are close to each other and false otherwise.
     */
    public boolean closeTo(LngLat lngLat) {
        return this.distanceTo(lngLat) < 0.00015;
    }

    /**
     * Takes a direction then calculates and returns the new position.
     * @param direction Direction enum value which represents a compass direction
     *                  and its associated angle.
     * @return LngLat value representing the new position after a move is made.
     */
    public LngLat nextPosition(Direction direction){
        if (direction == null) {
            return this;
        }
        double angleRadians = Math.toRadians(direction.angle);
        double destinationLng = lng + Math.cos(angleRadians) * 0.00015;
        double destinationLat = lat + Math.sin(angleRadians) * 0.00015;
        LngLat lngLat = new LngLat(destinationLng, destinationLat);
        return lngLat;
    }
};
