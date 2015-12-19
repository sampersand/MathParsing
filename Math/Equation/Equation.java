package Math.Equation;

import Math.MathObject;
import Math.Equation.CustomFunction;
import Math.Equation.Expression;
import Math.Exception.NotDefinedException;
import Math.Exception.InvalidArgsException;

import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * A class that represents an equation in Math. It really is just a collection of Expressions that are equal to
 * each other.
 * 
 * @author Sam Westerman
 * @version 0.6
 * @since 0.1
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
        for(String pStr : pStrs) {
            for(String str : splitInputString(pStr)){
                System.out.println("\t" + str);
                expressions.add(new Expression(str));
            }
        }
        System.out.println("\n");
        return this;
    }

    public ArrayList<String> splitInputString(String toSplit){
        if(toSplit.indexOf("=") == -1)
            return new ArrayList<String>(){{add(toSplit);}};
        ArrayList<String> split = new ArrayList<String>(){{add("");}};
        int paren = 0, pos = -1;
        while(++pos < toSplit.length()){
            char c = toSplit.charAt(pos);
            if(c == '(' && paren++ == 0){
                split.add("");
            } else if(c == ')' && --paren == 0){
                split.add("");
            } else{
                split.set(split.size() - 1, split.get(split.size() - 1) + c);
            }
        }
        ArrayList<String> ret = new ArrayList<String>(){{
        add("");}};
        return ret;
    }

    // public ArrayList<String> splitInputString(String toSplit){
    //     if(toSplit.indexOf("=") == -1)
    //         return new ArrayList<String>(){{add(toSplit);}};
    //     ArrayList<String> split = new ArrayList<String>();
    //     int paren = 0, pos = -1;
    //     String toAdd = "";
    //     while(++pos < toSplit.length()){
    //         char c = toSplit.charAt(pos);
    //         if(c == '(' && paren++ == 0){
    //             split.add(toAdd);
    //             toAdd = "";
    //         }
    //         else if(c == ')' && --paren == 0){
    //             split.add(toAdd);
    //             toAdd = "";
    //         }
    //         else
    //             toAdd += c;
    //     }
    //     if(!toAdd.equals(""))
    //         split.add(toAdd);
    //     System.out.println("--@--");
    //     for(String s : split)
    //         System.out.println(s);
    //     System.out.println("--@--");
    //     return split;
    // }


    /**
     * Returns the {@link #expressions} that this class defines.
     * @return {@link #expressions}
     */
    public ArrayList<Expression> expressions() {
        return expressions;
    }

    /**
     * Gets a string representing the {@link Expression#expression} for each {@link Expression} in {@link #expressions}.
     * @return A string comprised of each expression, with <code> = </code> between each one.
     */
    public String rawExpressions(){
        String ret = "";
        for(Expression expr : expressions)
            ret += expr.expression + " = ";
        return ret.substring(0, ret.length() - (expressions.size() > 0 ? 3 : 0));
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
    public String toFancyString(int idtLvl) {
        String ret = indent(idtLvl) + "Equation:";

        if(expressions == null)
            ret += "\n" + indent(idtLvl + 1) + "Null Expressions";
        else if(expressions.size() == 0)
            ret += "\n" + indent(idtLvl + 1) + "Empty Expressions";
        else
            ret += "\n" + indent(idtLvl + 1);
        for(Expression expr : expressions) {
            ret += "'" + expr.toFancyString() + "' = ";
        }
        return ret.substring(0, ret.length() - (expressions.size() > 0 ? 3 : 0));
    }

    @Override
    public String toFullString(int idtLvl) {
        String ret = indent(idtLvl) + "Equation:\n";
        ret += indent(idtLvl + 1) + "Raw:\n" + indent(idtLvl + 2) + rawExpressions() + "\n";
        ret += indent(idtLvl + 1) + "Expressions";
        for(Expression expr : expressions)
            ret += "\n" + expr.toFullString(idtLvl + 2);
        return ret;
    }

    @Override
    public Equation copy(){
        return new Equation().add(expressions);
    }
}