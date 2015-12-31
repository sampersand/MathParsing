package West.Math.Equation.Function.CustomFunctions;

import West.Math.Equation.Function.Function;
import West.Math.Equation.EquationSystem;
import West.Math.Set.Node.TokenNode;
import West.Math.Equation.Function.CustomFunction;


import West.Math.Exception.NotDefinedException;

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
    public double exec(EquationSystem pEq,
                       TokenNode pNode) throws
                           NotDefinedException,
                           IllegalArgumentException {
        double[] vals = evalNode(pEq, pNode);
        if(vals.length == 0 || vals.length > 3)
            throw new IllegalArgumentException("ERROR when parsing summation. Syntax: " + syntax());
        if(vals.length == 1) { vals = new double[]{0,vals[0],1};}
        if(vals.length == 2) { vals = new double[]{vals[0],vals[1],1};}
        double ret = 0;
        for(double x = vals[0]; x <= vals [1]; x+= vals[2]) ret += x; 
        return ret;
    }
}