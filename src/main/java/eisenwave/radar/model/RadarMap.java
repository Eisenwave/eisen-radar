package eisenwave.radar.model;

import eisenwave.radar.model.pos.RadarPosition;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class RadarMap extends HashMap<String, WayPoint> {
    
    private double maxDotRange = 256;
    
    public RadarMap() {
        super();
    }
    
    public RadarMap(@NotNull Map<String, ? extends WayPoint> c) {
        super(c);
    }
    
    public double getWayPointRange() {
        return maxDotRange;
    }
    
    public void setWayPointRange(double maxDotRange) {
        this.maxDotRange = maxDotRange;
    }
    
    public WayPoint add(String id, RadarPosition position, RadarSymbol symbol) {
        WayPoint wayPoint = new WayPoint(id, position, symbol);
        this.put(id, wayPoint);
        return wayPoint;
    }
    
    // MISC
    
    @Override
    public final RadarMap clone() {
        return new RadarMap(this);
    }
    
}
