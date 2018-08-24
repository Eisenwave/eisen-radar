package eisenwave.radar.io;

import eisenwave.commons.io.TextSerializer;
import eisenwave.radar.data.Pair;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.QuoteMode;
import org.bukkit.util.BlockVector;

import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.UUID;

public class DeathPointsSerializer implements TextSerializer<List<Pair<UUID, BlockVector>>> {
    
    public final static CSVFormat CSV_FORMAT = CSVFormat.DEFAULT
        .withQuoteMode(QuoteMode.ALL)
        .withHeader("uuid", "x", "y", "z");
    
    @Override
    public void toWriter(List<Pair<UUID, BlockVector>> pairs, Writer writer) throws IOException {
        try (CSVPrinter printer = new CSVPrinter(writer, CSV_FORMAT)) {
            for (Pair<UUID, BlockVector> pair : pairs) {
                BlockVector loc = pair.getB();
                printer.printRecord(pair.getA(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
            }
        }
    }
    
}
