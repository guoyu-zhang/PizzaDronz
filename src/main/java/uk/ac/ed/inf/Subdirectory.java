package uk.ac.ed.inf;

/**
 * Subdirectories that can be used for getting responses from client.
 * Each subdirectory enum has a path string.
 * @author s1808795
 * @version 1.0
 */
public enum Subdirectory {
    /** Subdirectories along with their corresponding path strings. */
    RESTAURANTS("restaurants"),
    NO_FLY_ZONES("noFlyZones"),
    ORDERS("orders"),
    CENTRAL_AREA("centralArea");


    /** The path string associated with the specific subdirectory. */
    public final String subdirectoryString;

    /**
     * Class constructor which allows a value for a particular subdirectory
     * to be associated with a path string
     * @param subdirectoryString Path string for a subdirectory.
     */
    Subdirectory(String subdirectoryString) {
        this.subdirectoryString = subdirectoryString;
    }
}
