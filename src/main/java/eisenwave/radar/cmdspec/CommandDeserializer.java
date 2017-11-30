package eisenwave.radar.cmdspec;

import eisenwave.commons.io.TextDeserializer;
import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.Reader;

public class CommandDeserializer implements TextDeserializer<ParsedCommand> {
    
    private final ParsedCommand result = new ParsedCommand();
    private final CommandSpec specification;
    
    public CommandDeserializer(@NotNull CommandSpec specification) {
        this.specification = specification;
    }
    
    @NotNull
    @Override
    public ParsedCommand fromReader(Reader reader) throws IOException {
        JSONObject json;
        try {
            json = (JSONObject) new JSONParser().parse(reader);
        } catch (ParseException | ClassCastException ex) {
            throw new IOException(ex);
        }
        
        
    
        return result;
    }
    
}
