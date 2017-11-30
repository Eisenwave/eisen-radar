package eisenwave.radar.view;

import eisenwave.radar.model.RadarSymbol;
import org.jetbrains.annotations.NotNull;

public class RadarBarStyle {
    
    private final RadarSymbol background;
    private final String prefix;
    private final String suffix;
    
    public RadarBarStyle(@NotNull RadarSymbol background, @NotNull String prefix, @NotNull String suffix) {
        this.background = background;
        this.prefix = prefix;
        this.suffix = suffix;
    }
    
    public RadarSymbol getBackground() {
        return background;
    }
    
    public String getPrefix() {
        return prefix;
    }
    
    public String getSuffix() {
        return suffix;
    }
    
}
