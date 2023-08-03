package uk.ac.ed.inf;

/**
 * Order outcomes that can be used for the order classification.
 * @author s1808795
 * @version 1.0
 */
public enum OrderOutcome {
    VALID,
    DELIVERED,
    VALID_BUT_NOT_DELIVERED,
    INVALID_CARD_NUMBER,
    INVALID_EXPIRY_DATE,
    INVALID_CVV,
    INVALID_TOTAL,
    INVALID_PIZZA_NOT_DEFINED,
    INVALID_PIZZA_COUNT,
    INVALID_PIZZA_COMBINATION_MULTIPLE_SUPPLIERS,
}
