package Math.Equation.Function;

import Math.Print;
import Math.Equation.EquationSystem;
import Math.Equation.Equation;
import Math.Equation.Node;
import Math.Equation.Function.InBuiltFunction;
import Math.Equation.Token.Type;
import Math.Exception.NotDefinedException;
import Math.Set.NumberCollection;
import Math.Display.GraphComponents;
import Math.Display.Grapher;

import java.util.ArrayList;


public class GraphFunction extends InBuiltFunction{
    public GraphFunction(){
        super("graph", "graph the arguments", "graph(A, B, ... )");
    }
    protected EquationSystem equationsToGraph;
    protected ArrayList<NumberCollection<Double>> numcToGraph;
    protected GraphComponents gcomp;
    protected Node node;

    public String help() {
        return "Graphs any combination of numcs and / or functions";
    }
    public String syntax() {
        return "Any combination of 'numc:','eq:', 'eqandnumc:' and 'eqtonumc:'.\n" + 
                "- numc syntax is: \"numc:(.val()1,.val()2,.val()3...)(valA,valB,ValC...)\". Just displays that numc.\n" + 
                "- eqandnumc syntax is: \"eqandnumc:EQUATION,[STEP],[MIN MAX]\". " +
                    "Graphs a numc based off the equationsToGraph and params as well as the equationsToGraph itself.\n" +
                "- eqtonumc syntax is: \"eqandnumc:EQUATION,[STEP],[MIN, MAX]\". "+
                    "Graphs a numc based off the equationsToGraph and params, but not the equationsToGraph itself.";
    }

    @Override
    public double exec(final EquationSystem pEqSys,
                       Node pNode) throws
                           NotDefinedException,
                           IllegalArgumentException {
        assert pNode.size() > 0 : "Cannot evaluate the node '" + pNode.token().val() + "' when it's size isn't greater"+
                                  " than 1";
        for(Node n : pNode.subNodes()) {
            if(n.token().type() != Type.ARGS) {
                throw new IllegalArgumentException("All arguments of graph must be of Token.Type 'ARG'!");
            }
        }
        equationsToGraph = new EquationSystem();
        numcToGraph = new ArrayList<NumberCollection<Double>>();
        gcomp = new GraphComponents();
        gcomp = new GraphComponents(new int[]{1250, 750}, new double[]{- 10, - 10, 10, 10}, 1000);
        node = pNode;

        for(Node n : node.subNodes()) {
            String id = n.token().val().replaceAll("^(.*):.*","$1");
            String[] vals = n.token().val().replaceAll("^" + id + ":","").replaceAll(" ","").split(",");
            switch(id) {
                case "eq": case "":
                    equationsToGraph.add(vals[0]);
                    break;
                case "eqandnumc": //make sure to put these in front l0l
                    equationsToGraph.add(new Equation().add(vals[0]));
                case "eqtoset":
                    numcToGraph.add(varsToNumC(vals));
                    break;
                case "set":
                    numcToGraph.add(getNumC(vals[0].split(",")));
                    break;
                case "eqandresid":
                    equationsToGraph.add(new Equation().add(vals[0]));
                case "eqtoresid":
                    numcToGraph.add(varsToNumC(vals).resid());
                    break;
                case "resid": //residuals
                    numcToGraph.add(getNumC(vals[0].split(",")).resid());
                    break;
                default:
                    Print.printe("[ERROR] Unrecognized Argument: '" + id + "'!");
            } 
        }
        Grapher grapher = new Grapher(equationsToGraph, pEqSys, numcToGraph, gcomp);
        System.out.println(grapher.toFullString());
        grapher.graph();
        return 0;

    }

    private NumberCollection<Double> varsToNumC(String[] vals) {
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
                throw new IllegalArgumentException("If using Identifier 'eqset', the only allowed arguments "+
                    "are: 'eq' OR 'eq, cstep' OR 'eq, cstep, min, max'!");
            }
        } catch (NumberFormatException err) {
            throw new IllegalArgumentException("One of the args for eqset (not the equation) isn't a double!");
        }
        return new NumberCollection<Double>(new EquationSystem().add(vals[0]), min, max, cStep);
    }
    private NumberCollection<Double> getNumC(String[] vals) {
        return new NumberCollection<Double>(){{
            for(String val : vals)
                add(Double.parseDouble(val));
        }};

    }
}