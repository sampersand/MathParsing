package Math.Equation;
import Math.Exception.TypeMisMatchException;
/**
 * A node that represents either a variable or a constant.
 * Note: This class doesn't interact with {@link Node#subNodes}, and consequently, <code>this.size()</code> will
 * always yeild 0.
 * @author Sam Westerman
 * @version 0.1
 */
public class FinalNode extends Node {

    /** The string representation of this. Only used if FinalNode is representing a variable. */
    public String sVal;

    /** The numeric representation of this. Only used if FinalNode is representing a constant. */    
    public double dVal;

    /**
     * The default constructor for FinalNode. Just passes null to the main constructor.
     */
    public FinalNode() {
        this(new Token());
    }

    /**
     * The main constructor for FinalNode. This attempts to determine whether it is representing a variable or a 
     * constant, and sets the respective ({@link #sVal} / {@link #dVal}) variable to pToken.VAL.
     * @param pToken    The token that this node is based off of.
     * @throws TypeMisMatchException    Thrown when either pToken isn't a num or var, or when it is a num, but no double
     *                                  can be parsed from it.
     */
    public FinalNode(Token pToken) throws TypeMisMatchException {
        super(pToken); // this sets token.
        if (token.TYPE == Token.Types.NUM)
            try {
                dVal = Double.parseDouble(token.VAL);
            } catch(NumberFormatException err) {
                throw new TypeMisMatchException("pToken.TYPE is a NUM, but pToken.VAL cant be parsed as a double!");
            }
        else if (token.TYPE == Token.Types.VAR || token.TYPE == Token.Types.ARGS)
            sVal = token.VAL;
        else
            throw new TypeMisMatchException("pToken.TYPE isn't NUM, VAR, or ARGS!");
        
    }

    @Override
    public String fullString() {
        String ret = "[";
        ret += token.TYPE == Token.Types.NUM ? dVal : "\"" + sVal + "\"";
        ret += ": " + token.TYPE;
        return ret + "]";

    }

    @Override
    public String toString() {
        return "[" + (token.TYPE == Token.Types.NUM ? dVal : sVal) + "]";
    }

    /** 
     * Just returns the {@link #toString} of this object. Mainly used for indentations.
     * @see   Node#toStringL
     */
    @Override
    public String toStringL(int pos){
        return "" + this;
    }
    
}