package eisenwave.radar.io;

import eisenwave.radar.model.WayPoint;
import eisenwave.radar.model.RadarMap;
import eisenwave.radar.model.pos.FixedRadarPos;
import eisenwave.radar.model.pos.RadarPosition;
import eisenwave.radar.model.pos.WorldRadarPos;
import eisenwave.radar.model.tracker.RadarTracker;
import eisenwave.radar.model.tracker.TrackerType;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class RadarMapSerializer implements YamlSerializer<RadarMap> {
    
    private final static String COLOR_CHAR = String.valueOf(ChatColor.COLOR_CHAR);
    
    private RadarMap map;
    
    @SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
    @Override
    public void toYaml(@NotNull RadarMap map, YamlConfiguration yaml) {
        this.map = map;
        
        writeSettings(yaml.createSection("settings"));
        writeTrackers(yaml.createSection("trackers"));
        writeDots(yaml.createSection("dots"));
    }
    
    private void writeSettings(ConfigurationSection section) {
        section.set("waypoint_range", map.getWayPointRange());
    }
    
    private void writeTrackers(ConfigurationSection section) {
        for (TrackerType type : TrackerType.values()) {
            RadarTracker tracker = map.getTracker(type);
            ConfigurationSection trackerSection = section.createSection(type.toString().toLowerCase());
            trackerSection.set("enabled", tracker != null);
        }
    }
    
    private void writeDots(ConfigurationSection section) {
        for (Map.Entry<String, WayPoint> entry : map.entrySet()) {
            WayPoint dot = entry.getValue();
            RadarPosition pos = dot.getPosition();
            if (dot.isTransient()) continue;
            
            ConfigurationSection dotSection = section.createSection(entry.getKey());
            dotSection.set("symbol", dot.getSymbol().toString().replace(COLOR_CHAR, "&"));
        
            String permission = dot.getPermission();
            if (permission != null)
                dotSection.set("permission", permission);
            
            if (pos instanceof FixedRadarPos) {
                dotSection.set("yaw", ((FixedRadarPos) pos).getYaw());
            } else if (pos instanceof WorldRadarPos) {
                WorldRadarPos worldPos = (WorldRadarPos) pos;
                dotSection.set("x", worldPos.getX());
                dotSection.set("z", worldPos.getZ());
                dotSection.set("inf_range", dot.hasInfiniteRange());
            }
        }
    }
    
}
