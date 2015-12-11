package Math.Equation;

import Math.MathObject;
import Math.Equation.CustomFunction;
import Math.Equation.Expression;
import Math.Exception.NotDefinedException;
import Math.Exception.InvalidArgsException;

import java.util.ArrayList;

/**
 * A class that represents an equation in Math. It really is just a collection of Expressions that are equal to
 * each other.
 * 
 * @author Sam Westerman
 * @version 0.1
 */
public class Equation implements MathObject {

    /** This classe's list of expressions that are equal to eachother. */
    private ArrayList<Expression> expressions;

    /**
     * The default constructor. This just instantiates {@link #expressions} as an empty ArrayList.
     */
    public Equation() {
        expressions = new ArrayList<Expression>();
    }

    /**
     * The constructor that takes any amount of {@link Expression Expressions} as parameters. It just instantiates
     * {@link #expressions} with the passed parameters <code>pExprs</code>.
     * @param pExprs    A variable amount of Expressions that will instatiate {@link #expressions Expressions}.
     */
    public Equation(Expression... pExprs) throws InvalidArgsException{
        expressions = new ArrayList<Expression>() {{
            for(Expression expr : pExprs)
                add(expr);
        }};
    }

    /**
     * The constructor that takes an ArraylList of {@link Expression Expressions} as
     */
    public Equation(ArrayList<Expression> pExprs) throws InvalidArgsException{
        expressions = pExprs;
    }

    /**
     * The construcor that takes any amount of Strings as parameters. It just instantiates {@link #expressions} with
     * {@link Expression expressions}, which are in turn instantiated with each string of <code>pStrs</code>. 
     * NOTE: Somehow connect this with {@link #add(String...) }... hmm 
     * @param pStrs    A variable amount of Strings that will instatiate {@link #expressions Expressions}.
     * */
    public Equation(String... pStrs) {
        expressions = new ArrayList<Expression>();
        for(String str : pStrs) {
            if(str.split("=").length != 1) {
                for(String strSpl : str.split("=")) {
                    expressions.add(new Expression(strSpl));
                }
            } else {
                expressions.add(new Expression(str));
            }
        }
    }

    /**
     * Adds all of the expressions in <code>pExprs</code> to {@link #expressions}.
     * @param pExprs    A variable amount of {@link Expresion Expressions} that will be added to {@link #expressions}.
     */
    public void add(Expression... pExprs) {
        for(Expression expr : pExprs)
            expressions.add(expr);
    }

    /**
     * Adds all of the expressions in <code>pExprs</code> to {@link #expressions}.
     * @param pExprs    A variable amount of Strings that will be added to {@link #expressions}.
     */    
    public void add(String... pStrs) {
        for(String str : pStrs) {
            if(str.split("=").length != 1) {
                for(String strSpl : str.split("=")) {
                    expressions.add(new Expression(strSpl));
                }
            } else {
                expressions.add(new Expression(str));
            }
        }
    }

    /**
     * Returns the {@link #expressions} that this class defines.
     * @return {@link #expressions}
     */
    public ArrayList<Expression> expressions() {
        return expressions;
    }

    @Override
    public String toString() {
        if(expressions == null)
            return "Null Expressions";
        if(expressions.size() == 0)
            return "Empty Expressions";

        String ret = "";
        for(Expression expr : expressions) {
            ret += expr + " = ";
        }
        return ret.substring(0, ret.length() - 3);
    }

    @Override
    public String toFancyString() {
        if(expressions == null)
            return "Null Expressions";
        if(expressions.size() == 0)
            return "Empty Expressions";
        String ret = "";
        for(Expression expr : expressions) {
            ret += "'" + expr.toFancyString() + "' = ";
        }
        return ret.substring(0, ret.length() - 3);
    }

    @Override
    public String toFullString() {
        throw new NotDefinedException();
    }
}