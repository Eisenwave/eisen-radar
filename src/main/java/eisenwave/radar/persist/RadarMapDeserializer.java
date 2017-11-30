package eisenwave.radar.persist;

import eisenwave.radar.model.RadarMap;
import eisenwave.radar.model.RadarSymbol;
import eisenwave.radar.model.pos.FixedRadarPos;
import eisenwave.radar.model.pos.RadarPosition;
import eisenwave.radar.model.pos.WorldRadarPos;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

public class RadarMapDeserializer implements YamlDeserializer<RadarMap> {
    
    @NotNull
    @Override
    public RadarMap fromYaml(YamlConfiguration yaml) {
        RadarMap map = new RadarMap();
        
        ConfigurationSection dotsSection = yaml.getConfigurationSection("dots");
        if (dotsSection == null) return map;
        
        for (String key : dotsSection.getKeys(false)) {
            ConfigurationSection dotSection = dotsSection.getConfigurationSection(key);
            if (dotSection == null) continue;
    
            String symbolStr = dotSection.getString("symbol", "?");
            RadarSymbol symbol = new RadarSymbol(ChatColor.translateAlternateColorCodes('&', symbolStr));
            RadarPosition pos;
            if (dotSection.contains("yaw", true)) {
                pos = new FixedRadarPos((float) dotSection.getDouble("yaw"));
            }
            else if (dotSection.contains("x", true) && dotSection.contains("z", true)) {
                pos = new WorldRadarPos(dotSection.getDouble("x"), dotSection.getDouble("z"));
            }
            else continue;
            
            map.add(key, pos, symbol);
        }
        
        return map;
    }
    
}
