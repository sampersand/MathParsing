package West.Math.Exception;

/**
 * An error that is thrown when something hasn't been defined. An example is when there is no known way to handle
 * a parameter. 
 * 
 * @author Sam Westerman
 * @version 0.75
 * @since 0.1
 */
public class NotDefinedException extends MathException {

    /**
     * Default Constructor. Just passes <code>"Define me!"</code> to
     * {@link #NotDefinedException(String) the main constructor}.
     */
    public NotDefinedException() {
        this("Define me!");
    }

    // Inherits javadoc from MathException
    public NotDefinedException(String cause) {
        super(cause);
    }
}

