package Math.Equation.CustomFunctions;

import Math.Equation.Function;
import Math.Equation.Equation;
import Math.Equation.Node;
import Math.Equation.CustomFunction;


import Math.Exception.InvalidArgsException;
import Math.Exception.NotDefinedException;

public class summation extends CustomFunction{
    public static String help(){
        return "Adds up numbers from Start to END, with STEP step. Can only be END, START + END, or START + END + STEP";
    }
    public static String syntax(){
        return "[START], END, [STEP]";
    }

    /**
     * Summation from START to END, with step STEP
     * Params: ([START],END,[STEP])
     * if START is omitted, then 0 is used in its place
     * if STEP is omitted, 1 is used in its place
     * @param pFactors      The factors
     * @return The summation of the numbers defined by pNode.
     */
    @Override
    public double exec(Equation eq, Node pNode) throws NotDefinedException, InvalidArgsException {
        double[] vals = evalNodes(eq, pNode);
        if(vals.length == 0 || vals.length > 3)
            throw new InvalidArgsException("ERROR when parsing summation. Syntax: " + syntax());
        if(vals.length == 1) { vals = new double[]{0,vals[0],1};}
        if(vals.length == 2) { vals = new double[]{vals[0],vals[1],1};}
        double ret = 0;
        for(double x = vals[0]; x <= vals [1]; x+= vals[2]) ret += x; 
        return ret;
    }
}