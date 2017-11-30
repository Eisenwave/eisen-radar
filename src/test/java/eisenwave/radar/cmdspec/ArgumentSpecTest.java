package eisenwave.radar.cmdspec;

import org.junit.Test;

import static org.junit.Assert.*;

public class ArgumentSpecTest {
    
    @Test
    public void test() throws Exception {
        ArgumentSpec arg = new ArgumentSpec("test:int?");
        assertEquals("test", arg.getId());
        assertEquals(int.class, arg.getType());
        //System.out.println(Integer.toBinaryString(arg.getType().getFlags()));
        assertTrue(arg.isOptional());
        assertFalse(arg.isCaseInsensitive());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConflictingCases() {
        new ArgumentSpec("test:int^_");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConflictingQuantifiers() {
        new ArgumentSpec("test:int+*");
    }
    
    @Test
    public void testWhitespace() {
        ArgumentSpec arg = new ArgumentSpec(" test : int ?");
        assertEquals("test", arg.getId());
        assertEquals(int.class, arg.getType());
        assertTrue(arg.isOptional());
        assertFalse(arg.isCaseInsensitive());
    }
    
}
