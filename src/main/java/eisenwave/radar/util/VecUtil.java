package eisenwave.radar.util;

import eisenwave.commons.util.PrimMath;
import eisenwave.radar.data.Vec2;

public final class VecUtil {
    
    private VecUtil() {}
    
    // PREDICATES
    
    public float yawOf(double x, double z) {
        return (float) Math.toDegrees( -Math.atan2(x, z) );
    }
    
    public static boolean inRadius(Vec2 a, Vec2 b, double r) {
        return a.minus(b).getLengthSquared() <= r * r;
    }
    
    public static boolean inCube(Vec2 a, Vec2 b, double r) {
        return cubicalDistance(a, b) <= r;
    }
    
    // FUNCTIONS
    
    public static double circularDistance(Vec2 a, Vec2 b) {
        return a.minus(b).getLength();
    }
    
    public static double cubicalDistance(Vec2 a, Vec2 b) {
        return PrimMath.max(a.getX() - b.getX(), a.getY() - b.getY());
    }
    
}
