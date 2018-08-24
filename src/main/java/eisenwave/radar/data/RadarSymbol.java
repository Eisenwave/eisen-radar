package eisenwave.radar.data;

import eisenwave.radar.util.ColorCodeUtil;
import eisenwave.radar.view.RadarBar;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;

/**
 * A symbol, visible on a {@link RadarBar}
 */
public class RadarSymbol {
    
    private final String colors;
    private final char character;
    private final String str;
    
    private RadarSymbol(String colors, char c) {
        this.colors = colors;
        this.character = c;
        this.str = colors + c;
    }
    
    public RadarSymbol(@NotNull String value) {
        // replace all characters which are not preceded by a color char
        this(ColorCodeUtil.colorCodePrefixOf(value), ColorCodeUtil.firstCharOf(value));
    }
    
    public RadarSymbol(@NotNull ChatColor color, char c) {
        this(color.toString(), c);
    }
    
    public RadarSymbol(char value) {
        this("", value);
    }
    
    /**
     * Returns all color codes preceding this radar symbol.
     *
     * @return all color codes
     */
    @NotNull
    public String getColors() {
        return colors;
    }
    
    /**
     * Returns the character representing this radar symbol
     *
     * @return the character
     */
    public char getCharacter() {
        return character;
    }
    
    @Override
    public String toString() {
        return str;
    }
    
}
