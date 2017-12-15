package eisenwave.radar.model.track;

import eisenwave.radar.model.RadarDot;
import org.bukkit.entity.Player;

public interface RadarTracker {
    
    /**
     * Updates this tracker.
     */
    abstract RadarDot update();
    
    /**
     * Returns the type of this tracker.
     *
     * @return the type
     */
    abstract TrackerType getType();
    
    /**
     * Returns whether this tracker is visible to a given player.
     * <p>
     * The result of this method can depend on a variety of factors, such as the distance of the tracked object from
     * the player or the location.
     *
     * @param player the player
     * @return whether the tracker is visible
     */
    abstract boolean isVisibleTo(Player player);
    
}
