package eisenwave.radar.view;

import eisenwave.radar.data.RadarSymbol;
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
    
    @Test
    public void canSee() {
        RadarBar testBar = new RadarBar(null, 5, 90, new RadarBarStyle(new RadarSymbol("-")));
        assertTrue(testBar.canSee(0));
        assertTrue(testBar.canSee(-45));
        assertTrue(testBar.canSee(45));
        
        assertFalse(testBar.canSee(180)); // behind
        assertFalse(testBar.canSee(-46)); // out of FOV
        assertFalse(testBar.canSee(46)); // out of FOV
    }
    
}
