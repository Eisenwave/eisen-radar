package eisenwave.radar.model;

import eisenwave.radar.data.RadarSymbol;
import eisenwave.radar.model.pos.RadarPosition;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RadarDot {
    
    private RadarPosition pos;
    private RadarSymbol symbol;
    
    public RadarDot(@NotNull RadarPosition pos, @NotNull RadarSymbol symbol) {
        this.pos = pos;
        this.symbol = symbol;
    }
    
    // GETTERS
    
    public RadarPosition getPosition() {
        return pos;
    }
    
    public RadarSymbol getSymbol() {
        return symbol;
    }
    
    // SETTERS
    
    public void setPosition(@NotNull RadarPosition pos) {
        this.pos = pos;
    }
    
    public void setSymbol(@NotNull RadarSymbol symbol) {
        this.symbol = symbol;
    }
    
}
