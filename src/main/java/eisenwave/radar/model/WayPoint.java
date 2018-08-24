package eisenwave.radar.model;

import com.sun.istack.internal.NotNull;
import eisenwave.radar.data.RadarSymbol;
import eisenwave.radar.model.pos.RadarPosition;
import eisenwave.radar.model.pos.WorldRadarPos;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.Nullable;

public class WayPoint extends RadarDot {
    
    private String id;
    private boolean infRange = false;
    
    private String permission = null;
    private boolean trans = false;
    
    public WayPoint(@NotNull String id, @NotNull RadarPosition pos, @NotNull RadarSymbol symbol) {
        super(pos, symbol);
        this.id = id;
    }
    
    // GETTERS
    
    /**
     * Returns the id of this waypoint.
     *
     * @return the id
     */
    public String getId() {
        return id;
    }
    
    /**
     * Returns whether this waypoint is always visible, even if it's outside the regular maximum view range in the
     * world.
     *
     * @return whether this waypoint is infinitely visible
     */
    public boolean hasInfiniteRange() {
        return infRange;
    }
    
    public String getPermission() {
        return permission;
    }
    
    public boolean isTransient() {
        return trans;
    }
    
    // SETTERS
    
    public void setInfiniteRange(boolean infRange) {
        this.infRange = infRange;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public void setPermission(@Nullable String permission) {
        this.permission = permission;
    }
    
    public void setTransient(boolean trans) {
        this.trans = trans;
    }
    
    // MISC
    
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("{symbol: \"")
            .append(getSymbol())
            .append(ChatColor.RESET)
            .append("\", pos: ")
            .append(getPosition());
    
        if (getPermission() != null)
            builder
                .append(", permission: ")
                .append(getPermission());
        if (getPosition() instanceof WorldRadarPos && infRange)
            builder.append(", infRange");
        if (isTransient())
            builder.append(", transient");
        
        return builder.append("}").toString();
    }
    
}
