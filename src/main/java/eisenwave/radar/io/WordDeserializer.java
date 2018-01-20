package eisenwave.radar.io;

import eisenwave.commons.io.TextDeserializer;
import eisenwave.radar.data.WordOfEisenwave;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.Reader;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class WordDeserializer implements TextDeserializer<WordOfEisenwave> {
    
    private final Instant currentTimestamp;
    private final String currentVersion;
    
    public WordDeserializer(@NotNull Instant currentTimestamp, @NotNull String currentVersion) {
        this.currentTimestamp = currentTimestamp;
        this.currentVersion = currentVersion;
    }
    
    @NotNull
    @Override
    public WordOfEisenwave fromReader(Reader reader) throws IOException {
        JSONObject json;
        try {
            json = (JSONObject) new JSONParser().parse(reader);
        } catch (ParseException e) {
            throw new IOException(e);
        }
        
        String versionMessage;
        {
            JSONObject objLatest = (JSONObject) json.get("latest");
            Instant timestamp = Instant.ofEpochMilli((long) objLatest.get("timestamp"));
            
            if (currentTimestamp.isBefore(timestamp)) {
                LocalDateTime dateTime = LocalDateTime.ofInstant(timestamp, ZoneId.systemDefault());
                String version = objLatest.get("version").toString();
                String infoFormat = objLatest.get("info_format").toString();
                versionMessage = applyEscapes(String.format(infoFormat, version, dateTime));
            }
            else versionMessage = null;
        }
        
        List<String> messages = new ArrayList<>();
        {
            JSONObject objMOTD = (JSONObject) json.get("motd");
            String all = (String) objMOTD.get("*");
            String current = (String) objMOTD.get(currentVersion);
            
            if (all != null) messages.add(applyEscapes(all));
            if (current != null) messages.add(applyEscapes(current));
        }
        
        return new WordOfEisenwave(versionMessage, messages);
    }
    
    private static String applyEscapes(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg.replace("\\n", "\n"));
    }
    
}
