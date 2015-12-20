package Math;

import Math.Exception.*;
import Math.Equation.*;

/**
 * A tester class for the whole equation
 * @author Sam Westerman
 * @version 0.66
 * @since 0.1
 */
public class Tester {

    /**
     * The main function for the Math package.
     * @param args The arguemnts passed in - usually through the command line
     * @throws NotDefinedException Thrown when the first value isn't equal to "--e" of --f 
     */
    public static void main(String[] args) throws NotDefinedException {


        /*
         * --TODOS--
         * NOTE: Set will not be supported fully until version 1.0 comes out
         * correct all the Exception declarations to make them clearer. - v 0.68
             * make sure copy, toString, toFullSring, and toFancyString are updated - v0.69
         * Try and break the graphing code, among other things - v0.7
         * Javadoc everything - v ??
         * make isolate work - v ??
         */
        EquationSystem eqsys = new EquationSystem();
        if(args.length == 0) {
            eqsys.add("y = graph('eq:sin(alpha)','eq:cos(beta)')");
            eqsys.add("alpha = 2*x");
            eqsys.add("beta = alpha / 4");
            // eqsys.add("y = graph('eq:1 + sin(alpha) + c')");
            // eqsys.add("alpha = (9.45 + x ^ theta) / 2");
            // eqsys.add("theta = 2 * c + x");
            // eqsys.add("c = 0 - 9");
            eqsys.add(new CustomFunction("graph"));
            // 1 + sin((9.45 + (4 - 9) ^ (2 * - 9 + 4 - 9))/2) - 9
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
        // Print.print(eqsys.toFullString());
        Print.printi("RESULT:", eqsys.eval("y"));
    }


}