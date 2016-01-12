package West.Math;
import West.Math.Equation.*;
import West.Math.Display.*;
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
        double PI = Math.PI;

        String indep = "";
        ArrayList<String> dep = new ArrayList<String>();

        GraphComponents.GraphTypes gtype = GraphComponents.GraphTypes.XY;

        int[] winbounds = new int[]{750, 750};
        double[] eqBounds = new double[]{-10, -10, 10, 10};
        double[] step = new double[]{1000};

        EquationSystem eqsys = new EquationSystem();

        if(args.length == 0) {
            indep = "theta";
            step = new double[]{-PI*2, PI*2, 1000};
            eqBounds = new double[]{-PI, -PI, PI, PI};
            dep.add("r");
            gtype = GraphComponents.GraphTypes.POLAR;
            eqsys.add("r=2*sin(theta)^2+1/2*cos(theta)^2");
            // eqsys.add("y=1+sin((9.45+x^(ln(pi)-x^x))/2)+x");

                //SIN THING

            // eqsys.add("y = 1 + sin(alpha)/c");
            // eqsys.add("alpha = (9.45 + x ^ theta) / 2");
            // eqsys.add("c = √atanx * pi/x^(-e)");
            // eqsys.add("theta = ln(pi) - c ^ x ");

                //TWISTY THING
            // eqsys.add("y = z · (x%2 > 1 ∨ x%2 < -1)");// ∨ z%2 < -1)");
            // eqsys.add("z = x₀ · x₁ · x₂ · x₃ · x₄");
            // eqsys.add("x₀ = (x) · c");
            // eqsys.add("x₁ = (x - 8) · c");
            // eqsys.add("x₂ = (x - 4) · c");
            // eqsys.add("x₃ = (x + 4) · c");
            // eqsys.add("x₄ = (x + 8) · c");
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
                    if(args[i].equals("--func")) {type = 'f'; continue;}
                    if(args[i].equals("--eq")) {type = 'e'; continue;}
                    if(args[i].equals("--idep")) {type = 'i'; continue;}
                    if(args[i].equals("--dep")) {type = 'd'; continue;}
                    if(args[i].equals("--gtype")) {type = 'g'; continue;}
                    if(args[i].equals("--step")) {type = 's'; continue;}
                    if(args[i].equals("--bounds")) {type = 'b'; continue;}
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
                    } else if (type == 'i'){

                    }
                }
            }
        }


        if(indep.isEmpty()){
            EquationSystem eqsysfinal = eqsys;
            dep.forEach(s -> Print.printi("RESULT ("+s+"):", eqsysfinal.eval(s)));
        }
        // MathCollection g3 = new MathCollection("{y | y = sinx}", -10, 10, 1);
        // g3.graph();
        String[] depl = new String[dep.size()];
        for(int i = 0; i < dep.size(); i++)
            depl[i] = dep.get(i);
        eqsys.graph(new GraphComponents(winbounds,
                                        eqBounds,
                                        step,
                                        gtype,
                                        indep,
                                        depl));
        // Print.printi("RESULT (y):", eqsys.eval("y"));
        // Print.printi("RESULT (x):", eqsys.eval("x"));
    }


}











