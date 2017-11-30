package eisenwave.radar.model;

import eisenwave.radar.model.pos.RadarPosition;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class RadarMap extends HashMap<String, RadarDot> {
    
    public RadarMap() {
        super();
    }
    
    public RadarMap(@NotNull Map<String, ? extends RadarDot> c) {
        super(c);
    }
    
    public void add(String id, RadarPosition position, RadarSymbol symbol) {
        this.put(id, new RadarDot(id, position, symbol));
    }
    
    // MISC
    
    @Override
    public final RadarMap clone() {
        return new RadarMap(this);
    }
    
}
