package Math.Exception;

/**
 * An error that is thrown when there is no known way to handle something (usually a parameter). Differs from 
 * {@link #DoesntExistException} because this one is for handling Objects, and the other one is for accessing Objects.
 * 
 * @author Sam Westerman
 * @version 0.1
 */
public class NotDefinedException extends MathException {

    /**
     * The empty constructor. Just passes <code>"Define me!"</code> to
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

