package West.Math.Equation.Function;

import West.Math.MathObject;
import West.Math.Print;
import West.Math.Equation.EquationSystem;
import West.Math.Set.Node.TokenNode;
import West.Math.Exception.NotDefinedException;

import java.util.HashMap;
import java.util.Random;

import West.Math.Set.Collection;

public class InBuiltFunction extends Function {
    /**
     * A Hashmap containing all the different InBuiltFunctions and their names. The keys are the names (like "+", "cos",
     * or "round"), and the values are the InBuiltFunctions / {@link OperationFunction}s corresponding to the
     * names.
     */
    public static HashMap<String, InBuiltFunction> FUNCTIONS = new HashMap<String, InBuiltFunction>() {{
        put("+", new InBuiltFunction("+", "Adds 'A' to 'B'", "+(A, B)", 0, 
            new Collection.Builder<Integer>().add(2).build(),
            a -> a[0] + a[1]
            ));

        put("-", new InBuiltFunction("-", "Subtracts 'A' to 'B'", "-(A, B)", 0,
            new Collection.Builder<Integer>().add(2).build(),
            a -> a[0] - a[1]
            ));

        put("*", new InBuiltFunction("*", "Multiplies 'A' to 'B'", "*(A, B)", 1,
            new Collection.Builder<Integer>().add(2).build(),
            a -> a[0] * a[1]
            ));

        put("/", new InBuiltFunction("/", "Divides 'A' to 'B'", "/(A, B)", 1,
            new Collection.Builder<Integer>().add(2).build(),
            a -> a[0] / a[1]
            ));

        put("^", new InBuiltFunction("^", "Raises 'A' to 'B'", "^(A, B)", 2,
            new Collection.Builder<Integer>().add(2).build(),
            a -> Math.pow(a[0],a[1])
            ));

        put("sin", new InBuiltFunction("sin", "sin of 'A'", "sin(A)", -1,
            new Collection.Builder<Integer>().add(1).build(),
            a -> Math.sin(a[0])
            ));

        put("cos", new InBuiltFunction("cos", "cos of 'A'", "cos(A)", -1,
            new Collection.Builder<Integer>().add(1).build(),
            a -> Math.cos(a[0])
            ));

        put("tan", new InBuiltFunction("tan", "tan of 'A'", "tan(A)", -1,
            new Collection.Builder<Integer>().add(1).build(),
            a -> Math.tan(a[0])
            ));

        put("csc", new InBuiltFunction("csc", "csc of 'A'", "csc(A)", -1,
            new Collection.Builder<Integer>().add(1).build(),
            a -> 1D / Math.sin(a[0])
            ));

        put("sec", new InBuiltFunction("sec", "sec of 'A'", "sec(A)", -1,
            new Collection.Builder<Integer>().add(1).build(),
            a -> 1D / Math.cos(a[0])
            ));

        put("cot", new InBuiltFunction("cot", "cot of 'A'", "cot(A)", -1,
            new Collection.Builder<Integer>().add(1).build(),
            a -> 1D / Math.tan(a[0])
            ));

        put("sinh", new InBuiltFunction("sinh", "sinh of 'A'", "sinh(A)", -1,
            new Collection.Builder<Integer>().add(1).build(),
            a -> Math.sinh(a[0])
            ));

        put("cosh", new InBuiltFunction("cosh", "cosh of 'A'", "cosh(A)", -1,
            new Collection.Builder<Integer>().add(1).build(),
            a -> Math.cosh(a[0])
            ));

        put("tanh", new InBuiltFunction("tanh", "tanh of 'A'", "tanh(A)", -1,
            new Collection.Builder<Integer>().add(1).build(),
            a -> Math.tanh(a[0])
            ));

        put("asin", new InBuiltFunction("asin", "asin of 'A'", "asin(A)", -1,
            new Collection.Builder<Integer>().add(1).build(),
            a -> Math.asin(a[0])
            ));

        put("acos", new InBuiltFunction("acos", "acos of 'A'", "acos(A)", -1,
            new Collection.Builder<Integer>().add(1).build(),
            a -> Math.acos(a[0])
            ));

        put("atan", new InBuiltFunction("atan", "atan of 'A'", "atan(A)", -1,
            new Collection.Builder<Integer>().add(1).build(),
            a -> Math.atan(a[0])
            ));

        put("abs", new InBuiltFunction("abs", "absolute value of 'A'", "abs(A)", -1,
            new Collection.Builder<Integer>().add(1).build(),
            a -> Math.abs(a[0])
            ));

        put("ceil", new InBuiltFunction("ceil", "closest integer greater than 'A'", "ceil(A)", -1,
            new Collection.Builder<Integer>().add(1).build(),
            a -> Math.ceil(a[0])
            ));

        put("floor", new InBuiltFunction("floor", "closest integer less than 'A'", "floor(A)", -1,
            new Collection.Builder<Integer>().add(1).build(),
            a -> Math.floor(a[0])
            ));

        put("hypot", new InBuiltFunction("hypot", "hypotenuse of 'A' and 'B' ( √[A² + B²] )", "hypot(A, B)", -1,
            new Collection.Builder<Integer>().add(2).build(),
            a -> Math.hypot(a[0], a[1])
            ));

        put("ln", new InBuiltFunction("ln", "natural log of 'A'", "ln(A)", -1,
            new Collection.Builder<Integer>().add(1).build(),
            a -> Math.log(a[0])
            ));

        put("log", new InBuiltFunction("log", "log base 10 of 'A'", "log(A)", -1,
            new Collection.Builder<Integer>().add(1).build(),
            a -> Math.log10(a[0])
            ));

        put("round", new InBuiltFunction("round", "rounds 'A' to the nearest integer", "round(A)", -1,
            new Collection.Builder<Integer>().add(1).build(),
            a -> Long.valueOf(Math.round(a[0])).doubleValue()
            ));

        put("sqrt", new InBuiltFunction("sqrt", "the square root (√) of 'A'", "sqrt(A)", -1,
            new Collection.Builder<Integer>().add(1).build(),
            a -> Math.sqrt(a[0])
            ));

        put("degrees", new InBuiltFunction("degr", "turns 'A' into degrees (from radians)", "degrees(A)", -1,
            new Collection.Builder<Integer>().add(1).build(),
            a -> Math.toDegrees(a[0])
            ));

        put("radians", new InBuiltFunction("radi", "turns 'A' into radians (from degrees)", "radians(A)", -1,
            new Collection.Builder<Integer>().add(1).build(),
            a -> Math.toRadians(a[0])
            ));

        put("ri", new InBuiltFunction("randi", "random integer from [0, 100], [0, 'A'], or ['A', 'B']", "ri(A, B)", -1,
            new Collection.Builder<Integer>().add(0).add(1).add(2).build(),
            a -> {
               if(a.length == 0)
                    return Integer.valueOf(new Random().nextInt(100)).doubleValue();
                else if(a.length == 1)
                    return Integer.valueOf(new Random().nextInt((int)a[0])).doubleValue();
                else if(a.length == 2)
                    return Integer.valueOf(new Random().nextInt((int)a[1])+a[0]).doubleValue();
                else
                    throw new NotDefinedException();
                }
            ));

        put("rd", new InBuiltFunction("randd", "random double from [0, 1), [0, 'A'), or ['A', 'B')", "rd(A, B)", -1,
            new Collection.Builder<Integer>().add(0).add(1).add(2).build(),
            a -> {
                if(a.length == 1)
                    return new Random().nextDouble() * a[0];
                else if(a.length == 2)
                    return (new Random().nextDouble() + a[0]) * a[1];
                else if(a.length == 0)
                    return Math.random();
                else
                    throw new NotDefinedException();
                }
            ));
 

    //         case "fac":
    //             double ret = 1;
    //             for(int x = 1; x <= (int)args[0]; x++)
    //                 ret *= x;
    //             rethm.put("**TEMP**", ret);
    //             break;
        put("fac", new InBuiltFunction("fac", "factorial of 'A'", "fac(A)", -1,
            new Collection.Builder<Integer>().add(1).build(),
            a -> null
            ));

        put("neg", new InBuiltFunction("negate", "'A' * -1", "neg(A)", -1,
            new Collection.Builder<Integer>().add(1).build(),
            a -> -1 * a[0]
            ));

        put("graph", new GraphFunction());

        put("", new InBuiltFunction("","","", -1,
            new Collection.Builder<Integer>().add(1).build(),
            a -> a[0]
            ));

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
        super();
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
                           String pSyntax,
                           int priority,
                           Collection<Integer> pArgsLength,
                           FuncObj pFuncObj){
        super(pName, pHelp, pSyntax, priority, pArgsLength, pFuncObj);
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
    public static HashMap<String, Double> exec(String pName, final EquationSystem pEqSys, TokenNode pNode) {
        if(FUNCTIONS.get(pName) == null)
            throw new NotDefinedException("Cannot execute the InBuiltFunction '" + pName +"' because it isn't defined "+
                    "in FUNCTIONS.");
        return FUNCTIONS.get(pName).exec(pEqSys, pNode);
    }

    // @Override
    // public HashMap<String, Double> exec(final EquationSystem pEqSys,
    //                                     TokenNode pNode) throws
    //                                         NotDefinedException,
    //                                         IllegalArgumentException {
    //     Object[] rargs = evalNode(pEqSys, pNode);
    //     double[] args = (double[])rargs[0];
    //     HashMap<String, Double> rethm = (HashMap<String, Double>)rargs[1];
    //     // assert args.length == 1;
    //     switch(name) {
    //         case "": //used for a grouop.
    //             assert args.length == 1;
    //             rethm.put("**TEMP**", args[0]); break;
    //         case "negate":
    //             rethm.put("**TEMP**", args[0] * -1); break;
    //         case "sin":
    //             rethm.put("**TEMP**", Math.sin(args[0])); break;
    //         case "cos":
    //             rethm.put("**TEMP**", Math.cos(args[0])); break;
    //         case "tan":
    //             rethm.put("**TEMP**", Math.tan(args[0])); break;

    //         case "csc":
    //             rethm.put("**TEMP**", 1D / Math.sin(args[0])); break;
    //         case "sec":
    //             rethm.put("**TEMP**", 1D / Math.cos(args[0])); break;
    //         case "cot":
    //             rethm.put("**TEMP**", 1D / Math.tan(args[0])); break;


    //         case "sinh":
    //             rethm.put("**TEMP**", Math.sinh(args[0])); break;
    //         case "cosh":
    //             rethm.put("**TEMP**", Math.cosh(args[0])); break;
    //         case "tanh":
    //             rethm.put("**TEMP**", Math.tanh(args[0])); break;

    //         case "asin":
    //             rethm.put("**TEMP**", Math.asin(args[0])); break;
    //         case "acos":
    //             rethm.put("**TEMP**", Math.acos(args[0])); break;
    //         case "atan":
    //             rethm.put("**TEMP**", Math.atan(args[0])); break;

    //         case "abs":
    //             rethm.put("**TEMP**", Math.abs(args[0])); break;
    //         case "ceil":
    //             rethm.put("**TEMP**", Math.ceil(args[0])); break;
    //         case "floor":
    //             rethm.put("**TEMP**", Math.floor(args[0])); break;
    //         case "hypot":
    //             rethm.put("**TEMP**", Math.hypot(args[0], args[1])); break;
    //         case "ln":
    //             rethm.put("**TEMP**", Math.log(args[0])); break;
    //         case "log":
    //             rethm.put("**TEMP**", Math.log10(args[0])); break;

    //         case "round":
    //             rethm.put("**TEMP**", Double.parseDouble("" + Math.round(args[0]))); break;
    //         case "sqrt":
    //             rethm.put("**TEMP**", Math.sqrt(args[0])); break;
    //         case "degr":
    //             rethm.put("**TEMP**", Math.toDegrees(args[0])); break;
    //         case "radi":
    //             rethm.put("**TEMP**", Math.toRadians(args[0])); break;

    //         case "randi": 
    //             if(args.length == 0)
    //                 rethm.put("**TEMP**", Double.parseDouble("" + new Random().nextInt(100)));
    //             else if(args.length == 1)
    //                 rethm.put("**TEMP**", Double.parseDouble("" + new Random().nextInt((int)args[0])));
    //             else if(args.length == 2)
    //                 rethm.put("**TEMP**", Double.parseDouble("" + new Random().nextInt((int)args[1])+args[0]));
    //             else {
    //                 Print.printw(name + " takes 0, 1, or 2 params. Returning 0 instead.");
    //                 rethm.put("**TEMP**", 0D);
    //             }
    //             break;

    //         case "randd":
    //             if(args.length == 1)
    //                 rethm.put("**TEMP**", new Random().nextDouble() * args [0]);
    //             else if(args.length == 2)
    //                 rethm.put("**TEMP**", (new Random().nextDouble() + args[0]) * args[1]);
    //             else if(args.length == 0)
    //                 rethm.put("**TEMP**", Math.random());
    //             else{
    //                 Print.printw(name + " takes 0, 1, or 2 params. Returning a random num from 0- 1 instead.");
    //                 rethm.put("**TEMP**", 0D);
    //             }
    //             break;

    //         case "fac":
    //             double ret = 1;
    //             for(int x = 1; x <= (int)args[0]; x++)
    //                 ret *= x;
    //             rethm.put("**TEMP**", ret);
    //             break;
    //         default:
    //             throw new NotDefinedException("Cannot evaluate the InBuiltFunction '" + name + "' because it doesn't " +
    //                                            "have a defined way to compute it!");
    //     }
    //     return rethm;
    // }

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
        return new InBuiltFunction(name, help, syntax, priority, argsLength, funcObj);
    }

}