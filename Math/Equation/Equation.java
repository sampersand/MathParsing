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
    protected ArrayList<Expression> expressions;

    /**
     * The default constructor. This just instantiates {@link #expressions} as an empty ArrayList.
     */
    public Equation() {
        expressions = new ArrayList<Expression>();
    }


    /**
     * Adds all of the {@link Expression}s as defined in <code>pExprs</code>.
     * @param pExprs    An Arraylist of {@link Expression}s that will be added to {@link #expressions}.
     * @return This class, with <code>pExprs</code> added.
     */
    public Equation add(ArrayList<Expression> pExprs) {
        expressions.addAll(pExprs);
        return this;
    }

    /**
     * Adds all of the {@link Expression}s in <code>pExprs</code> to {@link #expressions}.
     * @param pExprs    A variable amount of {@link Expression}s that will be added to {@link #expressions}.
     * @return This class, with <code>pExprs</code> added.
     */
    public Equation add(Expression... pExprs) {
        for(Expression expr : pExprs)
            expressions.add(expr);
        return this;
    }

    /**
     * Adds new {@link Expression}s, each instatiated by a paramater in <code>pStrs</code>, to {@link #expressions}.
     * @param pStrs    A variable amount of Strings that will be added to {@link #expressions}.
     * @return This class, with <code>pStrs</code> added.
     */    
    public Equation add(String... pStrs) {
        for(String str : pStrs) {
            if(str.split("=").length != 1) {
                for(String strSpl : str.split("=")) {
                    expressions.add(new Expression(strSpl));
                }
            } else {
                expressions.add(new Expression(str));
            }
        }
        return this;
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