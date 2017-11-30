package eisenwave.radar.cmdspec;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("Duplicates")
public class ParsedCommand {
    
    private final Map<String, Object> map = new HashMap<>();
    
    public ParsedCommand() {}
    
    /**
     * <p>
     *     Tries to return an object in this map. 
     * </p>
     * <p>
     *     If the object can not by found by the specified key, the fallback value is returned. If the specified class
     *     can not be assigned from the object's class, the fallback value is also returned.
     *     (using {@code Object.class}) effectively eliminates this check.)
     * </p>
     *
     * @param <T> the type parameter of the fallback value and the class
     * @param key the key
     * @param fallback the fallback value
     * @param clazz the class of the object (nonnull)
     * @return the object found in the map or the fallback value
     */
    @SuppressWarnings("unchecked")
    public <T> T get(String key, T fallback, @NotNull Class<? super T> clazz) {
        if (!map.containsKey(key)) return fallback;
        Object value = map.get(key);
        if (value == null) return fallback;
        if (!clazz.isAssignableFrom(value.getClass())) return fallback;
        return (T) value;
    }
    
    /**
     * Tries to return an object in this map. This method is identical to
     * <blockquote>
     *     {@code getObject(key, null, clazz);}
     * </blockquote>
     * @param <T> the type parameter of the fallback value and the class
     * @param key the key
     * @param clazz the class of the object (nonnull)
     * @return the object found in the map or null
     */
    @SuppressWarnings("unchecked")
    public <T> T get(String key, Class<? super T> clazz) {
        return (T) get(key, null, clazz);
    }
    
    /**
     * Tries to return an object in this map. This method is identical to
     * <blockquote>
     *     {@code getObject(key, fallback, Object.class);}
     * </blockquote>
     * @param key the key
     * @param fallback the fallback value in case the value can not be found
     * @return the object found in the map or the fallback value
     */
    public Object get(String key, Object fallback) {
        return get(key, fallback, Object.class);
    }
    
    public long getLong(String key, long fallback) {
        return get(key, fallback, Number.class);
    }
    
    public int getInt(String key, int fallback) {
        return get(key, fallback, Number.class);
    }
    
    public short getShort(String key, short fallback) {
        return get(key, fallback, Number.class);	}
    
    public byte getByte(String key, byte fallback) {
        return get(key, fallback, Number.class);	}
    
    public double getDouble(String key, double fallback) {
        return get(key, fallback, Number.class);	}
    
    public float getFloat(String key, float fallback) {
        return get(key, fallback, Number.class);	}
    
    public boolean getBoolean(String key, boolean fallback) {
        return get(key, fallback, Boolean.class);	}
    
    //public FALLBACK VALUES
    
    public long getLong(String key) {
        return getLong(key, 0);
    }
    
    public int getInt(String key) {
        return getInt(key, 0);
    }
    
    public short getShort(String key) {
        return getShort(key, (short) 0);
    }
    
    public byte getByte(String key) {
        return getByte(key, (byte) 0);
    }
    
    public double getDouble(String key) {
        return getDouble(key, 0);
    }
    
    public float getFloat(String key) {
        return getFloat(key, 0);
    }
    
    public boolean getBoolean(String key) {
        return get(key, Boolean.class);
    }
    
    // MUTATORS
    
    public void put(String key, Object value) {
        map.put(key, value);
    }
    
}
