package Math.Equation;

import Math.MathObject;
import Math.Print;
import Math.Equation.Token.Type;
import Math.Exception.NotDefinedException;
import Math.Exception.InvalidArgsException;

import java.util.HashMap;
import java.util.Random;

public class InBuiltFunction extends Function {

    /**
     * A Hashmap containing all the different InBuiltFunctions and their names. The keys are the names (like "+", "cos",
     * or "round"), and the values are the InBuiltFunctions / {@link OperationFunction}s corresponding to the
     * names.
     */
    public static HashMap<String, InBuiltFunction> FUNCTIONS = new HashMap<String, InBuiltFunction>() {{
        put("+", new OperationFunction("+", "Adds the first number to the second one.", "A + B"));
        put("-", new OperationFunction("-", "Subtracts the first number to the second one.", "A - B"));
        put("*", new OperationFunction("*", "Multiplies the first number to the second one.", "A * B"));
        put("/", new OperationFunction("/", "Divides the first number to the second one.", "A / B"));
        put("^", new OperationFunction("^", "Raises the first number to the second one.", "A ^ B"));
        put("sin", new InBuiltFunction("sin", "Returns the sin of the argument.", "sin ( A )"));
        put("cos", new InBuiltFunction("cos", "Returns the cos of the argument.", "cos ( A )"));
        put("tan", new InBuiltFunction("tan", "Returns the tan of the argument.", "tan ( A )"));
        put("csc", new InBuiltFunction("csc", "Returns the csc of the argument.", "csc ( A )"));
        put("sec", new InBuiltFunction("sec", "Returns the sec of the argument.", "sec ( A )"));
        put("cot", new InBuiltFunction("cot", "Returns the cot of the argument.", "cot ( A )"));
        put("sinh", new InBuiltFunction("sinh", "Returns the sinh of the argument.", "sinh ( A )"));
        put("cosh", new InBuiltFunction("cosh", "Returns the cosh of the argument.", "cosh ( A )"));
        put("tanh", new InBuiltFunction("tanh", "Returns the tanh of the argument.", "tanh ( A )"));
        put("asin", new InBuiltFunction("asin", "Returns the asin of the argument.", "asin ( A )"));
        put("acos", new InBuiltFunction("acos", "Returns the acos of the argument.", "acos ( A )"));
        put("atan", new InBuiltFunction("atan", "Returns the atan of the argument.", "atan ( A )"));
        put("abs", new InBuiltFunction("abs", null, "abs ( A )"));
        put("ceil", new InBuiltFunction("ceil", null, "ceil ( A )"));
        put("floor", new InBuiltFunction("floor", null, "floor ( A )"));
        put("hypot", new InBuiltFunction("hypot", null, "hypot ( A )"));
        put("ln", new InBuiltFunction("ln", null, "ln ( A )"));
        put("log", new InBuiltFunction("log", null, "log ( A )"));
        put("round", new InBuiltFunction("round", null, "round ( A )"));
        put("sqrt", new InBuiltFunction("sqrt", null, "sqrt ( A )"));
        put("degr", new InBuiltFunction("degr", null, "degr ( A )"));
        put("rad", new InBuiltFunction("radi", null, "radi ( A )"));
        put("randi", new InBuiltFunction("randi", null, "randi ( A )"));
        put("randd", new InBuiltFunction("randd", null, "randd ( A )"));
        put("fac", new InBuiltFunction("fac", null, "fac ( A )"));
    }};

    /**
     * Default constructor. Just passes <code>null, null, null</code> to
     * {@link #InBuiltFunction(String,String,String) another InBuiltFunction constructor}.
     */
    public InBuiltFunction() {
        this(null, null, null);
    }

    /**
     * The main constructor for InBuiltFunction. Takes a name, a help string, and a syntax string.
     * @param pName     The name of this function.
     * @param pHelp     The "help" text that will be displayed when the {@link #help()} function is called.
     * @param pSyntax   The "syntax" text that will be displayed when the {@link #syntax()} function is called.
     */
    public InBuiltFunction(String pName,
                           String pHelp,
                           String pSyntax) {
        super(pName, pHelp, pSyntax);
    }

    /**
     * Executes a function with the name <code>pName</code>, as defined in {@link #FUNCTIONS}. Just passes
     * <code>pEq</code> and <code>pNode</code> to {@link #exec(EquationSystem,Node) the other exec function}.
     * @param pName         The name of the function. Some exapmles are "sin", and "abs".
     * @param pEqSys        The {@link EquationSystem} that the function of name <code>pName</code> will be evaluated 
     *                      by.
     * @param pNode         The {@link Node} that will be passed to <code>pName</code>.
     * @return A double representing the value of <code>pNode</code>, when solved for with <code>pEqSys</code>.
     * @throws NotDefinedException    Thrown when the function is defined, but how to execute it isn't.
     * @throws InvalidArgsException   Thrown when the function required parameters, and the ones passed aren't right.
     */
    public static double exec(String pName,
                              EquationSystem pEqSys,
                              Node pNode) throws
                                  NotDefinedException,
                                  InvalidArgsException {
        if(FUNCTIONS.get(pName) == null)
            throw new NotDefinedException("There is no InBuiltFunction '" + pName + "' defined in FUNCTIONS!");
        return FUNCTIONS.get(pName).exec(pEqSys, pNode);
    }

    @Override
    public double exec(EquationSystem pEqSys,
                       Node pNode) throws
                           NotDefinedException,
                           InvalidArgsException {
        double[] args = evalNode(pEqSys, pNode);
        switch(name) {

            case "sin":
                return Math.sin(args[0]);
            case "cos":
                return Math.cos(args[0]);
            case "tan":
                return Math.tan(args[0]);

            case "csc":
                return 1D / Math.sin(args[0]);
            case "sec":
                return 1D / Math.cos(args[0]);
            case "cot":
                return 1D / Math.tan(args[0]);


            case "sinh":
                return Math.sinh(args[0]);
            case "cosh":
                return Math.cosh(args[0]);
            case "tanh":
                return Math.tanh(args[0]);

            case "asin":
                return Math.asin(args[0]);
            case "acos":
                return Math.acos(args[0]);
            case "atan":
                return Math.atan(args[0]);

            case "abs":
                return Math.abs(args[0]);
            case "ceil":
                return Math.ceil(args[0]);
            case "floor":
                return Math.floor(args[0]);
            case "hypot":
                return Math.hypot(args[0], args[1]);
            case "ln":
                return Math.log(args[0]);
            case "log":
                return Math.log10(args[0]);

            case "round":
                return Math.round(args[0]);
            case "sqrt":
                return Math.sqrt(args[0]);
            case "degr":
                return Math.toDegrees(args[0]);
            case "radi":
                return Math.toRadians(args[0]);

            case "randi": 
                if(args.length == 0) return new Random().nextInt(100);
                if(args.length == 1) return new Random().nextInt((int)args[0]);
                if(args.length == 2) return new Random().nextInt((int)args[1]) + args[0];
                Print.printw(name + " takes 0, 1, or 2 params. Returning 0 instead.");
                return 0;

            case "randd":
                if(args.length == 1) return new Random().nextDouble() * args [0];
                if(args.length == 2) return (new Random().nextDouble() + args[0]) * args[1];
                if(args.length == 0) return Math.random();
                if(args.length != 0)
                    Print.printw(name + " takes 0, 1, or 2 params. Returning a random num from 0-1 instead.");

            case "fac":
                double ret = 1;
                for(int x = 1; x <= (int)args[0]; x++)
                    ret *= x;
                return ret;

            default:
                throw new NotDefinedException("InBuiltFunction " + this + " doesn't have a defined way to compute it!");
        }
    }

    @Override
    public String toString() {
        return "InBuiltFunction: '" + name + "'";
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