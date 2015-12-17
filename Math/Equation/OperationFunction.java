package Math.Equation;

import Math.MathObject;
import Math.Print;
import Math.Equation.Token.Type;
import Math.Exception.NotDefinedException;
import Math.Exception.InvalidArgsException;

import java.util.HashMap;
import java.util.Random;

/**
 * A class that represents an operation in mathametics. It acts very similar to an {@link InBuiltFunction}.
 * 
 * @author Sam Westerman
 * @version 0.5 
 * @see <a href="https://en.wikipedia.org/wiki/Operation_(mathematics)">Operation</a>
 */
public class OperationFunction extends InBuiltFunction {

    /**
     * Default constructor. Just passes <code>null, null, null</code> to
     * {@link #OperationFunction(String,String,String) another OperationFunction constructor}.
     */
    public OperationFunction() {
        this(null, null, null);
    }

    /**
     * The main constructor for OperationFunction. Takes a name, a help string, and a syntax string.
     * @param pOper     The symbol of the operator, like <code>+, -, *, /, ^</code>.
     * @param pHelp     The "help" text that will be displayed when the {@link #help()} function is called.
     * @param pSyntax   The "syntax" text that will be displayed when the {@link #syntax()} function is called.
     */
    public OperationFunction(String pOper,
                             String pHelp,
                             String pSyntax) {
        super(pOper, pHelp, pSyntax);
    }

    @Override
    public double exec(EquationSystem pEq,
                       Node pNode) throws 
                           NotDefinedException,
                           InvalidArgsException {
        if(pNode.subNodes().size() == 0)
            throw new InvalidArgsException("Node size cannot be 0!");
        double ret = pNode.get(0).eval(pEq);
        switch(name) {
            case "+":
                for(int i = 1; i < pNode.subNodes().size(); i++) {
                    ret += pNode.get(i).eval(pEq);
                }
                break;
            case "-":
                for(int i = 1; i < pNode.subNodes().size(); i++) {
                    ret -= pNode.get(i).eval(pEq);
                }
                break;
            case "*":
                for(int i = 1; i < pNode.subNodes().size(); i++) {
                    ret *= pNode.get(i).eval(pEq);
                }
                break;
            case "/":
                for(int i = 1; i < pNode.subNodes().size(); i++) {
                    ret /= pNode.get(i).eval(pEq);
                }
                break;
            case "^":
                for(int i = 1; i < pNode.subNodes().size(); i++) {
                    ret = Math.pow(ret, pNode.get(1).eval(pEq)); // not sure this works
                }
                break;
            default:
                throw new NotDefinedException("OperationFunction " + this + " doesnt have a defined way to compute it!");
        }
        return ret;
    }
    @Override
    public String toString() {
        return "OperationFunction: '" + name + "'";
    }

    @Override
    public String toFancyString() {
        throw new NotDefinedException();
    }

    @Override
    public String toFullString() {
        throw new NotDefinedException();
    }

    @Override
    public OperationFunction copy(){
        return new OperationFunction(name, help, syntax);
    }

}