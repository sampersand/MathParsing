// package West.Math.Equation.Function;

// import West.Math.MathObject;
// import West.Print;
// import West.Math.Equation.EquationSystem;
// import West.Math.Set.Node.TokenNode;

// import java.util.HashMap;
// import java.util.Random;

// import West.Math.Set.Collection;

// public class InBuiltFunction extends Function {
//     /**
//      * A Hashmap containing all the different InBuiltFunctions and their names. The keys are the names (like "+", "cos",
//      * or "round"), and the values are the InBuiltFunctions / {@link OperationFunction}s corresponding to the
//      * names.
//      */    
//     public static Collection<String> BINOPER = new Collection<String>(){{
//         //assignment
//         add("=");
//         //Comparisons
//         add("≣");add("≠");
//         add(">");add("<");
//         add("≥");add("≤");
//         add("∧");add("∨");add("⊻");
//         //Math opers
//         add("+");add("-");add("*");add("/");add("^");
//         //ETC
//         add("_nCr_");add("_nPr_");
//         add("≫");add("≪");
//     }};

//     public static boolean isBinOper(String s){
//         if(FUNCTIONS.get(s) == null)
//             return false;
//         return FUNCTIONS.get(s).isBinOper());
//     }
//     /**
//      * Default constructor. Instatiates {@link #name}, {@link #help}, and {@link #syntax} as empty strings.
//      * @throws IllegalArgumentException When either name, help, and / or syntax is null, should never happen as these
//      *                                  are pre-defined, and therefor shouldnt have null.
//      */
//     public InBuiltFunction() throws IllegalArgumentException{
//         super();
//     }

//     /**
//      * The main constructor for InBuiltFunction. Takes a name, a help string, and a syntax string.
//      * @param pName     The name of this function.
//      * <!-- @param pInverse  The name of the function that is the inverse of this function.-->
//      * @param pHelp     The "help" text that will be displayed when the {@link #help()} function is called.
//      * @param pSyntax   The "syntax" text that will be displayed when the {@link #syntax()} function is called.
//      * @throws IllegalArgumentException When either name, help, and / or syntax is null.
//      */
//     public InBuiltFunction(String pName,
//                            // String pInverse,
//                            String pHelp,
//                            String pSyntax,
//                            int priority,
//                            Collection<Integer> pArgsLength,
//                            FuncObj pFuncObj){
//         super(pName, pHelp, pSyntax, priority, pArgsLength, pFuncObj);
//         // inverse = pInverse;
//     }

//     /**
//      * Executes a function with the name <code>pName</code>, as defined in {@link #FUNCTIONS}. Just passes
//      * <code>pEq</code> and <code>pNode</code> to {@link #exec(EquationSystem,Node) the other exec function}.
//      * @param pName         The name of the function. Some exapmles are "sin", and "abs".
//      * @param pEqSys        The {@link EquationSystem} that the function of name <code>pName</code> will be evaluated 
//      *                      by.
//      * @param pNode         The {@link Node} that will be passed to <code>pName</code>.
//      * @return A double representing the value of <code>pNode</code>, when solved for with <code>pEqSys</code>.
//      * @throws NullPointerException    Thrown when the function is defined, but how to execute it isn't.
//      * @throws IllegalArgumentException   Thrown when the function required parameters, and the ones passed aren't right.
//      */
//     public static HashMap<String, Double> exec(String pName, final EquationSystem pEqSys, TokenNode pNode) {
//         if(FUNCTIONS.get(pName) == null)
//             throw new NullPointerException("Cannot execute the InBuiltFunction '" + pName +"' because it isn't defined "+
//                     "in FUNCTIONS.");
//         return FUNCTIONS.get(pName).exec(pEqSys, pNode);
//     }


//     @Override
//     public String toString() {
//         return "InBuiltFunction '" + name + "'";
//     }

//     @Override
//     public String toFancyString(int idtLvl) {
//         String ret = indent(idtLvl) + "InBuiltFunction '" + name + "':\n";
//         ret += indent(idtLvl + 1) + "Help = " + help + "\n";
//         ret += indent(idtLvl + 1) + "Syntax = " + syntax + "";
//         return ret;
//     }

//     @Override
//     public String toFullString(int idtLvl) {
//         String ret = indent(idtLvl) + "InBuiltFunction:\n";
//         ret += indent(idtLvl + 1) + "Name:\n" + indentE(idtLvl + 2) + name + "\n";
//         ret += indent(idtLvl + 1) + "Help:\n" + indentE(idtLvl + 2) + help + "\n";
//         ret += indent(idtLvl + 1) + "Syntax:\n" + indentE(idtLvl + 2) + syntax;
//         return ret + "\n" + indentE(idtLvl + 1);
//     }

//     @Override
//     public InBuiltFunction copy(){
//         return new InBuiltFunction(name, help, syntax, priority, argsLength, funcObj);
//     }

// }