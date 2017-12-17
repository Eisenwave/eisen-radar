package eisenwave.radar.model;

import eisenwave.radar.util.ColorCodeUtil;
import org.bukkit.ChatColor;

import java.util.regex.Pattern;

public class RadarSymbol {
    
    //private static final Pattern NOT_PRECEDED_BY_COLOR_CODE = Pattern.compile("(?<!"+ ChatColor.COLOR_CHAR+").");
    
    private final String colors;
    private final char character;
    private final String str;
    
    private RadarSymbol(String colors, char c) {
        this.colors = colors;
        this.character = c;
        this.str = colors + c;
    }
    
    public RadarSymbol(String value) {
        // replace all characters which are not preceded by a color char
        this(ColorCodeUtil.colorCodePrefixOf(value), ColorCodeUtil.firstCharOf(value));
    }
    
    public RadarSymbol(char value) {
        this("", value);
    }
    
    /**
     * Returns all color codes preceding this radar symbol.
     *
     * @return all color codes
     */
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
    
    // UTIL
    
    
    
}
