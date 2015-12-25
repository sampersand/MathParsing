package Math.Equation;

import Math.MathObject;
import Math.Print;
import Math.Equation.Token.Type;
import Math.Exception.NotDefinedException;

import java.util.HashMap;
import java.util.Random;

/**
 * A class that represents an operation in mathametics. It acts very similar to an {@link InBuiltFunction}.
 * 
 * @author Sam Westerman
 * @version 0.72
 * @since 0.1
  * @see <a href="https://en.wikipedia.org/wiki/Operation_(mathematics)">Operation</a>
 */
public class OperationFunction extends InBuiltFunction {

    /**
     * Default constructor. Instatiated {@link #name}, {@link #help}, and {@link #syntax} as empty strings.
     * @throws IllegalArgumentException When either name, help, and / or syntax is null.
     */
    public OperationFunction() throws NotDefinedException{
        this("", "", "");
    }

    /**
     * Main constructor. Takes a name, a help string, and a syntax string.
     * @param pOper     The symbol of the operator, like <code>+, -, *, /, ^</code>.
     * @param pHelp     The "help" text that will be displayed when the {@link #help()} function is called.
     * @param pSyntax   The "syntax" text that will be displayed when the {@link #syntax()} function is called.
     * @throws IllegalArgumentException When either name, help, and / or syntax is null.
     */
    public OperationFunction(String pOper,
                             String pHelp,
                             String pSyntax) throws IllegalArgumentException{
        super(pOper, pHelp, pSyntax);
    }

    @Override
    public double exec(final EquationSystem pEqSys,
                       Node pNode) throws 
                           NotDefinedException,
                           IllegalArgumentException {
        assert pNode.subNodes().size() != 0 : "Node size cannot be 0!";
        assert name.equals("+") || name.equals("-") || name.equals("*") || name.equals("/") || name.equals("^");
        double ret = pNode.get(0).eval(pEqSys);
        switch(name) {
            case "+":
                for(int i = 1; i < pNode.subNodes().size(); i++) {
                    ret += pNode.get(i).eval(pEqSys);
                }
                break;
            case "-":
                for(int i = 1; i < pNode.subNodes().size(); i++) {
                    ret -= pNode.get(i).eval(pEqSys);
                }
                break;
            case "*":
                for(int i = 1; i < pNode.subNodes().size(); i++) {
                    ret *= pNode.get(i).eval(pEqSys);
                }
                break;
            case "/":
                for(int i = 1; i < pNode.subNodes().size(); i++) {
                    ret /= pNode.get(i).eval(pEqSys);
                }
                break;
            case "^":
                for(int i = 1; i < pNode.subNodes().size(); i++) {
                    ret = Math.pow(ret, pNode.get(1).eval(pEqSys)); // not sure this works
                }
                break;
        }
        return ret;
    }

    @Override
    public String toString() {
        return "OperationFunction '" + name + "'";
    }

    @Override
    public String toFancyString(int idtLvl) {
        String ret = indent(idtLvl) + "OperationFunction '" + name + "':\n";
        ret += indent(idtLvl + 1) + "Help = " + help + "\n";
        ret += indent(idtLvl + 1) + "Syntax = " + syntax + "";
        return ret;
    }

    @Override
    public String toFullString(int idtLvl) {
        String ret = indent(idtLvl) + "OperationFunction:\n";
        ret += indent(idtLvl + 1) + "Name:\n" + indentE(idtLvl + 2) + name + "\n";
        ret += indent(idtLvl + 1) + "Help:\n" + indentE(idtLvl + 2) + help + "\n";
        ret += indent(idtLvl + 1) + "Syntax:\n" + indentE(idtLvl + 2) + syntax;
        return ret + "\n" + indentE(idtLvl + 1);
    }

    @Override
    public OperationFunction copy(){
        return new OperationFunction(name, help, syntax);
    }
}