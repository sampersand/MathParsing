package Math;

/**
 * An object that all classes that can be instantiated implement. This forces them to implement {@link #toString()}, 
 * {@link #toFancyString()}, and {@link #toFullString(int idtLvl)} methods.
 * 
 * @author Sam Westerman
 * @version 0.72
 * @since 0.1
 */
public interface MathObject {
    /**
     * Returns the simple String representation of this class. This should just give a simple overview, only stating
     * key variables.
     * @return the simple string representation of this class.
     */
    public String toString();

    /**
     * Returns the fancy String representation of this class, with a starting indentation level of 0. This should
     * give a more complex overview than {@link toString()}, but less comprehensive than {@link #toFullString}.
     * @return the fancy string representation of this class, with a starting indentation level of 0.
     * @see #toFancyString(int)
     */
    public default String toFancyString(){
        return toFancyString(0);
    }

    /**
     * Returns the fancy String representation of this class, with a starting indentation level of <code>idtLvl</code>.
     * This should give a more complex overview than {@link toString()}, but less comprehensive than
     * {@link #toFullString}.
     * @param idtLvl    The amount of tabs to put before each and every line. 
     * @return the fancy String representation of this class, with a starting indentation level of <code>idtLvl</code>.
     */
    public String toFancyString(int idtLvl);


    /**
     * Returns the complete String representation of this class. This should state all variables, in the format
     * <code>varName:\n\tINFO</code>.
     * @return the complete string representation of this class.
     */
    public default String toFullString(){
        return toFullString(0);
    }

    /**
     * Returns the complete String representation of this class, with a starting indentation level of
     * <code>idtLvl</code>. This should state all variables, in the format <code>varName:\n\tINFO</code>.
     * @param idtLvl    The amount of tabs to put before each and every line. 
     * @return the complete String representation of this class, with a starting indentation level of
     *          <code>idtLvl</code>.
     */
    public String toFullString(int idtLvl);

    /**
     * Gets an indentation level, used for {@link #toFancyString} and {@link #toFullString}.
     */

    public default String indentU(int idtLvl){
        String ret = "";
        for(int i = 0; i < idtLvl; i++)
            ret += (i % 2 == 0 ? "│" : "║") + "  ";
        return ret;
        //┌
        //│─
        //├
        //└
    }
    public default String indent(int idtLvl){
        return idtLvl == 0 ? indentS(idtLvl) : indentM(idtLvl);
    }
    public default String indentS(int idtLvl){
        return indentU(idtLvl) + (idtLvl % 2 == 0 ? "┌─ " : "╔═ ");
    }
    public default String indentE(int idtLvl){
        return indentU(idtLvl) + (idtLvl % 2 == 0 ? "└─ " : "╚═ ");
    }
    public default String indentM(int idtLvl){
        return indentU(idtLvl) + (idtLvl % 2 == 0 ? "├─ ": "╠═ ");
    }

    /**
     * Copies this object.
     * @return An exact copy of this object.
     */
    public Object copy();

    public boolean equals(Object obj);
}