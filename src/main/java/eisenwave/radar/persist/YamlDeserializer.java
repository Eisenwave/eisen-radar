package eisenwave.radar.persist;

import eisenwave.commons.io.TextDeserializer;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.Reader;

public interface YamlDeserializer<T> extends TextDeserializer<T> {
    
    public abstract T fromYaml(YamlConfiguration yaml);
    
    @NotNull
    @Override
    default T fromReader(Reader reader) throws IOException {
        YamlConfiguration yaml = new YamlConfiguration();
        try {
            yaml.load(reader);
        } catch (InvalidConfigurationException e) {
            throw new IOException(e);
        }
        return fromYaml(yaml);
    }
    
    @NotNull
    @Override
    default T fromString(String str) throws IOException {
        YamlConfiguration yaml = new YamlConfiguration();
        try {
            yaml.load(str);
        } catch (InvalidConfigurationException e) {
            throw new IOException(e);
        }
        return fromYaml(yaml);
    }
    
    @NotNull
    @Override
    default T fromFile(File file) throws IOException {
        YamlConfiguration yaml = new YamlConfiguration();
        try {
            yaml.load(file);
        } catch (InvalidConfigurationException e) {
            throw new IOException(e);
        }
        return fromYaml(yaml);
    }
    
}
