package eisenwave.radar.persist;

import eisenwave.radar.model.WayPoint;
import eisenwave.radar.model.RadarMap;
import eisenwave.radar.model.pos.FixedRadarPos;
import eisenwave.radar.model.pos.RadarPosition;
import eisenwave.radar.model.pos.WorldRadarPos;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.Map;

public class RadarMapSerializer implements YamlSerializer<RadarMap> {
    
    private final static String COLOR_CHAR = String.valueOf(ChatColor.COLOR_CHAR);
    
    @Override
    public void toYaml(RadarMap map, YamlConfiguration yaml) {
        writeSettings(yaml.createSection("settings"), map);
        writeDots(yaml.createSection("dots"), map);
    }
    
    private void writeSettings(ConfigurationSection section, RadarMap map) {
        section.set("waypoint_range", map.getWayPointRange());
    }
    
    private void writeDots(ConfigurationSection section, RadarMap map) {
        for (Map.Entry<String, WayPoint> entry : map.entrySet()) {
            WayPoint dot = entry.getValue();
            RadarPosition pos = dot.getPosition();
        
            ConfigurationSection dotSection = section.createSection(entry.getKey());
            dotSection.set("symbol", dot.getSymbol().toString().replace(COLOR_CHAR, "&"));
        
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
