package eisenwave.radar.cmd;

import eisenwave.radar.EisenRadarPlugin;
import org.bukkit.command.CommandExecutor;
import org.jetbrains.annotations.NotNull;

public abstract class EisenRadarCommand implements CommandExecutor {
    
    protected final EisenRadarPlugin plugin;
    
    public EisenRadarCommand(@NotNull EisenRadarPlugin plugin) {
        this.plugin = plugin;
    }
    
    public abstract String getName();
    
}
