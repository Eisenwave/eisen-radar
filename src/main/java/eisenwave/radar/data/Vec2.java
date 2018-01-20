package eisenwave.radar.data;

public class Vec2 {
    
    private final double x, y;
    
    public Vec2(double x, double y) {
        this.x = x;
        this.y = y;
    }
    
    public double getX() {
        return x;
    }
    
    public double getY() {
        return y;
    }
    
    public float getYaw() {
        return (float) Math.toDegrees( -Math.atan2(x, y) );
    }
    
    public double getLengthSquared() {
        return x * x + y * y;
    }
    
    public double getLength() {
        return Math.sqrt(x * x + y * y);
    }
    
    public Vec2 plus(double x, double y) {
        return new Vec2(this.x + x, this.y + y);
    }
    
    public Vec2 minus(double x, double y) {
        return new Vec2(this.x - x, this.y - y);
    }
    
    public Vec2 minus(Vec2 v) {
        return minus(v.getX(), v.getY());
    }
    
}
