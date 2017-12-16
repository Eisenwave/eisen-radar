package eisenwave.radar.cmd;

import eisenwave.radar.EisenRadarPlugin;
import eisenwave.radar.lang.Localizer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class EisenRadarCommand implements CommandExecutor {
    
    protected final EisenRadarPlugin plugin;
    protected final Localizer localizer;
    
    public EisenRadarCommand(@NotNull EisenRadarPlugin plugin) {
        this.plugin = plugin;
        this.localizer = plugin.getLocalizer();
    }
    
    // ABSTRACT
    
    public abstract String getName();
    
    @Override
    public abstract boolean onCommand(CommandSender sender, Command command, String label, String[] args);
    
    // "UTILITY"
    
    protected boolean validatePermission(CommandSender sender, String permission) {
        if (sender.hasPermission(permission)) return true;
        else {
            localizer.messageFormat(sender, "format.err", "error.permission", permission);
            return false;
        }
    }
    
    @Nullable
    protected Player validatePlayerSender(CommandSender sender) {
        if (!(sender instanceof Player)) {
            localizer.messageFormat(sender, "format.err", "error.not_a_player");
            return null;
        }
        else return (Player) sender;
    }
    
    @Nullable
    protected Player validatePlayerWithPermission(CommandSender sender, String permission) {
        if (!sender.hasPermission(permission)) {
            localizer.messageFormat(sender, "format.err", "error.permission", permission);
            return null;
        }
        if (!(sender instanceof Player)) {
            localizer.messageFormat(sender, "format.err", "error.not_a_player");
            return null;
        }
        return (Player) sender;
    }
    
}
