/** 
 * A node that represents either a varriable or a constant.
 * Note: This class doesn't interact with {@link Node#subNodes}, and consequently, <code>this.size()</code> will
 * always yeild 0.
 * @author Sam Westerman
 * @version 0.1
 */
public class FinalNode extends Node {

    /** The string representation of this. Only used if FinalNode is representing a varriable. */
    public String sVal;

    /** The numeric representation of this. Only used if FinalNode is representing a constant. */    
    public double dVal;

    /** 
     * The default constructor for FinalNode. Just passes null to the main constructor.
     */
    public FinalNode() {
        this(null);
    }

    /** 
     * The main constructor for FinalNode. This attempts to determine whether it is representing a varriable or a 
     * constant, and sets the respective ({@link #sVal} / {@link #dVal}) varriable to pToken.VAL.
     * @param pToken    The token that this node is based off of.
     * @throws TypeMisMatchException    Thrown when either pToken isn't a num or var, or when it is a num, but no double
     *                                  can be parsed from it.
     */
    public FinalNode(Token pToken) throws TypeMisMatchException {
        super(pToken); // this sets TOKEN.
        if (TOKEN.TYPE == Token.Types.NUM)
            try {
                dVal = Double.parseDouble(TOKEN.VAL);
            } catch(NumberFormatException err) {
                throw new TypeMisMatchException("pToken.TYPE is a NUM, but pToken.VAL cant be parsed as a double!");
            }
        else if (TOKEN.TYPE == Token.Types.VAR)
            sVal = TOKEN.VAL;
        else
            throw new TypeMisMatchException("pToken.TYPE isn't NUM or VAR!");
        
    }

    @Override
    public String fullString() {
        String ret = "[";
        ret += TOKEN.TYPE == Token.Types.VAR ? "\"" + sVal + "\"" : dVal;
        ret += ": " + (TOKEN.TYPE == Token.Types.VAR ? "VAR" : TOKEN.TYPE == Token.Types.NUM ? "NUM" : "NULL");
        return ret + "]";

    }

    @Override
    public String toString() {
        return "[" + (TOKEN.TYPE == Token.Types.VAR ? sVal : dVal) + "]";
    }
    
}