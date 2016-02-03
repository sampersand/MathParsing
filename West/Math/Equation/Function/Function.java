package West.Math.Equation.Function;
import West.Math.MathObject;
import West.Math.Equation.EquationSystem;
import West.Math.Equation.Equation;
import West.Math.Set.Node.TokenNode;
import West.Math.Set.Collection;
import static West.Math.Declare.*;
import java.util.HashMap;
import java.util.Random;
import West.Math.DoubleSupplier;
import West.Math.Complex;
import static West.Math.Complex.NAN;

/**
 * A class that simulates both any kind of operation and any fuction in West.Math.
 * Simply put, anything that isn't a number or variable should be a function somehow.
 * For example, in <code>f(x)</code>, this class would represent f.
 * 
 * @author Sam Westerman
 * @version 1.1
 * @since 0.1
 */
public class Function implements MathObject {
    public static enum Type{
        UNL,
        UNR,
        BIN,
        ASSIGN,
        NORM
    } // Assign extends Bin
    
    @FunctionalInterface
    public interface FuncObj{
        public DoubleSupplier exec(HashMap<String, DoubleSupplier> hm, EquationSystem eqsys, TokenNode tn);
    }

    public static final int DEFAULT_PRIORITY = 12;

    public static Collection<Function> FUNCTIONS = new Collection<Function>() {{
        //  =                : 0
        //  ∨, ⊽,            : 1
        //  ⊻                : 2
        //  ∧, ⊼,            : 3
        //  b|               : 4
        //  b^               : 5
        //  b&               : 6
        //  ≣, ≠             : 7
        //  >, <, ≥, ≤,       : 8
        //  b≫, b≪           : 9
        //  +, -             : 10
        //  *, /, %          : 11
        //  ^,               : 12
        //  unary –, b~      : 13
        //  unary !          : 14

        add(new Function(new Collection<String>(){{add("");}},
            "Parenthesis, literally: ('A')", "(A)",
            DEFAULT_PRIORITY,
            Type.NORM,
            new Collection<Integer>().addE(-1),
            (hm, eqsys, tn) -> {
                String[] p = tn.parens();
                int s = tn.size();
                switch(p[0]){
                    case "<":
                        switch(p[1]){
                            case ">":
                                if(tn.size() <= 1)
                                    return Complex.NAN;
                                Complex ret = new Complex(0d);
                                for(West.Math.Set.Node.Node<?, ?> tnd : tn)
                                    ret = ret.plus(((Complex)((TokenNode)tnd).evald(hm, eqsys)).pow(2d));
                                return ret.pow(0.5d);
                        }
                    case "{":
                        switch(p[1]){
                            case "}":
                                assert s == 3 : tn;
                                assert !tn.get(0).isFinal() && tn.get(0).get(0).isFinal(): tn;
                                assert tn.get(1).token().isDelim() && (tn.get(1).token().val().equals("|") ||
                                                                       tn.get(1).token().val().equals(":")) : tn;
                                assert tn.get(2).token().isDelim() && tn.get(2).token().val().equals("$") : tn;
                                assert tn.get(2).size() == 3 : tn;
                                Collection<Double> col = new Collection<Double>();
                                String toeval = tn.get(0).token().val();
                                TokenNode condits = tn.get(1).get(0);
                                Double min = ((Complex)tn.get(2).get(0).evald(hm, eqsys)).aIsOnlyReal().real();
                                Double max = ((Complex)tn.get(2).get(1).evald(hm, eqsys)).aIsOnlyReal().real();
                                Double step = ((Complex)tn.get(2).get(2).evald(hm, eqsys)).aIsOnlyReal().real();
                                assert !min.isNaN();
                                assert !max.isNaN();
                                assert !step.isNaN();
                                for(Double d = min; d < max; d+=step){
                                    assert false : "TODO: SET NOTATION";
                                    //  Double d2 = 
                                    //  Double d2 = new EquationSystem().add(toeval+"="+d).eval(toeval,hm, eqsys);
                                                              //  West.Math.Equation.Token.Type.VAR)).evald(hm, eqsys);
                                    //  if(!d.isNaN())
                                    //      col.add(d2);
                                }
                                return new Complex(col.size());

                            }

                    case "√":
                        switch(p[1]){
                            case ")":
                                assert s == 1 : tn;
                                return ((Complex)eval(0, tn, hm, eqsys)).pow(0.5d);
                            }
                    case "|":
                        switch(p[1]){
                            case "|":
                                assert s == 1 : tn;
                                return ((Complex)eval(0, tn, hm, eqsys)).abs();
                            }
                    case "(": case "": default:
                        switch(p[1]){
                            case ")":case "":
                                assert s == 1 || tn.token().isDelim(): tn;
                                return ((Complex)eval(0, tn, hm, eqsys));
                            default:
                                throw new IllegalArgumentException("The Parenthesis '" + p[0] + ", " + p[1] +
                                                       "' (args: " + s + ") have no function associated!");
                        }
                }
            }
        ));

        // priority 0: equals
        add(new Function(new Collection<String>(){{add("=");}},
            "Sets 'A' to 'B'", "A = B",
            0,
            Type.ASSIGN,
            new Collection<Integer>().addE(2), 
            (hm, eqsys, tn) -> ((Complex)eval(0, tn, hm, eqsys)) // doesnt matter
        ));

        // priority 1: or / nor
        add(new Function(new Collection<String>(){{add("∨"); add("or");}},
            "Checks if 'A' ∨ 'B'", "A ∨ B",
            1,
            Type.BIN,
            new Collection<Integer>().addE(2),
            (hm, eqsys, tn) -> ((Complex)eval(0, tn, hm, eqsys)).or((Complex)eval(1, tn, hm, eqsys))
        ));
        add(new Function(new Collection<String>(){{add("⊽");add("nor");}},
            "Checks if 'A' ⊽ 'B'", "A ⊽ B",
            1,
            Type.BIN,
            new Collection<Integer>().addE(2),
            (hm, eqsys, tn) -> ((Complex)eval(0, tn, hm, eqsys)).nor((Complex)eval(1, tn, hm, eqsys))
        ));

        // priority 2: xor
        add(new Function(new Collection<String>(){{add("⊻"); add("xor");}},
            "Checks if 'A' ⊻ 'B'", "A ⊻ B",
            2,
            Type.BIN,
            new Collection<Integer>().addE(2),
            (hm, eqsys, tn) -> ((Complex)eval(0, tn, hm, eqsys)).xor((Complex)eval(1, tn, hm, eqsys))
        ));

        // priority 3: and / nand
        add(new Function(new Collection<String>(){{add("∧"); add("and");}},
            "Checks if 'A' ∧ 'B'", "A ∧ B",
            3,
            Type.BIN,
            new Collection<Integer>().addE(2),
            (hm, eqsys, tn) -> ((Complex)eval(0, tn, hm, eqsys)).and((Complex)eval(1, tn, hm, eqsys))
        ));
        add(new Function(new Collection<String>(){{add("⊼"); add("nand");}},
            "Checks if 'A' ⊼ 'B'", "A ⊼ B",
            3,
            Type.BIN,
            new Collection<Integer>().addE(2),
            (hm, eqsys, tn) -> ((Complex)eval(0, tn, hm, eqsys)).nand((Complex)eval(1, tn, hm, eqsys))
        ));

        // priority 4:  b|
        add(new Function(new Collection<String>(){{add("b|");}},
            "Bitwise or of 'A' and 'B' (A|B)", "A b| B", 
            4,
            Type.BIN,
            new Collection<Integer>().addE(2),
            (hm, eqsys, tn) -> ((Complex)eval(0, tn, hm, eqsys)).bor((Complex)eval(1, tn, hm, eqsys))
        ));

        // priority 5: b^
        add(new Function(new Collection<String>(){{add("b^");}},
            "Bitwise xor of 'A' and 'B' (A^B)", "A b^ B", 
            5,
            Type.BIN,
            new Collection<Integer>().addE(2),
            (hm, eqsys, tn) -> ((Complex)eval(0, tn, hm, eqsys)).bxor((Complex)eval(1, tn, hm, eqsys))
        ));

        // priority 6: b&
        add(new Function(new Collection<String>(){{add("b&");}},
            "Bitwise and of 'A' and 'B' (A&B)", "A b& B", 
            6,
            Type.BIN,
            new Collection<Integer>().addE(2),
            (hm, eqsys, tn) -> ((Complex)eval(0, tn, hm, eqsys)).band((Complex)eval(1, tn, hm, eqsys))
        ));

        // priority 7: ≣, ≠
        add(new Function(new Collection<String>(){{add("≣");}},
            "Checks if 'A' == 'B'", "A ≣ B",
            7,
            Type.BIN,
            new Collection<Integer>().addE(2),
            (hm, eqsys, tn) -> ((Complex)eval(0, tn, hm, eqsys)).eq((Complex)eval(1, tn, hm, eqsys))
        ));
        add(new Function(new Collection<String>(){{add("≠");}},
            "Checks if 'A' ≠ 'B'", "A ≠ B",
            7,
            Type.BIN,
            new Collection<Integer>().addE(2),
            (hm, eqsys, tn) -> ((Complex)eval(0, tn, hm, eqsys)).neq((Complex)eval(1, tn, hm, eqsys))
        ));

        // priority 8: >, <, ≥, ≤
        add(new Function(new Collection<String>(){{add("＜");}},
            "Checks if 'A' ＜ 'B'", "A ＜ B",
            8,
            Type.BIN,
            new Collection<Integer>().addE(2),
            (hm, eqsys, tn) -> ((Complex)eval(0, tn, hm, eqsys)).lt((Complex)eval(1, tn, hm, eqsys))
        ));
        add(new Function(new Collection<String>(){{add("＞");}},
            "Checks if 'A' ＞ 'B'", "A ＞ B",
            8,
            Type.BIN,
            new Collection<Integer>().addE(2),
            (hm, eqsys, tn) -> ((Complex)eval(0, tn, hm, eqsys)).gt((Complex)eval(1, tn, hm, eqsys))
        ));
        add(new Function(new Collection<String>(){{add("≤");}},
            "Checks if 'A' ≤ 'B'", "A ≤ B",
            8,
            Type.BIN,
            new Collection<Integer>().addE(2),
            (hm, eqsys, tn) -> ((Complex)eval(0, tn, hm, eqsys)).lte((Complex)eval(1, tn, hm, eqsys))
        ));
        add(new Function(new Collection<String>(){{add("≥");}},
            "Checks if 'A' ≥ 'B'", "A ≥ B",
            8,
            Type.BIN,
            new Collection<Integer>().addE(2),
            (hm, eqsys, tn) -> ((Complex)eval(0, tn, hm, eqsys)).gte((Complex)eval(1, tn, hm, eqsys))
        ));

        // priority 9: b≫, b≪
        add(new Function(new Collection<String>(){{add("≫");add("b≫");}},
            "'A' Shifted to the right 'B' bits", "≫(A, B)",
            9,
            Type.BIN,
            new Collection<Integer>().addE(2),
            (hm, eqsys, tn) -> ((Complex)eval(0, tn, hm, eqsys)).bsr((Complex)eval(1, tn, hm, eqsys))
        ));
        add(new Function(new Collection<String>(){{add("≪");add("b≪");}},
            "'A' Shifted to the left 'B' bits", "≪(A, B)",
            9,
            Type.BIN,
            new Collection<Integer>().addE(2),
            (hm, eqsys, tn) -> ((Complex)eval(0, tn, hm, eqsys)).bsl((Complex)eval(1, tn, hm, eqsys))
        ));

        // priority 10: +, -
        add(new Function(new Collection<String>(){{add("+");}},
            "Adds 'A' to 'B'", "A + B", 
            10,
            Type.BIN,
            new Collection<Integer>().addE(2),
            (hm, eqsys, tn) -> ((Complex)eval(0, tn, hm, eqsys)).plus((Complex)eval(1, tn, hm, eqsys))
        ));
        add(new Function(new Collection<String>(){{add("-");}},
            "Subtracts 'A' to 'B'", "A - B",
            10,
            Type.BIN,
            new Collection<Integer>().addE(2),
            (hm, eqsys, tn) -> ((Complex)eval(0, tn, hm, eqsys)).
                                 minus((Complex)eval(1, tn, hm, eqsys))
            // (hm, eqsys, tn) -> tn.size() == 1 ? 0 - ((Complex)eval(0, tn, hm, eqsys)) :
            // ((Complex)eval(0, tn, hm, eqsys)) - ((Complex)eval(1, tn, hm, eqsys))
        ));

        // priority 11: *, /, %
        add(new Function(new Collection<String>(){{add("*");add("·");add("×");}},
            "Multiplies 'A' to 'B'", "A * B",
            11,
            Type.BIN,
            new Collection<Integer>().addE(2),
            (hm, eqsys, tn) -> ((Complex)eval(0, tn, hm, eqsys)).mult((Complex)eval(1, tn, hm, eqsys))
        ));
        add(new Function(new Collection<String>(){{add("/");add("÷");}},
            "Divides 'A' to 'B'", "A / B",
            11,
            Type.BIN,
            new Collection<Integer>().addE(2),
            (hm, eqsys, tn) -> ((Complex)eval(0, tn, hm, eqsys)).div((Complex)eval(1, tn, hm, eqsys))
        ));
        add(new Function(new Collection<String>(){{add("%");}},
            "'A' Modulo 'B'", "A % B",
            11,
            Type.BIN,
            new Collection<Integer>().addE(2),
            (hm, eqsys, tn) -> ((Complex)eval(0, tn, hm, eqsys)).
                                 modulo((Complex)eval(1, tn, hm, eqsys))
        ));

        // priority 12: ^
        add(new Function(new Collection<String>(){{add("^");}},
            "Raises 'A' to 'B'", "A ^ B",
            12,
            Type.BIN,
            new Collection<Integer>().addE(2),
            (hm, eqsys, tn) -> ((Complex)eval(0, tn, hm, eqsys)).pow((Complex)eval(1, tn, hm, eqsys))
        ));

        // priority 13: –ELE, b~ELE
        add(new Function(new Collection<String>(){{add("–");}}, // negation
            "negate 'A'", "–(A)",
            13,
            Type.UNL,
            new Collection<Integer>().addE(1),
            (hm, eqsys, tn) -> new Complex(-1d).mult(((Complex)eval(0, tn, hm, eqsys)))
        ));
        add(new Function(new Collection<String>(){{add("b~");}}, // negation
            "bitwise negation of 'A' (~A)", "b~(A)",
            13,
            Type.UNL,
            new Collection<Integer>().addE(1),
            (hm, eqsys, tn) -> ((Complex)eval(0, tn, hm, eqsys)).bneg()
        ));

        //  priority 14: ELE!
        add(new Function(new Collection<String>(){{add("!");}}, // negation
            "factorial 'A'", "!(A)",
            14,
            Type.UNR,
            new Collection<Integer>().addE(1),
            (hm, eqsys, tn) -> ((Complex)eval(0, tn, hm, eqsys)).factorial()
        ));


        add(new Function(new Collection<String>(){{add("compare");}},
            "See Double.compare(A, B)", "compare(A, B)",
            DEFAULT_PRIORITY,
            Type.NORM,
            new Collection<Integer>().addE(2),
            (hm, eqsys, tn) -> new Complex(((Complex)eval(0, tn, hm, eqsys)).
                                                 compareTo((Complex)eval(1, tn, hm, eqsys)))
        ));


        // Trigonometric functions
        add(new Function(new Collection<String>(){{add("sin");}},
            "sin of 'A'", "sin(A)",
            DEFAULT_PRIORITY,
            Type.NORM,
            new Collection<Integer>().addE(1),
            (hm, eqsys, tn) -> ((Complex)eval(0, tn, hm, eqsys)).sin()
        ));

        add(new Function(new Collection<String>(){{add("cos");}},
            "cos of 'A'", "cos(A)",
            DEFAULT_PRIORITY,
            Type.NORM,
            new Collection<Integer>().addE(1),
            (hm, eqsys, tn) -> ((Complex)eval(0, tn, hm, eqsys)).cos()
        ));

        add(new Function(new Collection<String>(){{add("tan");}},
            "tan of 'A'", "tan(A)",
            DEFAULT_PRIORITY,
            Type.NORM,
            new Collection<Integer>().addE(1),
            (hm, eqsys, tn) -> ((Complex)eval(0, tn, hm, eqsys)).tan()
        ));

        add(new Function(new Collection<String>(){{add("csc");}},
            "csc of 'A'", "csc(A)",
            DEFAULT_PRIORITY,
            Type.NORM,
            new Collection<Integer>().addE(1),
            (hm, eqsys, tn) -> ((Complex)eval(0, tn, hm, eqsys)).csc()
        ));

        add(new Function(new Collection<String>(){{add("sec");}},
            "sec of 'A'", "sec(A)",
            DEFAULT_PRIORITY,
            Type.NORM,
            new Collection<Integer>().addE(1),
            (hm, eqsys, tn) -> ((Complex)eval(0, tn, hm, eqsys)).sec()
        ));

        add(new Function(new Collection<String>(){{add("cot");}},
            "cot of 'A'", "cot(A)",
            DEFAULT_PRIORITY,
            Type.NORM,
            new Collection<Integer>().addE(1),
            (hm, eqsys, tn) -> ((Complex)eval(0, tn, hm, eqsys)).cot()
        ));

        add(new Function(new Collection<String>(){{add("sinh");}},
            "sinh of 'A'", "sinh(A)",
            DEFAULT_PRIORITY,
            Type.NORM,
            new Collection<Integer>().addE(1),
            (hm, eqsys, tn) -> ((Complex)eval(0, tn, hm, eqsys)).sinh()
        ));

        add(new Function(new Collection<String>(){{add("cosh");}},
            "cosh of 'A'", "cosh(A)",
            DEFAULT_PRIORITY,
            Type.NORM,
            new Collection<Integer>().addE(1),
            (hm, eqsys, tn) -> ((Complex)eval(0, tn, hm, eqsys)).cosh()
        ));

        add(new Function(new Collection<String>(){{add("tanh");}},
            "tanh of 'A'", "tanh(A)",
            DEFAULT_PRIORITY,
            Type.NORM,
            new Collection<Integer>().addE(1),
            (hm, eqsys, tn) -> ((Complex)eval(0, tn, hm, eqsys)).tanh()
        ));

        add(new Function(new Collection<String>(){{add("asin");}},
            "asin of 'A'", "asin(A)",
            DEFAULT_PRIORITY,
            Type.NORM,
            new Collection<Integer>().addE(1),
            (hm, eqsys, tn) -> ((Complex)eval(0, tn, hm, eqsys)).asin()
        ));

        add(new Function(new Collection<String>(){{add("acon");}},
            "acos of 'A'", "acos(A)",
            DEFAULT_PRIORITY,
            Type.NORM,
            new Collection<Integer>().addE(1),
            (hm, eqsys, tn) -> ((Complex)eval(0, tn, hm, eqsys)).acos()
        ));

        add(new Function(new Collection<String>(){{add("atan");}},
            "atan of 'A'", "atan(A)",
            DEFAULT_PRIORITY,
            Type.NORM,
            new Collection<Integer>().addE(1),
            (hm, eqsys, tn) -> ((Complex)eval(0, tn, hm, eqsys)).atan()
        ));


        // Misc math functions
        add(new Function(new Collection<String>(){{add("abs");}},
            "absolute value of 'A'", "abs(a)",
            DEFAULT_PRIORITY,
            Type.NORM,
            new Collection<Integer>().addE(1),
            (hm, eqsys, tn) -> ((Complex)eval(0, tn, hm, eqsys)).abs()
        ));

        add(new Function(new Collection<String>(){{add("ceil");}},
            "closest integer greater than 'A'", "ceil(A)",
            DEFAULT_PRIORITY,
            Type.NORM,
            new Collection<Integer>().addE(1),
            (hm, eqsys, tn) -> ((Complex)eval(0, tn, hm, eqsys)).ceil()
        ));

        add(new Function(new Collection<String>(){{add("floor");}},
            "closest integer less than 'A'", "floor(A)",
            DEFAULT_PRIORITY,
            Type.NORM,
            new Collection<Integer>().addE(1),
            (hm, eqsys, tn) -> ((Complex)eval(0, tn, hm, eqsys)).floor()
        ));

        add(new Function(new Collection<String>(){{add("hypot");}},
            "hypotenuse of 'A' and 'B' ( √[A² + B²] )", "hypot(A, B)",
            DEFAULT_PRIORITY,
            Type.NORM,
            new Collection<Integer>().addE(-1),
            (hm, eqsys, tn) -> {
                    Complex ret = Complex.ZERO;
                    for(West.Math.Set.Node.Node<?, ?> tnd : tn)
                        ret = ret.plus(
                                ((Complex)((TokenNode)tnd).evald(hm, eqsys)
                                 ).pow(new Complex(2d)));
                    return ret.pow(new Complex(0.5d));
                }
        ));

        add(new Function(new Collection<String>(){{add("ln");}},
            "natural log of 'A'", "ln(A)",
            DEFAULT_PRIORITY,
            Type.NORM,
            new Collection<Integer>().addE(1),
            (hm, eqsys, tn) -> ((Complex)eval(0, tn, hm, eqsys)).ln()
        ));

        add(new Function(new Collection<String>(){{add("log");}},
            "log base 10 of 'A'", "log(A)",
            DEFAULT_PRIORITY,
            Type.NORM,
            new Collection<Integer>().addE(1).addE(2),
            (hm, eqsys, tn) -> tn.size() == 1 ?
                                ((Complex)eval(0, tn, hm, eqsys)).log10() : 
                               ((Complex)eval(0, tn, hm, eqsys)).
                                 log((Complex)eval(1, tn, hm, eqsys))
        ));
        add(new Function(new Collection<String>(){{add("round");}},
            "rounds 'A' to the nearest integer", "round(A)",
            DEFAULT_PRIORITY,
            Type.NORM,
            new Collection<Integer>().addE(1).addE(2),
            (hm, eqsys, tn) ->  tn.size() == 1 ? ((Complex)eval(0, tn, hm, eqsys)).round()
                                : ((Complex)eval(0, tn, hm, eqsys)).round(
                                ((Complex)eval(1, tn, hm, eqsys)).aIsOnlyReal().real().intValue())
        ));

        add(new Function(new Collection<String>(){{add("sqrt");}},
            "the square root (√) of 'A'", "sqrt(A)",
            DEFAULT_PRIORITY,
            Type.NORM,
            new Collection<Integer>().addE(1),
            (hm, eqsys, tn) -> ((Complex)eval(0, tn, hm, eqsys)).pow(0.5d)
        ));

        add(new Function(new Collection<String>(){{add("real");}},
            "real component of'A'", "real(A)",
            DEFAULT_PRIORITY,
            Type.NORM,
            new Collection<Integer>().addE(1),
            (hm, eqsys, tn) -> new Complex(((Complex)eval(0, tn, hm, eqsys)).real())
        ));

        add(new Function(new Collection<String>(){{add("imag");}},
            "imaginary component of'A'", "imag(A)",
            DEFAULT_PRIORITY,
            Type.NORM,
            new Collection<Integer>().addE(1),
            (hm, eqsys, tn) -> new Complex(((Complex)eval(0, tn, hm, eqsys)).imag())
        ));

        add(new Function(new Collection<String>(){{add("deg");add("degrees");add("toDegrees");}},
            "turns 'A' into degrees (from radians)", "degrees(A)",
            DEFAULT_PRIORITY,
            Type.NORM,
            new Collection<Integer>().addE(1),
            (hm, eqsys, tn) -> ((Complex)eval(0, tn, hm, eqsys)).toDegrees()
        ));

        add(new Function(new Collection<String>(){{add("rad");add("radians");add("toRadians");}},
            "turns 'A' into radians (from degrees)", "radians(A)",
            DEFAULT_PRIORITY,
            Type.NORM,
            new Collection<Integer>().addE(1),
            (hm, eqsys, tn) -> ((Complex)eval(0, tn, hm, eqsys)).toRadians()
        ));

        add(new Function(new Collection<String>(){{add("randi");}},
            "random integer from [0, 100], [0, 'A'], or ['A', 'B']", "ri(A, B)",
            DEFAULT_PRIORITY,
            Type.NORM,
            new Collection<Integer>().addE(0).addE(1).addE(2),
            (hm, eqsys, tn) -> {
                switch(tn.size()){
                    case 0:
                        return new Complex(Integer.valueOf(new Random().nextInt(100)).doubleValue());
                    case 1:
                        return new Complex(Integer.valueOf(new Random().nextInt(
                                                 ((Complex)eval(0, tn, hm, eqsys)).
                                                 aIsOnlyReal().intValue())).doubleValue());
                    case 2:
                        return new Complex(Integer.valueOf(new Random().nextInt(
                                                 ((Complex)eval(1, tn, hm, eqsys)).
                                                 aIsOnlyReal().intValue()) +((Complex)eval(0, tn, hm, eqsys)).
                                                 aIsOnlyReal().intValue()).doubleValue());
                    default:
                        throw new IllegalArgumentException();
                }
            }
        ));

        add(new Function(new Collection<String>(){{add("rand");}},
            "random double from [0, 1), [0, 'A'), or ['A', 'B')", "rd(A, B)",
            DEFAULT_PRIORITY,
            Type.NORM,
            new Collection<Integer>().addE(0).addE(1).addE(2),
            (hm, eqsys, tn) -> {
                switch(tn.size()){
                    case 0:
                        return new Complex(Math.random());
                    case 1:
                        return new Complex(new Random().nextDouble()).
                            mult(((Complex)eval(0, tn, hm, eqsys)).aIsOnlyReal());
                    case 2:
                        return new Complex(new Random().nextDouble()).plus(
                                                 ((Complex)eval(0, tn, hm, eqsys))).aIsOnlyReal().mult(
                                                 ((Complex)eval(1, tn, hm, eqsys)).aIsOnlyReal());
                default:
                    throw new IllegalArgumentException();
                }
            }
        ));

        add(new Function(new Collection<String>(){{add("summation");add("Σ");}},
            "Summation of 'EQ', incrementing 'N' from 'S' to 'E', and solving for 'X'", "Σ(N, S, E, X, EQ)",
            DEFAULT_PRIORITY,
            Type.NORM,
            new Collection<Integer>().addE(3).addE(5),
            (hm, eqsys, tn) -> {

                int siz = tn.size();
                String incVar, findVar;
                Complex ret, min, max;
                TokenNode equ;
                ret = Complex.ZERO;

                incVar = tn.get(0).get(0).toString(); // without second get(0), it's still a delim
                findVar = tn.get(siz == 3 ? 1 : 3).get(0).toString(); // without second get(0), it's still a delim
                equ = tn.get(siz == 3 ? 2 : 4).get(0); // without second get(0), it's still a delim
                
                if(siz == 5){
                    min = (Complex)tn.get(1).get(0).evald(new HashMap<String, DoubleSupplier>(), eqsys);
                    max = (Complex)tn.get(2).get(0).evald(new HashMap<String, DoubleSupplier>(), eqsys);
                } else {
                    min = Complex.ONE;
                    max = Complex.INF_P;
                }
                assert min.isOnlyReal() && max.isOnlyReal() : min + " | " + max + " <-- needs to be only real";
                while(min.compareTo(max) <= 0){
                    ret = ret.plus(new EquationSystem().add(equ.toString()).
                                   add(incVar + "=" + min).add(eqsys).eval(findVar));
                    min = min.plus(Complex.ONE);
                }
                return ret;
            }
        ));

        add(new Function(new Collection<String>(){{add("fac");}},
            "factorial of 'A'", "fac(A)",
            DEFAULT_PRIORITY,
            Type.NORM,
            new Collection<Integer>().addE(1),
            (hm, eqsys, tn) -> ((Complex)eval(0, tn, hm, eqsys)).factorial()
        ));

        add(new Function(new Collection<String>(){{add("negte");}},
            "'A' * -1", "neg(A)",
            DEFAULT_PRIORITY,
            Type.NORM,
            new Collection<Integer>().addE(1),
            (hm, eqsys, tn) -> ((Complex)eval(0, tn, hm, eqsys)).mult(new Complex(-1d))
        ));

        add(new Function(new Collection<String>(){{add("sign");}},
            "1 if 'A' > 0, -1 if 'A' < 0, and 0 if 'A' == 0", "sign(A)",
            DEFAULT_PRIORITY,
            Type.NORM,
            new Collection<Integer>().addE(1),
            (hm, eqsys, tn) -> 
                    new Complex(Integer.valueOf(((Complex)eval(0, tn, hm, eqsys)).
                                      compareTo(new Complex(0d))).doubleValue())
        ));
    }};

    private static DoubleSupplier eval(int pos, TokenNode tn, HashMap<String, DoubleSupplier> hm, EquationSystem eqsys){
        return tn.get(pos).evald(hm, eqsys);
    }

    public static Collection<String> BIN_OPERS = new Collection<String>(){{
            for(Function f : FUNCTIONS)
                if(f.type() == Type.BIN || f.type() == Type.ASSIGN)
                    for(String name : f.names())
                        add(name);
        }};
    public static String isBinOper(String s){
        if(s.matches(".*[eE][-\\d.]*$"))// if its sci notation, say no
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
        return West.Math.Equation.Token.isDelim(name) == null ? null : get(""); //  if its a delim, then return "()" Func
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
        //  assert pName != null;
        //  assert pHelp != null;
        //  assert pSyntax != null;
        //  assert pType != null;
        //  assert pArgsLength.size() >= 0;
        funcObj = pFuncObj;
        names = pName;
        help = pHelp;
        syntax = pSyntax;
        type = pType;
        priority = pPriority;
        argsLength = pArgsLength;
        if(funcObj == null)
            funcObj = (hm, eqsys, tn) -> null;
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

    /**
     * Takes the parameter {@link Node} (and {@link EquationSystem}, performs whatever this function is defined to do,
     * and returns the result.
     * @param pEqSys        An {@link EquationSystem} that contains all relevant information about
     *                      {@link Equation Equations} and {@link Function Functions} is stored.
     * @param pNode         The {@link Node} that is going to be solved.
     * @return A double representing the value of <code>pNode</code>, when solved for with <code>pEqSys</code>.
     * @throws IllegalArgumentException   Thrown when the function required parameters, and the ones passed aren't right.
     */
    public HashMap<String, DoubleSupplier> exec(HashMap<String, DoubleSupplier> ret,
                                                final EquationSystem pEqSys,
                                                TokenNode pNode) {
        if(type == Type.ASSIGN){
            assert pNode.size() == 2;
            ret.putAll(pNode.get(1).eval(ret, pEqSys));
            ret.put(pNode.get(0).toString(), ret.get(pNode.get(1).toString()));
        }
        else
            for(West.Math.Set.Node.Node<?, ?> n : pNode)
                ret.putAll(((TokenNode)n).eval(ret, pEqSys));
        assert argsLength.contains(pNode.size()) || argsLength.contains(-1) :
        "'" + names + "' got bad args. Allowed args: '" + syntax + "' (" + argsLength + ").   Inputted args = '("
                + pNode.size() +"') " + pNode;
        try{
            return addArgs(ret, pNode.toString(), funcObj.exec(ret, pEqSys, pNode));
        } catch (java.lang.NullPointerException n ){
            System.err.println("An error happened while executing '" + names + "'");
            System.err.println("\tThe HashMap was: " + ret);
            System.err.println("\tThe EquationSystem was: " + pEqSys);
            System.err.println("\tThe TokenNode was: " + pNode);
            throw n;
        }
    }

    public static HashMap<String, DoubleSupplier> exec(HashMap<String, DoubleSupplier> ret,
                                              String pStr,
                                              final EquationSystem pEqSys,
                                              TokenNode pNode) {
        Function f;
        if((f = get(pStr)) == null)
            return null;
        return f.exec(ret, pEqSys, pNode);
    }

    public static HashMap<String, DoubleSupplier> addArgs(HashMap<String, DoubleSupplier> hm,
                                                          String key,
                                                          DoubleSupplier val){
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
    public Function clone(){
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