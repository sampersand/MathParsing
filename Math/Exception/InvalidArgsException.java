package Math.Exception;

/**
 * Thrown when the arguments to a function are not correct for one reason or another
 * 
 * @author Sam Westerman
 * @version 0.1
 */
public class InvalidArgsException extends MathException {

    /**
     * Default Constructor. Just passes <code>"Define me!"</code> to
     * {@link #InvalidArgsException(String) the main constructor}.
     */
    public InvalidArgsException() {
        super();
    }

    // Inherits javadoc from MathException
    public InvalidArgsException(String cause) {
        super(cause);
    }
}