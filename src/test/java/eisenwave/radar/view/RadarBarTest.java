package eisenwave.radar.view;

import org.junit.Test;

import static org.junit.Assert.*;

public class RadarBarTest {
    
    @Test
    public void normalize() {
        //System.out.println(-10 % 360);
        assertEquals(-90, RadarBar.normalize(270), 0);
        assertEquals(  0, RadarBar.normalize(360), 0);
        assertEquals( 30, RadarBar.normalize(-330), 0);
        assertEquals( 30, RadarBar.normalize(390), 0);
        assertEquals( 90, RadarBar.normalize(-270), 0);
    }
    
}