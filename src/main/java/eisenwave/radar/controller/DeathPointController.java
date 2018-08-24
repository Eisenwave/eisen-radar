package eisenwave.radar.controller;

import eisenwave.radar.EisenRadarPlugin;
import eisenwave.radar.data.Pair;
import eisenwave.radar.io.DeathPointsDeserializer;
import eisenwave.radar.io.DeathPointsSerializer;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.world.*;
import org.bukkit.util.BlockVector;
import org.jetbrains.annotations.*;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class DeathPointController implements Listener {
    
    private final Map<UUID, Location> deathLocations = new HashMap<>();
    private final EisenRadarPlugin plugin;
    private final File deathPointDir;
    
    public DeathPointController(EisenRadarPlugin plugin) {
        this.plugin = plugin;
        this.deathPointDir = new File(plugin.getDataFolder(), "death_points");
    }
    
    // EVENTS
    
    public void onEnable() {
        for (World world : plugin.getServer().getWorlds())
            loadWorld(world);
    }
    
    public void onDisable() {
        for (World world : plugin.getServer().getWorlds())
            saveWorld(world);
    }
    
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onWorldSave(WorldSaveEvent event) {
        if (plugin.getEisenRadarConfig().getAutoSave()) {
            saveWorld(event.getWorld());
        }
    }
    
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onWorldLoad(WorldLoadEvent event) {
        loadWorld(event.getWorld());
    }
    
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onWorldUnload(WorldUnloadEvent event) {
        if (plugin.getEisenRadarConfig().getAutoSave()) {
            World world = event.getWorld();
            saveWorld(event.getWorld());
            deathLocations.values().removeIf(loc -> loc.getWorld().equals(world));
        }
    }
    
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        put(player.getUniqueId(), player.getLocation());
    }
    
    // STUFF
    
    @Nullable
    public Location getDeathLocation(Player player) {
        return deathLocations.get(player.getUniqueId());
    }
    
    public void clear() {
        deathLocations.clear();
    }
    
    public void put(@NotNull UUID uuid, @NotNull Location loc) {
        deathLocations.put(uuid, loc);
    }
    
    // IO
    
    public void saveWorld(@NotNull World world) {
        if (!deathPointDir.exists() && !deathPointDir.mkdir()) {
            plugin.getLogger().severe("failed to create " + deathPointDir);
            return;
        }
        
        File file = new File(deathPointDir, world.getName() + ".csv");
        try {
            List<Pair<UUID, BlockVector>> entries = deathLocations.entrySet().stream()
                .filter(entry -> entry.getValue().getWorld().equals(world))
                .map(entry -> new Pair<>(entry.getKey(), entry.getValue().toVector().toBlockVector()))
                .collect(Collectors.toList());
            
            new DeathPointsSerializer().toFile(entries, file);
        } catch (IOException ex) {
            plugin.getLogger().severe("Failed to save world death points for \"" + world + '"');
            ex.printStackTrace();
        }
    }
    
    public void loadWorld(World world) {
        File file = new File(deathPointDir, world.getName() + ".csv");
        if (!file.isFile()) return;
        
        try {
            new DeathPointsDeserializer().fromFile(file).forEach(pair -> {
                UUID uuid = pair.getA();
                Location loc = pair.getB().toLocation(world);
                put(uuid, loc);
            });
        } catch (IOException ex) {
            plugin.getLogger().severe("Failed to load world death points for \"" + world + '"');
            ex.printStackTrace();
        }
    }
    
}
