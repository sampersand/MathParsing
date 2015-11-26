package Math.Equation.CustomFunctions;
import Math.Set.Set;

import Math.Equation.Function;
import Math.Equation.Factors;
import Math.Equation.Node;
import Math.Equation.CustomFunction;

import Math.Equation.Exception.NotDefinedException;
import Math.Equation.Exception.InvalidArgsException;


public class graph extends CustomFunction{

    @Override
    public double exec(Factors pFactors, Node pNode) throws NotDefinedException, InvalidArgsException {
        if(pNode.size() == 2){
            double[] vals1 = evalNode(pFactors, pNode.get(0));
            double[] vals2 = evalNode(pFactors, pNode.get(1));
            Set set = new Set(vals1, vals2);
            set.graph();
            // java -cp bin Math.Equation.Equation
        }

        // Set set = new Set(vals);
        return 0;

    }
}