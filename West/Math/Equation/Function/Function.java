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
 * @version 1.0
 * @since 0.1
 */
public class Function implements MathObject {
    private static Double NaN = Double.NaN;
    public static enum Type{
        UNL,
        UNR,
        BIN,
        ASSIGN,
        NORM
    } //Assign extends Bin

    @FunctionalInterface
    public interface FuncObj{
        public Double exec(Double[] args);
    }

    public static final int DEFAULT_PRIORITY = 100; //TODO: FIX

    public static Collection<Function> FUNCTIONS = new Collection<Function>() {{
        // =                : 0
        // ()               : 1
        // ∧, ∨, ⊻, ⊼, ⊽    : 2
        // >, <, ≣, ≥, ≤, ≠ : 3
        // ^, ≫, ≪          : 4
        // *, /, %          : 5
        // +, -             : 6
        // unary !          : 7
        // unary –          : 8
        add(new Function(new Collection<String>(){{add("=");}},
            "Sets 'A' to 'B'", "A = B",
            0,
            Type.ASSIGN,
            new Collection.Builder<Integer>().add(2).build(), 
            a -> a[0] //doesnt matter
            ));
        add(new Function(new Collection<String>(){{add("");}},
            "Parenthesis, literally: ('A')", "(A)",
            DEFAULT_PRIORITY,
            Type.NORM,
            new Collection.Builder<Integer>().add(1).build(),
            a -> a[0]
            ));

        //Comparators

            //Normal Comparators
        add(new Function(new Collection<String>(){{add(">");}},
            "Checks if 'A' > 'B'", "A > B",
            3,
            Type.BIN,
            new Collection.Builder<Integer>().add(2).build(),
            a -> /*!a[0].isNaN() && !a[1].isNaN() &&*/
                  a[0].compareTo(a[1]) == 1 ? 1D : NaN
            ));

        add(new Function(new Collection<String>(){{add("<");}},
            "Checks if 'A' < 'B'", "A < B",
            3,
            Type.BIN,
            new Collection.Builder<Integer>().add(2).build(),
            a -> /*!a[0].isNaN() && !a[1].isNaN() &&*/
                  a[0].compareTo(a[1]) == -1 ? 1D : NaN
            ));

        add(new Function(new Collection<String>(){{add("≣");}},
            "Checks if 'A' == 'B'", "A ≣ B",
            3,
            Type.BIN,
            new Collection.Builder<Integer>().add(2).build(),
            a -> /*!a[0].isNaN() && !a[1].isNaN() &&*/
                  a[0].compareTo(a[1]) == 0 ? 1D : NaN
            ));

        add(new Function(new Collection<String>(){{add("≥");}},
            "Checks if 'A' ≥ 'B'", "A ≥ B",
            3,
            Type.BIN,
            new Collection.Builder<Integer>().add(2).build(),
            a -> /*!a[0].isNaN() && !a[1].isNaN() &&*/
                  a[0].compareTo(a[1]) != -1 ? 1D : NaN
            ));

        add(new Function(new Collection<String>(){{add("≤");}},
            "Checks if 'A' ≤ 'B'", "A ≤ B",
            3,
            Type.BIN,
            new Collection.Builder<Integer>().add(2).build(),
            a -> /*!a[0].isNaN() && !a[1].isNaN() &&*/
                  a[0].compareTo(a[1]) != 1 ? 1D : NaN
            ));

        add(new Function(new Collection<String>(){{add("≠");}},
            "Checks if 'A' ≠ 'B'", "A ≠ B",
            3,
            Type.BIN,
            new Collection.Builder<Integer>().add(2).build(),
            a -> /*!a[0].isNaN() && !a[1].isNaN() &&*/
                  a[0].compareTo(a[1]) != 0 ? 1D : NaN
            ));

        add(new Function(new Collection<String>(){{add("compare");}},
            "See Double.compare(A, B)", "compare(A, B)",
            DEFAULT_PRIORITY,
            Type.NORM,
            new Collection.Builder<Integer>().add(2).build(),
            a -> new Double(a[0].compareTo(a[1]))
            ));


            //Boolean Comparators
        add(new Function(new Collection<String>(){{add("∧");}},
            "Checks if 'A' ∧ 'B'", "A ∧ B",
            2,
            Type.BIN,
            new Collection.Builder<Integer>().add(2).build(),
            a -> !a[0].isNaN() && !a[1].isNaN() &&
                  a[0].compareTo(0D) == 1 && a[1].compareTo(0D) == 1 ? 1D : NaN
            ));

        add(new Function(new Collection<String>(){{add("∨");}},
            "Checks if 'A' ∨ 'B'", "A ∨ B",
            2,
            Type.BIN,
            new Collection.Builder<Integer>().add(2).build(),
            a -> !(a[0].isNaN() && a[1].isNaN()) &&
                  (a[0].compareTo(0D) == 1 || a[1].compareTo(0D) == 1) ? 1D : NaN
            ));

        add(new Function(new Collection<String>(){{add("⊻");}},
            "Checks if 'A' ⊻ 'B'", "A ⊻ B",
            2,
            Type.BIN,
            new Collection.Builder<Integer>().add(2).build(),
            a -> !a[0].isNaN() && !a[1].isNaN() &&
                  a[0].compareTo(0D) == 1 ^ a[1].compareTo(0D) == 1 ? 1D : NaN
            ));

        add(new Function(new Collection<String>(){{add("⊼");}},
            "Checks if 'A' ⊼ 'B'", "A ⊼ B",
            2,
            Type.BIN,
            new Collection.Builder<Integer>().add(2).build(),
            a -> !a[0].isNaN() && !a[1].isNaN() &&
                 a[0].compareTo(0D) != 1 && a[1].compareTo(0D) != 1 ? 1D : NaN
            ));
        add(new Function(new Collection<String>(){{add("⊻");}},
            "Checks if 'A' ⊻ 'B'", "A ⊻ B",
            2,
            Type.BIN,
            new Collection.Builder<Integer>().add(2).build(),
            a -> !a[0].isNaN() && !a[1].isNaN() &&
                  a[0].compareTo(0D) != 1 && a[1].compareTo(0D) != 1 ? 1D : NaN
            ));



        // Standard math operators
        add(new Function(new Collection<String>(){{add("+");}},
            "Adds 'A' to 'B'", "A + B", 
            4,
            Type.BIN,
            new Collection.Builder<Integer>().add(2).build(),
            a -> a[0] + a[1]
            ));

        add(new Function(new Collection<String>(){{add("-");}},
            "Subtracts 'A' to 'B'", "A - B",
            4,
            Type.BIN,
            new Collection.Builder<Integer>().add(2).build(),
            a -> a.length == 1 ? 0 - a[0] : a[0] - a[1]
            ));

        add(new Function(new Collection<String>(){{add("*");add("·");add("×");}},
            "Multiplies 'A' to 'B'", "A * B",
            5,
            Type.BIN,
            new Collection.Builder<Integer>().add(2).build(),
            a -> a[0] * a[1]
            ));
        add(new Function(new Collection<String>(){{add("/");add("÷");}},
            "Divides 'A' to 'B'", "A / B",
            5,
            Type.BIN,
            new Collection.Builder<Integer>().add(2).build(),
            a -> a[0] / a[1]
            ));

        add(new Function(new Collection<String>(){{add("%");}},
            "'A' Modulo 'B'", "A % B",
            5,
            Type.BIN,
            new Collection.Builder<Integer>().add(2).build(),
            a -> a[0] % a[1]
            ));
        add(new Function(new Collection<String>(){{add("^");}},
            "Raises 'A' to 'B'", "A ^ B",
            6,
            Type.BIN,
            new Collection.Builder<Integer>().add(2).build(),
            a -> Math.pow(a[0],a[1])
            ));



        //UNARY


        //UN_L
        add(new Function(new Collection<String>(){{add("–");}}, //negation
            "negate 'A'", "–(A)",
            8,
            Type.UNL,
            new Collection.Builder<Integer>().add(1).build(),
            a -> -1 * a[0]
            ));
        //UN_R
        add(new Function(new Collection<String>(){{add("!");}}, //negation
            "factorial 'A'", "!(A)",
            7,
            Type.UNR,
            new Collection.Builder<Integer>().add(1).build(),
            a -> {
                    Double ret = 1D;
                    for(Double x = a[0]; x > 0; x--)
                        ret *= x;
                    return ret;
                }
            ));


        //Trigonometric functions
        add(new Function(new Collection<String>(){{add("sin");}},
            "sin of 'A'", "sin(A)",
            DEFAULT_PRIORITY,
            Type.NORM,
            new Collection.Builder<Integer>().add(1).build(),
            a -> Math.sin(a[0])
            ));

        add(new Function(new Collection<String>(){{add("cos");}},
            "cos of 'A'", "cos(A)",
            DEFAULT_PRIORITY,
            Type.NORM,
            new Collection.Builder<Integer>().add(1).build(),
            a -> Math.cos(a[0])
            ));

        add(new Function(new Collection<String>(){{add("tan");}},
            "tan of 'A'", "tan(A)",
            DEFAULT_PRIORITY,
            Type.NORM,
            new Collection.Builder<Integer>().add(1).build(),
            a -> Math.tan(a[0])
            ));

        add(new Function(new Collection<String>(){{add("csc");}},
            "csc of 'A'", "csc(A)",
            DEFAULT_PRIORITY,
            Type.NORM,
            new Collection.Builder<Integer>().add(1).build(),
            a -> 1D / Math.sin(a[0])
            ));

        add(new Function(new Collection<String>(){{add("sec");}},
            "sec of 'A'", "sec(A)",
            DEFAULT_PRIORITY,
            Type.NORM,
            new Collection.Builder<Integer>().add(1).build(),
            a -> 1D / Math.cos(a[0])
            ));

        add(new Function(new Collection<String>(){{add("cot");}},
            "cot of 'A'", "cot(A)",
            DEFAULT_PRIORITY,
            Type.NORM,
            new Collection.Builder<Integer>().add(1).build(),
            a -> 1D / Math.tan(a[0])
            ));

        add(new Function(new Collection<String>(){{add("sinh");}},
            "sinh of 'A'", "sinh(A)",
            DEFAULT_PRIORITY,
            Type.NORM,
            new Collection.Builder<Integer>().add(1).build(),
            a -> Math.sinh(a[0])
            ));

        add(new Function(new Collection<String>(){{add("cosh");}},
            "cosh of 'A'", "cosh(A)",
            DEFAULT_PRIORITY,
            Type.NORM,
            new Collection.Builder<Integer>().add(1).build(),
            a -> Math.cosh(a[0])
            ));

        add(new Function(new Collection<String>(){{add("tanh");}},
            "tanh of 'A'", "tanh(A)",
            DEFAULT_PRIORITY,
            Type.NORM,
            new Collection.Builder<Integer>().add(1).build(),
            a -> Math.tanh(a[0])
            ));

        add(new Function(new Collection<String>(){{add("asin");}},
            "asin of 'A'", "asin(A)",
            DEFAULT_PRIORITY,
            Type.NORM,
            new Collection.Builder<Integer>().add(1).build(),
            a -> Math.asin(a[0])
            ));

        add(new Function(new Collection<String>(){{add("acon");}},
            "acos of 'A'", "acos(A)",
            DEFAULT_PRIORITY,
            Type.NORM,
            new Collection.Builder<Integer>().add(1).build(),
            a -> Math.acos(a[0])
            ));

        add(new Function(new Collection<String>(){{add("atan");}},
            "atan of 'A'", "atan(A)",
            DEFAULT_PRIORITY,
            Type.NORM,
            new Collection.Builder<Integer>().add(1).build(),
            a -> Math.atan(a[0])
            ));



        //Shifting
        add(new Function(new Collection<String>(){{add("≫");}},
            "'A' Shifted to the right 'B' bits", "≫(A, B)",
            5,
        Type.BIN,
            new Collection.Builder<Integer>().add(2).build(),
            a -> Integer.valueOf(a[0].intValue() >> a[1].intValue()).doubleValue()
            ));
        add(new Function(new Collection<String>(){{add("≪");}},
            "'A' Shifted to the left 'B' bits", "≪(A, B)",
            5,
        Type.BIN,
            new Collection.Builder<Integer>().add(2).build(),
            a -> Integer.valueOf(a[0].intValue() << a[1].intValue()).doubleValue()
            ));



        //Misc math functions
        add(new Function(new Collection<String>(){{add("abs");}},
            "absolute value of 'A'", "abs(a)",
            DEFAULT_PRIORITY,
            Type.NORM,
            new Collection.Builder<Integer>().add(1).build(),
            a -> Math.abs(a[0])
            ));

        add(new Function(new Collection<String>(){{add("ceil");}},
            "closest integer greater than 'A'", "ceil(A)",
            DEFAULT_PRIORITY,
            Type.NORM,
            new Collection.Builder<Integer>().add(1).build(),
            a -> Math.ceil(a[0])
            ));

        add(new Function(new Collection<String>(){{add("floor");}},
            "closest integer less than 'A'", "floor(A)",
            DEFAULT_PRIORITY,
            Type.NORM,
            new Collection.Builder<Integer>().add(1).build(),
            a -> Math.floor(a[0])
            ));

        add(new Function(new Collection<String>(){{add("hypot");}},
            "hypotenuse of 'A' and 'B' ( √[A² + B²] )", "hypot(A, B)",
            DEFAULT_PRIORITY,
            Type.NORM,
            new Collection.Builder<Integer>().add(-1).build(),
            a -> {
                    if(a.length <= 1)
                        return NaN;
                    Double ret = 0D;
                    for(double d : a)
                        ret += d * d;
                    return Math.pow(ret,0.5);
                }
            ));

        add(new Function(new Collection<String>(){{add("ln");}},
            "natural log of 'A'", "ln(A)",
            DEFAULT_PRIORITY,
            Type.NORM,
            new Collection.Builder<Integer>().add(1).build(),
            a -> Math.log(a[0])
            ));

        add(new Function(new Collection<String>(){{add("log");}},
            "log base 10 of 'A'", "log(A)",
            DEFAULT_PRIORITY,
            Type.NORM,
            new Collection.Builder<Integer>().add(1).build(),
            a -> Math.log10(a[0])
            ));
        add(new Function(new Collection<String>(){{add("round");}},
            "rounds 'A' to the nearest integer", "round(A)",
            DEFAULT_PRIORITY,
            Type.NORM,
            new Collection.Builder<Integer>().add(1).build(),
            a -> Long.valueOf(Math.round(a[0])).doubleValue()
            ));

        add(new Function(new Collection<String>(){{add("sqrt");}},
            "the square root (√) of 'A'", "sqrt(A)",
            DEFAULT_PRIORITY,
            Type.NORM,
            new Collection.Builder<Integer>().add(1).build(),
            a -> Math.sqrt(a[0])
            ));

        add(new Function(new Collection<String>(){{add("√");}},
            "the square root (√) of 'A'", "√(A)",
            DEFAULT_PRIORITY,
            Type.NORM,
            new Collection.Builder<Integer>().add(1).build(),
            a -> Math.sqrt(a[0])
            ));

        add(new Function(new Collection<String>(){{add("deg");}},
            "turns 'A' into degrees (from radians)", "degrees(A)",
            DEFAULT_PRIORITY,
            Type.NORM,
            new Collection.Builder<Integer>().add(1).build(),
            a -> Math.toDegrees(a[0])
            ));

        add(new Function(new Collection<String>(){{add("rad");}},
            "turns 'A' into radians (from degrees)", "radians(A)",
            DEFAULT_PRIORITY,
            Type.NORM,
            new Collection.Builder<Integer>().add(1).build(),
            a -> Math.toRadians(a[0])
            ));

        add(new Function(new Collection<String>(){{add("randi");}},
            "random integer from [0, 100], [0, 'A'], or ['A', 'B']", "ri(A, B)",
            DEFAULT_PRIORITY,
            Type.NORM,
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

        add(new Function(new Collection<String>(){{add("rand");}},
            "random double from [0, 1), [0, 'A'), or ['A', 'B')", "rd(A, B)",
            DEFAULT_PRIORITY,
            Type.NORM,
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
 

        add(new Function(new Collection<String>(){{add("fac");}},
            "factorial of 'A'", "fac(A)",
            DEFAULT_PRIORITY,
            Type.NORM,
            new Collection.Builder<Integer>().add(1).build(),
            a -> {
                Double ret = 1D;
                for(Double x = a[0]; x > 0; x--)
                    ret *= x;
                return ret;
                }
            ));

        add(new Function(new Collection<String>(){{add("negte");}},
            "'A' * -1", "neg(A)",
            DEFAULT_PRIORITY,
            Type.NORM,
            new Collection.Builder<Integer>().add(1).build(),
            a -> -1 * a[0]
            ));

        add(new Function(new Collection<String>(){{add("sign");}},
            "1 if 'A' > 0, -1 if 'A' < 0, and 0 if 'A' == 0", "sign(A)",
            DEFAULT_PRIORITY,
            Type.NORM,
            new Collection.Builder<Integer>().add(1).build(),
            a -> Integer.valueOf(a[0].compareTo(0D)).doubleValue()
            ));

    }};

    public static Collection<String> BIN_OPERS = new Collection<String>(){{
            for(Function f : FUNCTIONS)
                if(f.type() == Type.BIN || f.type() == Type.ASSIGN)
                    for(String name : f.names())
                        add(name);
        }};
    public static String isBinOper(String s){
        if(s.matches(".*E[-\\d.]*$"))
            return null;
        return Equation.isInLast(s, BIN_OPERS);
    }

    public static Collection<String> ASSIGN_OPERS = new Collection<String>(){{
            for(Function f : FUNCTIONS)
                if(f.type() == Type.ASSIGN)
                    for(String name : f.names())
                        add(name);
        }};

    public static String isAssign(String s){
        return Equation.isInLast(s, ASSIGN_OPERS);
    }

    public static Collection<String> UNL = new Collection<String>(){{
            for(Function f : FUNCTIONS)
                if(f.type() == Type.UNL)
                    for(String name : f.names())
                        add(name);
        }};

    public static String isUNL(String s){
        return Equation.isInFirst(s, UNL);
    }

    public static Collection<String> UNR = new Collection<String>(){{
            for(Function f : FUNCTIONS)
                if(f.type() == Type.UNR)
                    for(String name : f.names())
                        add(name);
        }};
    public static String isUNR(String s){
        return Equation.isInLast(s, UNR);
    }


    public static Function get(String name){
        for(Function f : FUNCTIONS)
            if(f.names().contains(name))
                return f;
        return West.Math.Equation.Token.isDelim(name) == null ? null : get(""); // if its a delim, then return "()" Func
    }
    /**
     * A String that holds either the function name ({@link Function}) or the file name ({@link CustomFunction}).
     */
    protected Collection<String> names;

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

    protected Type type;

    protected FuncObj funcObj;

    /**
     * The default constructor for the Function class. Instatiates {@link #names}, {@link #help}, and {@link #syntax} as
     * empty strings.
     */
    public Function() {
        this(null, null, null, -1, null, null, null);
    }
    
    /**
     * The main cosntructor for the Function class. All it does is instantiates names, help, and syntax.
     * @param pName         The names of the function - serves as an identifier for {@link Function}s, and as the
     *                      filename for {@link CustomFunction}s. Cannot be null.
     * @param pHelp         The help string for the function. Cannot be null.
     * @param pSyntax       The syntax string for the function. Cannot be null.
     * @throws IllegalArgumentException When either names, help, and / or syntax is null.
     */
    public Function(Collection<String> pName,
                    String pHelp,
                    String pSyntax,
                    int pPriority,
                    Type pType,
                    Collection<Integer> pArgsLength,
                    FuncObj pFuncObj) throws IllegalArgumentException{
        // assert pName != null;
        // assert pHelp != null;
        // assert pSyntax != null;
        // assert pType != null;
        // assert pArgsLength.size() >= 0;
        funcObj = pFuncObj;
        names = pName;
        help = pHelp;
        syntax = pSyntax;
        type = pType;
        priority = pPriority;
        argsLength = pArgsLength;
        if(funcObj == null)
            funcObj = a -> null;
    }

    /**
     * Returns this class's {@link #names}.
     * @return this class's {@link #names}.
     */
    public final Collection<String> names() {
        return names;
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

    // /**
    //  * This thing takes a {@link Node} (usually the node from {@link #exec(EquationSystem,Node) exec}), and returns an
    //  * array of the numerical values of each {@link Node#elements() subNode}.
    //  * @param pEqSys        The {@link EquationSystem} that will be used when evaluating <code>pNode</code>.
    //  * @param pNode         The node to be evaluated.
    //  * @return An array of doubles, with each position corresponding to the value of each Node of that position in 
    //  *         {@link Node#elements() pNode's elements()}.
    //  */
    // public Object[] evalNode(final EquationSystem pEqSys, TokenNode pNode) {
    //     if(get(pNode.toString()).type == Type.ASSIGN){
    //         assert pNode.size() == 2 : "size != 2: " + pNode;
    //         // return new Object[]{null, pNode.}
    //     }
    //     Double[] retd = new Double[pNode.size()];
    //     HashMap<String, Double> rethm = new HashMap<String, Double>();
    //     for(int i = 0; i < pNode.size(); i++) {
    //         rethm.putAll(((TokenNode)pNode.elements().get(i)).eval(pEqSys));
    //         retd[i] = rethm.get(pNode.elements().get(i).toString());
    //     }
    //     return new Object[]{retd, rethm};
    // }


    /**
     * Takes the parameter {@link Node} (and {@link EquationSystem}, performs whatever this function is defined to do,
     * and returns the result.
     * @param pEqSys        An {@link EquationSystem} that contains all relevant information about
     *                      {@link Equation Equations} and {@link Function Functions} is stored.
     * @param pNode         The {@link Node} that is going to be solved.
     * @return A double representing the value of <code>pNode</code>, when solved for with <code>pEqSys</code>.
     * @throws IllegalArgumentException   Thrown when the function required parameters, and the ones passed aren't right.
     */
    public HashMap<String,Double> exec(HashMap<String, Double> ret, final EquationSystem pEqSys, TokenNode pNode) {
        if(type == Type.ASSIGN){
            assert pNode.size() == 2;
            ret.putAll(pNode.get(1).eval(ret, pEqSys));
            ret.put(pNode.get(0).toString(),ret.get(pNode.get(1).toString()));
        }
        else
            for(West.Math.Set.Node.Node<?, ?> n : pNode)
                ret.putAll(((TokenNode)n).eval(ret, pEqSys));

        Double[] args = new Double[pNode.size()];

        for(int i = 0; i < args.length; i++)
            args[i] = ret.get(pNode.get(i).toString());

        assert argsLength.contains(args.length) || argsLength.contains(-1) :
        "'" +names+ "' got bad args. Allowed args: " + argsLength + ", inputted args = '"+ pNode + "'("+args.length+")";
        try{
            return addArgs(ret, pNode.toString(), funcObj.exec(args));
        } catch (java.lang.NullPointerException n ){
            System.err.println("An error happened while executing '" + names + "'");
            System.err.println("\tThe HashMap was: " + ret);
            System.err.println("\tThe EquationSystem was: " + pEqSys);
            System.err.println("\tThe TokenNode was: " + pNode);
            throw n;
        }
    }

    public static HashMap<String,Double> exec(HashMap<String, Double> ret,
                                              String pStr,
                                              final EquationSystem pEqSys,
                                              TokenNode pNode) {
        Function f;
        if((f = get(pStr)) == null)
            return null;
        return f.exec(ret, pEqSys, pNode);
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
        return names.equals(((Function)pObj).names()) &&
               help.equals(((Function)pObj).help()) &&
               syntax.equals(((Function)pObj).syntax());
    }

    @Override
    public Function copy(){
        return new Function(names, help, syntax, priority, type, argsLength, funcObj);
    }

    @Override
    public String toString() {
        return "Funciton '" + names + "'";
    }

    @Override
    public String toFancyString(int idtLvl) {
        String ret = indent(idtLvl) + "Funciton '" + names + "':\n";
        ret += indent(idtLvl + 1) + "Help = " + help + "\n";
        ret += indent(idtLvl + 1) + "Syntax = " + syntax + "";
        return ret;
    }

    @Override
    public String toFullString(int idtLvl) {
        String ret = indent(idtLvl) + "Funciton:\n";
        ret += indent(idtLvl + 1) + "Name:\n" + indentE(idtLvl + 2) + names + "\n";
        ret += indent(idtLvl + 1) + "Help:\n" + indentE(idtLvl + 2) + help + "\n";
        ret += indent(idtLvl + 1) + "Syntax:\n" + indentE(idtLvl + 2) + syntax + "\n";
        ret += indent(idtLvl + 1) + "Priority:\n" + indentE(idtLvl + 2) + syntax + "\n";
        ret += indent(idtLvl + 1) + "Allowed Argument Length(s):\n" + indentE(idtLvl + 2) + argsLength + "\n";
        ret += indent(idtLvl + 1) + "Function Type:\n" + indentE(idtLvl + 2) + type + "\n";
        ret += indent(idtLvl + 1) + "Function Object:\n" + indentE(idtLvl + 2) + funcObj + "\n";
        return ret + "\n" + indentE(idtLvl + 1);
    }

}