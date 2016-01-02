package West.Math;

import West.Math.Exception.*;
import West.Math.Equation.*;
import West.Math.Equation.Function.*;
import West.Math.Set.*;


/**
 * A tester class for the whole equation
 * @author Sam Westerman
  * @version 0.90
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
            //FIXED

            // eqsys.add("=(y,+(1,+(sin(/(+(9.45,^(^(e,pi),-(ln(pi),^(-(pi,e),^(e,pi))))),2)),-(pi,e)))))");

                //FIXED SIN THING
            // eqsys.add("=(y, +(1,+(sin(alpha),c)))");
            // eqsys.add("=(alpha, /(+(9.45,^(x,theta)),2))");
            // eqsys.add("=(c, ^(pi,-(0,e)))");
            // eqsys.add("=(theta, -(ln(pi),^(c,x)))");
            // eqsys.add("=(x, ^(e,pi))");

            // eqsys.add("=(y, ^(x, compare(x, 0))");
            // eqsys.addConstraint("⊻(>(x, y),<(0, x))");


            eqsys.add("=(y, *(z, ⊻(>(x,0),<(z,0))))");
            eqsys.add("=(z, *(x0, *(x1, *(x2, *(x3, x4)))))");
            eqsys.add("=(x0, *(x, c)");
            eqsys.add("=(x1, *(-(x, 8), c)");
            eqsys.add("=(x2, *(-(x, 4), c)");
            eqsys.add("=(x3, *(+(x, 8), c)");
            eqsys.add("=(x4, *(+(x, 4), c)");
            eqsys.add("=(c, /(1,4))");

            //UNFIXED

            // eqsys.add("y = 1 + sin((9.45 + (e^pi) ^ (ln(pi) - (pi - e) ^ (e^pi))) / 2) + (pi - e)");

                //SIN THING
            // eqsys.add("y = 1 + sin(alpha) + c");
            // eqsys.add("alpha = (9.45 + x ^ theta) / 2");
            // eqsys.add("c = pi^(0-e)");
            // eqsys.add("theta = ln(pi) - c ^ x ");
            // eqsys.add("x = e^pi");

                //TWISTY THING
            // eqsys.add("y = x0 * x1 * x2 * x3 * x4");
            // eqsys.add("x0 = (x) * c");
            // eqsys.add("x1 = (x - 8) * c");
            // eqsys.add("x2 = (x - 4) * c");
            // eqsys.add("x3 = (x + 8) * c");
            // eqsys.add("x4 = (x + 4) * c");
            // eqsys.add("c = 1/4");

            // eqsys.addConstraint("x < pi * e && y > -1");
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


        Print.print(eqsys.toFancyString());
        // Print.printi(eqsys.toFullString());
        eqsys.graph();
        // Print.printi("RESULT:", eqsys.eval("y"));
    }


}












