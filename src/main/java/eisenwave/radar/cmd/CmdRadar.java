package eisenwave.radar.cmd;

import eisenwave.radar.EisenRadarPlugin;
import eisenwave.radar.lang.Localizer;
import eisenwave.radar.model.WayPoint;
import eisenwave.radar.model.RadarMap;
import eisenwave.radar.model.RadarSymbol;
import eisenwave.radar.model.pos.FixedRadarPos;
import eisenwave.radar.model.pos.RadarPosition;
import eisenwave.radar.model.pos.WorldRadarPos;
import eisenwave.radar.view.RadarBar;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

public class CmdRadar extends EisenRadarCommand implements SimpleTabCompleter {
    
    private final static String
        USAGE = "/radar (add|edit|list|on|off|settings|toggle)",
        USAGE_ADD = "/radar add <id> <symbol> (here|<yaw>|<x> <z>)",
        USAGE_REMOVE = "/radar remove <id>",
        USAGE_EDIT = "/radar edit <id> (infrange|pos|symbol) ...",
        USAGE_EDIT_INFRANGE = "/radar edit <id> infrange (true|false)",
        USAGE_EDIT_POS = "/radar edit <id> pos (here|<yaw>|<x> <z>)",
        USAGE_EDIT_SYMBOL = "/radar edit <id> symbol <symbol>",
        USAGE_SETTINGS = "/radar settings wprange [infinite|<max waypoint range>]";
    
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
        String usage = "/radar (" + getTabCompleteOptions(sender).stream().collect(Collectors.joining("|")) + ")";
        command.setUsage(localizer.translate(sender, "format.use", usage));
        
        if (args.length == 0) {
            return false;
        }
        
        switch (args[0]) {
            case "settings": {
                Player player = validatePlayerWithPermission(sender, "eisenradar.set.settings");
                if (player == null) return true;
                
                command.setUsage(localizer.translate(player, "format.use", USAGE_SETTINGS));
                if (args.length < 2) return false;
                
                switch (args[1]) {
                    case "wprange": {
                        if (args.length >= 3) {
                            double range = parseRange(player, args[2]);
                            if (Double.isNaN(range)) return true;
                            plugin.getRadarController().getRadarMap(player.getWorld()).setWayPointRange(range);
                            localizer.messageFormat(player, "format.msg",
                                "command.radar.settings.wprange.set", args[2]);
                        }
                        else {
                            RadarMap map = plugin.getRadarController().getRadarMap(player.getWorld());
                            double range = map.getWayPointRange();
                            String rangeStr = Double.isInfinite(range)? "infinite" : Double.toString(range);
                            localizer.messageFormat(player, "format.msg",
                                "command.radar.settings.wprange.get", rangeStr);
                        }
                        return true;
                    }
                    
                    default: return false;
                }
            }
            
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
                RadarPosition pos = parsePosition(player, Arrays.copyOfRange(args, 3, args.length));
                if (pos == null) return true;
                
                map.add(id, pos, symbol);
                localizer.messageFormat(player, "format.msg", "command.radar.add.success", id);
                return true;
            }
            
            case "edit": {
                Player player = validatePlayerWithPermission(sender, "eisenradar.set.edit");
                if (player == null) return true;
                
                command.setUsage(localizer.translate(player, "format.use", USAGE_EDIT));
                if (args.length < 3) return false;
                
                String id = args[1];
                RadarMap map = plugin.getRadarController().getRadarMap(player.getWorld());
                WayPoint dot = map.get(id);
                if (dot == null) {
                    localizer.messageFormat(player, "format.err", "command.radar.edit.bad_id", args[1]);
                    return true;
                }
                
                switch (args[2]) {
                    case "pos": {
                        if (args.length < 4) {
                            command.setUsage(localizer.translate(player, "format.use", USAGE_EDIT_POS));
                            return false;
                        }
                        RadarPosition pos = parsePosition(player, Arrays.copyOfRange(args, 3, args.length));
                        if (pos == null) return true;
                        
                        dot.setPosition(pos);
                        localizer.messageFormat(player, "format.msg", "command.radar.edit.pos", id, pos);
                        return true;
                    }
                    case "infrange": {
                        if (args.length < 4) {
                            command.setUsage(localizer.translate(player, "format.use", USAGE_EDIT_INFRANGE));
                            return false;
                        }
                        boolean infRange = Boolean.parseBoolean(args[3]);
                        dot.setInfiniteRange(infRange);
                        
                        String message = "command.radar.edit.infrange." + infRange;
                        localizer.messageFormat(player, "format.msg", message, id);
                        return true;
                    }
                    case "symbol": {
                        if (args.length < 4) {
                            command.setUsage(localizer.translate(player, "format.use", USAGE_EDIT_SYMBOL));
                            return false;
                        }
                        String symbolStr = ChatColor.translateAlternateColorCodes('&', args[3]);
                        RadarSymbol symbol = new RadarSymbol(symbolStr);
                        dot.setSymbol(symbol);
                        localizer.messageFormat(player, "format.msg", "command.radar.edit.symbol",
                            id, symbolStr + ChatColor.RESET);
                        return true;
                    }
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
                WayPoint dot = map.remove(args[1]);
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
    public List<String> getTabCompleteOptions(CommandSender sender) {
        List<String> result = new ArrayList<>(8);
        if (sender.hasPermission("eisenradar.set.add")) result.add("add");
        if (sender.hasPermission("eisenradar.set.edit")) result.add("edit");
        if (sender.hasPermission("eisenradar.get.list")) result.add("list");
        if (sender.hasPermission("eisenradar.view.off")) result.add("off");
        if (sender.hasPermission("eisenradar.view.on")) result.add("on");
        if (sender.hasPermission("eisenradar.set.remove")) result.add("remove");
        if (sender.hasPermission("eisenradar.set.settings")) result.add("settings");
        if (sender.hasPermission("eisenradar.view.toggle")) result.add("toggle");
        return result;
    }
    
    // STRING
    
    @Nullable
    private RadarPosition parsePosition(Player player, String[] args) {
        if (args[0].equals("here")) {
            Location loc = player.getLocation();
            return new WorldRadarPos(loc.getX(), loc.getZ());
        }
        else if (args.length >= 2) {
            double x;
            try {
                x = Double.parseDouble(args[0]);
            } catch (NumberFormatException ex) {
                localizer.messageFormat(player, "format.err", "error.nan", args[0]);
                return null;
            }
            double z;
            try {
                z = Double.parseDouble(args[1]);
            } catch (NumberFormatException ex) {
                localizer.messageFormat(player, "format.err", "error.nan", args[1]);
                return null;
            }
            return new WorldRadarPos(x, z);
        }
        else {
            float yaw;
            try {
                yaw = Float.parseFloat(args[0]);
            } catch (NumberFormatException ex) {
                localizer.messageFormat(player, "format.err", "error.nan", args[0]);
                return null;
            }
            return new FixedRadarPos(yaw);
        }
    }
    
    private double parseRange(Player player, String range) {
        switch (range.toLowerCase()) {
            case "inf":
            case "infinite":
            case "infinity": return Double.POSITIVE_INFINITY;
            
            default: try {
                double result = Double.parseDouble(range);
                if (result < 0) {
                    localizer.messageFormat(player, "format.err", "command.radar.settings.wprange.bad_range_negative");
                    return Double.NaN;
                }
                return result;
            } catch (NumberFormatException ex) {
                localizer.messageFormat(player, "format.err", "command.radar.settings.wprange.bad_range");
                return Double.NaN;
            }
        }
    }
    
    // UTIL
    
}
