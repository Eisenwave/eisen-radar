package eisenwave.radar.model.pos;

import com.google.common.primitives.Floats;

public class FixedRadarPos implements RadarPosition {
    
    private final float yaw;
    
    public FixedRadarPos(float yaw) {
        if (!Floats.isFinite(yaw)) throw new IllegalArgumentException("yaw must be finite");
        this.yaw = yaw;
    }
    
    public float getYaw() {
        return yaw;
    }
    
    @Override
    public float yawRelTo(double x, double y) {
        return yaw;
    }
    
    @Override
    public float yawRelTo(double x, double y, float yaw) {
        return this.yaw - yaw;
    }
    
    @Override
    public String toString() {
        return String.format("%d", (int) yaw);
    }
    
}
