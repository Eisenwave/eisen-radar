package eisenwave.radar;

import eisenwave.radar.cmd.CmdEisenRadar;
import eisenwave.radar.cmd.CmdRadar;
import eisenwave.radar.cmd.EisenRadarCommand;
import eisenwave.radar.controller.RadarController;
import eisenwave.radar.io.*;
import eisenwave.radar.lang.Localizer;
import eisenwave.radar.lang.PluginLanguage;
import eisenwave.radar.model.RadarMap;
import eisenwave.radar.model.WordOfEisenwave;
import eisenwave.radar.persist.RadarMapDeserializer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Instant;

public class EisenRadarPlugin extends JavaPlugin implements Listener {
    
    public final static Instant TIMESTAMP = Instant.ofEpochMilli(1513371716091L);
    
    private URL wordURL;
    private WordOfEisenwave wordOfEisenwave;
    private EisenRadarConfig config;
    private RadarMap defaultMap;
    private RadarController controller;
    private Localizer localizer;
    private BukkitTask controllerTask;
    private boolean enabledOnce = false;
    
    @Override
    public void onLoad() {
        try {
            wordURL = new URL("https://pastebin.com/raw/7RU1Wmuy");
        } catch (MalformedURLException ex) {
            throw new AssertionError(ex);
        }
    }
    
    // INIT
    
    @Override
    public void onEnable() {
        if (!initConfig() || !initLanguage() || !initDefaultMap()) {
            setEnabled(false);
            return;
        }
    
        initController();
        
        if (!enabledOnce) {
            initWord();
            initCommands();
            initEvents();
            this.enabledOnce = true;
        }
    }
    
    private boolean initConfig() {
        saveDefaultConfig();
        try {
            config = new EisenRadarConfig(getConfig());
            return true;
        } catch (Exception ex) {
            getLogger().severe("Failed to load plugin config");
            return false;
        }
    }
    
    private boolean initLanguage() {
        PluginLanguage primary;
        try {
            primary = new LanguageDeserializer("en_us.lang").fromResource(getClass(), "lang/en_us.lang");
            localizer = new Localizer(primary);
        } catch (IOException ex) {
            getLogger().severe("Failed to load internal language: " + ex.getMessage());
            return false;
        }
        
        File langFolder = new File(getDataFolder(), "lang");
        if (!langFolder.exists() && !langFolder.mkdirs()) {
            getLogger().severe("Failed to load make lang folder");
            return false;
        }
        
        String[] locales = {"en_us", "de_de"};
        
        for (String locale : locales) {
            String fileName = locale + ".lang";
            File file = new File(langFolder, fileName);
            try {
                saveResource("lang/" + fileName, false);
                PluginLanguage lang = new LanguageDeserializer(locale).fromFile(file);
                lang.setParent(primary);
                localizer.registerLanguage(locale, lang);
            } catch (IOException ex) {
                getLogger().warning("Failed to load language: " + locale);
            }
        }
        
        return true;
    }
    
    private boolean initDefaultMap() {
        try (InputStream stream = getResource("default_radar.yml")) {
            defaultMap = new RadarMapDeserializer().fromStream(stream);
            return true;
        } catch (IOException ex) {
            getLogger().severe("Failed loading radar.yml: " + ex.getMessage());
            return false;
        }
    }
    
    private void initController() {
        if (!enabledOnce)
            controller = new RadarController(this);
    
        BukkitScheduler scheduler = this.getServer().getScheduler();
        controllerTask = scheduler.runTaskTimerAsynchronously(this, controller::onAsyncTick, 0, config.getPeriod());
        controller.onEnable();
    }
    
    private void initWord() {
        getServer().getScheduler().runTaskAsynchronously(this, () -> {
            try {
                wordOfEisenwave = new WordDeserializer(TIMESTAMP, getDescription().getVersion()).fromURL(wordURL);
            } catch (IOException e) {
                wordOfEisenwave = null;
                getLogger().warning("Word Of Eisenwave could not be downloaded: " + e.getMessage());
            }
        });
    }
    
    private void initCommands() {
        EisenRadarCommand[] commands = {
            new CmdRadar(this),
            new CmdEisenRadar(this)
        };
        
        for (EisenRadarCommand cmd : commands)
            getCommand(cmd.getName()).setExecutor(cmd);
    }
    
    private void initEvents() {
        getServer().getPluginManager().registerEvents(this, this);
        getServer().getPluginManager().registerEvents(controller, this);
    }
    
    // DISABLE
    
    @Override
    public void onDisable() {
        controllerTask.cancel();
        controller.onDisable();
    }
    
    // EVENTS
    
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onWordOfEisenwave(PlayerJoinEvent event) {
        if (wordOfEisenwave == null) return;
        Player player = event.getPlayer();
        if (!player.isOp()) return;
        
        if (wordOfEisenwave.getVersionMessage() != null)
            player.sendMessage(wordOfEisenwave.getVersionMessage());
        wordOfEisenwave.getMessages().forEach(player::sendMessage);
    }
    
    // PUBLIC GETTERS
    
    /**
     * Returns a new default {@link RadarMap}.
     *
     * @return the radar map
     */
    public RadarMap getNewDefaultMap() {
        return defaultMap.clone();
    }
    
    /**
     * Returns the plugin's localizer.
     *
     * @return the localizer
     */
    public Localizer getLocalizer() {
        return localizer;
    }
    
    /**
     * Returns the fully parsed config of this plugin
     *
     * @return the config
     */
    public EisenRadarConfig getEisenRadarConfig() {
        return config;
    }
    
    /**
     * Returns the radar controller.
     *
     * @return the radar controller
     */
    public RadarController getRadarController() {
        return controller;
    }
    
    /*
    public static void main(String[] args) {
        System.out.println( new WorldRadarPos(1, -1).yawRelTo(0, 0) );
    }
    */
    
}
