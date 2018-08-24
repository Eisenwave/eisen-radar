package eisenwave.radar.model.tracker;

import eisenwave.radar.EisenRadarPlugin;
import eisenwave.radar.data.RadarSymbol;
import eisenwave.radar.view.RadarBar;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public abstract class RadarTracker {
    
    private final Plugin plugin;
    protected final World world;
    protected RadarSymbol symbol;
    
    public RadarTracker(EisenRadarPlugin plugin, @NotNull World world, @NotNull RadarSymbol symbol) {
        this.plugin = plugin;
        this.world = world;
        this.symbol = symbol;
    }
    
    // IMPL
    
    public Plugin getPlugin() {
        return plugin;
    }
    
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
     * Displays this tracker to a player and their radar bar.
     * <p>
     * This method is usually called once a tick.
     *
     * @param player the player
     * @param bar the radar bar
     */
    public abstract void display(Player player, RadarBar bar);
    
    /**
     * Returns the type of this tracker.
     *
     * @return the type
     */
    public abstract TrackerType getType();
    
    // ABSTRACT OPTIONAL
    
    /**
     * Called when the the tracker is being enabled.
     */
    public void onEnable() {}
    
    /**
     * Called when the tracker is being disabled.
     */
    public void onDisable() {}
    
    /**
     * Called each tick and allows the tracker to make updates to the data it is meant to display exactly once a tick.
     */
    public void onTick() {}
    
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
