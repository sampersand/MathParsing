package Math;

import Math.Exception.*;
import Math.Equation.*;
import Math.Equation.Function.*;
import Math.Set.*;

/**
 * A tester class for the whole equation
 * @author Sam Westerman
 * @version 0.72
 * @since 0.1
 */
public class Tester {

    /**
     * The main function for the Math package.
     * @param args The arguemnts passed in - usually through the command line
     * @throws NotDefinedException Thrown when the first value isn't equal to "--e" of --f 
     */
    public static void main(String[] args) throws NotDefinedException {
        EquationSystem eqsys = new EquationSystem();
        if(args.length == 0) {
            eqsys.add("y=x");

            eqsys.add("x=-2.0539125955565396E-15");

            // eqsys.add("y = 1 + sin(alpha) + c");
            // eqsys.add("alpha = (9.45 + x ^ theta) / 2");
            // eqsys.add("c = pi - e");
            // eqsys.add("theta = ln(pi) - c ^ x ");
            // eqsys.add("x = pi - e");
        } else {
            eqsys = new EquationSystem();
            if(args.length == 1) {
                eqsys = new EquationSystem().add(new Equation().add(args[0]));
            } else if(args.length > 1) {
                int i = -1;
                char type = ' ';
                if(!args[0].equals("--f") && !args[0].equals("--e"))
                    throw new NotDefinedException("first value has to be --f, or --e");
                while(i < args.length - 1) { //args.length is String.
                    i++;
                    if(args[i].equals("--f")) {type = 'f'; continue;}
                    if(args[i].equals("--e")) {type = 'e'; continue;}
                    if (type == 'f') {
                        try {
                            eqsys.add(args[i].split(":")[0], new CustomFunction(args[i].split(":")[1])); //fix me.
                        } catch(NumberFormatException err) {
                            Print.printw("Syntax: FUNCNAME:FUNC.val() (" + args[i] + ")");
                        } catch(ArrayIndexOutOfBoundsException err) {
                            Print.printw("Syntax: FUNCNAME:FUNC.val() (" + args[i] + ")");
                        }
                    } else if (type == 'e') {
                        eqsys.add(new Equation().add(args[i]));
                    }
                }
            }
        }


        eqsys.graph();
        Print.printi("RESULT:", eqsys.eval("y"));
        // MathSet<Double> g3 = new MathSet<Double>("{y : y = x^2}");
            // "{y : y = 1 + sin(alpha) + c ∧ alpha = (9.45 + x ^ theta) / 2 ∧ c = 0 - 7 ∧ theta = 4 ^ x - ln(pi)}");
        // System.out.println(g3.linReg().toFancyString());
        // g3.graph();
        // NumberCollection<Double> g1 = new NumberCollection<Double>(new Double[]{0D,1D,1D,2D,3D,5D,8D,13D,21D});

        // NumberCollection<Double> g2 = new NumberCollection<Double>(new Double[]{2D,3D,5D,7D,11D,13D,17D});
        // System.out.println(g1);
        // System.out.println(g1.mean());
        // System.out.println(g2);
        // System.out.println(g1.intersect(g2));
    }


}












