package uk.ac.ed.inf;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static uk.ac.ed.inf.ValidationHelper.*;

/**
 * Class to represent an order. With methods to perform validation and manipulate order data.
 * @author s1808795
 * @version 1.0
 */
public class Order {
    private static final Integer FIXED_CHARGE_IN_PENCE = 100;

    @JsonProperty("orderNo")
    private String orderNo;

    @JsonProperty("orderDate")
    private String orderDate;

    @JsonProperty("customer")
    private String customer;

    @JsonProperty("creditCardNumber")
    private String creditCardNumber;

    @JsonProperty("creditCardExpiry")
    private String creditCardExpiry;

    @JsonProperty("cvv")
    private String cvv;

    @JsonProperty("priceTotalInPence")
    private Integer priceTotalInPence;

    @JsonProperty("orderItems")
    private ArrayList<String> orderItems;

    private Restaurant orderRestaurant = null;

    private OrderOutcome orderOutcome = null;

    private ArrayList<LngLat> flightPath;

    private long flightPathTicks;

    /**
     * Calculates delivery cost in pence of having all pizzas delivered by drone.
     * @return Delivery cost in pence.
     */
    public Integer calculateDeliveryCost() {
        Integer totalCost = FIXED_CHARGE_IN_PENCE;
        MenuItem[] restaurantItems = orderRestaurant.getMenuItems();
        for (String pizza : orderItems) {
            MenuItem menuItem = Arrays.stream(restaurantItems).filter(item -> item.getName().equals(pizza)).findFirst().get();
            totalCost += menuItem.getPriceInPence();
        }
        return totalCost;
    }

    /**
     * Find corresponding restaurant for order.
     * @param restaurants List of all restaurants.
     */
    public void searchCorrespondingRestaurant(Restaurant[] restaurants) {
        for (Restaurant restaurant : restaurants){
            ArrayList<String> restaurantItems = Arrays.stream(restaurant.getMenuItems())
                    .map(MenuItem::getName)
                    .collect(Collectors.toCollection(ArrayList::new));
            if (restaurantItems.containsAll(orderItems)) {
                orderRestaurant = restaurant;
            }
        }
    }

    /**
     * Makes sure order is valid by calling all other validation methods.
     * @param restaurants List of all restaurants.
     * @return True if order passes all validation checks, false otherwise.
     */
    public boolean validateOrder(Restaurant[] restaurants) {
         return ( validateCardNumber()
         && validateExpiryDate()
         && validateCvv()
         && validatePizzaNotDefined(restaurants)
         && validatePizzaCount()
         && validatePizzaCombinationMultipleSuppliers(restaurants)
         && validateTotal(restaurants));
    }

    /**
     * Go through each order and validate it according to specifications.
     * @param orders List of all orders.
     * @param restaurants List of all restaurants.
     */
    public static void validateOrders(Order[] orders, Restaurant[] restaurants) {
        for (Order order : orders) {
            if (order.validateOrder(restaurants)) {
                order.setOrderOutcome(OrderOutcome.VALID);
            };
        }
    }

    /**
     * Find only the valid orders from a list of all orders.
     * @param orders List of all orders.
     * @return List of only valid orders.
     */
    public static Order[] findOnlyValidOrders(Order[] orders) {
        Order[] validOrders = Arrays.stream(orders).filter(o -> o.getOrderOutcome() == OrderOutcome.VALID).toList().toArray(new Order[0]);
        return validOrders;
    }

    /**
     * Ensures credit card number is valid, and is of Visa or Mastercard.
     * @return True if card number is valid, false otherwise.
     */
    private boolean validateCardNumber() {
        boolean isValid = false;
        if (isNumeric(creditCardNumber)
                && checkLuhn(creditCardNumber)
                && creditCardNumber.length() == 16) {
            String regex = "^(?:(?<visa>4[0-9]{3})|"
                    + "(?<mastercard>(222[1-9]|22[3-9][0-9]|2[3-6][0-9]{2}|27[01][0-9]|2720|5[1-5][0-9]{2})))";
            isValid = checkRegex(regex, creditCardNumber.substring(0,4));
        }
        if (!isValid) {
            orderOutcome = OrderOutcome.INVALID_CARD_NUMBER;
        }
        return isValid;
    }

    /**
     * Ensures credit card has a valid expiry date that occurs after order date.
     * @return True if expiry date is valid, false otherwise.
     */
    private boolean validateExpiryDate() {
        boolean isValid = false;
        LocalDate orderDateValid = validateDate("uuuu-MM-dd", orderDate);
        String regex = "^(0[1-9]|1[012])[- \\/.]([0-9]{2})$";
        if (checkRegex(regex, creditCardExpiry)) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yy");
            YearMonth yearMonth = YearMonth.parse(creditCardExpiry, formatter);
            LocalDate cardExpiryDate = yearMonth.atEndOfMonth();
            isValid = cardExpiryDate.isAfter(orderDateValid) || cardExpiryDate.isEqual(orderDateValid);
        }
        if (!isValid) {
            orderOutcome = OrderOutcome.INVALID_EXPIRY_DATE;
        }
        return isValid;
    }

    /**
     * Ensures credit card cvv is valid, being made up of three numbers.
     * @return True if cvv is valid, false otherwise.
     */
    private boolean validateCvv() {
        boolean isValid = false;
        if (isNumeric(cvv)) {
            String regex = "^[0-9]{3}$";
            isValid = checkRegex(regex, cvv);
        }
        if (!isValid) {
            orderOutcome = OrderOutcome.INVALID_CVV;
        }
        return isValid;
    }

    /**
     * Ensures the total price in pence of order is correct, as in the cost of individual
     * items and delivery sums up to the total price associated with order.
     * @param restaurants List of all restaurants.
     * @return True if total price in pence of order is correct, false otherwise.
     */
    private boolean validateTotal(Restaurant[] restaurants) {
        boolean isValid = false;
        if (validatePizzaNotDefined(restaurants) && validatePizzaCombinationMultipleSuppliers(restaurants)) {
            if (orderRestaurant != null) {
                Integer deliveryCost = calculateDeliveryCost();
                isValid = (deliveryCost.equals(priceTotalInPence));
            }
        }
        if (!isValid) {
            orderOutcome = OrderOutcome.INVALID_TOTAL;
        }
        return isValid;
    }

    /**
     * Ensures order item(s) exist on at least one restaurant's menu.
     * @param restaurants List of all restaurants.
     * @return True if order item(s) exist on at least one restaurant's menu, false otherwise.
     */
    private boolean validatePizzaNotDefined(Restaurant[] restaurants) {
        boolean isValid;
        ArrayList<String> validPizzas = Arrays.stream(restaurants)
                .map(restaurant -> Arrays.stream(restaurant.getMenuItems())
                        .map(MenuItem::getName)
                        .collect(Collectors.toList()))
                .flatMap(Collection::stream)
                .collect(Collectors.toCollection(ArrayList::new));
        isValid = validPizzas.containsAll(orderItems);
        if (!isValid) {
            orderOutcome = OrderOutcome.INVALID_PIZZA_NOT_DEFINED;
        }
        return isValid;
    }

    /**
     * Ensures the number of pizzas ordered is valid (between and including 1 to 4).
     * @return True if number of pizzas ordered is valid, otherwise false.
     */
    private boolean validatePizzaCount() {
        boolean isValid = (orderItems.size() >= 1 && orderItems.size() <= 4);
        if (!isValid) {
            orderOutcome = OrderOutcome.INVALID_PIZZA_COUNT;
        }
        return isValid;
    }

    //  Makes sure pizzas on the order doesn't come from multiple restaurants
    //  InvalidPizzaCombinationMultipleSuppliers is for pizzas that do exist in one of the restaurants
    //  but the customer is trying to order from multiple restaurants at once which is not allowed.

    /**
     * Ensures order items (pizzas) do not come from multiple restaurants.
     * @param restaurants List of all restaurants.
     * @return True if order items do not come from multiple restaurants, otherwise false.
     */
    private boolean validatePizzaCombinationMultipleSuppliers(Restaurant[] restaurants) {
        boolean supplierIdentified = false;
        for (Restaurant restaurant : restaurants){
            ArrayList<String> restaurantItems = Arrays.stream(restaurant.getMenuItems()).map(MenuItem::getName).collect(Collectors.toCollection(ArrayList::new));
            if (restaurantItems.containsAll(orderItems)) {
                supplierIdentified = true;
            }
        }
        if (!supplierIdentified) {
            orderOutcome = OrderOutcome.INVALID_PIZZA_COMBINATION_MULTIPLE_SUPPLIERS;
        }
        return supplierIdentified;
    }

    /**
     * @param orderOutcome Outcome of order.
     */
    public void setOrderOutcome(OrderOutcome orderOutcome) {
        this.orderOutcome = orderOutcome;
    }

    /**
     * @param flightPath Flight path associated with collecting and delivering order.
     */
    public void setFlightPath(ArrayList<LngLat> flightPath){
        this.flightPath = flightPath;
    }

    /**
     * @param ticks Ticks for flight path computation for order.
     */
    public void setFlightPathTicks(long ticks) {
        flightPathTicks = ticks;
    }

    /**
     * @return Order number.
     */
    public String getOrderNo() {
        return orderNo;
    }

    /**
     * @return Date associated with order.
     */
    public String getOrderDate() {
        return orderDate;
    }

    /**
     * @return Customer associated with order.
     */
    public String getCustomer() {
        return customer;
    }

    /**
     * @return Credit card number associated with order.
     */
    public String getCreditCardNumber() {
        return creditCardNumber;
    }

    /**
     * @return Credit card expiry date associated with order.
     */
    public String getCreditCardExpiry() {
        return creditCardExpiry;
    }

    /**
     * @return Credit card cvv associated with order.
     */
    public String getCvv() {
        return cvv;
    }

    /**
     * @return Total price in pence of order.
     */
    public Integer getPriceTotalInPence() {
        return priceTotalInPence;
    }

    /**
     * @return Items of order.
     */
    public ArrayList<String> getOrderItems() {
        return orderItems;
    }

    /**
     * @return Restaurant associated with order.
     */
    public Restaurant getOrderRestaurant() {
        return orderRestaurant;
    }

    /**
     * @return Outcome of order, whether it has been validated or not.
     */
    public OrderOutcome getOrderOutcome() {
        return orderOutcome;
    }

    /**
     * @return Flight path associated with collecting and delivering order.
     */
    public ArrayList<LngLat> getFlightPath() {
        return flightPath;
    }

    /**
     * @return Ticks for flight path computation for order.
     */
    public long getFlightPathTicks(){
        return flightPathTicks;
    }
}
