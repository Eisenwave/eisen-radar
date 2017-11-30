package eisenwave.radar.cmd;

import eisenwave.radar.EisenRadarPlugin;
import eisenwave.radar.lang.Localizer;
import eisenwave.radar.model.RadarDot;
import eisenwave.radar.model.RadarMap;
import eisenwave.radar.model.RadarSymbol;
import eisenwave.radar.model.pos.FixedRadarPos;
import eisenwave.radar.model.pos.RadarPosition;
import eisenwave.radar.model.pos.WorldRadarPos;
import eisenwave.radar.view.RadarBar;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CmdRadar extends EisenRadarCommand implements SimpleTabCompleter {
    
    private final static String
        USAGE = "/radar (add|edit|list|on|off|toggle)",
        USAGE_ADD = "/radar add <id> <symbol> (<yaw>|<x> <z>)",
        USAGE_REMOVE = "/radar remove <id>",
        USAGE_EDIT = "/radar edit <id> pos (<yaw>|<x> <z>) OR /radar edit <id> symbol <symbol>";
    
    private final Localizer localizer;
    
    public CmdRadar(@NotNull EisenRadarPlugin plugin) {
        super(plugin);
        this.localizer = plugin.getLocalizer();
    }
    
    @Override
    public String getName() {
        return "radar";
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        command.setUsage(localizer.translate(sender, "format.use", USAGE));
        
        if (args.length == 0) {
            return false;
        }
        
        switch (args[0]) {
            case "add": {
                Player player = validatePlayerWithPermission(sender, "eisenradar.set.add");
                if (player == null) return true;
                
                if (args.length < 4) {
                    command.setUsage(localizer.translate(player, "format.use", USAGE_ADD));
                    return false;
                }
                
                String id = args[1];
                RadarMap map = plugin.getRadarController().getRadarMap(player.getWorld());
                if (map.containsKey(id)) {
                    localizer.messageFormat(player, "format.err", "command.radar.add.bad_id", id);
                    return true;
                }
                
                RadarSymbol symbol = new RadarSymbol(ChatColor.translateAlternateColorCodes('&', args[2]));
                RadarPosition pos = parsePosition(player, args, 3);
                if (pos == null) return true;
                
                map.add(id, pos, symbol);
                localizer.messageFormat(player, "format.msg", "command.radar.add.success", id);
                return true;
            }
            
            case "edit": {
                Player player = validatePlayerWithPermission(sender, "eisenradar.set.edit");
                if (player == null) return true;
                
                command.setUsage(localizer.translate(player, "format.use", USAGE_EDIT));
                if (args.length < 4) return false;
                
                String id = args[1];
                RadarMap map = plugin.getRadarController().getRadarMap(player.getWorld());
                RadarDot dot = map.get(id);
                if (dot == null) {
                    localizer.messageFormat(player, "format.err", "command.radar.edit.bad_id", args[1]);
                    return true;
                }
                
                if (args[2].equals("pos")) {
                    RadarPosition pos = parsePosition(player, args, 3);
                    if (pos == null) return true;
                    
                    dot.setPosition(pos);
                    localizer.messageFormat(player, "format.msg", "command.radar.edit.pos", id, pos);
                    return true;
                }
                else if (args[2].equals("symbol")) {
                    String symbolStr = ChatColor.translateAlternateColorCodes('&', args[3]);
                    RadarSymbol symbol = new RadarSymbol(symbolStr);
                    dot.setSymbol(symbol);
                    localizer.messageFormat(player, "format.msg", "command.radar.edit.symbol",
                        id, symbolStr + ChatColor.RESET);
                    return true;
                }
                return false;
            }
            
            case "remove": {
                Player player = validatePlayerWithPermission(sender, "eisenradar.set.remove");
                if (player == null) return true;
                
                if (args.length < 2) {
                    command.setUsage(localizer.translate(player, "format.use", USAGE_REMOVE));
                    return false;
                }
                
                RadarMap map = plugin.getRadarController().getRadarMap(player.getWorld());
                RadarDot dot = map.remove(args[1]);
                if (dot == null) {
                    localizer.messageFormat(player, "format.err", "command.radar.remove.bad_id", args[1]);
                }
                else {
                    localizer.messageFormat(player, "format.msg", "command.radar.remove.success", args[1]);
                }
                return true;
            }
            
            case "list": {
                Player player = validatePlayerWithPermission(sender, "eisenradar.get.list");
                if (player == null) return true;
                
                RadarMap map = plugin.getRadarController().getRadarMap(player.getWorld());
                String str = map.entrySet().stream()
                    .sorted(Comparator.comparing(Map.Entry::getKey))
                    .map(entry -> "- " + ChatColor.BLUE + entry.getKey() + ": " + ChatColor.RESET + entry.getValue())
                    .collect(Collectors.joining("\n"));
                
                localizer.messageFormat(player, "format.msg", "command.radar.list");
                player.sendMessage(str);
                return true;
            }
            
            case "on": {
                Player player = validatePlayerWithPermission(sender, "eisenradar.view.on");
                if (player == null) return true;
                
                plugin.getRadarController().setRadarVisible(player, true);
                localizer.messageFormat(player, "format.msg", "command.radar.on");
                return true;
            }
            
            case "off": {
                Player player = validatePlayerWithPermission(sender, "eisenradar.view.off");
                if (player == null) return true;
                
                plugin.getRadarController().setRadarVisible(player, false);
                localizer.messageFormat(player, "format.msg", "command.radar.off");
                return true;
            }
            
            case "toggle": {
                Player player = validatePlayerWithPermission(sender, "eisenradar.view.toggle");
                if (player == null) return true;
                
                boolean enable;
                {
                    RadarBar bar = plugin.getRadarController().getRadarBar(player);
                    enable = bar == null || !bar.isVisible();
                }
                localizer.messageFormat(player, "format.msg", enable? "command.radar.on" : "command.radar.off");
                plugin.getRadarController().setRadarVisible(player, enable);
                return true;
            }
            
            default: return false;
        }
    }
    
    // TAB COMPLETE
    
    @Override
    public List<String> getTabCompleteOptions() {
        return Arrays.asList("add", "edit", "list", "off", "on", "remove", "toggle");
    }
    
    // STRING
    
    @Nullable
    private RadarPosition parsePosition(Player player, String[] args, int offset) {
        if (args.length >= 5) {
            double x;
            try {
                x = Double.parseDouble(args[3]);
            } catch (NumberFormatException ex) {
                localizer.messageFormat(player, "format.err", "error.nan", args[offset]);
                return null;
            }
            double z;
            try {
                z = Double.parseDouble(args[4]);
            } catch (NumberFormatException ex) {
                localizer.messageFormat(player, "format.err", "error.nan", args[offset + 1]);
                return null;
            }
            return new WorldRadarPos(x, z);
        }
        else {
            float yaw;
            try {
                yaw = Float.parseFloat(args[3]);
            } catch (NumberFormatException ex) {
                localizer.messageFormat(player, "format.err", "error.nan", args[offset]);
                return null;
            }
            return new FixedRadarPos(yaw);
        }
    }
    
    // UTIL
    
    private boolean validatePermission(CommandSender sender, String permission) {
        if (sender.hasPermission(permission)) return true;
        else {
            localizer.messageFormat(sender, "format.err", "error.permission", permission);
            return false;
        }
    }
    
    @Nullable
    private Player validatePlayerSender(CommandSender sender) {
        if (!(sender instanceof Player)) {
            localizer.messageFormat(sender, "format.err", "error.not_a_player");
            return null;
        }
        else return (Player) sender;
    }
    
    @Nullable
    private Player validatePlayerWithPermission(CommandSender sender, String permission) {
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
