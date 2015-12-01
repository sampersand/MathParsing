package Math;
import Math.Exception.*;
import Math.Equation.*;
public class Tester {
    public static void main(String[] args) throws NotDefinedException, TypeMisMatchException {
        Equation eq = new Equation();
        if(args.length == 0){
            eq.add("x=1+2");
        } else {
            eq = new Equation();
            if(args.length == 1){
                eq = new Equation(args[0]);
            } else if(args.length > 1){
                int i = -1;
                char type = ' ';
                if(!args[0].equals("--f") && !args[0].equals("--e"))
                    throw new NotDefinedException("first value has to be --f, or --e");
                while(i < args.length - 1){ //args.length is String.
                    i++;
                    if(args[i].equals("--f")){type = 'f'; continue;}
                    if(args[i].equals("--e")){type = 'e'; continue;}
                    if (type == 'f'){
                        try{
                            eq.add(args[i].split(":")[0], new CustomFunction(args[i].split(":")[1])); //fix me.
                        } catch(NumberFormatException err){
                            Print.printw("Syntax: FUNCNAME:FUNCVAL (" + args[i] + ")");
                        } catch(ArrayIndexOutOfBoundsException err){
                            Print.printw("Syntax: FUNCNAME:FUNCVAL (" + args[i] + ")");
                        }
                    } else if (type == 'e'){
                        eq.add(Equation.genExprs(args[i]));
                    }
                }
            }
        }
        Print.print(eq);
        Print.printi("RESULT:",eq.eval("X"));
    }
}