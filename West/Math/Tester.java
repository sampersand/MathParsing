package West.Math;

import West.Math.Exception.*;
import West.Math.Equation.*;
import West.Math.Equation.Function.*;
import West.Math.Set.*;


/**
 * A tester class for the whole equation
 * @author Sam Westerman
 * @version 0.76
 * @since 0.1
 */
public class Tester {

    /**
     * The main function for the West.Math.package.
     * @param args The arguemnts passed in - usually through the command line
     * @throws NotDefinedException Thrown when the first value isn't equal to "--e" of --f 
     */
    public static void main(String[] args) throws NotDefinedException {
        EquationSystem eqsys = new EquationSystem();
        if(args.length == 0) {
            eqsys.add("y = 1 + sin(alpha) + c");
            eqsys.add("alpha = (9.45 + x ^ theta) / 2");
            eqsys.add("c = pi - e");
            eqsys.add("theta = ln(pi) - c ^ x ");
            eqsys.add("x = 3");

            // eqsys.add("y = x0 * x1 * x2 * x3 * x4");
            // eqsys.add("x0 = (x) * c");
            // eqsys.add("x1 = (x - 8) * c");
            // eqsys.add("x2 = (x - 4) * c");
            // eqsys.add("x3 = (x + 8) * c");
            // eqsys.add("x4 = (x + 4) * c");
            // eqsys.add("c = 1/4");
            // eqsys.addConstraint("x > 5 | x < 4");
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


        // System.out.println(eqsys.toFullString());
        // eqsys.graph();
        Print.printi("RESULT:", eqsys.eval("y"));
        // NumberCollection<Double> g1 = new NumberCollection<Double>(new Double[]{0D,1D,1D,2D,3D,5D,8D,13D,21D});
        // System.out.println(g1.mean());
        // NumberCollection<Double> g2 = new NumberCollection<Double>(new Double[]{2D,3D,5D,7D,11D,13D,17D});
        // MathCollection<Double> g3 = MathCollection.fromSetNotation("{y ∈ ℝ : y = x^2}");
            // "{y : y = 1 + sin(alpha) + c ∧ alpha = (9.45 + x ^ theta) / 2 ∧ c = 0 - 7 ∧ theta = 4 ^ x - ln(pi)}");
        // g3.graph(NumberCollection.enumerationD(-10, 10, .8));
        // System.out.println(g3.linReg().toFancyString());
    }


}












