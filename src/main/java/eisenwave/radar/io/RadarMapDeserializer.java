package eisenwave.radar.io;

import eisenwave.radar.EisenRadarPlugin;
import eisenwave.radar.model.RadarMap;
import eisenwave.radar.data.RadarSymbol;
import eisenwave.radar.model.WayPoint;
import eisenwave.radar.model.pos.FixedRadarPos;
import eisenwave.radar.model.pos.RadarPosition;
import eisenwave.radar.model.pos.WorldRadarPos;
import eisenwave.radar.model.tracker.RadarTracker;
import eisenwave.radar.model.tracker.TrackerFactory;
import eisenwave.radar.model.tracker.TrackerType;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RadarMapDeserializer implements YamlDeserializer<RadarMap> {
    
    private final EisenRadarPlugin plugin;
    private final World world;
    private RadarMap map;
    
    public RadarMapDeserializer(EisenRadarPlugin plugin, World world) {
        this.plugin = plugin;
        this.world = world;
    }
    
    @NotNull
    @Override
    public RadarMap fromYaml(YamlConfiguration yaml) {
        map = new RadarMap();
    
        applySettings(yaml.getConfigurationSection("settings"));
        applyTrackers(yaml.getConfigurationSection("trackers"));
        applyDots(yaml.getConfigurationSection("dots"));
    
        //noinspection ReturnOfCollectionOrArrayField
        return map;
    }
    
    private void applySettings(@Nullable ConfigurationSection section) {
        if (section == null) return;
        
        double waypointRange = section.getDouble("waypoint_range", 256);
        map.setWayPointRange(waypointRange);
    }
    
    private void applyTrackers(@Nullable ConfigurationSection section) {
        if (section == null) return;
        TrackerFactory factory = plugin.getTrackerFactory();
        
        for (TrackerType type : TrackerType.values()) {
            ConfigurationSection trackerSection = section.getConfigurationSection(type.name().toLowerCase());
            if (trackerSection == null) continue;
            if (!trackerSection.getBoolean("enabled")) continue;
    
            RadarTracker tracker = factory.createTracker(world, type);
            map.addTracker(tracker);
        }
    }
    
    private void applyDots(@Nullable ConfigurationSection section) {
        if (section == null) return;
        
        for (String key : section.getKeys(false)) {
            ConfigurationSection dotSection = section.getConfigurationSection(key);
            if (dotSection == null) continue;
        
            String symbolStr = dotSection.getString("symbol", "?");
            RadarSymbol symbol = new RadarSymbol(ChatColor.translateAlternateColorCodes('&', symbolStr));
        
            boolean worldPos;
            RadarPosition pos;
            if (dotSection.contains("yaw")) {
                pos = new FixedRadarPos((float) dotSection.getDouble("yaw"));
                worldPos = false;
            }
            else if (dotSection.contains("x") && dotSection.contains("z")) {
                pos = new WorldRadarPos(dotSection.getDouble("x"), dotSection.getDouble("z"));
                worldPos = true;
            }
            else continue;
        
            WayPoint wayPoint = map.addWayPoint(key, pos, symbol);
        
            if (worldPos) {
                boolean infRange = dotSection.getBoolean("inf_range", false);
                wayPoint.setInfiniteRange(infRange);
            }
    
            String permission = dotSection.getString("permission", null);
            wayPoint.setPermission(permission);
        }
    }
    
}
