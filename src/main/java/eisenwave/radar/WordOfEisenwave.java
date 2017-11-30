package eisenwave.radar;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class WordOfEisenwave {
    
    private final String versionMessage;
    private final List<String> messages;
    
    public WordOfEisenwave(@Nullable String versionMessage,
                           @NotNull List<String> messages) {
        this.versionMessage = versionMessage;
        this.messages = messages;
    }
    
    /**
     * Returns the version message.
     *
     * @return the version message
     */
    public String getVersionMessage() {
        return versionMessage;
    }
    
    /**
     * Returns a list of messages to print.
     *
     * @return a list of messages to print
     */
    public List<String> getMessages() {
        return messages;
    }
    
}
