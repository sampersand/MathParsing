package Math.Equation;

import Math.Equation.Exception.NotDefinedException;
import Math.Equation.Exception.TypeMisMatchException;
import Math.Equation.Exception.DoesntExistException;

import java.util.HashMap;
import java.util.ArrayList;
/**
 * A class that keeps track of variables and functions. Used when evaluating an {@link Node}.
 * @author Sam Westerman
 * @version 0.1
 */
public class Factors {

    /**
     * A HashMap of variables; The keys are the names, the values are, well, the values of the variables.
     */
    private HashMap<String, Double> vars;

    /**
     * A HashMap of functions; The keys are the names, and the values are CustomFunction Classes that correspond to the names.
    */
    private HashMap<String, CustomFunction> funcs;

    /**
     * Default constructor. Just calls the {@link #Factors(HashMap,HashMap) main constructor} with empty HashMaps.
     */
    public Factors() {
        this(new HashMap<String, Double>(), new HashMap<String, CustomFunction>());
    }

    /**
     * The main constructor for Factors.
     * @param pVars     The HashMap of variable names and their corresponding values.
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
     *                      be used. When a variable is encountered, first pVars is consulted, then "vars" is. 
     * @return A double representing the value of pNode when evaluated with "vars", "funcs", and pVars.
     * @throws NotDefinedException      Thrown if either a variable or a function wans't defined.
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
                } else {
                    return (double)getVar(fNode.sVal);
                }
            } else if(fNode.TOKEN.TYPE == Token.Types.ARGS){
                System.err.println("[WARNING] Attempting to evaluate args! probably won't go well :P");
                return (double)getVar(fNode.sVal);
            } else {
                throw new TypeMisMatchException("FinalNode '" +fNode.sVal + "&" + fNode.dVal +
                                                "' isn't a NUM, VAR, OR ARGS!");
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
     * Puts a {@link CustomFunction} with the name funcName into {@link #funcs} with the key funcName.
     * @param funcName      The function name that will be used for the key, and file name for the CustomFunction.
     */
    public void addFunc(String funcName){
        addFunc(funcName, funcName);
    }
    
    /**
     * Puts <code>0.0D</code> into {@link #vars} with the key varName.
     * @param varName       The variable name that will be used for the key.
     */
    public void addVar(String varName){
        addVar(varName, 0);
    }
    
    /**
     * Puts a {@link CustomFunction} with the name fileName into {@link #funcs} with the key funcName.
     * @param funcName      The function name that will be used for the key.
     * @param fileName      The name of the file for the {@link CustomFunction}.
     */
    public void addFunc(String funcName, String fileName){
        addFunc(funcName, new CustomFunction(fileName));
    }
    
    /**
     * Puts pFunc into {@link #funcs} with the key funcName.
     * @param funcName      The function name that will be used for the key.
     * @param pFunc         {@link CustomFunction} that will be used as the value.
     */
    public void addFunc(String funcName, CustomFunction pFunc){
        funcs.put(funcName, pFunc);
    }


    /**
     * Puts pVal into {@link #vars} with the key varName.
     * @param varName       The variable name that will be used for the key.
     * @param pVal          The double that will be used for the value.
     */
    public void addVar(String varName, double pVal){
        vars.put(varName, pVal);
    }

    /**
     * Calls {@link #addFunc(String)} for each String in funcNames - insterting them into {@link #funcs}. The key is 
     * each argument, with the value being a {@link CustomFunction} initiated with said argument.
     * @param funcNames     An array of each function's name.
     */
    public void addFuncs(String[] funcNames){
        for(String funcName : funcNames){
            addFunc(funcName);
        }
    }

    /**
     * Calls {@link #addVar(String)} for each String in varNames - insterting them into {@link #vars}. The key is 
     * each argument, with the value being <code>0.0D</code>.
     * @param varNames     An array of each function's name.
     */
    public void addVars(String[] varNames){
        for(String fName : varNames){
            addVar(fName);
        }
    }

    /**
     * Adds each key-value pair from pFuncs into {@link #funcs}.
     * @param pFuncs        The HashMap containing the name-function pairs. The key is a function name, while the 
     *                      value is a {@link CustomFunction}.
     */
    public void addFuncs(HashMap<String, CustomFunction> pFuncs){
        funcs.putAll(pFuncs);
    }

    /**
     * Adds each key-value pair from pVars into {@link #vars}.
     * @param pVars        The HashMap containing the name-value pairs. The key is a variable name, while the 
     *                     value is a double.
     */
    public void addVars(HashMap<String, Double> pVars){
        vars.putAll(pVars);
    }

    /**
     * Returns the {@link CustomFunction} value of funcName as defined in {@link #funcs}.
     * @param funcName      The name of the function in {@link #funcs}.
     * @return A {@link CustomFunction} if funcName exists in {@link #funcs}. If it doesn't, it will return null.
     */
    public CustomFunction getFunc(String funcName){
        return funcs.get(funcName);
    }

    /**
     * Returns the double value of varName, as defined is {@link #vars}
     * @param varName       The name of the variable in {@link #vars}.
     * @return A double if varName exists in {@link #funcs}. If it doesn't, it will return null (null isnt tested)
     */
    public double getVar(String varName){
        return vars.get(varName);
    }

    /** 
     * Checks and sees if funcName is an actual function inside {@link #funcs}.
     * @param funcName      The name of the function to check its existance.
     * @return True if funcName has a value in {@link #funcs}.
     */
    public boolean inFunc(String funcName){
        return funcs.containsKey(funcName);
    }

    /** 
     * Checks and sees if varName is an actual variable inside {@link #vars}.
     * @param varName      The name of the variable to check its existance.
     * @return True if varName has a value in {@link #vars}.
     */
    public boolean inVar(String varName){
        return vars.containsKey(varName);
    }

    /** 
     * Just returns the vars in string format, and the funcs in string format, with a little prettier formatting.
     * @return A string representation of this factor.
     */
    public String toString(){
        return "Factor: \n\tVARS: " + vars.toString() + "\n\tFUNCS: " + funcs.toString();
    }

}
