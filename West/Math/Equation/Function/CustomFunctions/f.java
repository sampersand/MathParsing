package West.Math.Equation.Function.CustomFunctions;

import West.Math.Equation.EquationSystem;
import West.Math.Set.Node.TokenNode;
import West.Math.Equation.Function.CustomFunction;

import West.Math.Exception.NotDefinedException;
import java.util.HashMap;
public class f extends CustomFunction{
    public String help() {
        return "Adds up the reciprocal of each argument";
    }
    public String syntax() {
        return "arg1, arg2, ..., N";
    }
    @Override
    public HashMap<String, Double> exec(EquationSystem pEqSys, TokenNode pNode) {
        return null;
        // Object[] rargs = evalNode(pEqSys, pNode);
        // double[] args = (double[])rargs[0];
        // HashMap<String, Double> rethm = (HashMap<String, Double>)rargs[1];
        // double ret = 0;
        // for(double val : args)
        //     if(val != 0)
        //         ret += 1D / val;
        // rethm.put(pNode.token().val(), ret);
        // return rethm;
    }
}