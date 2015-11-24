import java.util.HashMap;
public class Factors {
    public HashMap<String, Double> vars;
    public HashMap<String, Function> funcs;
    public Factors() {
        this(new HashMap<String, Double>(),new HashMap<String, Function>());
    }
    public Factors(HashMap<String, Double> pVars, HashMap<String, Function> pFuncs) {
        vars = pVars;
        funcs = pFuncs;
    }
    public void setVars(HashMap<String, Double> pVars) {
        vars = pVars;
    }
    public void setFuncs(HashMap<String, Function> pFuncs) {
        funcs = pFuncs;
    }    
    public double eval(Node pNode) throws DoesntExistException, NotDefinedException,
            TypeMisMatchException, ClassNotFoundException, NoSuchMethodException,
            java.lang.reflect.InvocationTargetException, IllegalAccessException {
        return eval(pNode, new HashMap<String, Double>());
    }
    /** pVars allow for overwriting parameters. */
    public double eval(Node pNode, HashMap<String, Double> pVars)
        throws DoesntExistException, NotDefinedException, TypeMisMatchException,
            ClassNotFoundException, NoSuchMethodException, java.lang.reflect.InvocationTargetException,
            IllegalAccessException {
        if (pNode instanceof FinalNode) {
            FinalNode fNode = (FinalNode)pNode;
            if (fNode.TOKEN.TYPE == Token.Types.NUM) { return fNode.dVal; }
            else if (fNode.TOKEN.TYPE == Token.Types.VAR) {
                if(pVars.get(fNode.sVal) != null) {return (double)pVars.get(fNode.sVal); }
                else if(vars.get(fNode.sVal) == null) {
                    switch(fNode.sVal.toLowerCase()){
                        case "e": return Math.E;
                        case "pi": case "π": return Math.PI;
                        default:
                            throw new NotDefinedException("FinalNode '" + fNode.TOKEN.VAL +
                                "' isn't defined in vars!");
                        }
                } else { return (double)vars.get(fNode.sVal); }
            } else {
                throw new TypeMisMatchException("FinalNode '" +fNode.sVal + "&" + fNode.dVal 
                    + "' isn't a NUM or VAR!");
            }
        }

        if(pNode.TOKEN.TYPE == Token.Types.FUNC ) {
            try{
                return funcs.get(pNode.TOKEN.VAL).exec(this, pNode);
            } catch (NullPointerException err){
                try {
                    return new Function(pNode.TOKEN.VAL).exec(this, pNode);
                } catch (NullPointerException err2){
                    throw new NotDefinedException("Function '" + pNode.TOKEN.VAL +
                        "' isn't defined in functions!");
                }
            }
        }
        else if(pNode.TOKEN.TYPE == Token.Types.GROUP || pNode instanceof FinalNode ||
               pNode.TOKEN.isUni()) { return eval(pNode.get(0)); }
        else if(pNode.TOKEN.TYPE == Token.Types.OPER) {
            switch(pNode.TOKEN.VAL) {
                case "+":
                    return eval(pNode.get(0)) + eval(pNode.get(1));
                case "-":
                    return eval(pNode.get(0)) - eval(pNode.get(1));
                case "*":
                    return eval(pNode.get(0)) * eval(pNode.get(1));
                case "/":
                    return eval(pNode.get(0)) / eval(pNode.get(1));
                case "^":
                    return Math.pow(eval(pNode.get(0)), eval(pNode.get(1))); // not sure this works
                default:
                    throw new NotDefinedException("Node: '" + pNode.TOKEN.VAL + 
                        "' is an OPERATOR, but no known way to evaluate it.");
            }
        }
        else {
            throw new NotDefinedException("Node: '" + pNode.TOKEN.VAL + 
                "' has no known way to evaluate it");
        }
    }        
}