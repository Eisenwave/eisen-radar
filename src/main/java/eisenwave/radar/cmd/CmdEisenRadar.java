package eisenwave.radar.cmd;

import eisenwave.radar.EisenRadarPlugin;
import eisenwave.radar.controller.RadarController;
import eisenwave.radar.io.EisenRadarConfig;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class CmdEisenRadar extends EisenRadarCommand implements TabCompleter {
    
    private final static String
        USAGE = "/eisenradar (info|reload|save) ...",
        USAGE_SAVE = "/eisenradar save [world]";
    
    public CmdEisenRadar(@NotNull EisenRadarPlugin plugin) {
        super(plugin);
    }
    
    @Override
    public String getName() {
        return "eisenradar";
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        command.setUsage(localizer.translate(sender, "format.use", USAGE));
        if (args.length < 1) return false;
        
        switch (args[0]) {
            case "info": {
                localizer.messageFormat(sender, "format.msg", "command.eisenradar.info");
                String keyVal = localizer.translate(sender, "format.key_val");
    
                EisenRadarConfig config = plugin.getEisenRadarConfig();
                RadarController controller = plugin.getRadarController();
                String[] lines = {
                    String.format(keyVal, "Autosave", config.getAutoSave()),
                    String.format(keyVal, "Period", config.getPeriod()),
                    String.format(keyVal, "World-Count", controller.getLoadedWorldCount()),
                    String.format(keyVal, "Player-Count", controller.getLoadedBarCount())
                };
                
                sender.sendMessage(lines);
                return true;
            }
            
            case "reload": {
                plugin.onDisable();
                plugin.onEnable();
                localizer.messageFormat(sender, "format.msg", "command.eisenradar.reload");
                return true;
            }
            
            case "save": {
                if (args.length < 2) {
                    RadarController controller = plugin.getRadarController();
                    Bukkit.getWorlds().forEach(controller::saveRadarMap);
                    localizer.messageFormat(sender, "format.msg", "command.eisenradar.save.all");
                }
                else {
                    World world = Bukkit.getWorld(args[1]);
                    if (world == null) {
                        localizer.messageFormat(sender, "format.err", "error.world", args[1]);
                        return true;
                    }
                    plugin.getRadarController().saveRadarMap(world);
                    localizer.messageFormat(sender, "format.msg", "command.eisenradar.save.one");
                }
                return true;
            }
            
            default: return false;
        }
    }
    
    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String label, String[] args) {
        return args.length == 1? Arrays.asList("reload", "save") : null;
    }
    
}
