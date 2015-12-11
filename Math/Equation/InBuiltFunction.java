package Math.Equation;

import Math.MathObject;
import Math.Print;
import Math.Equation.Token.Type;
import Math.Exception.NotDefinedException;
import Math.Exception.InvalidArgsException;

import java.util.HashMap;
import java.util.Random;

public class InBuiltFunction extends Function {
    public static HashMap<String, InBuiltFunction> FUNCTIONS = new HashMap<String, InBuiltFunction>() {{
        put("+", new OperatorFunction("+", "Adds the first number to the second one.", "A + B"));
        put("-", new OperatorFunction("-", "Subtracts the first number to the second one.", "A - B"));
        put("*", new OperatorFunction("*", "Multiplies the first number to the second one.", "A * B"));
        put("/", new OperatorFunction("/", "Divides the first number to the second one.", "A / B"));
        put("^", new OperatorFunction("^", "Raises the first number to the second one.", "A ^ B"));
        put("sin", new InBuiltFunction("sin", "Returns the sin of the arguments.", "sin(args)"));
        put("cos", new InBuiltFunction("cos", "Returns the cos of the arguments.", "cos(args)"));
        put("tan", new InBuiltFunction("tan", "Returns the tan of the arguments.", "tan(args)"));
        put("csc", new InBuiltFunction("csc", "Returns the csc of the arguments.", "csc(args)"));
        put("sec", new InBuiltFunction("sec", "Returns the sec of the arguments.", "sec(args)"));
        put("cot", new InBuiltFunction("cot", "Returns the cot of the arguments.", "cot(args)"));
        put("sinh", new InBuiltFunction("sinh", "Returns the sinh of the arguments.", "sinh(args)"));
        put("cosh", new InBuiltFunction("cosh", "Returns the cosh of the arguments.", "cosh(args)"));
        put("tanh", new InBuiltFunction("tanh", "Returns the tanh of the arguments.", "tanh(args)"));
        put("asin", new InBuiltFunction("asin", "Returns the asin of the arguments.", "asin(args)"));
        put("acos", new InBuiltFunction("acos", "Returns the acos of the arguments.", "acos(args)"));
        put("atan", new InBuiltFunction("atan", "Returns the atan of the arguments.", "atan(args)"));
        put("abs", new InBuiltFunction("abs", null, null));
        put("ceil", new InBuiltFunction("ceil", null, null));
        put("floor", new InBuiltFunction("floor", null, null));
        put("hypot", new InBuiltFunction("hypot", null, null));
        put("ln", new InBuiltFunction("ln", null, null));
        put("log", new InBuiltFunction("log", null, null));
        put("round", new InBuiltFunction("round", null, null));
        put("sqrt", new InBuiltFunction("sqrt", null, null));
        put("degr", new InBuiltFunction("degr", null, null));
        put("radi", new InBuiltFunction("radi", null, null));
        put("randint", new InBuiltFunction("randint", null, null));
        put("rand", new InBuiltFunction("rand", null, null));
        put("fac", new InBuiltFunction("fac", null, null));
    }};


    public InBuiltFunction() {
        this(null, null, null);
    }

    public InBuiltFunction(String pVal, String pHelp, String pSyntax) {
        super(pVal, pHelp, pSyntax);
    }

    public static double exec(String name, EquationSystem pEq, Node pNode) throws NotDefinedException{
        if(FUNCTIONS.get(name) == null)
            throw new NotDefinedException("There is no InBuiltFunction '" + name + "' defined in FUNCTIONS!");
        return FUNCTIONS.get(name).exec(pEq, pNode);
    }
    @Override
    public double exec(EquationSystem pEq, Node pNode) throws NotDefinedException, InvalidArgsException {
        double[] args = evalNode(pEq, pNode);
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


            case "randint": case "randomint": case "ri":
                if(args.length == 0) return new Random().nextInt(100);
                if(args.length == 1) return new Random().nextInt((int)args[0]);
                if(args.length == 2) return new Random().nextInt((int)args[1]) + args[0];
                Print.printw(name + " takes 0, 1, or 2 params. Returning 0 instead.");
                return 0;

            case "rand": case "random": case "randd": case "randomdouble": case "r":
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