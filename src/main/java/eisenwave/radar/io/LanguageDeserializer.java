package eisenwave.radar.io;

import eisenwave.commons.io.TextDeserializer;
import eisenwave.radar.lang.PluginLanguage;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class LanguageDeserializer implements TextDeserializer<PluginLanguage> {
    
    private final String locale;
    
    public LanguageDeserializer(@NotNull String locale) {
        this.locale = locale;
    }
    
    @NotNull
    @Override
    public PluginLanguage fromReader(Reader reader) throws IOException {
        Properties properties = new Properties();
        properties.load(reader);
    
        return new PluginLanguage(locale, vocabOf(properties));
    }
    
    @NotNull
    @Override
    public PluginLanguage fromStream(InputStream stream) throws IOException {
        Properties properties = new Properties();
        properties.load(stream);
        
        return new PluginLanguage(locale, vocabOf(properties));
    }
    
    private static Map<String, String> vocabOf(Properties properties) {
        Map<String, String> map = new HashMap<>();
        properties.stringPropertyNames().forEach(name -> map.put(name, properties.getProperty(name)));
        
        return map;
    }
    
}
