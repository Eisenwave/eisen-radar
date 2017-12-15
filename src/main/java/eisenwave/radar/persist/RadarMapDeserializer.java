package eisenwave.radar.persist;

import eisenwave.radar.model.RadarMap;
import eisenwave.radar.model.RadarSymbol;
import eisenwave.radar.model.WayPoint;
import eisenwave.radar.model.pos.FixedRadarPos;
import eisenwave.radar.model.pos.RadarPosition;
import eisenwave.radar.model.pos.WorldRadarPos;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RadarMapDeserializer implements YamlDeserializer<RadarMap> {
    
    @NotNull
    @Override
    public RadarMap fromYaml(YamlConfiguration yaml) {
        RadarMap map = new RadarMap();
    
        applySettings(yaml.getConfigurationSection("settings"), map);
    
        applyDots(yaml.getConfigurationSection("dots"), map);
        
        return map;
    }
    
    private void applySettings(@Nullable ConfigurationSection section, RadarMap map) {
        if (section == null) return;;
        
        double waypointRange = section.getDouble("waypoint_range", 256);
        map.setWayPointRange(waypointRange);
    }
    
    private void applyDots(@Nullable ConfigurationSection section, RadarMap map) {
        if (section == null) return;
        
        for (String key : section.getKeys(false)) {
            ConfigurationSection dotSection = section.getConfigurationSection(key);
            if (dotSection == null) continue;
        
            String symbolStr = dotSection.getString("symbol", "?");
            RadarSymbol symbol = new RadarSymbol(ChatColor.translateAlternateColorCodes('&', symbolStr));
        
            boolean worldPos;
            RadarPosition pos;
            if (dotSection.contains("yaw", true)) {
                pos = new FixedRadarPos((float) dotSection.getDouble("yaw"));
                worldPos = false;
            }
            else if (dotSection.contains("x", true) && dotSection.contains("z", true)) {
                pos = new WorldRadarPos(dotSection.getDouble("x"), dotSection.getDouble("z"));
                worldPos = true;
            }
            else continue;
        
            WayPoint wayPoint = map.add(key, pos, symbol);
        
            if (worldPos) {
                boolean infRange = dotSection.getBoolean("inf_range", false);
                wayPoint.setInfiniteRange(infRange);
            }
        }
    }
    
}
