package eisenwave.radar.io;

import eisenwave.radar.data.Pair;
import org.bukkit.util.BlockVector;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;

public class DeathPointsDeserializerTest {
    
    @Test
    public void fromReader() throws IOException {
        List<Pair<UUID, BlockVector>> pairs = new DeathPointsDeserializer().fromResource(getClass(), "death_points.csv");
        assertEquals(UUID.fromString("40249269-39e1-4258-ae73-26765e0fa120"), pairs.get(0).getA());
        assertEquals(UUID.fromString("0b7d4f89-fd4b-4633-96bd-6d313fa34aaf"), pairs.get(1).getA());
        assertEquals(new BlockVector(1, 2, 3), pairs.get(0).getB());
        assertEquals(new BlockVector(-1, -2, -3), pairs.get(1).getB());
    }
}
