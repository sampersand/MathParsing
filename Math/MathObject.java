package Math;

/**
 * An object that all classes that can be instantiated implement. This forces them to implement {@link #toString()}, 
 * {@link #toFancyString()}, and {@link #toFullString(int idtLvl)} methods.
 * 
 * @author Sam Westerman
 * @version 0.66
 * @since 0.1
 */
public interface MathObject {
    /**
     * Returns the simple String representation of this class.
     * @return the simple string representation of this class.
     */
    public String toString();

    /**
     * Returns the fancy String representation of this class, with a starting indentation level of 0.
     * @return the fancy string representation of this class, with a starting indentation level of 0.
     */
    public default String toFancyString(){
        return toFancyString(0);
    }

    /**
     * Returns the fancy String representation of this class, with a starting indentation level of <code>idtLvl</code>.
     * @param idtLvl    The amount of tabs to put before each and every line. 
     * @return the fancy String representation of this class, with a starting indentation level of <code>idtLvl</code>.
     */
    public String toFancyString(int idtLvl);


    /**
     * Returns the complete String representation of this class.
     * @return the complete string representation of this class.
     */
    public default String toFullString(){
        return toFullString(0);
    }

    /**
     * Returns the complete String representation of this class, with a starting indentation level of
     * <code>idtLvl</code>.
     * @param idtLvl    The amount of tabs to put before each and every line. 
     * @return the complete String representation of this class, with a starting indentation level of
     *          <code>idtLvl</code>.
     */
    public String toFullString(int idtLvl);

    /**
     * Gets an indentation level, used for {@link #toFancyString} and {@link #toFullString}.
     */
    public default String indent(int idtLvl){
        String ret = "";
        for(int i = 0; i < idtLvl; i++)
            ret +="\t";
        return ret;
    }
    
    /**
     * Copies this object.
     * @return An exact copy of this object.
     */
    public Object copy();
}