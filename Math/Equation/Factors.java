// package Math.Equation;
// import Math.Print;

// import Math.Exception.NotDefinedException;
// import Math.Exception.TypeMisMatchException;
// import Math.Exception.DoesntExistException;

// import java.util.HashMap;
// import java.util.ArrayList;
// /**
//  * A class that keeps track of variables and functions. Used when evaluating an {@link Node}.
//  * @author Sam Westerman
//  * @version 0.1
//  */
// public class Factors {

//     /**
//      * A HashMap of variables; The keys are the names, the values are, well, the values of the variables.
//      */
//     // public HashMap<String, Double> vars;

//     /**
//      * A HashMap of functions; The keys are the names, and the values are CustomFunction Classes that correspond to the names.
//     */
//     // public HashMap<String, CustomFunction> funcs;

//     /**
//      * Default constructor. Just calls the {@link #Factors(HashMap,HashMap) main constructor} with empty HashMaps.
//      */
//     // public Factors() {
//     //     this(new HashMap<String, Double>(), new HashMap<String, CustomFunction>());
//     // }

//     /**
//      * A constructor for Factors that just takes varriables and no functions.
//      * @param pVars         The HashMap of variable names and their corresponding values.
//      */
//     // public Factors(HashMap<String, Double> pVars) {
//     //     this(pVars, new HashMap<String, CustomFunction>());
//     // }

//     /**
//      * The main constructor for Factors.
//      * @param pVars         The HashMap of variable names and their corresponding values.
//      * @param pFuncs        The HashMap of function names and their corresponding CustomFunction classes.
//      */
//     // public Factors(HashMap<String, Double> pVars, HashMap<String, CustomFunction> pFuncs) {
//     //     vars = pVars;
//     //     funcs = pFuncs;
//     // }

//     /**
//      * Overloaded eval constructor. Just passes to the main one with an empty pVars.
//      * @param pNode         The node that will be evaluated.     
//      * @return A double representing the value of pNode when evaluated with "vars" and "funcs".
//      */
//     // public double eval(Node pNode) throws NotDefinedException {
//     //     return eval(pNode, new HashMap<String, Double>());
//     // }

//     /**
//      * Gets a numerical representation of pNode using "vars", "funcs", and pVars.
//      * @param pNode         The node that will be evaluated.
//      * @param pVars         For ease of use, Instead of having to change vars every time eval will be re-run, pVars can
//      *                      be used. When a variable is encountered, first pVars is consulted, then "vars" is. 
//      * @return A double representing the value of pNode when evaluated with "vars", "funcs", and pVars.
//      * @throws NotDefinedException      Thrown if either a variable or a function wans't defined.
//      */
//     public static double eval(EquationSystem pEq, Node pNode, ArrayList<Expression[]> pExprs, String toSolve)
//                               throws NotDefinedException {
//         HashMap<String, Double> vars = new HashMap<String, Double>();
//         for(Expression[] exprs : pExprs) {
//             for(Expression expr: exprs) {
                
//             }
//         }
//     }
//     public static double eval(Node pNode, HashMap<String, Double> pVars) throws NotDefinedException {
//         if (pNode instanceof FinalNode) {
//             FinalNode fNode = (FinalNode)pNode;
//             if (fNode.token().type() == Token.Type.NUM) {
//                 return fNode.sVal;
//             } else if (fNode.token().type() == Token.Type.VAR) {
//                 if(pVars.get(fNode.sVal) != null) {
//                     return (double)pVars.get(fNode.sVal);
//                 } else if(!inVar(fNode.sVal)) {
//                     switch(fNode.sVal.toLowerCase()) {
//                         case "e": return Math.E;
//                         case "pi": return Math.PI;
//                         case "rand": case "random": return Math.random();
//                         default:
//                             throw new NotDefinedException("FinalNode '" + fNode.token().val() + "' isn't defined in vars!");
//                     }
//                 } else {
//                     return (double)getVar(fNode.sVal);
//                 }
//             } else if(fNode.token().type() == Token.Type.ARGS) {
//                 Print.printw("[WARNING] Attempting to evaluate args! probably won't go well :P");
//                 return (double)getVar(fNode.dVal);
//             } else {
//                 throw new TypeMisMatchException("FinalNode '" +fNode.dVal + "&" + fNode.dVal +
//                                                 "' isn't a NUM, VAR, OR ARGS!");
//             }
//         } else if(pNode.token().type() == Token.Type.FUNC ) {
//             if(inFunc(pNode.token().val())) //if it is a function
//                 return getFunc(pNode.token().val()).exec(this.addVars(pVars), pNode);
//             else {
//                 try { //if it is a built in
//                     return InBuiltFunction.exec(pNode.token().val(), this.addVars(pVars), pNode);
//                 } catch (NotDefinedException err2) {
//                     // try{
//                         // return new CustomFunction(pNode.token().val()).exec(this,pNode);
//                     // } //this isn't working now because of the way instantiating works.
//                     // catch(e)
//                     throw new NotDefinedException("Function '" + pNode.token().val() + "' isn't defined in funcs " + 
//                                                   "(and isn't inbuilt either), or one of the vars isn't defined!");
//                 }
//             }
//         }
//         else if(pNode.token().type() == Token.Type.GROUP || pNode instanceof FinalNode || pNode.token.isUni()) {
//                 return eval(pNode.get(0), pVars);
//         } else if(pNode.token().type() == Token.Type.OPER) {
//             switch(pNode.token().val()) {
//                 case "+":
//                     return eval(pNode.get(0), pVars) + eval(pNode.get(1), pVars);
//                 case "-":
//                     return eval(pNode.get(0), pVars) - eval(pNode.get(1), pVars);
//                 case "*":
//                     return eval(pNode.get(0), pVars) * eval(pNode.get(1), pVars);
//                 case "/":
//                     return eval(pNode.get(0), pVars) / eval(pNode.get(1), pVars);
//                 case "^":
//                     return Math.pow(eval(pNode.get(0), pVars), eval(pNode.get(1), pVars)); // not sure this works
//                 default:
//                     throw new NotDefinedException("Node: '" + pNode.token().val() + "' is an OPERATOR, but no known way " +
//                                                   "to evaluate it.");
//             }
//         } else {
//             throw new NotDefinedException("Node: '" + pNode.token().val() + "' has no known way to evaluate it");
//         }
//     }

//     // /**
//     //  * Puts a {@link CustomFunction} with the name funcName into {@link #funcs} with the key funcName.
//     //  * @param funcName      The function name that will be used for the key, and file name for the CustomFunction.
//     //  * @return The updated Factor class.
//     //  */
//     // public Factors addFunc(String funcName) {
//     //     return addFunc(funcName, funcName);
//     // }
    
//     // /**
//     //  * Puts <code>0.0D</code> into {@link #vars} with the key varName.
//     //  * @param varName       The variable name that will be used for the key.
//     //  * @return The updated Factor class.
//     //  */
//     // public Factors addVar(String varName) {
//     //     return addVar(varName, 0);
//     // }
    
//     // /**
//     //  * Puts a {@link CustomFunction} with the name fileName into {@link #funcs} with the key funcName.
//     //  * @param funcName      The function name that will be used for the key.
//     //  * @param fileName      The name of the file for the {@link CustomFunction}.
//     //  * @return The updated Factor class.
//     //  */
//     // public Factors addFunc(String funcName, String fileName) {
//     //     return addFunc(funcName, new CustomFunction(fileName));
//     // }
    
//     // /**
//     //  * Puts pFunc into {@link #funcs} with the key funcName.
//     //  * @param funcName      The function name that will be used for the key.
//     //  * @param pFunc         {@link CustomFunction} that will be used as the value.
//     //  * @return The updated Factor class.
//     //  */
//     // public Factors addFunc(String funcName, CustomFunction pFunc) {
//     //     Factors ret = copy();
//     //     ret.funcs.put(funcName, pFunc);
//     //     return ret;
//     // }


//     // /**
//     //  * Puts pVal into {@link #vars} with the key varName.
//     //  * @param varName       The variable name that will be used for the key.
//     //  * @param pVal          The double that will be used for the value.
//     //  * @return The updated Factor class.
//     //  */
//     // public Factors addVar(String varName, double pVal) {
//     //     Factors ret = copy();
//     //     ret.vars.put(varName, pVal);
//     //     return ret;
//     // }

//     // /**
//     //  * Calls {@link #addFunc(String)} for each String in funcNames - insterting them into {@link #funcs}. The key is 
//     //  * each argument, with the value being it's position in the Array.
//     //  * If a function name has a ":" in it, the String to the left of the ":" will be the name, and the String to the
//     //  * right will be the file name. If no ":" appear in the argument, the file's name will be set to the argument.
//     //  * @param funcNames      An array of each variable's name.
//     //  * @return The updated Factor class.
//     //  */
//     // public Factors addFuncs(String[] funcNames) {
//     //     Factors ret = copy();
//     //     for(int i = 0; i < funcNames.length; i++) {
//     //         String func = funcNames[i];
//     //         if(func.indexOf(":") != -1) {
//     //             ret = ret.addFunc(func.split(":")[0], new CustomFunction(func.split(":")[1]));
//     //         } else{
//     //             ret = ret.addFunc(func);
//     //         }
//     //     }
//     //     return ret;
//     // }

//     // /**
//     //  * Calls {@link #addVar(String)} for each String in varNames - insterting them into {@link #vars}. The key is 
//     //  * each argument, with the value being it's position in the Array. 
//     //  * If a variable name has a ":" in it, the String to the left of the ":" will be the name, and the String to the
//     //  * right will be the variable name. If no ":" appear in the argument, the value will be set to <code>0.0D</code>.
//     //  * @param varNames      An array of each variable's name.
//     //  * @return The updated Factor class.
//     //  * @throws java.lang.NumberFormatException    Thrown only when ":" is included in a variable's name, and 
//     //  *                                            The part on the right of that can't be evaluated as a double.
//     //  */
//     // public Factors addVars(String[] varNames) throws NumberFormatException{
//     //     Factors ret = copy();
//     //     for(int i = 0; i < varNames.length; i++) {
//     //         String var = varNames[i];
//     //         if(var.indexOf(":") != -1) {
//     //             ret = ret.addVar(var.split(":")[0], Double.parseDouble(var.split(":")[1]));
//     //         } else{
//     //             ret = ret.addVar(var);
//     //         }
//     //     }
//     //     return ret;
//     // }

//     // /**
//     //  * Adds each key-value pair from pFuncs into {@link #funcs}.
//     //  * @param pFuncs        The HashMap containing the name-function pairs. The key is a function name, while the 
//     //  *                      value is a {@link CustomFunction}.
//     //  * @return The updated Factor class.
//     //  */
//     // public Factors addFuncs(HashMap<String, CustomFunction> pFuncs) {
//     //     Factors ret = copy();
//     //     ret.funcs.putAll(pFuncs);
//     //     return ret;
//     // }

//     // /**
//     //  * Adds each key-value pair from pVars into {@link #vars}.
//     //  * @param pVars        The HashMap containing the name-value pairs. The key is a variable name, while the 
//     //  *                     value is a double.
//     //  * @return The updated Factor class.
//     //  */
//     // public Factors addVars(HashMap<String, Double> pVars) {
//     //     Factors ret = copy();
//     //     ret.vars.putAll(pVars);
//     //     return ret;
//     // }

//     // /**
//     //  * Returns the {@link CustomFunction} value of funcName as defined in {@link #funcs}.
//     //  * @param funcName      The name of the function in {@link #funcs}.
//     //  * @return A {@link CustomFunction} if funcName exists in {@link #funcs}. If it doesn't, it will return null.
//     //  */
//     // public CustomFunction getFunc(String funcName) {
//     //     return copy().funcs.get(funcName);
//     // }

//     // /**
//     //  * Returns the double value of varName, as defined is {@link #vars}
//     //  * @param varName       The name of the variable in {@link #vars}.
//     //  * @return A double if varName exists in {@link #funcs}. If it doesn't, it will return null (null isnt tested)
//     //  */
//     // public double getVar(String varName) {
//     //     return vars.get(varName);
//     // }

//     // /**
//     //  * Checks and sees if funcName is an actual function inside {@link #funcs}.
//     //  * @param funcName      The name of the function to check its existance.
//     //  * @return True if funcName has a value in {@link #funcs}.
//     //  */
//     // public boolean inFunc(String funcName) {
//     //     return funcs.containsKey(funcName);
//     // }

//     // /**
//     //  * Checks and sees if varName is an actual variable inside {@link #vars}.
//     //  * @param varName      The name of the variable to check its existance.
//     //  * @return True if varName has a value in {@link #vars}.
//     //  */
//     // public boolean inVar(String varName) {
//     //     return vars.containsKey(varName);
//     // }

//     // /**
//     //  * Just returns the vars in String format, and the funcs in String format, with a little prettier formatting.
//     //  * @return A String representation of this factor.
//     //  */
//     // public String toString() {
//     //     return "\n\tVARS: " + vars.toString() + "\n\tFUNCS: " + funcs.toString();
//     // }

//     // /**
//     //  * Makes an exact copy of this Factors class.
//     //  * @return An exact copy of this class
//     //  */
//     // public Factors copy() {
//     //     return new Factors(vars, funcs);
//     // }

// }
