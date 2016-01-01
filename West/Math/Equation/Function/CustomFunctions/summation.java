package West.Math.Equation.Function.CustomFunctions;

import West.Math.Equation.Function.Function;
import West.Math.Equation.EquationSystem;
import West.Math.Set.Node.TokenNode;
import West.Math.Equation.Function.CustomFunction;


import West.Math.Exception.NotDefinedException;
import java.util.HashMap;
public class summation extends CustomFunction{
    public String help() {
        return "Adds up numbers from Start to END, with STEP step. Can only be END, START + END, or START + END + STEP";
    }
    public String syntax() {
        return "[START], END, [STEP]";
    }

    /**
     * Summation from START to END, with step STEP
     * Params: ([START],END,[STEP])
     * if START is omitted, then 0 is used in its place
     * if STEP is omitted, 1 is used in its place
     * @param pEq      The EquationSystem
     * @param pNode     The Node
     * @return The summation of the numbers defined by pNode.
     */
    @Override
    public HashMap<String, Double> exec(EquationSystem pEqSys, TokenNode pNode) {
        return null;
        // Object[] rargs = evalNode(pEqSys, pNode);
        // double[] args = (double[])rargs[0];
        // HashMap<String, Double> rethm = (HashMap<String, Double>)rargs[1];
        // if(args.length == 0 || args.length > 3)
        //     throw new IllegalArgumentException("ERROR when parsing summation. Syntax: " + syntax());
        // if(args.length == 1) { args = new double[]{0,args[0],1};}
        // if(args.length == 2) { args = new double[]{args[0],args[1],1};}
        // double ret = 0;
        // for(double x = args[0]; x <= args[1]; x+= args[2]) ret += x; 
        // rethm.put(pNode.token().val(), ret);
        // return rethm;
    }
}