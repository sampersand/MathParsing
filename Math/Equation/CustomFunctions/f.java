package Math.Equation.CustomFunctions;

import Math.Equation.Function;
import Math.Equation.Factors;
import Math.Equation.Node;
import Math.Equation.Token;
import Math.Equation.CustomFunction;

import Math.Exception.NotDefinedException;
import Math.Exception.InvalidArgsException;

public class f extends CustomFunction{
    public static String help(){
        return "fhelp";
    }
    public static String syntax(){
        return "fsyntax";
    }
    @Override
    public double exec(Factors pFactors, Node pNode) throws NotDefinedException, InvalidArgsException {
        if(pNode.get(0).type() == Token.Types.ARGS);
        double[] vals = evalNode(pFactors, pNode);
        double ret = 0;
        for(double val : vals)
            if(val != 0)
                ret += 1D / val;
        return ret;

    }
}