package Math.Equation.CustomFunctions;
import Math.Set.Set;
import java.util.ArrayList;

import Math.Equation.Equation;
import Math.Equation.Function;
import Math.Equation.Factors;
import Math.Equation.Node;
import Math.Equation.CustomFunction;
import Math.Equation.Token.Types;
import Math.Exception.NotDefinedException;
import Math.Exception.InvalidArgsException;


public class graph extends CustomFunction{

    public static String help(){
        return "Graphs the two arguments, each of which should result in a double (like a function with outputs)";
    }
    public static String syntax(){
        return "'@matr','matr1','matr2' OR 'eq1','eq2', ... 'eqN'";
    }

    @Override
    public double exec(Factors pFactors, Node pNode) throws NotDefinedException, InvalidArgsException {
        if(pNode.size() == 0)
            throw new InvalidArgsException("the size has to be greater than 1!");
        if(pNode.get(0).token.VAL.equals("@matr")){
            double[] vals1, vals2;
            if(pNode.get(1).type() == Types.ARGS){
                String[] vals = pNode.get(1).token.VAL.split(",");
                vals1 = new double[vals.length];
                for(int i = 0; i < vals.length; i++){
                    vals1[i] = Double.parseDouble(vals[i]);
                }
            } else{
                vals1 = evalNode(pFactors, pNode.get(1));
            }

            if(pNode.get(2).type() == Types.ARGS){
                String[] vals = pNode.get(2).token.VAL.split(",");
                vals2 = new double[vals.length];
                for(int i = 0; i < vals.length; i++){
                    vals2[i] = Double.parseDouble(vals[i]);
                }
            } else{
                vals2 = evalNode(pFactors, pNode.get(2));
            }

            Set set = new Set(vals1, vals2);
            set.graph();
        } else {
            ArrayList<Equation> equations = new ArrayList<Equation>();
            for(int i = 0; i < pNode.size(); i++){
                if(pNode.get(i).type() != Types.ARGS)
                    throw new InvalidArgsException("uh oh, you need to have each argument to be of type ARG (" + i +
                                                   ") isn't");
            equations.add(new Equation(pNode.get(i).token.VAL, pFactors));
            }
            Set.graph(equations);
        }
        return 0;

    }
}