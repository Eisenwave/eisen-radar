package eisenwave.radar.cmdspec;

import eisenwave.commons.util.Primitives;
import org.jetbrains.annotations.Contract;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public class ArgumentSpec {
    
    public final static int
        OPTIONAL = 1,
        ZERO_OR_MORE = 2,
        ONE_OR_MORE = 4,
        UPPER_CASE = 8,
        LOWER_CASE = 16,
        CASE_INSENSITIVE = 32;
    
    private final String id;
    private final Class<?> type;
    private final int flags;
    private final String[] options;
    
    public ArgumentSpec(String arg) throws SpecParseException {
        if (arg.isEmpty()) throw new SpecParseException("arg must not be empty");
        String[] split = arg.split(":", 2);
        //System.out.println(Arrays.toString(split));
        
        if (split.length < 2) {
            int flagIndex = indexOfFlag(arg);
            this.id = flagIndex < 0? arg : arg.substring(0, flagIndex);
            this.flags = flagIndex < 0? 0 : parseFlags(arg.substring(flagIndex));
            this.type = String.class;
            this.options = null;
            return;
        }
    
        this.id = parseIdentifier(split[0].trim());
        String typeStr = split[1].trim();
        //type = new ArgumentType();
    
        if (typeStr.startsWith("(")) {
            int end = typeStr.lastIndexOf(')');
            this.type = String.class;
            this.options = typeStr.substring(1, end).split("\\|");
            this.flags = parseFlags(typeStr.substring(end + 1, typeStr.length()));
        }
        else {
            int flagIndex = indexOfFlag(typeStr);
            if (flagIndex < 0) {
                this.type = parseClass(typeStr);
                this.flags = 0;
            }
            else {
                String clsStr = typeStr.substring(0, flagIndex).trim();
                String flagStr = typeStr.substring(flagIndex).trim();
                this.type = parseClass(clsStr);
                this.flags = parseFlags(flagStr);
            }
            this.options = null;
        }
    }
    
    /**
     * Returns the identifier of this argument.
     *
     * @return the identifier
     */
    public String getId() {
        return id;
    }
    
    /**
     * Returns the type of this argument.
     *
     * @return the type of this argument
     */
    public Class<?> getType() {
        return this.type;
    }
    
    /**
     * Returns the options of this argument if it is a "string picking" argument.
     *
     * @return the options of this argument
     */
    public Collection<String> getOptions() {
        return options==null? Collections.emptyList() : Arrays.asList(options);
    }
    
    /**
     * Returns all flags of this type.
     *
     * @return all flags
     */
    public int getFlags() {
        return flags;
    }
    
    // FLAGS
    
    public boolean isOptional() {
        return (flags & OPTIONAL) != 0;
    }
    
    public boolean isZeroOrMore() {
        return (flags & ZERO_OR_MORE) != 0;
    }
    
    public boolean isOneOrMore() {
        return (flags & ONE_OR_MORE) != 0;
    }
    
    public boolean isUpperCase() {
        return (flags & UPPER_CASE) != 0;
    }
    
    public boolean isLowerCase() {
        return (flags & LOWER_CASE) != 0;
    }
    
    public boolean isCaseInsensitive() {
        return (flags & CASE_INSENSITIVE) != 0;
    }
    
    // PARSING
    
    protected static String parseIdentifier(String str) {
        for (int i = 0; i < str.length(); i++)
            if (!isIdentifierChar(str.charAt(i)))
                throw new SpecParseException(i+": invalid identifier: "+str);
        
        return str;
    }
    
    protected static Class<?> parseClass(String str) {
        try {
            return Primitives.primitiveByName(str);
        } catch (ClassNotFoundException ex) {
            try {
                return Class.forName(str);
            } catch (ClassNotFoundException e) {
                throw new SpecParseException("class not found: "+str);
            }
        }
    }
    
    protected static int parseFlags(String str) {
        int result = 0;
        char[] chars = str.toCharArray();
        for (char c : chars) {
            if (Character.isWhitespace(c)) continue;
            switch (c) {
                case '?': {
                    result |= OPTIONAL;
                    break;
                }
                case '*': {
                    if ((result & ONE_OR_MORE) != 0)
                        throw new SpecParseException("conflicting flags: '+' and '*'");
                    result |= ZERO_OR_MORE;
                    break;
                }
                case '+': {
                    if ((result & ZERO_OR_MORE) != 0)
                        throw new SpecParseException("conflicting flags: '*' and '+'");
                    result |= ONE_OR_MORE;
                    break;
                }
                case '^': {
                    if ((result & LOWER_CASE) != 0)
                        throw new SpecParseException("conflicting flags: '_' and '^'");
                    result |= UPPER_CASE;
                    break;
                }
                case '_': {
                    if ((result & UPPER_CASE) != 0)
                        throw new SpecParseException("conflicting flags: '_' and '^'");
                    result |= LOWER_CASE;
                    break;
                }
                case '~': {
                    result |= CASE_INSENSITIVE;
                    break;
                }
                default: throw new SpecParseException("unknown flag: '"+c+"'");
            }
        }
        
        // simplify "optional one or more" as "zero or more"
        int optionalAndPlus = OPTIONAL | ONE_OR_MORE;
        if ((result & optionalAndPlus) == optionalAndPlus) {
            //System.out.println("simplified "+Integer.toBinaryString(result));
            result &= ~optionalAndPlus;
            result |= ZERO_OR_MORE;
        }
        
        return result;
    }
    
    @Contract(pure = true)
    private static boolean isFlagChar(char c) {
        return c == '?' || c == '*' || c == '+' || c == '^' || c == '_' || c == '~';
    }
    
    @Contract(pure = true)
    private static int indexOfFlag(String str) {
        char[] chars = str.toCharArray();
        for (int i = 0; i < chars.length; i++)
            if (isFlagChar(chars[i]))
                return i;
        return -1;
    }
    
    @Contract(pure = true)
    private static boolean isIdentifierChar(char c) {
        return c == '_' || Character.isAlphabetic(c) || Character.isDigit(c);
    }
    
}
