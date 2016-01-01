package West.Math.Equation.Function;

import West.Math.Print;
import West.Math.Equation.EquationSystem;
import West.Math.Equation.Equation;
import West.Math.Set.Node.TokenNode;
import West.Math.Equation.Function.InBuiltFunction;
import West.Math.Exception.NotDefinedException;
import West.Math.Set.NumberCollection;
import West.Math.Display.GraphComponents;
import West.Math.Display.Grapher;

import java.util.ArrayList;
import java.util.HashMap;

public class GraphFunction extends InBuiltFunction{
    public GraphFunction(){
        super("graph", "graph the arguments", "graph(A, B, ... )");
    }
    protected EquationSystem equationsToGraph;
    protected ArrayList<ArrayList<NumberCollection<Double>>> numcToGraph;
    protected GraphComponents gcomp;
    protected TokenNode node;

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
    public HashMap<String, Double> exec(final EquationSystem pEqSys,
                       TokenNode pNode) throws
                           NotDefinedException,
                           IllegalArgumentException {
        assert pNode.size() > 0 : "Cannot evaluate the node '" + pNode.token().val() + "' when it's size isn't greater"+
                                  " than 1";
        equationsToGraph = new EquationSystem();
        numcToGraph = new ArrayList<ArrayList<NumberCollection<Double>>>();
        gcomp = new GraphComponents(new int[]{1250, 750}, new double[]{- 10, - 10, 10, 10}, 1000);
        node = pNode;

        for(Object o : node.elements()) { //TokenNode t
            TokenNode n = new TokenNode();
            String id = n.token().val().replaceAll("^(.*):.*","$1");
            String[] vals = n.token().val().replaceAll("^" + id + ":","").replaceAll(" ","").split(",");
            ArrayList<NumberCollection<Double>> temp;
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
                    temp = varsToNumC(vals);
                    assert temp.size() == 2;
                    numcToGraph.add(new ArrayList<NumberCollection<Double>>(){{
                        add(temp.get(0).resid(temp.get(1)));
                        add(temp.get(1));
                    }});
                    break;
                case "resid": //residuals
                    temp = getNumC(vals[0].split(","));
                    assert temp.size() == 2;
                    numcToGraph.add(new ArrayList<NumberCollection<Double>>(){{
                        add(temp.get(0).resid(temp.get(1)));
                        add(temp.get(1));
                    }});
                    break;
                case "bounds":
                    assert vals.length == 4 : "cannot do bounds with a vals length of 4"; //make this not assert
                    double x, y, X, Y;
                    x = Double.parseDouble(vals[0]);
                    y = Double.parseDouble(vals[0]);
                    X = Double.parseDouble(vals[0]);
                    Y = Double.parseDouble(vals[0]);
                    gcomp.setDispBounds(x, y, X, Y);
                    break;
                default:
                    Print.printe("[ERROR] Unrecognized Argument: '" + id + "'!");
            } 
        }
        // Grapher grapher = new Grapher(equationsToGraph, pEqSys, numcToGraph, gcomp);
        // grapher.graph();
        throw new NotDefinedException(); //TODO: THIS
        // return new HashMap<String, Double>();

    }

    private ArrayList<NumberCollection<Double>> varsToNumC(String[] vals) {
        double min, max, cStep;
        ArrayList<NumberCollection<Double>> ret = new ArrayList<NumberCollection<Double>>(){{
            add(new NumberCollection<Double>());
            add(new NumberCollection<Double>());
        }};
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
        ret.add(new NumberCollection<Double>(new EquationSystem().add(vals[0]), min, max, cStep));
        ret.add(new NumberCollection<Double>());
        for(double i = min; i < max; i += (max - min) / cStep) {
            ret.get(1).add(i);
        }
        return ret;
    }
    private ArrayList<NumberCollection<Double>> getNumC(String[] vals) {
        throw new NotDefinedException();
        // return new NumberCollection<Double>(){{
        //     for(String val : vals)
        //         add(Double.parseDouble(val));
        // }};

    }
}