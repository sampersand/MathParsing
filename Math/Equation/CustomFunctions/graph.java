package Math.Equation.CustomFunctions;

import Math.Print;
import Math.Equation.EquationSystem;
import Math.Equation.Equation;
import Math.Equation.Function;
import Math.Equation.Node;
import Math.Equation.CustomFunction;
import Math.Equation.Token.Type;
import Math.Exception.NotDefinedException;
import Math.Exception.InvalidArgsException;
import Math.Set.Set;
import Math.Display.GraphComponents;
import Math.Display.Grapher;

import java.util.ArrayList;


public class graph extends CustomFunction{
    public graph(){
        super("graph");
    }
    protected EquationSystem equationsys;
    protected ArrayList<Set> sets;
    protected GraphComponents gcomp;
    protected Node node;

    public String help() {
        return "Graphs any combination of sets and / or functions";
    }
    public String syntax() {
        return "Any combination of 'set:','eq:', 'eqandset:' and 'eqtoset:'.\n" + 
                "- set syntax is: \"set:(.val()1,.val()2,.val()3...)(valA,valB,ValC...)\". Just displays that set.\n" + 
                "- eqandset syntax is: \"eqandset:EQUATION,[STEP],[MIN MAX]\". Graphs a set based off the equationsys" +
                    " and params as well as the equationsys itself.\n" +
                "- eqtoset syntax is: \"eqandset:EQUATION,[STEP],[MIN, MAX]\". Graphs a set based off the equationsys" +
                    " and params, but not the equationsys itself.";
    }

    @Override
    public double exec(EquationSystem pEq,
                       Node pNode) throws
                           NotDefinedException,
                           InvalidArgsException {
        if(pNode.size() == 0)
            throw new InvalidArgsException("Cannot evaluate the node '" + pNode.token().val() +
                    "' when it's size isn't greater than 1");
        for(Node n : pNode.subNodes()) {
            if(n.token().type() != Type.ARGS) {
                throw new InvalidArgsException("All arguments of graph must be of Token.Type 'ARG'!");
            }
        }
        equationsys = new EquationSystem();
        sets = new ArrayList<Set>();
        gcomp = new GraphComponents();
        gcomp = new GraphComponents(new int[]{1250, 750}, new double[]{- 10, - 10, 10, 10}, 100);
        node = pNode;

        for(Node n : node.subNodes()) {
            String id = n.token().val().replaceAll("^(.*):.*","$1");
            String[] vals = n.token().val().replaceAll("^" + id + ":","").replaceAll(" ","").split(",");
            switch(id) {
                case "eq": case "":
                    equationsys.add(vals[0]);
                    break;
                case "eqandset": //make sure to put these in front l0l
                    equationsys.add(new Equation().add(vals[0]));
                case "eqtoset":
                    sets.add(varsToSet(vals));
                    break;
                case "set":
                    sets.add(getSet(vals));
                    break;
                case "eqandresid":
                    equationsys.add(new Equation().add(vals[0]));
                case "eqtoresid":
                    sets.add(varsToSet(vals).resid());
                    break;
                case "resid": //residuals
                    sets.add(getSet(vals).resid());
                    break;
                default:
                    Print.printe("[ERROR] Unrecognized Argument: '" + id + "'!");
            } 
        } 
        Grapher grapher = new Grapher(equationsys, sets, gcomp);

        grapher.graph();
        return 0;

    }

    private Set varsToSet(String[] vals) {
        double min, max, cStep;
        try {
            if(vals.length == 1 || vals.length == 2) {
                min = gcomp.dispBounds()[0]; // this might bring up an error if eqsets are defined before 
                max = gcomp.dispBounds()[2]; // custom gcomps are...
                cStep = vals.length == 2 ? Double.parseDouble(vals[1]) : 50;
            } else if(vals.length == 4) {
                min = Double.parseDouble(vals[2]);
                max = Double.parseDouble(vals[3]);
                cStep = Double.parseDouble(vals[1]);
            } else {
                throw new InvalidArgsException("If using Identifier 'eqset', the only allowed arguments "+
                    "are: 'eq' OR 'eq, cstep' OR 'eq, cstep, min, max'!");
            }
        } catch (NumberFormatException err) {
            throw new InvalidArgsException("One of the args for eqset (not the equation) isn't a double!");
        }
        return new Set(new EquationSystem().add(vals[0]), min, max, cStep);
    }
    private Set getSet(String[] vals) {
        double[] arr1, arr2;
        String arrs[];
        //array 1
        arrs = vals[0].split(",");
        arr1 = new double[arrs.length];
        for(int i = 0; i < arrs.length; i++) {
            arr1[i] = Double.parseDouble(arrs[i]);
        }
        //array 2
        arrs = vals[1].split(",");
        arr2 = new double[arrs.length];
        for(int i = 0; i < arrs.length; i++) {
            arr2[i] = Double.parseDouble(arrs[i]);
        }
        return new Set(arr1, arr2);

    }
}