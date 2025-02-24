package eisenwave.radar.controller;

import eisenwave.radar.EisenRadarPlugin;
import eisenwave.radar.io.EisenRadarConfig;
import eisenwave.radar.lang.PluginLanguage;
import eisenwave.radar.model.RadarMap;
import eisenwave.radar.data.RadarSymbol;
import eisenwave.radar.model.pos.RadarPosition;
import eisenwave.radar.io.RadarMapDeserializer;
import eisenwave.radar.io.RadarMapSerializer;
import eisenwave.radar.view.RadarBar;
import eisenwave.radar.view.RadarBarStyle;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.boss.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;
import org.bukkit.event.world.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

import java.io.File;
import java.io.IOError;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class RadarController implements Listener {
    
    private final File radarsDir;
    
    private final EisenRadarPlugin plugin;
    private final Map<World, RadarMap> worldRadarMap = new HashMap<>();
    private final Map<Player, RadarBar> playerRadarMap = new ConcurrentHashMap<>();
    
    private EisenRadarConfig config;
    private BarStyle barStyle;
    private BarColor barColor;
    private float barProgress;
    private BarFlag[] barFlags;
    
    public RadarController(EisenRadarPlugin plugin) {
        this.plugin = plugin;
        this.radarsDir = new File(plugin.getDataFolder(), "radars");
        if (radarsDir.isFile() || !radarsDir.exists() && !radarsDir.mkdirs()) {
            throw new Error("folder " + radarsDir + " doesn't exist and could not be created or is a file");
        }
    }
    
    // ENABLE & DISABLE
    
    /**
     * Run by the plugin when being enabled.
     */
    public void onEnable() {
        this.config = plugin.getEisenRadarConfig();
        this.barStyle = config.getBarStyle();
        this.barColor = config.getBarColor();
        this.barProgress = config.getBarProgress();
        this.barFlags = config.getBarFlags().toArray(new BarFlag[config.getBarFlags().size()]);
        
        plugin.getServer().getOnlinePlayers().forEach(this::loadRadarBar);
    }
    
    /**
     * Run by the plugin when being disabled.
     */
    public void onDisable() {
        worldRadarMap.keySet().forEach(this::saveRadarMap);
        worldRadarMap.clear();
        playerRadarMap.forEach((player, bar) -> bar.getBossBar().removeAll());
        playerRadarMap.clear();
    }
    
    // ACTIONS
    
    /**
     * Enables or disables radar visibility for a player.
     *
     * @param player the player
     * @param enable whether the radar should be visible
     */
    public void setRadarVisible(@NotNull Player player, boolean enable) {
        //UUID uuid = player.getUniqueId();
        if (enable) {
            if (playerRadarMap.containsKey(player))
                playerRadarMap.get(player).setVisible(true);
            else
                loadRadarBar(player);
        }
        else {
            RadarBar bar = playerRadarMap.remove(player);
            if (bar != null) bar.setVisible(false);
        }
    }
    
    // GETTERS
    
    /**
     * Returns the radar map of the world.
     * <p>
     * If no map is cached internally, this will load the saved radar map from files and cache it.
     * <p>
     * If not map exists yet and no has been saved, a new radar map will be created.
     *
     * @param world the world
     * @return the radar map of the world
     */
    @NotNull
    public RadarMap getRadarMap(World world) {
        return worldRadarMap.containsKey(world)? worldRadarMap.get(world) : loadRadarMap(world);
    }
    
    /**
     * Returns the radar bar of a player or {@code null} if they have none.
     *
     * @param player the player
     * @return the radar bar
     */
    @Nullable
    public RadarBar getRadarBar(Player player) {
        return playerRadarMap.get(player);
    }
    
    /**
     * Returns the amount of loaded worlds.
     *
     * @return the amount of loaded worlds
     */
    public int getLoadedWorldCount() {
        return worldRadarMap.size();
    }
    
    /**
     * Returns the amount of loaded radar bars.
     *
     * @return the amount of loaded radar bars
     */
    public int getLoadedBarCount() {
        return playerRadarMap.size();
    }
    
    // IO
    
    public void saveRadarMap(@NotNull World world) {
        RadarMap map = worldRadarMap.get(world);
        File file = fileOfRadarMap(world.getName());
        if (map != null) try {
            plugin.getLogger().info("Saving radar of world \"" + world.getName() + "\" ...");
            new RadarMapSerializer().toFile(map, file);
        } catch (IOException ex) {
            plugin.getLogger().severe("Failed to save radar map of world \"" + world + "\" to " + file);
        }
    }
    
    public void loadRadarBar(@NotNull Player player) {
        if (playerRadarMap.containsKey(player))
            throw new IllegalStateException("player " + player.getName() + " already has a radar bar");
        
        BossBar bossBar = plugin.getServer().createBossBar("", barColor, barStyle, barFlags);
        bossBar.setProgress(barProgress);
        RadarBar radarBar = new RadarBar(bossBar, config.getRadarSize(), config.getRadarFOV(), getRadarStyle(player));
        bossBar.addPlayer(player);
        playerRadarMap.put(player, radarBar);
        //System.out.println("put "+player.getUniqueId()+", bar");
        
        Location loc = player.getLocation();
        RadarMap map = getRadarMap(loc.getWorld());
        draw(map, player, radarBar, loc.getX(), loc.getZ(), loc.getYaw());
    }
    
    public RadarMap loadRadarMap(@NotNull World world) {
        RadarMap map;
        File file = fileOfRadarMap(world.getName());
        if (!file.exists()) {
            try {
                map = plugin.getNewDefaultMap(world);
            } catch (IOException e) {
                throw new IOError(e);
            }
        }
        else try {
            plugin.getLogger().info("Loading radar of world \"" + world.getName() + "\" ...");
            map = new RadarMapDeserializer(plugin, world).fromFile(file);
        } catch (IOException ex) {
            try {
                map = plugin.getNewDefaultMap(world);
            } catch (IOException e) {
                throw new IOError(e);
            }
            plugin.getLogger().severe("Failed to load radar map for world \"" + world.getName() + "\" from " + file);
            
            File newFile = fileOfRadarMap("BAD_" + Instant.now() + "_" + world.getName());
            try {
                Files.copy(file.toPath(), newFile.toPath());
            } catch (IOException e) {
                plugin.getLogger().severe("Failed to copy to bad file " + newFile);
            }
            plugin.getLogger().severe("To avoid overwriting, the file will be renamed to " + newFile);
        }
        
        worldRadarMap.put(world, map);
        //System.out.println("put "+world+", "+map);
        return map;
    }
    
    // PLUGIN EVENT HANDLERS
    
    /**
     * Run by the plugin every tick asynchronously.
     */
    public void onAsyncTick() {
        Location loc = new Location(null, 0, 0, 0);
        
        //this.worldRadarMap.forEach((world, map) -> map.getTrackers().forEach(RadarTracker::update));
        
        this.playerRadarMap.forEach((player, bar) -> {
            if (bar.isVisible()) {
                player.getLocation(loc);
                RadarMap map = getRadarMap(loc.getWorld());
                draw(map, player, bar, loc.getX(), loc.getZ(), loc.getYaw());
            }
        });
    }
    
    // BUKKIT/SERVER EVENT HANDLERS
    
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onSpawn(PlayerSpawnLocationEvent event) {
        //System.out.println("ON SPAWN");
        Player player = event.getPlayer();
        Bukkit.getScheduler().runTask(plugin, () -> {
            //System.out.println("SCHEDULER RUN");
            if (!playerRadarMap.containsKey(player))
                loadRadarBar(player);
        });
    }
    
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        playerRadarMap.remove(player);
        
        World world = player.getWorld();
        if (world.getPlayers().size() == 1) {
            worldRadarMap.remove(world);
            if (plugin.getEisenRadarConfig().getAutoSave())
                saveRadarMap(world);
        }
    }
    
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onWorldUnload(WorldUnloadEvent event) {
        World world = event.getWorld();
        worldRadarMap.remove(world);
        if (plugin.getEisenRadarConfig().getAutoSave())
            saveRadarMap(world);
    }
    
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onWorldSave(WorldSaveEvent event) {
        if (plugin.getEisenRadarConfig().getAutoSave()) {
            saveRadarMap(event.getWorld());
        }
    }
    
    // UTIL
    
    @NotNull
    private RadarBarStyle getRadarStyle(Player player) {
        PluginLanguage lang = plugin.getLocalizer().getLanguage(player);
        RadarSymbol symbol = new RadarSymbol(lang.get("radar.background"));
        String prefix = lang.get("radar.prefix");
        String suffix = lang.get("radar.suffix");
        
        return new RadarBarStyle(symbol, prefix, suffix);
    }
    
    private void draw(RadarMap map, Player player, RadarBar bar, double x, double z, float yaw) {
        double maxRange = map.getWayPointRange();
        final double maxRangeSquared = maxRange * maxRange;
    
        bar.clear();
        
        map.forEach((id, dot) -> {
            String permission = dot.getPermission();
            if (permission != null && !player.hasPermission(permission)) return;
            
            RadarPosition pos = dot.getPosition();
            boolean draw = dot.hasInfiniteRange();
            if (!draw) {
                double distance = pos.squaredDistanceTo(x, z);
                draw = Double.isInfinite(distance) || distance <= maxRangeSquared;
            }
            if (draw)
                bar.draw(dot.getPosition().yawRelTo(x, z, yaw), dot.getSymbol());
        });
    
        map.getTrackers().forEach(tracker -> tracker.display(player, bar));
        
        bar.update();
    }
    
    @NotNull
    private File fileOfRadarMap(@NotNull String worldName) {
        return new File(radarsDir, worldName + ".yml");
    }
    
}
