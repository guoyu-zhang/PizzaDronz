package uk.ac.ed.inf;

/**
 * Directions that can be used for the drone movement.
 * Each direction has a corresponding angle.
 * @author s1808795
 * @version 1.0
 */
public enum Direction {
    /** Tertiary inter-cardinal directions along with their corresponding angles. */
    E(0.0), ENE(22.5), NE(45.0), NNE(67.5),
    N(90.0), NNW(112.5), NW(135.0), WNW(157.5),
    W(180.0), WSW(202.5), SW(225.0), SSW(247.5),
    S(270.0), SSE(292.5), SE(315.0), ESE(337.5),
    HOVER(null);

    /** The angle associated with the specific direction. */
    public final Double angle;

    /**
     * Class constructor which allows a value for angle to be
     * associated with a particular direction.
     * @param angle Angle associated with a specific direction.
     */
    Direction(Double angle) {
        this.angle = angle;
    }

}
