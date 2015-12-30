package West.Math.Equation.Function;

import West.Math.MathObject;
import West.Math.Print;
import West.Math.Equation.EquationSystem;
import West.Math.Equation.Node;
import West.Math.Exception.NotDefinedException;

import java.util.HashMap;
import java.util.Random;

public class InBuiltFunction extends Function {

    /**
     * A Hashmap containing all the different InBuiltFunctions and their names. The keys are the names (like "+", "cos",
     * or "round"), and the values are the InBuiltFunctions / {@link OperationFunction}s corresponding to the
     * names.
     */
    public static HashMap<String, InBuiltFunction> FUNCTIONS = new HashMap<String, InBuiltFunction>() {{
        put("+", new OperationFunction("+", "Adds 'A' to 'B'", "A + B"));
        put("-", new OperationFunction("-", "Subtracts 'A' to 'B'", "A - B"));
        put("*", new OperationFunction("*", "Multiplies 'A' to 'B'", "A * B"));
        put("/", new OperationFunction("/", "Divides 'A' to 'B'", "A / B"));
        put("^", new OperationFunction("^", "Raises 'A' to 'B'", "A ^ B"));
        put("sin", new InBuiltFunction("sin", "sin of 'A'", "sin(A)"));
        put("cos", new InBuiltFunction("cos", "cos of 'A'", "cos(A)"));
        put("tan", new InBuiltFunction("tan", "tan of 'A'", "tan(A)"));
        put("csc", new InBuiltFunction("csc", "csc of 'A'", "csc(A)"));
        put("sec", new InBuiltFunction("sec", "sec of 'A'", "sec(A)"));
        put("cot", new InBuiltFunction("cot", "cot of 'A'", "cot(A)"));
        put("sinh", new InBuiltFunction("sinh", "sinh of 'A'", "sinh(A)"));
        put("cosh", new InBuiltFunction("cosh", "cosh of 'A'", "cosh(A)"));
        put("tanh", new InBuiltFunction("tanh", "tanh of 'A'", "tanh(A)"));
        put("asin", new InBuiltFunction("asin", "asin of 'A'", "asin(A)"));
        put("acos", new InBuiltFunction("acos", "acos of 'A'", "acos(A)"));
        put("atan", new InBuiltFunction("atan", "atan of 'A'", "atan(A)"));
        put("abs", new InBuiltFunction("abs", "absolute value of 'A'", "abs(A)"));
        put("ceil", new InBuiltFunction("ceil", "closest integer greater than 'A'", "ceil(A)"));
        put("floor", new InBuiltFunction("floor", "closest integer less than 'A'", "floor(A)"));
        put("hypot", new InBuiltFunction("hypot", "hypotenuse of 'A' and 'B' ( √[A² + B²] )", "hypot(A, B)"));
        put("ln", new InBuiltFunction("ln", "natural log of 'A'", "ln(A)"));
        put("log", new InBuiltFunction("log", "log base 10 of 'A'", "log(A)"));
        put("round", new InBuiltFunction("round", "rounds 'A' to the nearest integer", "round(A)"));
        put("sqrt", new InBuiltFunction("sqrt", "the square root (√) of 'A'", "sqrt(A)"));
        put("degrees", new InBuiltFunction("degr", "turns 'A' into degrees (from radians)", "degrees(A)"));
        put("radians", new InBuiltFunction("radi", "turns 'A' into radians (from degrees)", "radians(A)"));
        put("ri", new InBuiltFunction("randi", "random integer from [0, 100], [0, 'A'], or ['A', 'B']", "ri(A, B)"));
        put("rd", new InBuiltFunction("randd", "random double from [0, 1), [0, 'A'), or ['A', 'B')", "rd(A, B)"));
        put("fac", new InBuiltFunction("fac", "factorial of 'A'", "fac(A)"));
        put("neg", new InBuiltFunction("negate", "'A' * -1", "neg(A)"));
        put("graph", new GraphFunction());
    }};

    ///** The name of the {@link #inverse()} of this funtion. The reason it's not an actual function is because of 
    // * FUNCTIONS - you cannot "get" the inverse function if it hasn't been declared yet.
    // */
    // String inverse;

    /**
     * Default constructor. Instatiates {@link #name}, {@link #help}, and {@link #syntax} as empty strings.
     * @throws IllegalArgumentException When either name, help, and / or syntax is null, should never happen as these
     *                                  are pre-defined, and therefor shouldnt have null.
     */
    public InBuiltFunction() throws IllegalArgumentException{
        this("", "", "");
    }

    /**
     * The main constructor for InBuiltFunction. Takes a name, a help string, and a syntax string.
     * @param pName     The name of this function.
     * <!-- @param pInverse  The name of the function that is the inverse of this function.-->
     * @param pHelp     The "help" text that will be displayed when the {@link #help()} function is called.
     * @param pSyntax   The "syntax" text that will be displayed when the {@link #syntax()} function is called.
     * @throws IllegalArgumentException When either name, help, and / or syntax is null.
     */
    public InBuiltFunction(String pName,
                           // String pInverse,
                           String pHelp,
                           String pSyntax) {
        super(pName, pHelp, pSyntax);
        // inverse = pInverse;
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
     * @throws IllegalArgumentException   Thrown when the function required parameters, and the ones passed aren't right.
     */
    public static double exec(String pName,
                              final EquationSystem pEqSys,
                              Node pNode) throws
                                  NotDefinedException,
                                  IllegalArgumentException {
        if(FUNCTIONS.get(pName) == null)
            throw new NotDefinedException("Cannot execute the InBuiltFunction '" + pName +"' because it isn't defined "+
                    "in FUNCTIONS.");
        return FUNCTIONS.get(pName).exec(pEqSys, pNode);
    }

    @Override
    public double exec(final EquationSystem pEqSys,
                       Node pNode) throws
                           NotDefinedException,
                           IllegalArgumentException {
        double[] args = evalNode(pEqSys, pNode);
        switch(name) {

            case "negate":
                return args[0] * -1;
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
                    Print.printw(name + " takes 0, 1, or 2 params. Returning a random num from 0- 1 instead.");

            case "fac":
                double ret = 1;
                for(int x = 1; x <= (int)args[0]; x++)
                    ret *= x;
                return ret;

            default:
                throw new NotDefinedException("Cannot evaluate the InBuiltFunction '" + name + "' because it doesn't " +
                                               "have a defined way to compute it!");
        }
    }

    @Override
    public String toString() {
        return "InBuiltFunction '" + name + "'";
    }

    @Override
    public String toFancyString(int idtLvl) {
        String ret = indent(idtLvl) + "InBuiltFunction '" + name + "':\n";
        ret += indent(idtLvl + 1) + "Help = " + help + "\n";
        ret += indent(idtLvl + 1) + "Syntax = " + syntax + "";
        return ret;
    }

    @Override
    public String toFullString(int idtLvl) {
        String ret = indent(idtLvl) + "InBuiltFunction:\n";
        ret += indent(idtLvl + 1) + "Name:\n" + indentE(idtLvl + 2) + name + "\n";
        ret += indent(idtLvl + 1) + "Help:\n" + indentE(idtLvl + 2) + help + "\n";
        ret += indent(idtLvl + 1) + "Syntax:\n" + indentE(idtLvl + 2) + syntax;
        return ret + "\n" + indentE(idtLvl + 1);
    }

    @Override
    public InBuiltFunction copy(){
        return new InBuiltFunction(name, help, syntax);
    }

}