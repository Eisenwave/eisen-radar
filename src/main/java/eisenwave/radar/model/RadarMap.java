package eisenwave.radar.model;

import eisenwave.radar.data.RadarSymbol;
import eisenwave.radar.model.pos.RadarPosition;
import eisenwave.radar.model.track.RadarTracker;
import eisenwave.radar.model.track.TrackerType;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class RadarMap extends HashMap<String, WayPoint> {
    
    private double maxDotRange = 256;
    
    private final Map<TrackerType, RadarTracker> trackers = new EnumMap<>(TrackerType.class);
    
    public RadarMap() {
        super();
    }
    
    public RadarMap(@NotNull Map<String, ? extends WayPoint> c) {
        super(c);
    }
    
    // GETTERS
    
    public double getWayPointRange() {
        return maxDotRange;
    }
    
    public RadarTracker getTracker(@NotNull TrackerType type) {
        return trackers.get(type);
    }
    
    public Collection<RadarTracker> getTrackers() {
        return trackers.values();
    }
    
    // MUTATORS
    
    public void setWayPointRange(double maxDotRange) {
        this.maxDotRange = maxDotRange;
    }
    
    public void addTracker(RadarTracker tracker) {
        trackers.put(tracker.getType(), tracker);
    }
    
    public void removeTracker(TrackerType type) {
        trackers.remove(type);
    }
    
    public WayPoint addWayPoint(String id, RadarPosition position, RadarSymbol symbol) {
        WayPoint wayPoint = new WayPoint(id, position, symbol);
        this.put(id, wayPoint);
        return wayPoint;
    }
    
    @Override
    public void clear() {
        trackers.clear();
        super.clear();
    }
    
    // MISC
    
    @Override
    public final RadarMap clone() {
        return new RadarMap(this);
    }
    
}
