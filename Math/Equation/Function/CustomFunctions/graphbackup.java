// package Math.Equation.Function;

// import Math.Print;
// import Math.Equation.EquationSystem;
// import Math.Equation.Equation;
// import Math.Equation.Function.Function;
// import Math.Equation.Node;
// import Math.Equation.Function.InBuiltFunction;
// import Math.Equation.Token.Type;
// import Math.Exception.NotDefinedException;
// import Math.Set.Set;
// import Math.Display.GraphComponents;
// import Math.Display.Grapher;

// import java.util.ArrayList;


// public class graphbackup extends InBuiltFunction{
//     public graphbackup(){
//         super("graph","","");
//     }
//     protected EquationSystem equationsToGraph;
//     protected ArrayList<Set> setsToGraph;
//     protected GraphComponents gcomp;
//     protected Node node;

//     public String help() {
//         return "Graphs any combination of sets and / or functions";
//     }
//     public String syntax() {
//         return "Any combination of 'set:','eq:', 'eqandset:' and 'eqtoset:'.\n" + 
//                 "- set syntax is: \"set:(.val()1,.val()2,.val()3...)(valA,valB,ValC...)\". Just displays that set.\n" + 
//                 "- eqandset syntax is: \"eqandset:EQUATION,[STEP],[MIN MAX]\". " +
//                     "Graphs a set based off the equationsToGraph and params as well as the equationsToGraph itself.\n" +
//                 "- eqtoset syntax is: \"eqandset:EQUATION,[STEP],[MIN, MAX]\". "+
//                     "Graphs a set based off the equationsToGraph and params, but not the equationsToGraph itself.";
//     }

//     @Override
//     public double exec(final EquationSystem pEqSys,
//                        Node pNode) throws
//                            NotDefinedException,
//                            IllegalArgumentException {
//         assert pNode.size() > 0 : "Cannot evaluate the node '" + pNode.token().val() + "' when it's size isn't greater"+
//                                   " than 1";
//         for(Node n : pNode.subNodes()) {
//             if(n.token().type() != Type.ARGS) {
//                 throw new IllegalArgumentException("All arguments of graph must be of Token.Type 'ARG'!");
//             }
//         }
//         equationsToGraph = new EquationSystem();
//         setsToGraph = new ArrayList<Set>();
//         gcomp = new GraphComponents();
//         gcomp = new GraphComponents(new int[]{1250, 750}, new double[]{- 10, - 10, 10, 10}, 1000);
//         node = pNode;

//         for(Node n : node.subNodes()) {
//             String id = n.token().val().replaceAll("^(.*):.*","$1");
//             String[] vals = n.token().val().replaceAll("^" + id + ":","").replaceAll(" ","").split(",");
//             switch(id) {
//                 case "eq": case "":
//                     equationsToGraph.add(vals[0]);
//                     break;
//                 case "eqandset": //make sure to put these in front l0l
//                     equationsToGraph.add(new Equation().add(vals[0]));
//                 case "eqtoset":
//                     setsToGraph.add(varsToSet(vals));
//                     break;
//                 case "set":
//                     setsToGraph.add(getSet(vals));
//                     break;
//                 case "eqandresid":
//                     equationsToGraph.add(new Equation().add(vals[0]));
//                 case "eqtoresid":
//                     setsToGraph.add(varsToSet(vals).resid());
//                     break;
//                 case "resid": //residuals
//                     setsToGraph.add(getSet(vals).resid());
//                     break;
//                 default:
//                     Print.printe("[ERROR] Unrecognized Argument: '" + id + "'!");
//             } 
//         }
//         Grapher grapher = new Grapher(equationsToGraph, pEqSys, setsToGraph, gcomp);
//         System.out.println(grapher.toFullString());
//         grapher.graph();
//         return 0;

//     }

//     private Set varsToSet(String[] vals) {
//         double min, max, cStep;
//         try {
//             if(vals.length == 1 || vals.length == 2) {
//                 min = gcomp.dispBounds()[0]; // this might bring up an error if eqsets are defined before 
//                 max = gcomp.dispBounds()[2]; // custom gcomps are...
//                 cStep = vals.length == 2 ? Double.parseDouble(vals[1]) : 50;
//             } else if(vals.length == 4) {
//                 min = Double.parseDouble(vals[2]);
//                 max = Double.parseDouble(vals[3]);
//                 cStep = Double.parseDouble(vals[1]);
//             } else {
//                 throw new IllegalArgumentException("If using Identifier 'eqset', the only allowed arguments "+
//                     "are: 'eq' OR 'eq, cstep' OR 'eq, cstep, min, max'!");
//             }
//         } catch (NumberFormatException err) {
//             throw new IllegalArgumentException("One of the args for eqset (not the equation) isn't a double!");
//         }
//         return new Set(new EquationSystem().add(vals[0]), min, max, cStep);
//     }
//     private Set getSet(String[] vals) {
//         double[] arr1, arr2;
//         String arrs[];
//         //array 1
//         arrs = vals[0].split(",");
//         arr1 = new double[arrs.length];
//         for(int i = 0; i < arrs.length; i++) {
//             arr1[i] = Double.parseDouble(arrs[i]);
//         }
//         //array 2
//         arrs = vals[1].split(",");
//         arr2 = new double[arrs.length];
//         for(int i = 0; i < arrs.length; i++) {
//             arr2[i] = Double.parseDouble(arrs[i]);
//         }
//         return new Set(arr1, arr2);

//     }
// }