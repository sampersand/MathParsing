package Math;

import Math.Exception.*;
import Math.Equation.*;

/**
 * A tester class for the whole equation
 * @author Sam Westerman
 * @version 0.1
 */
public class Tester {

    /**
     * The main function for the Math package.
     * @param args The arguemnts passed in - usually through the command line
     * @throws NotDefinedException Thrown when the first value isn't equal to "--e" of --f 
     */
    public static void main(String[] args) throws NotDefinedException {
        EquationSystem eq = new EquationSystem();
        if(args.length == 0) {
            eq.add("e=1+2");
        } else {
            eq = new EquationSystem();
            if(args.length == 1) {
                eq = new EquationSystem().add(new Equation(args[0]));
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
                        try{
                            eq.add(args[i].split(":")[0], new CustomFunction(args[i].split(":")[1])); //fix me.
                        } catch(NumberFormatException err) {
                            Print.printw("Syntax: FUNCNAME:FUNC.val() (" + args[i] + ")");
                        } catch(ArrayIndexOutOfBoundsException err) {
                            Print.printw("Syntax: FUNCNAME:FUNC.val() (" + args[i] + ")");
                        }
                    } else if (type == 'e') {
                        eq.add(EquationSystem.genEq(args[i]));
                    }
                }
            }
        }
        Print.print(eq);
        Print.printi("RESULT:", eq.eval("X"));
    }
}