package eisenwave.radar.io;

import eisenwave.commons.io.TextDeserializer;
import eisenwave.radar.data.Pair;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.bukkit.util.BlockVector;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DeathPointsDeserializer implements TextDeserializer<List<Pair<UUID, BlockVector>>> {
    
    private final static CSVFormat CSV_FORMAT = DeathPointsSerializer.CSV_FORMAT
        .withSkipHeaderRecord();
    
    @SuppressWarnings("unchecked")
    @NotNull
    @Override
    public List<Pair<UUID, BlockVector>> fromReader(Reader reader) throws IOException {
        List<Pair<UUID, BlockVector>> result = new ArrayList<>();
        
        try (CSVParser parser = new CSVParser(reader, CSV_FORMAT)) {
            for (CSVRecord record : parser) try {
                UUID uuid = UUID.fromString(record.get("uuid"));
                int x = Integer.parseInt(record.get("x"));
                int y = Integer.parseInt(record.get("y"));
                int z = Integer.parseInt(record.get("z"));
                result.add(new Pair<>(uuid, new BlockVector(x, y, z)));
            } catch (IllegalArgumentException ex) {
                throw new IOException(ex);
            };
        }
        
        return result;
    }
    
}
