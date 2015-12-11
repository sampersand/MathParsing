package Math.Exception;

/**
 * Thrown when one or more objects are unable to be evaluated how they would normally be expected to because of a 
 * Type Mismatch. Yes, Type "MisMatch" Exception is a bit of a misnomer, because mismatch is one word.
 * 
 * @author Sam Westerman
 * @version 0.1
 */
public class TypeMisMatchException extends MathException {
    /**
     * Default Constructor. Just passes <code>"Define me!"</code> to
     * {@link #TypeMisMatchException(String) the main constructor}.
     */
    public TypeMisMatchException() {
        super();
    }

    // Inherits javadoc from MathException
    public TypeMisMatchException(String cause) {
        super(cause);
    }
}