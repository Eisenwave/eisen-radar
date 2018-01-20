package eisenwave.radar.model;

import eisenwave.radar.data.RadarSymbol;
import eisenwave.radar.model.pos.RadarPosition;
import org.jetbrains.annotations.NotNull;

public class RadarDot {
    
    private RadarPosition pos;
    private RadarSymbol symbol;
    
    public RadarDot(@NotNull RadarPosition pos, @NotNull RadarSymbol symbol) {
        this.pos = pos;
        this.symbol = symbol;
    }
    
    public RadarPosition getPosition() {
        return pos;
    }
    
    public RadarSymbol getSymbol() {
        return symbol;
    }
    
    public void setPosition(RadarPosition pos) {
        this.pos = pos;
    }
    
    public void setSymbol(RadarSymbol symbol) {
        this.symbol = symbol;
    }
    
}
