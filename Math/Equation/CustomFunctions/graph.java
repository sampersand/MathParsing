package Math.Equation.CustomFunctions;
import Math.Set.Set;

import Math.Equation.Function;
import Math.Equation.Factors;
import Math.Equation.Node;
import Math.Equation.CustomFunction;

import Math.Exception.NotDefinedException;
import Math.Exception.InvalidArgsException;


public class graph extends CustomFunction{

    @Override
    public double exec(Factors pFactors, Node pNode) throws NotDefinedException, InvalidArgsException {
        System.out.print(pNode);
        if(pNode.size() == 2){
            double[] vals1 = evalNode(pFactors, pNode.get(0));
            double[] vals2 = evalNode(pFactors, pNode.get(1));
            Set set = new Set(vals1, vals2);
            set.graph();
        }

        // Set set = new Set(vals);
        return 0;

    }
}