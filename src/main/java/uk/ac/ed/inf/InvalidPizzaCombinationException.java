package uk.ac.ed.inf;

/**
 * The InvalidPizzaCombinationException is used to show when the ordered pizza combination
 * cannot be delivered by the same restaurant.
 * @author s1808795
 * @version 1.0
 */
public class InvalidPizzaCombinationException extends Exception {

    /**
     * Class constructor.
     */
    public InvalidPizzaCombinationException() {
        super("Invalid Pizza Combination. Pizzas cannot be delivered by the same restaurant.");
    }
}
