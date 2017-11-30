package eisenwave.radar.model.pos;

public interface RadarPosition {
    
    /**
     * <p>
     *     Returns the yaw of this radar position relative to coordinates in the world. This is equivalent to the
     *     pitch of the vector between the given position and this position, should this position be in the world.
     * </p>
     * <p>
     *     For certain positions, such as cardinal directions which always stay the same, this may return a constant
     *     value.
     * </p>
     *
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @return the yaw of the vector between the given and this position
     */
    abstract float yawRelTo(double x, double y);
    
    /**
     * <p>
     *     Returns the yaw of this radar position relative to coordinates in the world. This is equivalent to the
     *     pitch of the vector between the given position and this position, should this position be in the world.
     * </p>
     * <p>
     *     For certain positions, such as cardinal directions which always stay the same, this may return a constant
     *     value.
     * </p>
     *
     * @param x the observer x-coordinate
     * @param y the observer y-coordinate
     * @param yaw the observer yaw (0 - 359)
     * @return the yaw of the vector between the given and this position
     */
    default float yawRelTo(double x, double y, float yaw) {
        return yawRelTo(x, y) - yaw;
    }
    
}
