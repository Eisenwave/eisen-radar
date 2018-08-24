package eisenwave.radar.model.tracker;

import eisenwave.radar.EisenRadarPlugin;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class TrackerFactory {
    
    private final EisenRadarPlugin plugin;
    
    public TrackerFactory(@NotNull EisenRadarPlugin plugin) {
        this.plugin = plugin;
    }
    
    @NotNull
    public RadarTracker createTracker(@NotNull World world, @NotNull TrackerType type) {
        switch (type) {
            case PLAYER: return new PlayerTracker(plugin, world);
            case DEATH: return new DeathPointTracker(plugin, world);
            default: return null;
        }
    }
    
}
