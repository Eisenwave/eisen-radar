package eisenwave.radar.util;

import org.junit.Test;

import static org.junit.Assert.*;

public class BukkitVersionTest {
    
    @Test
    public void getFirst() throws Exception {
        BukkitVersion version = new BukkitVersion("1.12.2");
        assertEquals(1, version.getFirst());
    }
    
    @Test
    public void getSecond() throws Exception {
        BukkitVersion version = new BukkitVersion("1.12.2");
        assertEquals(12, version.getSecond());
    }
    
    @Test
    public void getThird() throws Exception {
        BukkitVersion version = new BukkitVersion("1.12.2");
        assertEquals(2, version.getThird());
    }
    
    @Test
    public void isNewerThan() throws Exception {
        BukkitVersion version = new BukkitVersion("1.12.2");
        assertTrue(version.isNewerThan(new BukkitVersion("0.0.0")));
        assertTrue(version.isNewerThan(new BukkitVersion("1.0.0")));
        assertTrue(version.isNewerThan(new BukkitVersion("1.12.0")));
        assertTrue(version.isNewerThan(new BukkitVersion("1.12.1")));
        assertFalse(version.isNewerThan(new BukkitVersion("1.12.2")));
    }
    
}
