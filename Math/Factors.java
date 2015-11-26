import java.util.HashMap;
/** 
 * A class that keeps track of varriables and functions. Used when evaluating an {@link Node}.
 * @author Sam Westerman
 * @version 0.1
 */
public class Factors {
    /**
     * A HashMap of varriables; The keys are the names, the values are, well, the values of the varriables.
     */
    public HashMap<String, Double> vars;

    /**
     * A HashMap of functions; The keys are the names, and the values are Function Classes that correspond to the names.
    */
    public HashMap<String, Function> funcs;

    /** 
     * Default constructor. Just calls the main constructor with empty HashMaps.
     */
    public Factors() {
        this(new HashMap<String, Double>(),new HashMap<String, Function>());
    }

    /** 
     * The main constructor for Factors.
     * @param pVars     The HashMap of varriable names and their corresponding values.
     * @param pFuncs    The HashMap of function names and their corresponding Function classes.
     */
    public Factors(HashMap<String, Double> pVars, HashMap<String, Function> pFuncs) {
        vars = pVars;
        funcs = pFuncs;
    }

    /** 
     * Overloaded eval constructor. Just passes to the main one with an empty pVars.
     * @param pNode         The node that will be evaluated.     
     * @return A double representing the value of pNode when evaluated with "vars" and "funcs".
     */
    public double eval(Node pNode) throws NotDefinedException {
        return eval(pNode, new HashMap<String, Double>());
    }

    /**
     * Gets a numerical representation of pNode using "vars", "funcs", and pVars.
     * @param pNode         The node that will be evaluated.
     * @param pVars         For ease of use, Instead of having to change vars every time eval will be re-run, pVars can
     *                      be used. When a varriable is encountered, first pVars is consulted, then "vars" is. 
     * @return A double representing the value of pNode when evaluated with "vars", "funcs", and pVars.
     * @throws NotDefinedException      Thrown if either a varriable or a function wans't defined.
     */
    public double eval(Node pNode, HashMap<String, Double> pVars) throws NotDefinedException {
        if (pNode instanceof FinalNode) {
            FinalNode fNode = (FinalNode)pNode;
            if (fNode.TOKEN.TYPE == Token.Types.NUM) { return fNode.dVal; }
            else if (fNode.TOKEN.TYPE == Token.Types.VAR) {
                if(pVars.get(fNode.sVal) != null) {return (double)pVars.get(fNode.sVal); }
                else if(vars.get(fNode.sVal) == null) {
                    switch(fNode.sVal.toLowerCase()) {
                        case "e": return Math.E;
                        case "pi": case "π": return Math.PI;
                        default:
                            throw new NotDefinedException("FinalNode '" + fNode.TOKEN.VAL +
                                "' isn't defined in vars!");
                        }
                } else { return (double)vars.get(fNode.sVal); }
            } else {
                throw new TypeMisMatchException("FinalNode '" +fNode.sVal + "&" + fNode.dVal 
                    + "' isn't a NUM or VAR! WHAT?!?");
            }
        }

        if(pNode.TOKEN.TYPE == Token.Types.FUNC ) {
            try{
                return funcs.get(pNode.TOKEN.VAL).exec(this, pNode);
            } catch (NullPointerException err) {
                try {
                    return new Function(pNode.TOKEN.VAL).exec(this, pNode);
                } catch (NullPointerException err2) {
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