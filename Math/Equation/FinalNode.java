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
 * @version 0.65
 * @since 0.1
 */
public class FinalNode extends Node implements MathObject {

    /** The String representation of this. Only used if FinalNode is representing a variable. */
    protected String sVal;

    /** The numeric representation of this. Only used if FinalNode is representing a constant. */    
    protected double dVal;

    /**
     * The default constructor for FinalNode. Just passes an empty {@link Token} to the main constructor.
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
                throw new TypeMisMatchException("Cannot instatiate FinalNode because pToken.type (" + token.type() + ")"
                + " is a NUM, but pToken.val(" + pToken.val() + ") cannot be parsed as a double!");
            }
        else if (token.type() == Token.Type.VAR || token.type() == Token.Type.ARGS)
            sVal = token.val();
        else
            throw new TypeMisMatchException("Cannot instatiate FinalNode because pToken.type() can only be of Types: " +
                                            "NUM, VAR, or ARGS!");
        
    }

    /**
     * Returns this class's {@link #sVal}. Should only ever be called if
     * {@link Token#type() This class's token's type} is {@link Token.Types#VAR a variable}.
     * @return The name of the variable this class is modeled after.
     */
    public String sVal() {
        return sVal;
    }

    /**
     * Returns this class's {@link #sVal}. Should only ever be called if
     * {@link Token#type() This class's token's type} is {@link Token#Types#NUM a number}.
     * @param pEqSys    An {@link EquationSystem#isolate isolated EquationSystem} that will be used to evaluate.
     * @return The name of the variable this class is modeled after.
     */
    public double dVal() {
        return dVal;
    }

    @Override
    public double eval(final EquationSystem pEqSys) throws NotDefinedException {
        assert pEqSys.isolated();
        if (token.type() == Token.Type.NUM) {
            return dVal;
        } else if (token.type() == Token.Type.VAR) {
            if(pEqSys.varExist(sVal)) {
                for(Equation eq : pEqSys.equations()){
                    if(eq.expressions().get(0).node().get(0).token.val().equals(sVal)){
                        // System.out.println(eq.expressions().get(1).toFullString());
                        return eq.expressions().get(1).node().eval(pEqSys);
                    }
                }
            } switch(sVal.toLowerCase()) {
                case "e": return Math.E;
                case "pi": return Math.PI;
                case "rand": case "random": return Math.random();
                default:
                    throw new NotDefinedException("Cannot evaluate the FinalNode '" + sVal + "' because there it " + 
                        "defined as a variable, and isn't an in-built variable.");
            }
        } else if(token.type() == Token.Type.ARGS) {
            Print.printw("Attempting to evaluate args! probably won't go well :P");
            throw new NotDefinedException("define me");
            // return (double)getVar(dVal);
        } else {
            throw new TypeMisMatchException("Cannot evaluate the FinalNode '" +sVal + "' / '" + dVal +
                                            "' because it's type (" + token.type() + ") isn't a NUM, VAR, OR ARGS!");
        }
    }

    @Override
    public String toString() {
        return "[" + (token.type() == Token.Type.NUM ? dVal : sVal) + "]";
    }
    
    @Override
    public String toFancyString(int idtLvl) {
      return toString();
    }

    @Override
    public String toFullString(int idtLvl) {
        String ret = indent(idtLvl) + "FinalNode: [";
        ret += token.type() == Token.Type.NUM ? dVal : "\"" + sVal + "\"";
        ret += ": " + token.type() + "]";
        return ret;

    }

    @Override
    public FinalNode copy(){
        return new FinalNode(token);
    }
}