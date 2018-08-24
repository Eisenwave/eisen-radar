package eisenwave.radar.model.tracker;

import eisenwave.radar.EisenRadarPlugin;
import eisenwave.radar.controller.DeathPointController;
import eisenwave.radar.data.RadarSymbol;
import eisenwave.radar.model.pos.WorldRadarPos;
import eisenwave.radar.view.RadarBar;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class DeathPointTracker extends RadarTracker {
    
    private final DeathPointController controller;
    
    public DeathPointTracker(@NotNull EisenRadarPlugin plugin, @NotNull World world) {
        super(plugin, world, new RadarSymbol(ChatColor.DARK_RED, 'D'));
        this.controller = plugin.getDeathPointController();
    }
    
    @Override
    public TrackerType getType() {
        return TrackerType.DEATH;
    }
    
    // DISPLAY
    
    @Override
    public void display(Player player, RadarBar bar) {
        Location loc = controller.getDeathLocation(player);
        if (loc != null && player.getWorld().equals(player.getWorld())) {
            WorldRadarPos worldRadarPos = new WorldRadarPos(loc);
            float yaw = worldRadarPos.yawRelTo(player.getLocation());
            bar.draw(yaw, getSymbol());
        }
    }
    
}
