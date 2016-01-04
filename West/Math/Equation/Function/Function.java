package West.Math.Equation.Function;

import West.Math.MathObject;
import West.Math.Equation.EquationSystem;
import West.Math.Equation.Equation;
import West.Math.Set.Node.TokenNode;
import West.Math.Set.Collection;
import static West.Math.Declare.*;
import java.util.HashMap;
import java.util.Random;
/**
 * A class that simulates both any kind of operation and any fuction in West.Math.
 * Simply put, anything that isn't a number or variable should be a function somehow.
 * For example, in <code>f(x)</code>, this class would represent f.
 * 
 * @author Sam Westerman
 * @version 0.90
 * @since 0.1
 */
public class Function implements MathObject {
    public static enum Type{
        UN_L, UN_R, BIN, NORM    
    };

    public interface FuncObj{
        public Double exec(Double[] args);
    }

    public static HashMap<String, Function> FUNCTIONS = new HashMap<String, Function>() {{
        put("=", new Function("=","Sets 'A' to 'B'","=(A, B)", 4,
            new Collection.Builder<Integer>().add(2).build(),
            a -> null //doesnt matter
            ));


        //Comparators

            //Normal Comparators
        put(">", new Function(">", "Checks if 'A' > 'B'", ">(A, B)", 3,
            new Collection.Builder<Integer>().add(2).build(),
            a -> a[0].compareTo(a[1]) == 1 ? 1D : Double.NaN
            ));

        put("<", new Function("<", "Checks if 'A' < 'B'", "<(A, B)", 3,
            new Collection.Builder<Integer>().add(2).build(),
            a -> a[0].compareTo(a[1]) == -1 ? 1D : Double.NaN
            ));

        put("≣", new Function("≣", "Checks if 'A' == 'B'", "≣(A, B)", 3,
            new Collection.Builder<Integer>().add(2).build(),
            a -> a[0].compareTo(a[1]) == 0 ? 1D : Double.NaN
            ));
        put("≥", new Function("≥", "Checks if 'A' ≥ 'B'", "≥(A, B)", 3,
            new Collection.Builder<Integer>().add(2).build(),
            a -> a[0].compareTo(a[1]) != -1 ? 1D : Double.NaN
            ));

        put("≤", new Function("≤", "Checks if 'A' ≤ 'B'", "≤(A, B)", 3,
            new Collection.Builder<Integer>().add(2).build(),
            a -> a[0].compareTo(a[1]) != 1 ? 1D : Double.NaN
            ));

        put("≠", new Function("≠", "Checks if 'A' ≠ 'B'", "≠(A, B)", 3,
            new Collection.Builder<Integer>().add(2).build(),
            a -> a[0].compareTo(a[1]) != 0 ? 1D : Double.NaN
            ));

        put("compare", new Function("compare", "See Double.compare(A, B)", "compare(A, B)", 3,
            new Collection.Builder<Integer>().add(2).build(),
            a -> new Double(a[0].compareTo(a[1]))
            ));


            //Boolean Comparators
        put("∧", new Function("∧", "Checks if 'A' ∧ 'B'", "∧(A, B)", 3,
            new Collection.Builder<Integer>().add(2).build(),
            a -> a[0].compareTo(0D) == 1 && a[1].compareTo(0D) == 1 ? 1D : Double.NaN
            ));

        put("∨", new Function("∨", "Checks if 'A' ∨ 'B'", "∨(A, B)", 3,
            new Collection.Builder<Integer>().add(2).build(),
            a -> a[0].compareTo(0D) == 1 || a[1].compareTo(0D) == 1 ? 1D : Double.NaN
            ));

        put("⊻", new Function("⊻", "Checks if 'A' ⊻ 'B'", "⊻(A, B)", 3,
            new Collection.Builder<Integer>().add(2).build(),
            a -> a[0].compareTo(0D) == 1 ^ a[1].compareTo(0D) == 1 ? 1D : Double.NaN
            ));

        put("⊼", new Function("⊼", "Checks if 'A' ⊼ 'B'", "⊼(A, B)", 3,
            new Collection.Builder<Integer>().add(2).build(),
            a -> !(a[0].compareTo(0D) == 1 && a[1].compareTo(0D) == 1) ? 1D : Double.NaN
            ));
        put("⊽", new Function("⊻", "Checks if 'A' ⊻ 'B'", "⊻(A, B)", 3,
            new Collection.Builder<Integer>().add(2).build(),
            a -> !(a[0].compareTo(0D) == 1 || a[1].compareTo(0D) == 1) ? 1D : Double.NaN
            ));



        // Standard math operators
        put("+", new Function("+", "Adds 'A' to 'B'", "+(A, B)", 0, 
            new Collection.Builder<Integer>().add(2).build(),
            a -> a[0] + a[1]
            ));

        put("-", new Function("-", "Subtracts 'A' to 'B'", "-(A, B)", 0,
            new Collection.Builder<Integer>().add(2).build(),
            a -> a[0] - a[1]
            ));

        put("*", new Function("*", "Multiplies 'A' to 'B'", "*(A, B)", 1,
            new Collection.Builder<Integer>().add(2).build(),
            a -> a[0] * a[1]
            ));

        put("/", new Function("/", "Divides 'A' to 'B'", "/(A, B)", 1,
            new Collection.Builder<Integer>().add(2).build(),
            a -> a[0] / a[1]
            ));

        put("%", new Function("%", "'A' Modulo 'B'", "%(A, B)", 1,
            new Collection.Builder<Integer>().add(2).build(),
            a -> a[0] % a[1]
            ));
        put("^", new Function("^", "Raises 'A' to 'B'", "^(A, B)", 2,
            new Collection.Builder<Integer>().add(2).build(),
            a -> Math.pow(a[0],a[1])
            ));



        //Trigonometric functions
        put("sin", new Function("sin", "sin of 'A'", "sin(A)", -1,
            new Collection.Builder<Integer>().add(1).build(),
            a -> Math.sin(a[0])
            ));

        put("cos", new Function("cos", "cos of 'A'", "cos(A)", -1,
            new Collection.Builder<Integer>().add(1).build(),
            a -> Math.cos(a[0])
            ));

        put("tan", new Function("tan", "tan of 'A'", "tan(A)", -1,
            new Collection.Builder<Integer>().add(1).build(),
            a -> Math.tan(a[0])
            ));

        put("csc", new Function("csc", "csc of 'A'", "csc(A)", -1,
            new Collection.Builder<Integer>().add(1).build(),
            a -> 1D / Math.sin(a[0])
            ));

        put("sec", new Function("sec", "sec of 'A'", "sec(A)", -1,
            new Collection.Builder<Integer>().add(1).build(),
            a -> 1D / Math.cos(a[0])
            ));

        put("cot", new Function("cot", "cot of 'A'", "cot(A)", -1,
            new Collection.Builder<Integer>().add(1).build(),
            a -> 1D / Math.tan(a[0])
            ));

        put("sinh", new Function("sinh", "sinh of 'A'", "sinh(A)", -1,
            new Collection.Builder<Integer>().add(1).build(),
            a -> Math.sinh(a[0])
            ));

        put("cosh", new Function("cosh", "cosh of 'A'", "cosh(A)", -1,
            new Collection.Builder<Integer>().add(1).build(),
            a -> Math.cosh(a[0])
            ));

        put("tanh", new Function("tanh", "tanh of 'A'", "tanh(A)", -1,
            new Collection.Builder<Integer>().add(1).build(),
            a -> Math.tanh(a[0])
            ));

        put("asin", new Function("asin", "asin of 'A'", "asin(A)", -1,
            new Collection.Builder<Integer>().add(1).build(),
            a -> Math.asin(a[0])
            ));

        put("acos", new Function("acos", "acos of 'A'", "acos(A)", -1,
            new Collection.Builder<Integer>().add(1).build(),
            a -> Math.acos(a[0])
            ));

        put("atan", new Function("atan", "atan of 'A'", "atan(A)", -1,
            new Collection.Builder<Integer>().add(1).build(),
            a -> Math.atan(a[0])
            ));



        //Shifting
        put("≫", new Function("≫", "'A' Shifted to the right 'B' bits", "≫(A, B)", -1,
            new Collection.Builder<Integer>().add(2).build(),
            a -> Integer.valueOf(a[0].intValue() >> a[1].intValue()).doubleValue()
            ));
        put("≪", new Function("≪", "'A' Shifted to the left 'B' bits", "≪(A, B)", -1,
            new Collection.Builder<Integer>().add(2).build(),
            a -> Integer.valueOf(a[0].intValue() << a[1].intValue()).doubleValue()
            ));



        //Misc math functions
        put("abs", new Function("abs", "absolute value of 'A'", "abs(A)", -1,
            new Collection.Builder<Integer>().add(1).build(),
            a -> Math.abs(a[0])
            ));

        put("ceil", new Function("ceil", "closest integer greater than 'A'", "ceil(A)", -1,
            new Collection.Builder<Integer>().add(1).build(),
            a -> Math.ceil(a[0])
            ));

        put("floor", new Function("floor", "closest integer less than 'A'", "floor(A)", -1,
            new Collection.Builder<Integer>().add(1).build(),
            a -> Math.floor(a[0])
            ));

        put("hypot", new Function("hypot", "hypotenuse of 'A' and 'B' ( √[A² + B²] )", "hypot(A, B)", -1,
            new Collection.Builder<Integer>().add(2).build(),
            a -> Math.hypot(a[0], a[1])
            ));

        put("ln", new Function("ln", "natural log of 'A'", "ln(A)", -1,
            new Collection.Builder<Integer>().add(1).build(),
            a -> Math.log(a[0])
            ));

        put("log", new Function("log", "log base 10 of 'A'", "log(A)", -1,
            new Collection.Builder<Integer>().add(1).build(),
            a -> Math.log10(a[0])
            ));

        put("round", new Function("round", "rounds 'A' to the nearest integer", "round(A)", -1,
            new Collection.Builder<Integer>().add(1).build(),
            a -> Long.valueOf(Math.round(a[0])).doubleValue()
            ));

        put("sqrt", new Function("sqrt", "the square root (√) of 'A'", "sqrt(A)", -1,
            new Collection.Builder<Integer>().add(1).build(),
            a -> Math.sqrt(a[0])
            ));

        put("degrees", new Function("degr", "turns 'A' into degrees (from radians)", "degrees(A)", -1,
            new Collection.Builder<Integer>().add(1).build(),
            a -> Math.toDegrees(a[0])
            ));

        put("radians", new Function("radi", "turns 'A' into radians (from degrees)", "radians(A)", -1,
            new Collection.Builder<Integer>().add(1).build(),
            a -> Math.toRadians(a[0])
            ));

        put("ri", new Function("randi", "random integer from [0, 100], [0, 'A'], or ['A', 'B']", "ri(A, B)", -1,
            new Collection.Builder<Integer>().add(0).add(1).add(2).build(),
            a -> {
               if(a.length == 0)
                    return Integer.valueOf(new Random().nextInt(100)).doubleValue();
                else if(a.length == 1)
                    return Integer.valueOf(new Random().nextInt(a[0].intValue())).doubleValue();
                else if(a.length == 2)
                    return Integer.valueOf(new Random().nextInt(a[1].intValue())+a[0].intValue()).doubleValue();
                else
                    throw new UnsupportedOperationException();
                }
            ));

        put("rd", new Function("randd", "random double from [0, 1), [0, 'A'), or ['A', 'B')", "rd(A, B)", -1,
            new Collection.Builder<Integer>().add(0).add(1).add(2).build(),
            a -> {
                if(a.length == 1)
                    return new Random().nextDouble() * a[0];
                else if(a.length == 2)
                    return (new Random().nextDouble() + a[0]) * a[1];
                else if(a.length == 0)
                    return Math.random();
                else
                    throw new UnsupportedOperationException();
                }
            ));
 

        put("fac", new Function("fac", "factorial of 'A'", "fac(A)", -1,
            new Collection.Builder<Integer>().add(1).build(),
            a -> {
                Double ret = 1D;
                for(Double x = a[0]; x > 0; x--)
                    ret *= x;
                return ret;
                }
            ));

        put("neg", new Function("negate", "'A' * -1", "neg(A)", -1,
            new Collection.Builder<Integer>().add(1).build(),
            a -> -1 * a[0]
            ));

        put("sign", new Function("sign", "1 if 'A' > 0, -1 if 'A' < 0, and 0 if 'A' == 0", "neg(A)", -1,
            new Collection.Builder<Integer>().add(1).build(),
            a -> Integer.valueOf(a[0].compareTo(0D)).doubleValue()
            ));

        put("graph", new GraphFunction());

        put("", new Function("","","", -1,
            new Collection.Builder<Integer>().add(1).build(),
            a -> a[0]
            ));
    }};
    public static String isBinOper(String s){
        return Equaiton.isInLast(s);
    }
    /**
     * A String that holds either the function name ({@link Function}) or the file name ({@link CustomFunction}).
     */
    protected String name;

    /**
     * The Help text for this function
     */
    protected String help;

    /**
     * The Syntax text for this function.
     */
    protected String syntax;

    protected int priority;

    protected Collection<Integer> argsLength;

    protected FuncObj funcObj;

    /**
     * The default constructor for the Function class. Instatiates {@link #name}, {@link #help}, and {@link #syntax} as
     * empty strings.
     */
    public Function() {
        this(null, null, null, -1, null, null);
    }
    
    /**
     * The main cosntructor for the Function class. All it does is instantiates name, help, and syntax.
     * @param pName         The name of the function - serves as an identifier for {@link Function}s, and as the
     *                      filename for {@link CustomFunction}s. Cannot be null.
     * @param pHelp         The help string for the function. Cannot be null.
     * @param pSyntax       The syntax string for the function. Cannot be null.
     * @throws IllegalArgumentException When either name, help, and / or syntax is null.
     */
    public Function(String pName,
                    String pHelp,
                    String pSyntax,
                    int pPriority,
                    Collection<Integer> pArgsLength,
                    Type pType,
                    FuncObj pFuncObj) throws IllegalArgumentException{
        assert pName != null;
        assert pHelp != null;
        assert pSyntax != null;
        assert pType != null;
        assert pArgsLength.size() >= 0;
        funcObj = pFuncObj;
        name = pName;
        help = pHelp;
        syntax = pSyntax;
        priority = pPriority;
        type = pType;
        argsLength = pArgsLength;
        if(funcObj == null)
            funcObj = a -> null;
    }

    /**
     * Returns this class's {@link #name}.
     * @return this class's {@link #name}.
     */
    public final String name() {
        return name;
    }

    /**
     * Returns the "help" value for this function.
     * @return A String representing the "help" value.
     */
    public String help() {
        return help;
    }

    /**
     * Returns the "help" value for this function.
     * @return A String representing the "syntax" value.
     */
    public String syntax() {
        return syntax;
    }

    public int priority() {
        return priority;
    }
    public Collection<Integer> argsLength() {
        return argsLength;
    }

    public Type type() {
        return type;
    }
    public FuncObj funcObj() {
        return funcObj;
    }

    /**
     * This thing takes a {@link Node} (usually the node from {@link #exec(EquationSystem,Node) exec}), and returns an
     * array of the numerical values of each {@link Node#elements() subNode}.
     * @param pEqSys        The {@link EquationSystem} that will be used when evaluating <code>pNode</code>.
     * @param pNode         The node to be evaluated.
     * @return An array of doubles, with each position corresponding to the value of each Node of that position in 
     *         {@link Node#elements() pNode's elements()}.
     */
    public Object[] evalNode(final EquationSystem pEqSys, TokenNode pNode) {
        Double[] retd = new Double[pNode.size()];
        HashMap<String, Double> rethm = new HashMap<String, Double>();
        for(int i = 0; i < pNode.size(); i++) {
            rethm.putAll(((TokenNode)pNode.elements().get(i)).eval(pEqSys));
            retd[i] = rethm.get(pNode.elements().get(i).toString());
        }
        return new Object[]{retd, rethm};
    }


    /**
     * Takes the parameter {@link Node} (and {@link EquationSystem}, performs whatever this function is defined to do,
     * and returns the result.
     * @param pEqSys        An {@link EquationSystem} that contains all relevant information about
     *                      {@link Equation Equations} and {@link Function Functions} is stored.
     * @param pNode         The {@link Node} that is going to be solved.
     * @return A double representing the value of <code>pNode</code>, when solved for with <code>pEqSys</code>.
     * @throws IllegalArgumentException   Thrown when the function required parameters, and the ones passed aren't right.
     */
    public HashMap<String,Double> exec(final EquationSystem pEqSys, TokenNode pNode) {
        Object[] rargs = evalNode(pEqSys, pNode);
        Double[] args = (Double[])rargs[0];
        assert argsLength.contains(args.length) || argsLength.contains(-1) :
        "'" +name+ "' got incorrect args. Allowed args: " + argsLength + ", inputted args = '"+pNode + "'("+args.length+")";
        HashMap<String, Double> rethm = (HashMap<String, Double>)rargs[1];
        return addArgs(rethm, pNode.toString(), funcObj.exec(args));
    }

    public static HashMap<String,Double> exec(String pStr, final EquationSystem pEqSys, TokenNode pNode) {
        Function f;
        assert f = FUNCTIONS.get(f)
    }

    public HashMap<String, Double> addArgs(HashMap<String, Double> hm, String key, Double val){
        hm.put(key, val);
        return hm;
    }

    @Override
    public boolean equals(Object pObj){
        if(pObj == null || !(pObj instanceof  Function))
            return false;
        if(this == (Function)pObj)
            return true;
        return name.equals(((Function)pObj).name()) &&
               help.equals(((Function)pObj).help()) &&
               syntax.equals(((Function)pObj).syntax());
    }

    /**
     * Gets the inverse of this function - that is, what function should be done to undo this one. <br>The inverse of 
     * <code>+</code> is <code>-</code>, and the inverse of <code>cos</code> is <code>arccos</code>.
     * @return The inverse of this function.
     * @deprecated Not defined yet, will be in the future.
     */
    public Function inverse() throws NullPointerException{
        throw new NullPointerException();
    }


}