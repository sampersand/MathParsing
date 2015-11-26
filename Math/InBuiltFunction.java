import java.util.HashMap;
public class InBuiltFunction extends Function {
    public static final HashMap<String, InBuiltFunction> INBUILT_FUNCS = new HashMap<String, InBuiltFunction>(){{
        put("sin", new InBuiltFunction("sin"));
        put("cos", new InBuiltFunction("cos"));
        put("tan", new InBuiltFunction("tan"));
        put("sinh", new InBuiltFunction("sinh"));
        put("cosh", new InBuiltFunction("cosh"));
        put("tanh", new InBuiltFunction("tanh"));
        put("asin", new InBuiltFunction("asin"));
        put("acos", new InBuiltFunction("acos"));
        put("atan", new InBuiltFunction("atan"));
        put("abs", new InBuiltFunction("abs"));
        put("ceil", new InBuiltFunction("ceil"));
        put("floor", new InBuiltFunction("floor"));
        put("hypot", new InBuiltFunction("hypot"));
        put("ln", new InBuiltFunction("ln"));
        put("log", new InBuiltFunction("log"));
        put("random", new InBuiltFunction("random"));
        put("rand", new InBuiltFunction("rand"));
        put("round", new InBuiltFunction("round"));
        put("sqrt", new InBuiltFunction("sqrt"));
        put("degr", new InBuiltFunction("degr"));
        put("radi", new InBuiltFunction("radi"));
        put("fac", new InBuiltFunction("fac"));
    }};
    public InBuiltFunction(){
        this(null);
    }
    public InBuiltFunction(String pVal){
        super(pVal);
    }
    public static InBuiltFunction get(String pName) throws NullPointerException{
        return INBUILT_FUNCS.get(pName);
    }
    public double exec(Factors pFactors, Node pNode) throws NotDefinedException{
        double[] args = new double[pNode.size()];
        for(int i = 0; i < args.length; i++)
            args[i] = pFactors.eval(pNode.get(i));
        switch(fName) {
            case "sin":
                return Math.sin(args[0]);
            case "cos":
                return Math.cos(args[0]);
            case "tan":
                return Math.tan(args[0]);

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
            case "random":
            case "rand":
                return Math.random();
            case "round":
                return Math.round(args[0]);
            case "sqrt":
                return Math.sqrt(args[0]);
            case "degr":
                return Math.toDegrees(args[0]);
            case "radi":
                return Math.toRadians(args[0]);

            case "fac":
                double ret = 1;
                for(int x = 1; x <= (int)args[0]; x++)
                    ret *= x;
                return ret;

            default:
                throw new NotDefinedException("InBuiltFunction " + this + " doesn't have a defined way to compute it!");
        }
    }
}