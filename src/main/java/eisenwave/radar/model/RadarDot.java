package eisenwave.radar.model;

import com.sun.istack.internal.NotNull;
import eisenwave.radar.model.pos.RadarPosition;
import org.bukkit.ChatColor;

public class RadarDot {
    
    private String id;
    private RadarPosition pos;
    private RadarSymbol symbol;
    
    public RadarDot(@NotNull String id, @NotNull RadarPosition pos, @NotNull RadarSymbol symbol) {
        this.symbol = symbol;
        this.pos = pos;
        this.id = id;
    }
    
    public String getId() {
        return id;
    }
    
    public RadarPosition getPosition() {
        return pos;
    }
    
    public RadarSymbol getSymbol() {
        return symbol;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public void setPosition(RadarPosition pos) {
        this.pos = pos;
    }
    
    public void setSymbol(RadarSymbol symbol) {
        this.symbol = symbol;
    }
    
    @Override
    public String toString() {
        return "{symbol: \"" + getSymbol() + ChatColor.RESET + "\", pos: " + getPosition() + "}";
    }
    
}
