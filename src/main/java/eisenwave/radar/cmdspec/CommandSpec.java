package eisenwave.radar.cmdspec;

import org.jetbrains.annotations.NotNull;

import java.util.*;

public class CommandSpec {
    
    private final Map<String, ArgumentSpec[]> args = new HashMap<>();
    private final Map<String, ArgumentSpec[]> opts = new HashMap<>();
    
    public CommandSpec() {}
    
    public List<ArgumentSpec> getPath(String path) {
        return args.containsKey(path)? Arrays.asList(args.get(path)) : Collections.emptyList();
    }
    
    public List<ArgumentSpec> getOption(String path) {
        return opts.containsKey(path)? Arrays.asList(opts.get(path)) : Collections.emptyList();
    }
    
    public boolean hasPath(String path) {
        return args.containsKey(path);
    }
    
    public boolean hasOption(String option) {
        return opts.containsKey(option);
    }
    
    public void putPath(@NotNull String path, @NotNull List<ArgumentSpec> specs) {
        args.put(path, specs.toArray(new ArgumentSpec[specs.size()]));
    }
    
    public void putOption(@NotNull String option, @NotNull List<ArgumentSpec> specs) {
        opts.put(option, specs.toArray(new ArgumentSpec[specs.size()]));
    }
    
}
