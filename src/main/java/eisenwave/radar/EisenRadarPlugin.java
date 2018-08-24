package eisenwave.radar;

import eisenwave.radar.cmd.CmdEisenRadar;
import eisenwave.radar.cmd.CmdRadar;
import eisenwave.radar.cmd.EisenRadarCommand;
import eisenwave.radar.controller.RadarController;
import eisenwave.radar.io.*;
import eisenwave.radar.lang.Localizer;
import eisenwave.radar.lang.PluginLanguage;
import eisenwave.radar.model.RadarMap;
import eisenwave.radar.io.RadarMapDeserializer;
import eisenwave.radar.data.BukkitVersion;
import eisenwave.radar.controller.DeathPointController;
import eisenwave.radar.model.tracker.TrackerFactory;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Instant;

public class EisenRadarPlugin extends JavaPlugin implements Listener {
    
    public final static Instant TIMESTAMP = Instant.ofEpochMilli(1516459705506L);
    
    private URL wordURL;
    private WordOfEisenwave wordOfEisenwave;
    private EisenRadarConfig config;
    private RadarController controller;
    private DeathPointController deathPointController;
    private TrackerFactory trackerFactory;
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
        if (!initConfig() || !initLanguage()) {
            setEnabled(false);
            return;
        }
    
        initControllers();
        
        if (!enabledOnce) {
            initWord();
            initCommands();
            initEvents();
            this.enabledOnce = true;
        }
    }
    
    private boolean initConfig() {
        if (enabledOnce) reloadConfig();
        else saveDefaultConfig();
        
        try {
            config = new EisenRadarConfig(getConfig());
            return true;
        } catch (Exception ex) {
            getLogger().severe("Failed to load plugin config");
            return false;
        }
    }
    
    private boolean initLanguage() {
        boolean autoLocale;
        try {
            BukkitVersion version = new BukkitVersion(Bukkit.getBukkitVersion());
            autoLocale = version.isNewerEqual(new BukkitVersion("1.12"));
            getLogger().info(autoLocale? "enabled auto-localization" : "disabled auto-localization due to API version");
        } catch (Exception ex) {
            getLogger().warning("disabling auto-localization due to parse error:");
            ex.printStackTrace();
            autoLocale = false;
        }
        
        PluginLanguage primary;
        try {
            primary = new LanguageDeserializer("en_us.lang").fromResource(getClass(), "lang/en_us.lang");
            localizer = new Localizer(primary, autoLocale);
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
    
    /*
    private boolean initDefaultMap(World world) {
        try (InputStream stream = getResource("default_radar.yml")) {
            defaultMap = new RadarMapDeserializer(world).fromStream(stream);
            return true;
        } catch (IOException ex) {
            getLogger().severe("Failed loading radar.yml: " + ex.getMessage());
            return false;
        }
    }
    */
    
    private void initControllers() {
        if (!enabledOnce)
            controller = new RadarController(this);
    
        BukkitScheduler scheduler = this.getServer().getScheduler();
        controllerTask = scheduler.runTaskTimerAsynchronously(this, controller::onAsyncTick, 0, config.getPeriod());
        controller.onEnable();
    
        deathPointController = new DeathPointController(this);
        deathPointController.onEnable();
        
        trackerFactory = new TrackerFactory(this);
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
        PluginManager manager = getServer().getPluginManager();
    
        manager.registerEvents(this, this);
        manager.registerEvents(controller, this);
        manager.registerEvents(deathPointController, this);
    }
    
    // DISABLE
    
    @Override
    public void onDisable() {
        controllerTask.cancel();
        controller.onDisable();
        deathPointController.onDisable();
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
    public RadarMap getNewDefaultMap(World world) throws IOException {
        try (InputStream stream = getResource("default_radar.yml")) {
            return new RadarMapDeserializer(this, world).fromStream(stream);
        }
    }
    
    /**
     * Returns the plugin's localizer.
     *
     * @return the localizer
     */
    @NotNull
    public Localizer getLocalizer() {
        return localizer;
    }
    
    /**
     * Returns the fully parsed config of this plugin
     *
     * @return the config
     */
    @NotNull
    public EisenRadarConfig getEisenRadarConfig() {
        return config;
    }
    
    /**
     * Returns the radar controller.
     *
     * @return the radar controller
     */
    @NotNull
    public RadarController getRadarController() {
        return controller;
    }
    
    /**
     * Returns the death point controller.
     *
     * @return the death point controller
     */
    @NotNull
    public DeathPointController getDeathPointController() {
        return deathPointController;
    }
    
    /**
     * Returns the tracker factory.
     *
     * @return the tracker factory
     */
    @NotNull
    public TrackerFactory getTrackerFactory() {
        return trackerFactory;
    }
    
    /*
    public static void main(String[] args) {
        System.out.println( new WorldRadarPos(1, -1).yawRelTo(0, 0) );
    }
    */
    
}
