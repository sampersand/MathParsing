package West.Math.Equation.Function;

import West.Math.MathObject;
import West.Math.Print;
import West.Math.Equation.EquationSystem;
import West.Math.Set.Node.TokenNode;
import West.Math.Exception.NotDefinedException;

import java.util.HashMap;
import java.util.Random;

import West.Math.Set.Collection;

/**
 * A class that represents an operation in mathametics. It acts very similar to an {@link InBuiltFunction}.
 * 
 * @author Sam Westerman
  * @version 0.89
 * @since 0.1
  * @see <a href="https://en.wikipedia.org/wiki/Operation_(mathematics)">Operation</a>
 */
public class OperationFunction extends InBuiltFunction {

    public static final HashMap<String, Function> UNARY_LEFT = new HashMap<String, Function>()
    {{
        // put("+", null);
        put("-", InBuiltFunction.FUNCTIONS.get("negate"));
        put("~", null);
    }};

    public static final HashMap<String, Function> UNARY_RIGHT = new HashMap<String, Function>()
    {{
        put("!", InBuiltFunction.FUNCTIONS.get("fac"));
    }};

    public static final HashMap<String, Function> BINARY = new HashMap<String, Function>()
    {{
        put("+", InBuiltFunction.FUNCTIONS.get("+"));
        put("-", InBuiltFunction.FUNCTIONS.get("-"));
        put("*", InBuiltFunction.FUNCTIONS.get("*"));
        put("/", InBuiltFunction.FUNCTIONS.get("/"));
        put("^", InBuiltFunction.FUNCTIONS.get("^"));
    }};

    public static enum OPERATOR  { //TODO: REMOVE THIS
        ADDITION("+", 0),           // Algebra "addition"
        SUBTRACTION("-", 0),        // Algebra "subtraction"
        MULTIPLICATION("*", 1),     // Algebra "multiplication"
        DIVISION("/", 1),           // Algebra "division"
        POWER("^", 2),              // Algebra "power of"

        ST_IN("∈"),                 // Set Theory "in"
        ST_NIN("∉"),                // Set Theory "not in"
        ST_PSUB("⊂"),               // Set Theory "proper subset"
        ST_NPSUB("⊄"),              // Set Theory "not proper subset"
        ST_SUB("⊆"),                // Set Theory "subset"
        ST_NSUB("⊈"),               // Set Theory "not subset"
        ST_PSUP("⊂"),               // Set Theory "proper supset"
        ST_NPSUP("⊄"),              // Set Theory "not proper supset"
        ST_SUP("⊆"),                // Set Theory "supset"
        ST_NSUP("⊈"),               // Set Theory "not supset"
        ST_NOT("¬");                // Set Theory "not"

        private String notation;
        private int priority;
        private OPERATOR (String pNot){
            this(pNot, -1);
        }
        private OPERATOR (String pNot, int pPriority){
            notation = pNot;
            priority = pPriority;
        }

        public String notation(){
            return notation;
        }
        public int priority(){
            return priority;
        }
        public String toString(){
            return notation() + " | " + priority;
        }
        public static OPERATOR fromString(String pNot){
            if (pNot != null)
                for (OPERATOR o : OPERATOR.values())
                    if (pNot.equalsIgnoreCase(o.notation()))
                        return o;
            return null;
        }
    }
    /**
     * Default constructor. Instatiated {@link #name}, {@link #help}, and {@link #syntax} as empty strings.
     * @throws IllegalArgumentException When either name, help, and / or syntax is null.
     */
    public OperationFunction() throws NotDefinedException{
        super();
    }

    /**
     * Main constructor. Takes a name, a help string, and a syntax string.
     * @param pOper     The symbol of the operator, like <code>+, -, *, /, ^</code>.
     * @param pHelp     The "help" text that will be displayed when the {@link #help()} function is called.
     * @param pSyntax   The "syntax" text that will be displayed when the {@link #syntax()} function is called.
     * @throws IllegalArgumentException When either name, help, and / or syntax is null.
     */
    public OperationFunction(String pOper,
                             String pHelp,
                             String pSyntax,
                             Collection<Integer> pArgsLength,
                             FuncObj pFuncObj) throws IllegalArgumentException{
        super(pOper, pHelp, pSyntax, pArgsLength, pFuncObj);
    }

    // @Override
    // public HashMap<String, Double> exec(final EquationSystem pEqSys,
    //                    TokenNode pNode) throws 
    //                        NotDefinedException,
    //                        IllegalArgumentException {
    //     assert pNode.elements().size() == 2;
    //     Object[] rargs = evalNode(pEqSys, pNode);
    //     HashMap<String, Double> rethm = (HashMap<String, Double>)rargs[1];
    //     double[] args = (double[])rargs[0];
    //     Double ret = args[0];
    //     switch(name) {
    //         case "+":
    //             ret += args[1];
    //             break;
    //         case "-":
    //             ret -= args[1];
    //             break;
    //         case "*":
    //             ret *= args[1];
    //             break;
    //         case "/":
    //             ret /= args[1];
    //             break;
    //         case "^": // not sure this works
    //             ret = Math.pow(ret, args[1]);
    //             break;
    //         default:
    //             Print.printw("No known way to evaluate '" + this + "'");
    //     }
    //     rethm.put("**TEMP**", ret);
    //     return rethm;
    // }

    @Override
    public String toString() {
        return "OperationFunction '" + name + "'";
    }

    @Override
    public String toFancyString(int idtLvl) {
        String ret = indent(idtLvl) + "OperationFunction '" + name + "':\n";
        ret += indent(idtLvl + 1) + "Help = " + help + "\n";
        ret += indent(idtLvl + 1) + "Syntax = " + syntax + "";
        return ret;
    }

    @Override
    public String toFullString(int idtLvl) {
        String ret = indent(idtLvl) + "OperationFunction:\n";
        ret += indent(idtLvl + 1) + "Name:\n" + indentE(idtLvl + 2) + name + "\n";
        ret += indent(idtLvl + 1) + "Help:\n" + indentE(idtLvl + 2) + help + "\n";
        ret += indent(idtLvl + 1) + "Syntax:\n" + indentE(idtLvl + 2) + syntax;
        return ret + "\n" + indentE(idtLvl + 1);
    }

    @Override
    public OperationFunction copy(){
        return new OperationFunction(name, help, syntax, argsLength, funcObj);
    }
}