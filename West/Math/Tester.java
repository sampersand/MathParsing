package West.Math;
import West.Math.Equation.*;
import West.Math.Equation.Function.*;
import West.Math.Set.*;
import West.*;

import java.util.List;
import java.util.ArrayList;
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
     * @throws IllegalArgumentException Thrown when the first value isn't equal to "--e" of --f 
     */
    public static void main(String[] args) throws IllegalArgumentException {
        EquationSystem eqsys = new EquationSystem();
        if(args.length == 0) {
            // eqsys.add("y=1+sin((9.45+x^(ln(pi)-(pi-e)^x))/2)+(pi-e)");

                //SIN THING

            // eqsys.add("y = 1 + sin(alpha) + c");
            // eqsys.add("alpha = (9.45 + x ^ theta) / 2");
            // eqsys.add("c = pi^(0-e)");
            // eqsys.add("theta = ln(pi) - c ^ x ");


            // eqsys.add("y=3x-2x⁰");
            
            eqsys.add("y = tanx");
            eqsys.add("qq = sec²x");


            // eqsys.add("y = 4 * qwertyuip$");
            // eqsys.add("qwertyuip$ = (z = pi) * f/e");
            // eqsys.add("f = z * 1/x");
            // eqsys.add("z = 0");
                //TWISTY THING
            // eqsys.add("y = z * (x%2 > 1 ∨ x%2 < -1)");
            // eqsys.add("z = x0 * x1 * x2 * x3 * x4");
            // eqsys.add("x0 = (x) * c");
            // eqsys.add("x1 = (x - 8) * c");
            // eqsys.add("x2 = (x - 4) * c");
            // eqsys.add("x3 = (x + 4) * c");
            // eqsys.add("x4 = (x + 8) * c");
            // eqsys.add("c = 1/4"); 
        } else {
            eqsys = new EquationSystem();
            if(args.length == 1) {
                eqsys = new EquationSystem().add(new Equation().add(args[0]));
            } else if(args.length > 1) {
                int i = -1;
                char type = ' ';
                if(!args[0].equals("--f") && !args[0].equals("--e"))
                    throw new IllegalArgumentException("first value has to be --f, or --e");
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


        // MathCollection g3 = new MathCollection("{y | y = √(x³)}", -10, 10, 1);
        // g3.graph();
        // Print.print(eqsys.toFancyString());   
        eqsys.graph(-20, -20, 20, 20, 250, "x", "y", "qq");
        // Print.printi("RESULT (y):", eqsys.eval("y"));
        // Print.printi("RESULT (x):", eqsys.eval("x"));
    }


}











