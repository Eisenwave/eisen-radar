package eisenwave.radar.lang;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * A class which stores plugin languages by their locale and assists in translating messages for players based on
 * locale.
 */
public class Localizer {
    
    private final Map<String, PluginLanguage> languages = new HashMap<>();
    private final PluginLanguage main;
    private final boolean autoLocale;
    
    public Localizer(@NotNull PluginLanguage primaryLanguage, boolean autoLocale) {
        this.main = primaryLanguage;
        this.languages.put(main.getLocale(), main);
        this.autoLocale = autoLocale;
    }
    
    public boolean isAutoLocale() {
        return autoLocale;
    }
    
    // TRANSLATE
    
    /**
     * Translates a message for a sender.
     *
     * @param sender the sender
     * @param key the translation key
     * @return the translation
     */
    public String translate(CommandSender sender, String key) {
        return getLanguage(sender).get(key);
    }
    
    /**
     * Translates a message for a sender with given arguments.
     *
     * @param sender the sender
     * @param key the translation key
     * @param args the translation arguments
     * @return the translation
     */
    public String translate(CommandSender sender, String key, Object... args) {
        return getLanguage(sender).get(key, args);
    }
    
    // TRANSLATE WITH FORMAT
    
    /**
     * Translates a message for a sender with a format.
     * <p>
     * This works by translating the message first and using the translation as an argument for the format. The format
     * must be a format string with exactly one {@code %s} argument.
     *
     * @param sender the sender
     * @param fmtKey the format translation key
     * @param msgKey the message translation key
     * @return the translation
     */
    public String translateFormat(CommandSender sender, String fmtKey, String msgKey) {
        PluginLanguage lang = getLanguage(sender);
        return lang.get(fmtKey, lang.get(msgKey));
    }
    
    /**
     * Translates a message for a sender with a format and given arguments.
     * <p>
     * This works by translating the message first and using the translation as an argument for the format. The format
     * must be a format string with exactly one {@code %s} argument.
     *
     * @param sender the sender
     * @param fmtKey the format translation key
     * @param msgKey the message translation key
     * @param args the message translation arguments
     * @return the translation
     */
    public String translateFormat(CommandSender sender, String fmtKey, String msgKey, Object... args) {
        PluginLanguage lang = getLanguage(sender);
        return lang.get(fmtKey, lang.get(msgKey, args));
    }
    
    /**
     * Translates a message for a sender with a format.
     * <p>
     * This works by translating the message first and using the translation as an argument for the format. The format
     * must be a format string with exactly one {@code %s} argument.
     *
     * @param player the player
     * @param fmtKey the format translation key
     * @param msgKey the message translation key
     * @return the translation
     */
    public String translateFormat(Player player, String fmtKey, String msgKey) {
        PluginLanguage lang = getLanguage(player);
        return lang.get(fmtKey, lang.get(msgKey));
    }
    
    /**
     * Translates a message for a sender with a format and given arguments.
     * <p>
     * This works by translating the message first and using the translation as an argument for the format. The format
     * must be a format string with exactly one {@code %s} argument.
     *
     * @param player the player
     * @param fmtKey the format translation key
     * @param msgKey the message translation key
     * @param args the message translation arguments
     * @return the translation
     */
    public String translateFormat(Player player, String fmtKey, String msgKey, Object... args) {
        PluginLanguage lang = getLanguage(player);
        return lang.get(fmtKey, lang.get(msgKey, args));
    }
    
    // MESSAGE
    
    /**
     * Sends a localized message to a sender.
     *
     * @param sender the player to whom the messages is sent
     * @param key the translation key
     */
    public void message(CommandSender sender, String key) {
        sender.sendMessage(translate(sender, key));
    }
    
    /**
     * Sends a localized message to a sender.
     *
     * @param sender the player to whom the messages is sent
     * @param key the translation key
     * @param args the translation arguments
     */
    public void message(CommandSender sender, String key, Object... args) {
        sender.sendMessage(translate(sender, key, args));
    }
    
    // MESSAGE WITH FORMAT
    
    /**
     * Sends a localized message to a player.
     * <p>
     * This method uses two keys: <ul>
     * <li>A format key which contains a {@literal %s} placeholder for the translated message
     * <li>A message key which is being translated and substituted in the format
     * </ul>
     *
     * @param sender the player to whom the messages is sent
     * @param fmtKey the format key
     * @param msgKey the message key
     */
    public void messageFormat(CommandSender sender, String fmtKey, String msgKey) {
        sender.sendMessage(translateFormat(sender, fmtKey, msgKey));
    }
    
    /**
     * Sends a localized message to a player.
     * <p>
     * This method uses two keys: <ul>
     * <li>A format key which contains a {@literal %s} placeholder for the translated message
     * <li>A message key which is being translated and substituted in the format
     * </ul>
     *
     * @param sender the player to whom the messages is sent
     * @param fmtKey the format key
     * @param msgKey the message key
     * @param args the translation arguments
     */
    public void messageFormat(CommandSender sender, String fmtKey, String msgKey, Object... args) {
        sender.sendMessage(translateFormat(sender, fmtKey, msgKey, args));
    }
    
    // OTHER STUFF
    
    /**
     * Returns the primary language of this localizer.
     *
     * @return the primary language
     */
    @NotNull
    public PluginLanguage getPrimaryLanguage() {
        return main;
    }
    
    /**
     * Returns the language for the given locale or the primary language if none could be found.
     *
     * @param locale the locale
     * @return the player's language or the primary language if none could be found
     */
    @NotNull
    public PluginLanguage getLanguage(@Nullable String locale) {
        PluginLanguage primary = getPrimaryLanguage();
        return locale == null? primary : languages.getOrDefault(locale.toLowerCase(), primary);
    }
    
    /**
     * Returns the language of a command sender. If the command sender is a {@link Player}, the language will depend
     * on the {@link Player#getLocale() locale} of a player.
     * <p>
     * Otherwise the {@link #getPrimaryLanguage() primary language} will be used.
     *
     * @param sender the sender
     * @return the player's language or the primary language if none could be found
     * @see Player#getLocale()
     * @see #getPrimaryLanguage()
     */
    @NotNull
    public PluginLanguage getLanguage(@NotNull CommandSender sender) {
        return autoLocale && sender instanceof Player?
            getLanguage(((Player) sender).getLocale()) :
            getPrimaryLanguage();
    }
    
    /**
     * Returns whether this localizer has a language with the given locale.
     *
     * @param locale the locale
     * @return whether the localizer has the language
     */
    public boolean hasLanguage(@Nullable String locale) {
        return locale == null || languages.containsKey(locale.toLowerCase());
    }
    
    /**
     * Registers a language with a given locale.
     *
     * @param locale the locale
     * @param language the language
     */
    public void registerLanguage(@NotNull String locale, @NotNull PluginLanguage language) {
        this.languages.put(locale.toLowerCase(), language);
        language.setParent(getPrimaryLanguage());
    }
    
}
