package eisenwave.radar.model;

import com.sun.istack.internal.NotNull;
import eisenwave.radar.data.RadarSymbol;
import eisenwave.radar.model.pos.RadarPosition;
import eisenwave.radar.model.pos.WorldRadarPos;
import org.bukkit.ChatColor;

public class WayPoint extends RadarDot {
    
    private String id;
    private boolean infRange = false;
    
    public WayPoint(@NotNull String id, @NotNull RadarPosition pos, @NotNull RadarSymbol symbol) {
        super(pos, symbol);
        this.id = id;
    }
    
    // GETTERS
    
    /**
     * Returns the id of this waypoint.
     *
     * @return the id
     */
    public String getId() {
        return id;
    }
    
    /**
     * Returns whether this waypoint is always visible, even if it's outside the regular maximum view range in the
     * world.
     *
     * @return whether this waypoint is infinitely visible
     */
    public boolean hasInfiniteRange() {
        return infRange;
    }
    
    // SETTERS
    
    public void setInfiniteRange(boolean infRange) {
        this.infRange = infRange;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    // MISC
    
    @Override
    public String toString() {
        if (getPosition() instanceof WorldRadarPos)
            return String.format("{symbol: \"%s\", pos: %s, infRange: %b}",
                getSymbol().toString() + ChatColor.RESET,
                getPosition(),
                infRange
            );
        else
            return String.format("{symbol: \"%s\", pos: %s}",
                getSymbol().toString() + ChatColor.RESET,
                getPosition()
            );
    }
    
}
