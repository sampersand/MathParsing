package West.Math.Equation.Function.CustomFunctions;

import West.Math.Equation.EquationSystem;
import West.Math.Set.Node.TokenNode;
import West.Math.Equation.Function.CustomFunction;
import java.util.HashMap;
import West.Math.Operable;
public class f extends CustomFunction{
    public String help() {
        return "Adds up the reciprocal of each argument";
    }
    public String syntax() {
        return "arg1, arg2, ..., N";
    }
    @Override
    public HashMap<String, Operable> exec(HashMap<String, Operable> hm, EquationSystem pEqSys, TokenNode pNode) {
        return super.exec(hm, pEqSys, pNode);
        //  for(West.Math.Set.Node.Node<?, ?> n : pNode)
        //      ret.putAll(((TokenNode)n).eval(ret, pEqSys));
        //  double[] args = (double[])rargs[0];
        //  HashMap<String, Operable> rethm = (HashMap<String, Operable>)rargs[1];
        //  double ret = 0;
        //  for(double val : args)
        //      if(val != 0)
        //          ret += 1d / val;
        //  rethm.put(pNode.token().val(), ret);
        //  return rethm;
    }
}