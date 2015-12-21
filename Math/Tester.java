package Math;

import Math.Exception.*;
import Math.Equation.*;
import Math.Set.*;

/**
 * A tester class for the whole equation
 * @author Sam Westerman
 * @version 0.67
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
         * Make sets use ArrayLists instead of arrays.
         * Try and break the graphing code, among other things - v0.7
         * go over and make sure all the things can handle null and empty lists.
         * Javadoc everything - v ??
         * make isolate work - v ??
         * make it so a node can have mroe than 1 subnode.
         */
        EquationSystem eqsys = new EquationSystem();
        if(args.length == 0) {
            eqsys.add("y = 1 + sin(alpha) + c");
            eqsys.add("alpha = (9.45 + x ^ theta) / 2");
            eqsys.add("c = 0 - 9");
            eqsys.add("theta = 4 ^ x - ln(pi)");
            eqsys.add("x = pi - e");
            eqsys.add(new CustomFunction("graph"));
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

        // Set set = new Set(new double[]{14,7,3,5,3,1,7,1,5,15,1.5,25,2,5,1,5},
                          // new double[]{14,5,7,4,6,2,7.5,5,4,4,0.5,14,9,11,2,13});
        // Print.print(set);
        // Print.print();
        // Print.print(set.toFancyString());

        // Print.print(eqsys.equations().get(0).expressions().get(1).node().toFancyString());
        System.out.println(eqsys.toFullString());
        Print.printi("RESULT:", eqsys.eval("y"));
    }


}