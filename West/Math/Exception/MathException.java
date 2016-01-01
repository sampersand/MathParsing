package West.Math.Exception;

/**
 * The Exception that all other Math.Exceptions are based off of. All these exceptions were created purely to make
 * deciphering errors easier.
 * 
 * @author Sam Westerman
 * @version 0.87
 * @since 0.1
 */
public class MathException extends RuntimeException {

    /**
     * Default Constructor for this class. Just calls <code>super()</code>.
     */
    public MathException() {
        super();
    }

    /**
     * The main constructor for this class. Just calls <code>super(cause)</code>.
     * @param cause     The reason this exception was thrown.
     */
    public MathException(String cause) {
        super(cause);
    }
}