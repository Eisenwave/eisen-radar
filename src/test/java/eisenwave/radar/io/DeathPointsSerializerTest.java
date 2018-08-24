package eisenwave.radar.io;

import eisenwave.radar.data.Pair;
import org.bukkit.Location;
import org.bukkit.util.BlockVector;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;

import static org.junit.Assert.*;

public class DeathPointsSerializerTest {
    
    @SuppressWarnings("unchecked")
    @Test
    public void writeHeader() throws IOException {
        String result = new DeathPointsSerializer().toString(Collections.emptyList());
        
        assertEquals("$uuid$,$x$,$y$,$z$\r\n".replace('$', '"'), result.replace(" ", ""));
    }
    
    @SuppressWarnings("unchecked")
    @Test
    public void writeStuff() throws IOException {
        UUID uuid0 = UUID.randomUUID(), uuid1 = UUID.randomUUID();
        BlockVector v0 = new BlockVector(1, 2, 3), v1 = new BlockVector(-1, -2, -3);
        
        String expected = ("$uuid$,$x$,$y$,$z$\r\n"
            + "$" + uuid0 + "$,$" + v0.getBlockX() + "$,$" + v0.getBlockY() + "$,$" + v0.getBlockZ() + "$\r\n"
            + "$" + uuid1 + "$,$" + v1.getBlockX() + "$,$" + v1.getBlockY() + "$,$" + v1.getBlockZ() + "$\r\n"
        ).replace('$', '"');
        String actual = new DeathPointsSerializer().toString(Arrays.asList(new Pair(uuid0, v0), new Pair(uuid1, v1)));
        
        assertEquals(expected, actual.replace(" ", ""));
    }
    
}
