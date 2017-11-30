package eisenwave.radar.io;

import eisenwave.commons.io.TextDeserializer;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.time.Instant;
import java.util.stream.Collectors;

public class TimestampDeserializer implements TextDeserializer<Instant> {
    
    @NotNull
    @Override
    public Instant fromReader(Reader reader) throws IOException {
        String str = new BufferedReader(reader).lines().collect(Collectors.joining());
        try {
            return Instant.ofEpochMilli(Long.parseLong(str));
        } catch (NumberFormatException ex) {
            throw new IOException(ex);
        }
    }
    
}
