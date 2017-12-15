package eisenwave.radar.view;

import eisenwave.radar.model.WayPoint;
import eisenwave.radar.model.RadarSymbol;
import org.bukkit.boss.BossBar;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class RadarBar {
    
    //private final static char BACKGROUND_CHAR = '-';
    //private final static char BACKGROUND_CODE = '8';
    
    private final char backgroundChar;
    private final String backgroundStyle, prefix, suffix;
    
    private final BossBar bossBar;
    private final RadarSymbol[] buffer;
    private final float fov;
    
    public RadarBar(@NotNull BossBar bossBar, int width, float fov, RadarBarStyle style) {
        if (width < 0) throw new IllegalArgumentException("width must be positive");
        if (fov < 0) throw new IllegalArgumentException("fov must be positive");
        
        this.backgroundChar = style.getBackground().getCharacter();
        this.backgroundStyle = style.getBackground().getColors();
        this.prefix = style.getPrefix();
        this.suffix = style.getSuffix();
        
        this.bossBar = bossBar;
        this.buffer = new RadarSymbol[width];
        this.fov = fov;
    }
    
    // ACTIONS
    
    public void draw(float yaw, WayPoint dot) {
        float rel = ( normalize(yaw) / fov ) + 0.5F;
        if (rel >= 0 && rel <= 1) {
            int index = (int) (rel * (buffer.length - 1));
            buffer[index] = dot.getSymbol();
        }
    }
    
    public void setVisible(boolean visible) {
        bossBar.setVisible(visible);
    }
    
    /**
     * Updates the boss bar backing this radar bar. This will replace the title of the boss bar with the current buffer.
     */
    public void update() {
        bossBar.setTitle(toString());
    }
    
    /**
     * Clears the buffer.
     */
    public void clear() {
        Arrays.fill(buffer, null);
    }
    
    // GETTERS
    
    /**
     * Returns this radar's boss bar.
     *
     * @return the boss bar
     */
    public BossBar getBossBar() {
        return bossBar;
    }
    
    /**
     * Returns this radar's field of view.
     *
     * @return the field of view
     */
    public float getFOV() {
        return fov;
    }
    
    /**
     * Returns whether this radar bar is visible.
     *
     * @return whether this bar is visible
     */
    public boolean isVisible() {
        return bossBar.isVisible();
    }
    
    // UTIL
    
    @Contract(pure = true)
    protected static float normalize(float yaw) {
        yaw %= 360;
        if (yaw < -180) return yaw + 360;
        if (yaw >= 180) return yaw - 360;
        return yaw;
    }
    
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(prefix);
        
        boolean drawBackColor = true;
        for (RadarSymbol symbol : buffer) {
            if (drawBackColor) {
                builder.append(backgroundStyle);
            }
            if (symbol == null) {
                builder.append(backgroundChar);
                drawBackColor = false;
            } else {
                builder.append(symbol);
                drawBackColor = true;
            }
        }
        
        return builder
            .append(suffix)
            .toString();
    }
    
}
