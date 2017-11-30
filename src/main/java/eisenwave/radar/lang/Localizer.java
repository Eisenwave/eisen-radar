package eisenwave.radar.lang;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Localizer {
    
    private final Map<String, PluginLanguage> languages = new HashMap<>();
    private final PluginLanguage main;
    
    public Localizer(@NotNull PluginLanguage primaryLanguage) {
        this.main = primaryLanguage;
        this.languages.put(main.getLocale(), main);
    }
    
    // TRANSLATE
    
    public String translate(CommandSender sender, String key) {
        return getLanguage(sender).get(key);
    }
    
    public String translate(CommandSender sender, String key, Object... args) {
        return getLanguage(sender).get(key, args);
    }
    
    public String translate(Player player, String key) {
        return getLanguage(player).get(key);
    }
    
    public String translate(Player player, String key, Object... args) {
        return getLanguage(player).get(key, args);
    }
    
    // TRANSLATE WITH FORMAT
    
    public String translateFormat(CommandSender sender, String fmtKey, String msgKey) {
        PluginLanguage lang = getLanguage(sender);
        return lang.get(fmtKey, lang.get(msgKey));
    }
    
    public String translateFormat(CommandSender sender, String fmtKey, String msgKey, Object... args) {
        PluginLanguage lang = getLanguage(sender);
        return lang.get(fmtKey, lang.get(msgKey, args));
    }
    
    public String translateFormat(Player player, String fmtKey, String msgKey) {
        PluginLanguage lang = getLanguage(player);
        return lang.get(fmtKey, lang.get(msgKey));
    }
    
    public String translateFormat(Player player, String fmtKey, String msgKey, Object... args) {
        PluginLanguage lang = getLanguage(player);
        return lang.get(fmtKey, lang.get(msgKey, args));
    }
    
    // MESSAGE
    
    /**
     * Sends a localized message to a player.
     *
     * @param player the player to whom the messages is sent
     * @param key the translation key
     */
    public void message(CommandSender player, String key) {
        player.sendMessage(translate(player, key));
    }
    
    /**
     * Sends a localized message to a player.
     *
     * @param player the player to whom the messages is sent
     * @param key the translation key
     * @param args the translation arguments
     */
    public void message(CommandSender player, String key, Object... args) {
        player.sendMessage(translate(player, key, args));
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
     * @param player the player to whom the messages is sent
     * @param fmtKey the format key
     * @param msgKey the message key
     */
    public void messageFormat(CommandSender player, String fmtKey, String msgKey) {
        player.sendMessage(translateFormat(player, fmtKey, msgKey));
    }
    
    /**
     * Sends a localized message to a player.
     * <p>
     * This method uses two keys: <ul>
     * <li>A format key which contains a {@literal %s} placeholder for the translated message
     * <li>A message key which is being translated and substituted in the format
     * </ul>
     *
     * @param player the player to whom the messages is sent
     * @param fmtKey the format key
     * @param msgKey the message key
     * @param args the translation arguments
     */
    public void messageFormat(CommandSender player, String fmtKey, String msgKey, Object... args) {
        player.sendMessage(translateFormat(player, fmtKey, msgKey, args));
    }
    
    // OTHER STUFF
    
    /**
     * Returns the primary language of this localizer.
     *
     * @return the primary language
     */
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
    public PluginLanguage getLanguage(@NotNull String locale) {
        return languages.getOrDefault(locale.toLowerCase(), main);
    }
    
    // cache a pair of previous language and UUID
    private PluginLanguage previousLanguage;
    private UUID previousUUID; // use UUID instead of player reference so that the player object can be gc'd
    
    /**
     * Returns the language of a player. If the player's locale is not mapped onto some known {@link PluginLanguage},
     * the main primary language is being used.
     *
     * @param player the player
     * @return the player's language or the primary language if none could be found
     * @see Player#getLocale()
     * @see #getPrimaryLanguage()
     */
    @NotNull
    public PluginLanguage getLanguage(@NotNull Player player) {
        if (player.getUniqueId().equals(previousUUID))
            return previousLanguage;
        
        previousUUID = player.getUniqueId();
        return previousLanguage = getLanguage(player.getLocale());
    }
    
    /**
     * Returns the language of a command sender. If the command sender is a {@link Player}, the language will depend
     * on the {@link Player#getLocale() locale} of a player. Otherwise the
     * {@link #getPrimaryLanguage() primary language} will be used.
     *
     * @param sender the sender
     * @return the player's language or the primary language if none could be found
     * @see Player#getLocale()
     * @see #getPrimaryLanguage()
     * @see #getLanguage(Player)
     */
    @NotNull
    public PluginLanguage getLanguage(@NotNull CommandSender sender) {
        return sender instanceof Player? getLanguage((Player) sender) : getPrimaryLanguage();
    }
    
    /**
     * Returns whether this localizer has a language with the given locale.
     *
     * @param locale the locale
     * @return whether the localizer has the language
     */
    public boolean hasLanguage(@NotNull String locale) {
        return languages.containsKey(locale.toLowerCase());
    }
    
    public void registerLanguage(@NotNull String locale, @NotNull PluginLanguage language) {
        this.languages.put(locale.toLowerCase(), language);
        language.setParent(getPrimaryLanguage());
    }
    
}
