package Math.Equation;
import Math.Exception.*;
import Math.Equation.CustomFunction;
import Math.Equation.Expression;
import java.util.ArrayList;
public class Equation{
    private ArrayList<Expression> expressions;
    public Equation(){
        expressions = new ArrayList<Expression>();
    }
    public Equation(Expression... pExprs) throws InvalidArgsException{
        expressions = new ArrayList<Expression>(){{for(Expression expr : pExprs) add(expr);}};
    }
    public Equation(ArrayList<Expression> pExprs) throws InvalidArgsException{
        expressions = pExprs;
    }
    public Equation(String... pStr){
        expressions = new ArrayList<Expression>();
        for(String str : pStr){
            if(str.split("=").length != 1){
                for(String strSpl : str.split("=")){
                    expressions.add(new Expression(strSpl));
                }
            } else {
                expressions.add(new Expression(str));
            }
        }
    }
    public void add(Expression... pExpr){
        for(Expression expr : pExpr)
            expressions.add(expr);
    }
    public ArrayList<Expression> expressions(){
        return expressions;
    }

    public String toFancyString(){
        if(expressions == null)
            return "Null Expressions";
        if(expressions.size() == 0)
            return "Empty Expressions";
        String ret = "";
        for(Expression expr : expressions){
            ret += "'" + expr.toFancyString() + "' = ";
        }
        return ret.substring(0, ret.length() - 3);
    }

    public String toString(){
        if(expressions == null)
            return "Null Expressions";
        if(expressions.size() == 0)
            return "Empty Expressions";

        String ret = "";
        for(Expression expr : expressions){
            ret += expr + " = ";
        }
        return ret.substring(0, ret.length() - 3);
    }
}