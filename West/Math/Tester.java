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
            dep.add("r");
            step = new double[]{0, PI*2, 1000};
            eqBounds = new double[]{-PI*2, -PI*2, PI*2, PI*2};
            gtype = GraphComponents.GraphTypes.POLAR;
            eqsys.add("r=2sin²(theta)+½cos²(theta)");
            System.out.println(eqsys.toFullString());
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

            // MathCollection g3 = new MathCollection("{y | y = sinx}", -10, 10, 1);
            // g3.graph();
        } else {
            eqsys = new EquationSystem();
            if(args.length == 1) {
                eqsys = new EquationSystem().add(new Equation().add(args[0]));
            } else if(args.length > 1) {
                int i = -1;
                char type = ' ';
                if(!args[0].contains("--"))
                    throw new IllegalArgumentException("first value has to start with '--'");
                while(i < args.length - 1) { //args.length is String.
                    i++;
                    if(args[i].matches("--f(unc)?")) {type = 'f'; continue;}
                    if(args[i].matches("--e(q)?")) {type = 'e'; continue;}
                    if(args[i].matches("--i(dep)?")) {type = 'i'; continue;}
                    if(args[i].matches("--d(ep)?")) {type = 'd'; continue;}
                    if(args[i].matches("--g(type)?")) {type = 'g'; continue;}
                    if(args[i].matches("--s(tep)?")) {type = 's'; continue;}
                    if(args[i].matches("--b(ounds)?")) {type = 'b'; continue;}
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
                        indep = args[i];
                    } else if (type == 'd'){
                        dep.add(args[i]);
                    } else if (type == 'g'){
                        gtype = args[i].equals("P") ? GraphComponents.GraphTypes.POLAR : GraphComponents.GraphTypes.XY;
                    } else if (type == 's'){
                        String[] spl = args[i].split(",");
                        assert spl.length == 1 || spl.length == 3 : "Step is 'Step' or 'Min, Max, Step'";
                        try{
                            if(spl.length == 0)
                                step = new double[]{Double.parseDouble(spl[0])};
                            else
                                step = new double[]{Double.parseDouble(spl[0]),
                                                    Double.parseDouble(spl[1]),
                                                    Double.parseDouble(spl[2])};
                        } catch(NumberFormatException exc){
                            System.err.println("Step is 'Step' or 'Min, Max, Step' and all numbers, not '"+args[i]+"'");
                        }
                    } else if (type == 'b'){
                        String[] spl = args[i].split(",");
                        assert spl.length == 4 : "Bounds needs to be 'Min x, Min y, Max x, Max y' and all numbers.";
                        try{
                        eqBounds = new double[]{Double.parseDouble(spl[0]),
                                            Double.parseDouble(spl[1]),
                                            Double.parseDouble(spl[2]),
                                            Double.parseDouble(spl[3])};
                        } catch(NumberFormatException exc){
                            System.err.println("Bounds needs to be 'Min x, Min y, Max x, Max y' and all numbers.");
                        }
                    }
                }
            }
        }

        if(indep.isEmpty()){
            EquationSystem eqsysfinal = eqsys;
            dep.forEach(s -> Print.printi("RESULT ("+s+"):", eqsysfinal.eval(s)));
        } else {
            String[] depl = new String[dep.size()];
            for(int i = 0; i < dep.size(); i++)
                depl[i] = dep.get(i);
            eqsys.graph(new GraphComponents(winbounds,
                                            eqBounds,
                                            step,
                                            gtype,
                                            indep,
                                            depl));
        }
    }


}











