package Math.Equation.CustomFunctions;

import Math.Equation.Function;
import Math.Equation.Factors;
import Math.Equation.Node;
import Math.Equation.CustomFunction;

import Math.Equation.Exception.NotDefinedException;
import Math.Equation.Exception.InvalidArgsException;

public class f extends CustomFunction{
    public static String HELP1 = "f(a, b, ... n) = 1/a + 1/b + ... + 1/n";
    public static String SYNTAX2 = "f(a, b, ... n) such that a, b, ..., n are all numbers or variables.";

    @Override
    public String toString(){
        return "CustomFunction: '" + fName + "'\nHELP: " + HELP1 + "\nSYNTAX: " + SYNTAX2;
    }

    @Override
    public double exec(Factors pFactors, Node pNode) throws NotDefinedException, InvalidArgsException {
        for(Node n : pNode.subNodes)
            System.out.println(n);
        double[] vals = evalNode(pFactors, pNode);
        double ret = 0;
        for(double val : vals)
            if(val != 0)
                ret += 1D / val;
        return ret;

    }
}