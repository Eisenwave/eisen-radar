package eisenwave.radar.io;

import eisenwave.commons.util.Enumerations;
import eisenwave.commons.util.PrimMath;
import eisenwave.radar.model.RadarType;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.configuration.Configuration;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EisenRadarConfig {
    
    public final static int DEFAULT_PERIOD = 1;
    
    public final static RadarType DEFAULT_RADAR_TYPE = RadarType.BOSS;
    public final static int DEFAULT_RADAR_SIZE = 25;
    public final static float DEFAULT_RADAR_FOV = 90;
    
    public final static int DEFAULT_BAR_PROGRESS = 100;
    public final static BarStyle DEFAULT_BAR_STYLE = BarStyle.SOLID;
    public final static BarColor DEFAULT_BAR_COLOR = BarColor.WHITE;
    
    private final int period;
    
    private final RadarType radarType;
    private final int radarSize;
    private final float radarFov;
    
    private final float barProgress;
    private final BarStyle barStyle;
    private final BarColor barColor;
    private final List<BarFlag> flags = new ArrayList<>();
    
    public EisenRadarConfig(Configuration config) {
        this.period = PrimMath.clamp(0, config.getInt("period", DEFAULT_PERIOD), Integer.MAX_VALUE);
        
        this.radarType = Enumerations.parse(config.getString("radar.type"), DEFAULT_RADAR_TYPE);
        this.radarSize = PrimMath.clamp(0, config.getInt("radar.size", DEFAULT_RADAR_SIZE), Integer.MAX_VALUE);
        this.radarFov = PrimMath.clamp(0, (float) config.getDouble("radar.fov", DEFAULT_RADAR_FOV), 360);
        
        this.barProgress = PrimMath.clamp(0, config.getInt("bossbar.progress", DEFAULT_BAR_PROGRESS), 100) / 100F;
        this.barStyle = Enumerations.parse(config.getString("bossbar.style"), DEFAULT_BAR_STYLE);
        this.barColor = Enumerations.parse(config.getString("bossbar.color"), DEFAULT_BAR_COLOR);
        for (String flagStr : config.getStringList("bossbar.flags")) {
            BarFlag flag = Enumerations.parse(flagStr, BarFlag.class);
            if (flag != null)
                flags.add(flag);
        }
    }
    
    public int getPeriod() {
        return period;
    }
    
    @NotNull
    public RadarType getRadarType() {
        return radarType;
    }
    
    public int getRadarSize() {
        return radarSize;
    }
    
    public float getRadarFOV() {
        return radarFov;
    }
    
    public float getBarProgress() {
        return barProgress;
    }
    
    @NotNull
    public BarColor getBarColor() {
        return barColor;
    }
    
    @NotNull
    public BarStyle getBarStyle() {
        return barStyle;
    }
    
    @NotNull
    public List<BarFlag> getBarFlags() {
        return Collections.unmodifiableList(flags);
    }
    
}
