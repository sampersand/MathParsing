package Math.Equation;

import Math.MathObject;
import Math.Print;
import Math.Equation.Token.Type;
import Math.Exception.NotDefinedException;
import Math.Exception.InvalidArgsException;

import java.util.HashMap;
import java.util.Random;

public class OperatorFunction extends InBuiltFunction {

    public OperatorFunction() {
        this(null, null, null);
    }

    public OperatorFunction(String pVal, String pHelp, String pSyntax) {
        super(pVal, pHelp, pSyntax);
    }

    @Override
    public double exec(EquationSystem pEq, Node pNode) throws NotDefinedException, InvalidArgsException {
        if(pNode.subNodes().size() == 0)
            throw new InvalidArgsException("Node size cannot be 0!");
        double ret = pNode.get(0).eval(pEq);
        switch(name) {
            case "+":
                for(int i = 1; i < pNode.subNodes().size(); i++){
                    ret += pNode.get(i).eval(pEq);
                }
                break;
            case "-":
                for(int i = 1; i < pNode.subNodes().size(); i++){
                    ret -= pNode.get(i).eval(pEq);
                }
                break;
            case "*":
                for(int i = 1; i < pNode.subNodes().size(); i++){
                    ret *= pNode.get(i).eval(pEq);
                }
                break;
            case "/":
                for(int i = 1; i < pNode.subNodes().size(); i++){
                    ret /= pNode.get(i).eval(pEq);
                }
                break;
            case "^":
                for(int i = 1; i < pNode.subNodes().size(); i++){
                    ret = Math.pow(ret, pNode.get(1).eval(pEq)); // not sure this works
                }
                break;
            default:
                throw new NotDefinedException("OperatorFunction " + this + " doesnt have a defined way to compute it!");
        }
        return ret;
    }
    @Override
    public String toString() {
        return "OperatorFunction: '" + name + "'";
    }

    @Override
    public String toFancyString() {
        throw new NotDefinedException();
    }

    @Override
    public String toFullString() {
        throw new NotDefinedException();
    }

}