package West.Math.Exception;

/**
 * An error that is thrown when something hasn't been defined. An example is when there is no known way to handle
 * a parameter. 
 * 
 * @author Sam Westerman
  * @version 0.90
 * @since 0.1
 */
public class NotDefinedException extends RuntimeException {

    /**
     * Default Constructor. Just passes <code>"Define me!"</code> to
     * {@link #NotDefinedException(String) the main constructor}.
     */
    public NotDefinedException() {
        this("Define me!");
    }

    public NotDefinedException(String cause) {
        super(cause);
    }
}

