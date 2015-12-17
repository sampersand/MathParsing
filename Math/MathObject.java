package Math;

/**
 * An object that all classes that can be instantiated implement. This forces them to implement {@link #toString()}, 
 * {@link #toFancyString()}, and {@link #toFullString()} methods.
 * 
 * @author Sam Westerman
 * @version 0.1
 */
public interface MathObject {
    /**
     * Returns the simple String representation of this class.
     * @return the simple string representation of this class.
     */
    public String toString();

    /**
     * Returns the fancy String representation of this class.
     * @return the fancy string representation of this class.
     */
    public String toFancyString();

    /**
     * Returns the complete String representation of this class.
     * @return the complete string representation of this class.
     */
    public String toFullString();

}