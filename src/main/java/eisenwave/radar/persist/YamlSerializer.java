package eisenwave.radar.persist;

import eisenwave.commons.io.TextSerializer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.Writer;

public interface YamlSerializer<T> extends TextSerializer<T> {
    
    abstract void toYaml(T object, YamlConfiguration yaml);
    
    default YamlConfiguration toYaml(T object) {
        YamlConfiguration yaml = new YamlConfiguration();
        toYaml(object, yaml);
        return yaml;
    }
    
    @Override
    default void toWriter(T object, Writer writer) throws IOException {
        writer.write(toString(object));
    }
    
    @NotNull
    @Override
    default String toString(T object) throws IOException {
        return toYaml(object).saveToString();
    }
    
    @Override
    default void toFile(T object, File file) throws IOException {
        toYaml(object).save(file);
    }
    
}
