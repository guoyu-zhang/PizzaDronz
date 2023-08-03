package uk.ac.ed.inf;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Class to represent the longitude and latitude of a Location,
 * with methods to perform calculations using locations.
 * @author s1808795
 * @version 1.0
 */
public class LngLat implements Comparable<LngLat> {
    private static final double DISTANCE_TOLERANCE = 0.00015;
    private static final double MOVE_LENGTH = 0.00015;
    private final double lng;
    private final double lat;
    private LngLat parent = null;
    private double f = Double.MAX_VALUE;
    private double g = Double.MAX_VALUE;
    private ArrayList<LngLat> neighbours = new ArrayList<>();

    /**
     * Class constructor storing longitude and latitude values.
     * @param lng Longitude of position.
     * @param lat Latitude of position.
     */
    public LngLat(double lng, double lat) {
        this.lng = lng;
        this.lat = lat;
    }

    /**
     * This function checks whether the current location (LngLat object) lies within
     * the boundaries of central area. The calculation is sourced from link below.
     * https://web.archive.org/web/20161108113341/https://www.ecse.rpi.edu/Homepages/wrf/Research/Short_Notes/pnpoly.html
     * @return True if current location (LngLat object) lies within
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
     * @return Distance between two locations.
     */
    public double distanceTo(LngLat lngLat) {
        return Math.hypot(lngLat.lat - lat, lngLat.lng - lng);
    }

    /**
     * Determines whether two locations are close to each other.
     * In terms of specification, this is less than 0.00015 degrees.
     * @param lngLat The location to determine whether current location is close to.
     * @return True if locations are close to each other and false otherwise.
     */
    public boolean closeTo(LngLat lngLat) {
        return this.distanceTo(lngLat) < DISTANCE_TOLERANCE;
    }

    /**
     * Takes a direction then calculates and returns the new position.
     * @param direction Direction enum value which represents a compass direction
     *                  and its associated angle.
     * @return The new position after a move is made.
     */
    public LngLat nextPosition(Direction direction){
        if (direction == null || direction.angle == null) {
            return this;
        }
        double angleRadians = Math.toRadians(direction.angle);
        double destinationLng = lng + Math.cos(angleRadians) * MOVE_LENGTH;
        double destinationLat = lat + Math.sin(angleRadians) * MOVE_LENGTH;
        LngLat lngLat = new LngLat(destinationLng, destinationLat);
        return lngLat;
    }

    /**
     * Determines angle of direction taken from current node (current object) to get to destination
     * node (destination object) after a move.
     * @param destination Destination node (destination object) after a move.
     * @return Angle of direction taken.
     */
    public Double directionTaken(LngLat destination){
        for (Direction direction : Direction.values()) {
            if (nextPosition(direction).getLng() == destination.getLng()
                    && nextPosition(direction).getLat() == destination.getLat()) {
                return direction.angle;
            }
        }
        return null;
    }

    /**
     * Removes all neighbours from LngLat.
     */
    public void clearNeighbours(){
        neighbours.clear();
    }

    /**
     * Allows two nodes (LngLat) to be compared by their f values, used in path finding algorithm.
     * @param lngLat the object to be compared.
     * @return
     */
    @Override
    public int compareTo(LngLat lngLat) {
        return Double.compare(this.f, lngLat.f);
    }

    /**
     * @param f f value of this object, used in drone path finding algorithm.
     */
    public void setF(double f) {
        this.f = f;
    }

    /**
     * @param g g value of this object, used in drone path finding algorithm.
     */
    public void setG(double g) {
        this.g = g;
    }

    /**
     * @param parent Parent LngLat of this object.
     */
    public void setParent(LngLat parent) {
        this.parent = parent;
    }

    /**
     * Finds all nodes (LngLats) reachable from current object and adds them as neighbours.
     */
    public void setNeighbours() {
        Arrays.stream(Direction.values())
                .forEach(d -> neighbours.add(nextPosition(d)));
    }

    /**
     * @return Longitude of LngLat.
     */
    public double getLng() {
        return lng;
    }

    /**
     * @return Latitude of LngLat.
     */
    public double getLat() {
        return lat;
    }

    /**
     * @return f value of LngLat.
     */
    public double getF() {
        return f;
    }

    /**
     * @return g value of LngLat.
     */
    public double getG() {
        return g;
    }

    /**
     * @return Parent of LngLat.
     */
    public LngLat getParent() {
        return parent;
    }

    /**
     * @return Neighbours of LngLat.
     */
    public ArrayList<LngLat> getNeighbours() {
        return neighbours;
    }
}
