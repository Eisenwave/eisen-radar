package eisenwave.radar.model.track;

import org.bukkit.World;
import org.jetbrains.annotations.Nullable;

public final class TrackerFactory {
    
    private TrackerFactory() {}
    
    @Nullable
    public static RadarTracker createTracker(World world, TrackerType type) {
        switch (type) {
            case PLAYER: return new PlayerTracker(world);
            default: return null;
        }
    }
    
}
