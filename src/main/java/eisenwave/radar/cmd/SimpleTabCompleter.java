package eisenwave.radar.cmd;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.List;
import java.util.stream.Collectors;

public interface SimpleTabCompleter extends TabCompleter {
    
    abstract List<String> getTabCompleteOptions(CommandSender sender);
    
    @Override
    default List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length != 1) return null;
    
        return getTabCompleteOptions(sender)
            .stream()
            .filter(option -> option.startsWith(args[0]))
            .collect(Collectors.toList());
    }
    
}
