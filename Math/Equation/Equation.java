package Math.Equation;
import Math.Print;

import Math.Exception.TypeMisMatchException;
import Math.Exception.NotDefinedException;
import Math.Exception.InvalidArgsException;

import Math.Set.Set;
import Math.Display.Grapher;
import java.util.ArrayList;
import java.util.HashMap;

public class Equation {

    public HashMap<String, CustomFunction> functions;
    public ArrayList<Expression[]> expressions;

    public Equation(Object... objs) throws InvalidArgsException{
        expressions = new ArrayList<Expression[]>();
        functions = new HashMap<String, CustomFunction>();
        add(objs);
        // if(expressions.size() == 0)
            // throw new InvalidArgsException("You need an equation to instantiate the equation class!");
    }
    public void add(Object... objs) throws InvalidArgsException{
        int i = 0;
        String prev = "";
        while(i < objs.length){
            Object obj = objs[i];
            if(obj instanceof String){
                String strobj = "" + obj;
                if(strobj.split("=").length != 1){
                    String[] strs = strobj.split("=");
                    Expression[] exprs = new Expression[strs.length];
                    for(int j = 0; j < strs.length; j++)
                        exprs[j] = new Expression(strs[j]);
                    expressions.add(exprs);
                } else if(strobj.substring(0,5).equals("func:")){
                    throw new NotDefinedException("define me");
                } else {
                    prev = strobj;
                }
                /* Ignore us
            } else if(obj instanceof Expression){
                if(!prev.equals(""))
                    expressions.addAll(genExprs(prev, (Expression)obj));
                else{
                    throw new NotDefinedException("define me!");
                }*/
            } else if(obj instanceof Double){
                expressions.add(genExprs(prev, (Double)obj).get(0));
                prev = "";
            } else if(obj instanceof CustomFunction){
                if(!prev.equals(""))
                    functions.put(prev, (CustomFunction)obj);
                else{
                    throw new NotDefinedException("define me!");
                }
                prev = "";
            } else if(obj instanceof Expression[]){
                if(((Expression[])obj).length != 0)      
                    expressions.add((Expression[])obj);
            } else if(obj instanceof CustomFunction){
                functions.put(((CustomFunction) obj).fName, (CustomFunction) obj);
            } else if(obj instanceof Equation){
                expressions.addAll(((Equation) obj).expressions);
                functions.putAll(((Equation) obj).functions);
            } else {
                throw new InvalidArgsException("No known way to add an object of type '" + obj.getClass() + "'!");
            }
            i++;
        }
    }

    public Expression mainEq(){
        if(expressions.size() == 0 || expressions.get(0).length == 0)
            throw new NotDefinedException("Expressions needs to have at least one equation!");
        return expressions.get(0)[0];
    }

    public static double eval(Equation pEq, Node pNode, ArrayList<Expression[]> pExpr, String toSolve)
                              throws NotDefinedException {
        return Factors.eval(pEq, pNode, pExpr, toSolve);
    }
    public double eval(ArrayList<Expression[]> pExpr, String toSolve) throws NotDefinedException {
        return eval(this, mainEq().node, pExpr, toSolve);
    }
    public double eval(Node pNode, ArrayList<Expression[]> pExpr) throws NotDefinedException {
        return eval(this, pNode, pExpr, "");
    }
    public double eval() throws NotDefinedException {
        return eval(mainEq().node, null);
    }
    public double eval(Node pNode) throws NotDefinedException {
        return eval(pNode, null);
    }
    public double eval(String toSolve) throws NotDefinedException {
        return eval(this, mainEq().node, null, toSolve);
    }
    public double eval(ArrayList<Expression[]> pExpr) throws NotDefinedException{
        return eval(mainEq().node, pExpr);
    }

    public static ArrayList<Expression[]> genExprs(Object... pObjs){ 
        ArrayList<Expression[]> ret = new ArrayList<Expression[]>();
        int i = 0;
        while(i < pObjs.length){
            i++;
            if(pObjs[i] instanceof Expression[]){
                ret.add((Expression[])pObjs[i]);
                continue;
            }
            Expression expr1 = pObjs[i] instanceof Expression ? (Expression)pObjs[i] : new Expression(pObjs[i]);
            Expression expr2 = pObjs[i + 1] instanceof Expression ? (Expression)pObjs[i + 1] : new Expression(pObjs[i + 1]);
            ret.add(new Expression[]{expr1, expr2});
            i++;
        }
        return ret;
    }




    public void graph(){
        Print.printi("Currently, solve isn't very good. Oh well.");
        Grapher grapher = new Grapher(this);
        grapher.graph();
    }

    public String toFancyString(){
        String ret = "------=[" + mainEq().expression + "]=------";
        if(expressions.size() > 1){
            ret += "\nExpressions:";
            for(Expression[] exprs : expressions){
                ret += "\n\t";
                for(Expression expr : exprs)
                    ret += expr.expression + " = " ;
                ret = ret.substring(0,ret.length() - (exprs.length > 0 ? 3 : 0));
            }
        }
        if(functions.size() > 0){
            ret += "\nFunctions: (stuff in [] are optional)";
            Object[] keys = functions.keySet().toArray();
            for(int i = 0; i < keys.length; i++){
                String func = (String) keys[i];
                ret += "\n\t" + func + ": \n\t\tHelp   : " + functions.get(func).getHelp()  + 
                                          "\n\t\tSyntax : " + func + "(" + functions.get(func).getSyntax().
                                            replaceAll("\n","\n\t\t\t") + ")";
                if(i != keys.length - 1)
                ret += "\n\t---\t---";
            }
        }
        ret += "\n";
        for(char c : mainEq().expression.toCharArray()) ret += ("-");
        ret += "----------------\n";
        return ret;
    }

    public Equation clone(){
        return new Equation(expressions, functions);
    }
    public int size(){
        return expressions.size();
    }
    public Expression[] get(int pos){ return expressions.get(pos); }
    public String getStr(int pos){
        String ret = "";
        for(Expression expr : get(pos))
            ret += expr.expression + " = ";
        return ret.substring(0, ret.length() - 3);
    }
}

