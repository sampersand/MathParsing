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
    public ArrayList<Expression[] > equations;
    // public Expression mainEquation; //like 'y = mx + b', not 'mx + b'

    public Equation(Object... objs) throws InvalidArgsException{
        equations = new ArrayList<Expression[]>();
        functions = new HashMap<String, CustomFunction>();
        for(Object obj : objs){
            add(obj);
        }
        if(equations.size() == 0)
            throw new InvalidArgsException("You need an equation to instantiate the equation class!");
    }
    public void add(Object obj) throws InvalidArgsException{
        if(obj instanceof String){
            String strobj = "" + obj;
            if(((String)obj).split("=").length == 1)
                strobj = "y = " + obj;
            String[] strs = strobj.split("=");
            Expression[] exprs = new Expression[strs.length];
            for(int i = 0; i < strs.length; i++)
                exprs[i] = new Expression(strs[i]);
            equations.add(exprs);
        } else if(obj instanceof Expression){
            equations.add(new Expression[]{(Expression) obj});
        } else if(obj instanceof Expression[]){
            equations.add((Expression[])obj);
        } else if(obj instanceof CustomFunction){
            functions.put(((CustomFunction) obj).fName, (CustomFunction) obj);
        } else if(obj instanceof Equation){
            equations.addAll(((Equation) obj).equations);
            functions.putAll(((Equation) obj).functions);
        } else {
            throw new InvalidArgsException("No known way to add an object of type '" + obj.getClass() + "'!");
        }
    }

    public static double eval(Equation eq, Node pNode, ArrayList<Expression[]> pExpr) throws NotDefinedException {
        return eq.eval(pNode, pExpr);
    }

    public double eval() throws NotDefinedException {
        return eval(equations.get(0)[0].node); //gets the first node.
    }

    public double eval(Node pNode) throws NotDefinedException {
        return eval(pNode, null);
    }
    public double eval(ArrayList<Expression[]> pExpr) throws NotDefinedException {
        return eval(equations.get(0)[0].node, pExpr);
    }
    public double eval(Node pNode, ArrayList<Expression[]> pExpr) throws NotDefinedException {
        return 0;
        // return eval(node, pExpr);
    }
    public double eval(String str, ArrayList<Expression[]> pExpr) throws NotDefinedException {
        return 0;
        // return eval(node, pExpr);
    }






    public void graph(){
        Print.printi("Currently, solve isn't very good. Oh well.");
        Grapher grapher = new Grapher(this);
        grapher.graph();
    }

    public String toFancyString(){
        String ret = "------=[" + equations.get(0)[0].equation + "]=------";
        if(equations.size() > 1){
            ret += "\nEquations:";
            for(Expression[] exprs : equations){
                ret += "\n\t";
                for(Expression expr : exprs)
                    ret += expr.equation + " = " ;
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
        for(char c : equations.get(0)[0].equation.toCharArray()) ret += ("-");
        ret += "----------------\n";
        return ret;
    }

    public Equation clone(){
        return new Equation(equations, functions);
    }

}

    // public static void main(String[] args) throws NotDefinedException, TypeMisMatchException {
    //     Expression eq;
    //     if(args.length == 0){
    //         // eq = new Expression("@graph('sinx', 'cosx', 'tanx')");
    //         eq = new Expression("@graph('eq:x')");
    //         // eq = new Expression("@graph('eqandset:sinx,25, -3.1415, 3.1415','eqresid:cosx,25, -3.1415, 3.1415')");
    //         // eq = new Expression("@graph('eqandset:sinx,25, -3.1415, 3.1415','eqtoset:sinx,50')");
    //         // eq = new Expression("@graph('tan(f(y))','(y-2)*(y+2)*y') ");
    //         // eq = new Expression("sin(x+f('e,2,C')*f(4,f(D,3,pi)))");
    //         eq.functions = eq.functions.addVars(new String[]{"x:10","C:3","D:4"});
    //         eq.functions = eq.functions.addFuncs(new String[]{"f","graph","sum:summation"});
    //     } else {
    //         eq = new Expression();
    //         if(args.length == 1){
    //             eq = new Expression(args[0]);
    //         } else if(args.length > 1){
    //             int i = -1;
    //             char type = ' ';
    //             if(!args[0].equals("--f") && !args[0].equals("--v") && !args[0].equals("--e"))
    //                 throw new NotDefinedException("first value has to be --f, --v, or --e");
    //             while(i < args.length - 1){ //args.length is String.
    //                 i++;
    //                 if(args[i].equals("--v")){type = 'v'; continue;}
    //                 if(args[i].equals("--f")){type = 'f'; continue;}
    //                 if(args[i].equals("--e")){type = 'e'; continue;}
    //                 if(type == 'v'){
    //                     try{
    //                         eq.functions = eq.functions.addVar(args[i].split(":")[0],
    //                             Double.parseDouble(args[i].split(":")[1]));
    //                     } catch(NumberFormatException err){
    //                         Print.printw("Syntax: VARNAME:VARVAL (" + args[i] + ")");
    //                     } catch(ArrayIndexOutOfBoundsException err){
    //                         Print.printw("Syntax: VARNAME:VARVAL (" + args[i] + ")");
    //                     }
    //                 } else if (type == 'f'){
    //                     try{
    //                         eq.functions = eq.functions.addFunc(args[i].split(":")[0], args[i].split(":")[1]);
    //                     } catch(NumberFormatException err){
    //                         Print.printw("Syntax: FUNCNAME:FUNCVAL (" + args[i] + ")");
    //                     } catch(ArrayIndexOutOfBoundsException err){
    //                         Print.printw("Syntax: FUNCNAME:FUNCVAL (" + args[i] + ")");
    //                     }
    //                 } else if (type == 'e'){
    //                     eq.Expression += args[i];
    //                 }
    //             }
    //             eq.genNode();
    //         }
    //     }
    //     Print.print(eq);
    //     Print.printi("RESULT: ",eq.solve());
    // }

