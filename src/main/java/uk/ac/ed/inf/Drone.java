package uk.ac.ed.inf;

import java.awt.geom.Line2D;
import java.util.*;

/**
 * Class to represent a Drone. This includes storing variables it needs to operate (such as the maximum
 * moves it can make before running out of battery, or the paths it can take) and methods for its movement.
 * @author s1808795
 * @version 1.0
 */
public class Drone {
    private static final LngLat APPLETON_TOWER = new LngLat(-3.186874, 55.944494);
    private static final int MAX_MOVES = 2000;
    private static final double MOVE_WEIGHT = 0.00015;
    private static final double PATH_WEIGHT_MODIFIER = 0.8;
    private final ArrayList<LngLat> totalPath = new ArrayList<>();
    private long startTick = 0;
    private long pathCalculationTicks = 0;

    /**
     * Finds a path the drone can take from the start position to the end position whilst avoiding no-fly zones.
     * This also will not allow a drone to leave the central area once it is back inside having pathed from a
     * restaurant that is outside the central area.
     * This algorithm is adapted from the A* algorithm here: https://stackabuse.com/graphs-in-java-a-star-algorithm/.
     * More detail about this specific implementation can be found in the corresponding report documentation.
     * @param start Position the drone is starting from.
     * @param target Position the drone wants to finish at.
     * @param noFlyZones A list of no-fly zones which the drone cannot pass through.
     * @return A valid path for the drone, between start and target.
     */
    public ArrayList<LngLat> findPath(LngLat start, LngLat target, NoFlyZone[] noFlyZones) {
        PriorityQueue<LngLat> closedList = new PriorityQueue<>();
        PriorityQueue<LngLat> openList = new PriorityQueue<>();

        start.setG(0);
        start.setF(start.distanceTo(target));
        openList.add(start);

        while (!openList.isEmpty()) {
            LngLat node = openList.peek();
            node.setNeighbours();

            // If the current node is close to target we have found a path.
            if (node.closeTo(target)) {
                ArrayList<LngLat> path = retrievePath(node);
                return path;
            }

            for (LngLat neighbour : node.getNeighbours()) {
                // Below ensures drone only ever crosses central area boundaries once.
                // Specifics are detailed in drone control algorithm sections of corresponding report.
                if ((start.inCentralArea() && !node.inCentralArea() && neighbour.inCentralArea())) {
                    continue;
                }

                if (avoidNoFly(node, neighbour, noFlyZones)) {
                    if (!openList.contains(neighbour) && !closedList.contains(neighbour)) {
                        neighbour.setParent(node);
                        neighbour.setG(node.getG() + MOVE_WEIGHT);
                        neighbour.setF(neighbour.getG() * PATH_WEIGHT_MODIFIER + neighbour.distanceTo(target));
                        openList.add(neighbour);
                    } else if (neighbour.getG() > node.getG() + MOVE_WEIGHT) {
                        neighbour.setParent(node);
                        neighbour.setG(node.getG() + MOVE_WEIGHT);
                        neighbour.setF(neighbour.getG() * PATH_WEIGHT_MODIFIER + neighbour.distanceTo(target));

                        if (closedList.contains(neighbour)) {
                            closedList.remove(neighbour);
                            openList.add(neighbour);
                        }
                    }
                }
            }
            openList.remove(node);
            closedList.add(node);
            node.clearNeighbours();
        }
        return null;
    }

    /**
     * Checks if given a start position, and an end position, whether the move between them
     * passes through any no-fly zones.
     * @param origin Starting position of the move of the drone.
     * @param destination Ending positing of the move of the drone.
     * @param noFlyZones List of no-fly zones that the move cannot pass through.
     * @return True if move avoids no-fly zone, otherwise false.
     */
    public boolean avoidNoFly(LngLat origin, LngLat destination, NoFlyZone[] noFlyZones) {
        // This method creates a line for the move and makes sure it does not intersect any edges of any no-fly zone.
        Line2D line1 = new Line2D.Double(origin.getLng(),
                origin.getLat(),
                destination.getLng(),
                destination.getLat());

        // Creates a new line for each edge of no-fly zones.
        for (NoFlyZone noFlyZone : noFlyZones) {
            for (int i = 1; i < noFlyZone.getCoordinates().size(); i++) {
                ArrayList<Double> coords = noFlyZone.getCoordinates().get(i);
                ArrayList<Double> coords2 = noFlyZone.getCoordinates().get(i - 1);

                Line2D line2 = new Line2D.Double(coords.get(0),
                        coords.get(1),
                        coords2.get(0),
                        coords2.get(1));
                boolean result = line2.intersectsLine(line1);

                if (result) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Given a node, this will work the way up through its parents to create a path.
     * @param node A node, which in this case should be the final child node that is
     *             close to the destination of the drone flight path.
     * @return List of nodes (LngLat) representing a flight path.
     */
    public ArrayList<LngLat> retrievePath(LngLat node) {
        ArrayList<LngLat> path = new ArrayList<>();

        if (node == null)
            return path;

        while (node.getParent() != null) {
            path.add(node);
            node = node.getParent();
        }

        // Below is needed as the origin node does not have a parent,
        // so the while loop stops before it adds the origin node to the path.
        path.add(node);

        // The path is reversed below as the previous path is a result of working backwards
        // from the destination node.
        Collections.reverse(path);
        return path;
    }

    /**
     * Finds a valid path to get to and from all restaurant. The start position is always the location
     * of Appleton Tower as required by specifications.
     * The computation time recording also starts here. This will end in findTotalPath().
     * @param restaurants
     * @param noFlyZones
     */
    public void findPathsForRestaurants(ArrayList<Restaurant> restaurants, NoFlyZone[] noFlyZones) {
        startTick = System.nanoTime(); // Begins to record path computation time, this will end in findPathsForOrders.

        for (Restaurant restaurant : restaurants) {
            ArrayList<LngLat> path = new ArrayList<>();
            ArrayList<LngLat> pathTo = findPath(APPLETON_TOWER, restaurant.getLngLat(), noFlyZones);
            path.addAll(pathTo);
            Collections.reverse(pathTo); // Path is reversed therefore hover at restaurant (Collection) is accounted for.
            path.addAll(pathTo);
            path.add(pathTo.get(pathTo.size() - 1)); // Account for hover at Appleton Tower (Delivery).
            restaurant.setFlightPath(path);
        }
    }

    /**
     * Finds the flight paths for all valid orders.
     * This is done in a way to ensure the maximum number of orders are delivered before
     * the drone exhausts its maximum number of moves.
     * The computation time recording also ends in here. This started in findPathsForRestaurants.
     * @param validOrders
     */
    public void findPathForOrders(Order[] validOrders) {
        // Array is sorted in place.
        Arrays.sort(validOrders, Comparator.comparingInt(o -> o.getOrderRestaurant().getFlightPath().size()));

        // End of the path computation time, this started in findPathsForRestaurants, pathCalculationTime is now
        // the total of computing a valid path for each restaurant and sorting the valid orders.
        pathCalculationTicks += System.nanoTime() - startTick;

        for (Order order : validOrders) {
            long startTickPerOrder = System.nanoTime();
            ArrayList<LngLat> path = order.getOrderRestaurant().getFlightPath();

            if (totalPath.size() + path.size() < MAX_MOVES) {
                totalPath.addAll(path);
                order.setFlightPath(path);
                order.setOrderOutcome(OrderOutcome.DELIVERED);
            } else {
                order.setOrderOutcome(OrderOutcome.VALID_BUT_NOT_DELIVERED);
            }

            long elapsedTickPerOrder = System.nanoTime() - startTickPerOrder;
            order.setFlightPathTicks(elapsedTickPerOrder); // This will record the time taken for each order.
        }
    }

    /**
     * @return List containing the total path for the drone in one day.
     */
    public ArrayList<LngLat> getTotalPath() {
        return totalPath;
    }

    /**
     * @return Total time of computing valid paths for restaurants and sorting valid orders.
     */
    public long getPathCalculationTicks() {
        return pathCalculationTicks;
    }
}


