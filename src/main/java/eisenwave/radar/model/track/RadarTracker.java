package eisenwave.radar.model.track;

import eisenwave.radar.data.RadarSymbol;
import eisenwave.radar.view.RadarBar;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public abstract class RadarTracker {
    
    protected final World world;
    protected RadarSymbol symbol;
    
    public RadarTracker(@NotNull World world, @NotNull RadarSymbol symbol) {
        this.world = world;
        this.symbol = symbol;
    }
    
    // IMPL
    
    public World getWorld() {
        return world;
    }
    
    public RadarSymbol getSymbol() {
        return symbol;
    }
    
    public void setSymbol(@NotNull RadarSymbol symbol) {
        this.symbol = symbol;
    }
    
    // ABSTRACT
    
    
    /**
     * Returns the type of this tracker.
     *
     * @return the type
     */
    public abstract TrackerType getType();
    
    /**
     * Updates this tracker.
     */
    public abstract void update();
    
    public abstract void display(Player player, RadarBar bar);
    
    /*
     * Returns whether this tracker is visible to a given player.
     * <p>
     * The result of this method can depend on a variety of factors, such as the distance of the tracked object from
     * the player or the location.
     *
     * @param player the player
     * @return whether the tracker is visible
     *
    abstract boolean isVisibleTo(Player player);
    */
    
}
