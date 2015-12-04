package Math.Equation;

import Math.MathObject;
import Math.Print;
import Math.Exception.TypeMisMatchException;
import Math.Exception.NotDefinedException;

/**
 * A node that represents either a variable or a constant.
 * Note: This class doesn't interact with {@link Node#subNodes()}, and consequently, <code>this.size()</code> will
 * always yeild 0.
 * @author Sam Westerman
 * @version 0.1
 */
public class FinalNode extends Node implements MathObject{

    /** The String representation of this. Only used if FinalNode is representing a variable. */
    private String sVal;

    /** The numeric representation of this. Only used if FinalNode is representing a constant. */    
    private double dVal;

    /**
     * The default constructor for FinalNode. Just passes null to the main constructor.
     */
    public FinalNode() {
        this(new Token());
    }

    /**
     * The main constructor for FinalNode. This attempts to determine whether it is representing a variable or a 
     * constant, and sets the respective ({@link #sVal} / {@link #dVal}) variable to pToken.val().
     * @param pToken    The token that this node is based off of.
     * @throws TypeMisMatchException    Thrown when either pToken isn't a num or var, or when it is a num, but no double
     *                                  can be parsed from it.
     */
    public FinalNode(Token pToken) throws TypeMisMatchException {
        super(pToken); // this sets token.
        if (token.type() == Token.Type.NUM)
            try {
                dVal = Double.parseDouble(token.val());
            } catch(NumberFormatException err) {
                throw new TypeMisMatchException("pToken.type() is a NUM, but pToken.val() cant be parsed as a double!");
            }
        else if (token.type() == Token.Type.VAR || token.type() == Token.Type.ARGS)
            sVal = token.val();
        else
            throw new TypeMisMatchException("pToken.type() isn't NUM, VAR, or ARGS!");
        
    }

    public String sVal() {return sVal;}
    public double dVal() {return dVal;}

    @Override
    public double eval(EquationSystem pEqSys) throws NotDefinedException {
        if (token().type() == Token.Type.NUM) {
            return dVal();
        } else if (token().type() == Token.Type.VAR) {
            if(false) { //fix me
            // if(pEqSys.functions().get(sVal()) != null) {
                // return (double)pEqSys.functions().get(sVal());
            // } else if(!inVar(sVal())) {
                throw new NotDefinedException();
            } else if(true) {//fix me too;
                switch(sVal().toLowerCase()) {
                    case "e": return Math.E;
                    case "pi": return Math.PI;
                    case "rand": case "random": return Math.random();
                    default:
                        throw new NotDefinedException();
                }
            } else {
                throw new NotDefinedException("define me");
                // return (double)getVar(sVal());
            }
        } else if(token().type() == Token.Type.ARGS) {
            Print.printw("Attempting to evaluate args! probably won't go well :P");
            throw new NotDefinedException("define me");
            // return (double)getVar(dVal());
        } else {
            throw new TypeMisMatchException("FinalNode '" +dVal() + "&" + dVal() +
                                            "' isn't a NUM, VAR, OR ARGS!");
        }
    }

    @Override
    public String toString() {
        return "[" + (token.type() == Token.Type.NUM ? dVal : sVal) + "]";
    }
    
    @Override
    public String toFancyString() {
        throw new NotDefinedException();
    }

    @Override
    public String toFullString() {
        String ret = "[";
        ret += token.type() == Token.Type.NUM ? dVal : "\"" + sVal + "\"";
        ret += ": " + token.type();
        return ret + "]";

    }

    /**
     * Just returns the {@link #toString} of this object. Mainly used for indentations.
     * @see   Node#toStringL
     */
    @Override
    public String toStringL(int pos) {
        return "" + this;
    }

}