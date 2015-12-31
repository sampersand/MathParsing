package West.Math.Equation.Function.CustomFunctions;

import West.Math.Equation.EquationSystem;
import West.Math.Set.Node.TokenNode;
import West.Math.Equation.Function.CustomFunction;

import West.Math.Exception.NotDefinedException;

public class f extends CustomFunction{
    public String help() {
        return "Adds up the reciprocal of each argument";
    }
    public String syntax() {
        return "arg1, arg2, ..., N";
    }
    @Override
    public double exec(EquationSystem pEq,
                       TokenNode pNode) throws
                           NotDefinedException,
                           IllegalArgumentException {
        double[] vals = evalNode(pEq, pNode);
        double ret = 0;
        for(double val : vals)
            if(val != 0)
                ret += 1D / val;
        return ret;

    }
}