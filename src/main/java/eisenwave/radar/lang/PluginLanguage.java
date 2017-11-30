package eisenwave.radar.lang;

import org.bukkit.ChatColor;
import org.jetbrains.annotations.*;

import java.util.HashMap;
import java.util.Map;

public final class PluginLanguage {
    
    private final static char ALT_COLOR_CHAR = '&';
    
    private PluginLanguage parent;
    private final String locale;
    private final Map<String, String> vocab = new HashMap<>();
    
    public PluginLanguage(@Nullable PluginLanguage parent, @NotNull String locale, @Nullable Map<String, String> vocab) {
        this.parent = parent;
        this.locale = locale;
        if (vocab != null)
            defineAll(vocab);
    }
    
    public PluginLanguage(@Nullable PluginLanguage parent, @NotNull String locale) {
        this(parent, locale, null);
    }
    
    public PluginLanguage(@NotNull String name, @Nullable Map<String, String> vocab) {
        this(null, name, vocab);
    }
    
    public PluginLanguage(@NotNull String locale) {
        this(null, locale);
    }
    
    /**
     * Returns the parent language or {@code null} if it has none.
     *
     * @return the parent language or {@code null} if it has none
     */
    @Contract(pure = true)
    public PluginLanguage getParent() {
        return parent;
    }
    
    /**
     * Returns the locale of this language.
     *
     * @return the locale
     */
    @Contract(pure = true)
    @NotNull
    public String getLocale() {
        return locale;
    }
    
    /**
     * <p>
     * Returns the translation of a key or message indicating that the key is missing.
     * <p>
     * If no translation is available, this method returns its parent's translation. If the parent has no
     * translation, an error message is returned.
     *
     * @param key the key
     * @return the translation of the key
     * @see #define(String, String)
     */
    @NotNull
    public String get(@NotNull String key) {
        if (vocab.containsKey(key))
            return vocab.get(key);
        else if (parent != null)
            return parent.vocab.getOrDefault(key, "MISSING(" + key + ")");
        else
            return "MISSING(" + key + ")";
    }
    
    /**
     * Convenience method for formatting the translation of a key.
     *
     * @param key the key
     * @param args the arguments
     * @return the translated and formatted string
     * @see String#format(String, Object...)
     */
    public String get(@NotNull String key, Object... args) {
        return String.format(get(key), args);
    }
    
    /**
     * Returns the size of the vocabulary of this language.
     *
     * @return the vocabulary size
     */
    public int size() {
        return vocab.size();
    }
    
    // MUTATORS
    
    /**
     * Sets the parent of this language.
     *
     * @param parent the parent language
     */
    public void setParent(PluginLanguage parent) {
        this.parent = parent;
    }
    
    /**
     * Defines a translation. Also parser color codes.
     *
     * @param key the translation key
     * @param translation the translation
     * @see #get(String)
     */
    public void define(@NotNull String key, @NotNull String translation) {
        vocab.put(key, ChatColor.translateAlternateColorCodes(ALT_COLOR_CHAR, translation));
    }
    
    /**
     * Defines all translations in a vocabulary.
     *
     * @param vocab the vocabulary
     */
    public void defineAll(@NotNull Map<String, String> vocab) {
        vocab.forEach(this::define);
    }
    
    // MISC
    
    @Override
    public String toString() {
        return PluginLanguage.class.getSimpleName() + "{locale=" + locale + ", entries=" + size() + "}";
    }
    
    // UTIL
    
    
}
