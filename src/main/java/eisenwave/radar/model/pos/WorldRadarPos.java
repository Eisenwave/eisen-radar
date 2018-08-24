package eisenwave.radar.model.pos;

import org.bukkit.Location;

public class WorldRadarPos implements RadarPosition {
    
    private double x, z;
    
    public WorldRadarPos(double x, double z) {
        setX(x);
        setZ(z);
    }
    
    public WorldRadarPos(Location loc) {
        this(loc.getX(), loc.getZ());
    }
    
    @SuppressWarnings("SuspiciousNameCombination")
    @Override
    public float yawRelTo(double x, double z) {
        return (float) Math.toDegrees( -Math.atan2(this.x - x, this.z - z) );
    }
    
    @Override
    public double squaredDistanceTo(double x, double z) {
        double
            dx = x - this.x,
            dz = z - this.z;
        return dx * dx + dz * dz;
    }
    
    public double getX() {
        return x;
    }
    
    public double getZ() {
        return z;
    }
    
    public void setX(double x) {
        this.x = x;
    }
    
    public void setZ(double z) {
        this.z = z;
    }
    
    @Override
    public String toString() {
        return String.format("[%.1f, %.1f]", x, z);
    }
}
