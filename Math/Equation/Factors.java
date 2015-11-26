package Math.Equation;

import Math.Equation.Exception.NotDefinedException;
import Math.Equation.Exception.TypeMisMatchException;
import Math.Equation.Exception.DoesntExistException;

import java.util.HashMap;
import java.util.ArrayList;
/** 
 * A class that keeps track of varriables and functions. Used when evaluating an {@link Node}.
 * @author Sam Westerman
 * @version 0.1
 */
public class Factors {
    /**
     * A HashMap of varriables; The keys are the names, the values are, well, the values of the varriables.
     */
    private HashMap<String, Double> vars;

    /**
     * A HashMap of functions; The keys are the names, and the values are CustomFunction Classes that correspond to the names.
    */
    private HashMap<String, CustomFunction> funcs;

    /** 
     * Default constructor. Just calls the main constructor with empty HashMaps.
     */
    public Factors() {
        this(new HashMap<String, Double>(), new HashMap<String, CustomFunction>());
    }

    /** 
     * The main constructor for Factors.
     * @param pVars     The HashMap of varriable names and their corresponding values.
     * @param pFuncs    The HashMap of function names and their corresponding CustomFunction classes.
     */
    public Factors(HashMap<String, Double> pVars, HashMap<String, CustomFunction> pFuncs) {
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
            if (fNode.TOKEN.TYPE == Token.Types.NUM) {
                return fNode.dVal;
            } else if (fNode.TOKEN.TYPE == Token.Types.VAR) {
                if(pVars.get(fNode.sVal) != null) {
                    return (double)pVars.get(fNode.sVal);
                } else if(!inVar(fNode.sVal)) {
                    switch(fNode.sVal.toLowerCase()) {
                        case "e": return Math.E;
                        case "pi": return Math.PI;
                        case "rand": case "random": return Math.random();
                        default:
                            throw new NotDefinedException("FinalNode '" + fNode.TOKEN.VAL + "' isn't defined in vars!");
                    }
                } else if(fNode.TOKEN.TYPE == Token.Types){
                    return (double)getVar(fNode.sVal);
                }
            } else {
                throw new TypeMisMatchException("FinalNode '" +fNode.sVal + "&" + fNode.dVal + "' isn't a NUM or VAR!");
            }
        } else if(pNode.TOKEN.TYPE == Token.Types.FUNC ) {
            if(inFunc(pNode.TOKEN.VAL)) //if it is a function
                return getFunc(pNode.TOKEN.VAL).exec(this, pNode);
            else {
                try { //if it is a built in
                    return InBuiltFunction.exec(pNode.TOKEN.VAL, this, pNode);
                } catch (NotDefinedException err2) {
                    // try{
                        // return new CustomFunction(pNode.TOKEN.VAL).exec(this,pNode);
                    // } //this isn't working now because of the way instantiating works.
                    // catch(e)
                    throw new NotDefinedException("Function '" + pNode.TOKEN.VAL + "' isn't defined in funcs " + 
                                                  "(and isn't inbuilt either)!");
                }
            }
        }
        else if(pNode.TOKEN.TYPE == Token.Types.GROUP || pNode instanceof FinalNode || pNode.TOKEN.isUni()) {
                return eval(pNode.get(0));
        } else if(pNode.TOKEN.TYPE == Token.Types.OPER) {
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
                    throw new NotDefinedException("Node: '" + pNode.TOKEN.VAL + "' is an OPERATOR, but no known way " +
                                                  "to evaluate it.");
            }
        } else {
            throw new NotDefinedException("Node: '" + pNode.TOKEN.VAL + "' has no known way to evaluate it");
        }
    }

    /** 
     * Puts a {@link #CustomFunction} with the name fName into {@link #funcs} with the key fName.
     * @param fName     The name that will be used as the key and file name for the CustomFunction in {@link #funcs}.
     */
    public void addFunc(String fName){
        addFunc(fName, fName);
    }
    
    /** 
     * Puts <code>0.0D</code> into {@link #funcs} with the key vName.
     * @param vName     The name that will be used as the varriable name.
     */
    public void addVar(String vName){
        addVar(vName, 0);
    }
    
    /** 
     * Puts a {@link #CustomFunction} with the name fName into {@link #funcs} with the key kName.
     * @param kName     The name that will be used as the key.
     * @param fName     The name that will be used for the {@link CustomFunction}.
     */
    public void addFunc(String kName, String fName){
        addFunc(kName, new CustomFunction(fName));
    }
    
    /** 
     * Puts a fFunc into {@link #funcs} with the key kName.
     * @param kName     The name that will be used as the key.
     * @param fName     {@link CustomFunction} that will be used as the key..
     */
    public void addFunc(String kName, CustomFunction fFunc){
        funcs.put(kName, fFunc);
    }


    /** 
     * Puts pVal into {@link #funcs} with the key kName.
     * @param kName     The name that will be used as the key.
     * @param fName     The double that will be used as the value.
     */
    public void addVar(String vName, double pVal){
        vars.put(vName, pVal);
    }

    /** 
     * Calls {@link #addFunc(String)} for each String in fNames - insterting them into {@link #funcs}. The key is 
     * each argument, with the value being a {@link CustomFunction} initiated with said argument.
     */
    public void addFuncs(String[] fNames){
        for(String fName : fNames){
            addFunc(fName);
        }
    }

    public void addVars(String[] fNames){
        for(String fName : fNames){
            addVar(fName);
        }
    }

    public void addFuncs(HashMap<String, CustomFunction> pFuncs){
        funcs.putAll(pFuncs);
    }
    public void addVars(HashMap<String, Double> pFuncs){
        vars.putAll(pFuncs);
    }

    public CustomFunction getFunc(String pKey) throws NullPointerException {
        return funcs.get(pKey);
    }

    public double getVar(String pKey) throws NullPointerException {
        return vars.get(pKey);
    }

    public boolean inVar(String pKey){
        return vars.containsKey(pKey);
    }

    public boolean inFunc(String pKey){
        return funcs.containsKey(pKey);
    }
    public String toString(){
        return "Factor: \n\tVARS: " + vars.toString() + "\n\tFUNCS: " + funcs.toString();
    }

}
