package eisenwave.radar.cmdspec;

import org.junit.Test;

import static org.junit.Assert.*;

public class CommandSpecTest {
    
    @Test
    public void test_radar() throws Exception {
        new CommandSpecDeserializer().fromResource(getClass(), "radar.json");
    }
    
}
