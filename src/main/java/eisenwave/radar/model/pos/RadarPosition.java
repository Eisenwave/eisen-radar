package eisenwave.radar.model.pos;

public interface RadarPosition {
    
    /**
     * Returns the squared distance to an observer.
     * <p>
     * If the distance is constant and always far way, {@link Double#POSITIVE_INFINITY} is returned.
     *
     * @param x the x-coordinate
     * @param z the z-coordinate
     * @return the squared distance
     */
    abstract double squaredDistanceTo(double x, double z);
    
    /**
     * <p>
     * Returns the yaw of this radar position relative to coordinates in the world. This is equivalent to the
     * pitch of the vector between the given position and this position, should this position be in the world.
     * <p>
     * For certain positions, such as cardinal directions which always stay the same, this may return a constant
     * value.
     * </p>
     *
     * @param x the x-coordinate
     * @param z the z-coordinate
     * @return the yaw of the vector between the given and this position
     */
    abstract float yawRelTo(double x, double z);
    
    default double distanceTo(double x, double z) {
        return Math.sqrt(squaredDistanceTo(x, z));
    }
    
    /**
     * <p>
     * Returns the yaw of this radar position relative to coordinates in the world. This is equivalent to the
     * pitch of the vector between the given position and this position, should this position be in the world.
     * <p>
     * For certain positions, such as cardinal directions which always stay the same, this may return a constant
     * value.
     *
     * @param x the observer x-coordinate
     * @param z the observer z-coordinate
     * @param yaw the observer yaw (0 - 359)
     * @return the yaw of the vector between the given and this position
     */
    default float yawRelTo(double x, double z, float yaw) {
        return yawRelTo(x, z) - yaw;
    }
    
}
