package eisenwave.radar.cmdspec;

import eisenwave.commons.io.TextDeserializer;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;
import java.util.Set;

public class CommandSpecDeserializer implements TextDeserializer<CommandSpec> {
    
    private final static char SEPARATOR = '.';
    
    @NotNull
    @Override
    public CommandSpec fromReader(Reader reader) throws IOException {
        CommandSpec result = new CommandSpec();
        JSONObject pathArgs;
        JSONObject optArgs;
        try {
            JSONObject obj = (JSONObject) new JSONParser().parse(reader);
            pathArgs = (JSONObject) obj.get("args");
            optArgs = (JSONObject) obj.get("opts");
        } catch (ParseException | ClassCastException e) {
            throw new IOException(e);
        }
        
        if (pathArgs != null) {
            parsePathArgs(result, "", pathArgs);
        }
        if (optArgs != null) {
            //noinspection unchecked
            for (String option : (Set<String>) optArgs.keySet()) {
                JSONArray array = (JSONArray) optArgs.get(option);
                String[] args = (String[]) array.toArray(new String[array.size()]);
                result.putOption(option, Arrays.asList(parseArgs(args)));
            }
        }
        
        return result;
    }
    
    @SuppressWarnings("unchecked")
    private static void parsePathArgs(CommandSpec spec, String path, @NotNull Object value) throws IOException {
        if (value instanceof JSONObject) {
            JSONObject obj = (JSONObject) value;
            for (String key : (Set<String>) obj.keySet()) {
                String newPath = key.isEmpty()? path : path + SEPARATOR + key;
                parsePathArgs(spec, newPath, obj.get(key));
            }
        }
        else if (value instanceof JSONArray) try {
            JSONArray arr = (JSONArray) value;
            String[] args = (String[]) arr.toArray(new String[arr.size()]);
            spec.putOption(path, Arrays.asList(parseArgs(args)));
        } catch (SpecParseException ex) {
            throw new IOException(ex);
        }
        else throw new IOException("\"" +value.toString() + "\": all values must be arrays or lists");
    }
    
    @Contract(pure = true)
    private static ArgumentSpec[] parseArgs(String[] args) throws SpecParseException {
        if (args.length == 0)
            return new ArgumentSpec[0];
        else if (args.length == 1)
            return new ArgumentSpec[] {new ArgumentSpec(args[0])};
        
        ArgumentSpec[] result = new ArgumentSpec[args.length];
        boolean optional = false, quantified = false;
        
        for (int i = 0; i < args.length; i++) {
            ArgumentSpec arg  = new ArgumentSpec(args[i]);
            boolean argOptional = arg.isOptional();
            
            if (optional) {
                if (!argOptional && !arg.isZeroOrMore())
                    throw new SpecParseException("\""+args[i]+"\": optional arguments can't be followed by non-optionals");
            } else if (argOptional) {
                optional = true;
            }
            if (quantified) {
                throw new SpecParseException("\""+args[i]+"\": quantified arguments must be the last arguments");
            } else if (arg.isOneOrMore() || arg.isZeroOrMore()) {
                quantified = true;
            }
            
            result[i] = arg;
        }
        
        return result;
    }
    
}
