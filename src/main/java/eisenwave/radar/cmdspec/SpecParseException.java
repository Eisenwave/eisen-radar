package eisenwave.radar.cmdspec;

public class SpecParseException extends IllegalArgumentException {
    
    public SpecParseException() {
        super();
    }
    
    public SpecParseException(String s) {
        super(s);
    }
    
    public SpecParseException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public SpecParseException(Throwable cause) {
        super(cause);
    }
    
}
