package eisenwave.radar.util;

import org.bukkit.ChatColor;

public final class ColorCodeUtil {
    
    private ColorCodeUtil() {}
    
    /**
     * Translates alternative color codes with the option of escaping them.
     *
     * @param c the color char
     * @param str the string to translate
     * @return the translated string
     */
    public static String translate(char c, String str) {
        String pattern = "(?!" + c + ")" + c + "(?=[0-9a-fk-lr])";
        return str.replaceAll(pattern, String.valueOf(ChatColor.COLOR_CHAR));
    }
    
    /**
     * Returns all preceding color codes in the string.
     *
     * @param str the string
     * @return the color codes of the string
     */
    public static String colorCodePrefixOf(String str, char colorChar) {
        StringBuilder builder = new StringBuilder();
        char[] chars = str.toCharArray();
        boolean color = false;
        for (char c : chars) {
            if (c == colorChar) {
                if (!color)
                    builder.append(c);
                color = true;
            }
            else if (color) {
                color = false;
                builder.append(c);
            }
            else {
                break;
            }
        }
        return builder.toString();
    }
    
    public static String colorCodePrefixOf(String str) {
        return colorCodePrefixOf(str, ChatColor.COLOR_CHAR);
    }
    
    /**
     * Returns the first character which is not a color code.
     *
     * @param str the string
     * @return the first character which is not a color code
     */
    public static char firstCharOf(String str) {
        char[] chars = str.toCharArray();
        boolean color = false;
        for (char c : chars) {
            if (c == ChatColor.COLOR_CHAR)
                color = true;
            else if (color)
                color = false;
            else
                return c;
        }
        return ' ';
    }
    
}
