package Math.Equation.CustomFunctions;

import Math.Exception.NotDefinedException;
import Math.Exception.InvalidArgsException;

import Math.Equation.Equation;
import Math.Equation.Function;
import Math.Equation.Factors;
import Math.Equation.Node;
import Math.Equation.CustomFunction;
import Math.Equation.Token.Types;

import Math.Set.Set;
import Math.Display.GraphComponents;
import Math.Display.Grapher;

import java.util.ArrayList;


public class graph extends CustomFunction{

    public static String help(){
        return "Graphs any combination of sets and / or functions";
    }
    public static String syntax(){
        return "Any combination of 'set:','eq:', 'eqandset:' and 'eqtoset:'.\n" + 
                "- set syntax is: \"set:(VAL1,VAL2,VAL3...)(valA,valB,ValC...)\". Just displays that set.\n" + 
                "- eqandset syntax is: \"eqandset:EQUATION,[STEP],[MIN MAX]\". Displays a set based off the equation " +
                    "and params as well as the equation itself.\n" +
                "- eqtoset syntax is: \"eqandset:EQUATION,[STEP],[MIN, MAX]\". Displays a set based off the equation " +
                    "and params, but not the equation itself.";
    }

    @Override
    public double exec(Factors pFactors, Node pNode) throws NotDefinedException, InvalidArgsException {
        if(pNode.size() == 0)
            throw new InvalidArgsException("the size has to be greater than 1!");
        for(Node n : pNode.subNodes){
            if(n.type() != Types.ARGS){
                throw new InvalidArgsException("All arguments of graph must be of Token.Types 'ARG'!");
            }
        }
        ArrayList<Equation> equations = new ArrayList<Equation>();
        ArrayList<Set> sets = new ArrayList<Set>();
        GraphComponents gcomp = new GraphComponents();
        for(Node n : pNode.subNodes){
            String id = n.token.VAL.replaceAll("^(.*):.*","$1");
            String val = n.token.VAL.replaceAll("^" + id + ":","");
            System.err.println(id + " @ " + val);
            switch(id){
                case "eq": case "":
                    equations.add(new Equation(val, pFactors));
                    break;
                case "eqandset": //make sure to put these in front l0l
                    equations.add(new Equation(val.replaceAll(" ","").split(",")[0], pFactors));
                    System.out.println(equations.get(equations.size()-1).toFullString());
                case "eqtoset":
                    String[] args = val.replaceAll(" ","").split(",");
                    double min, max, cStep;
                    try{
                        if(args.length == 1 || args.length == 2){
                            min = gcomp.dispBounds()[0]; // this might bring up an error if eqsets are defined before 
                            max = gcomp.dispBounds()[2]; // custom gcomps are...
                            cStep = args.length == 2 ? Double.parseDouble(args[1]) : 10;
                        } else if(args.length == 4){
                            min = Double.parseDouble(args[2]);
                            max = Double.parseDouble(args[3]);
                            cStep = Double.parseDouble(args[1]);
                        } else{
                            throw new InvalidArgsException("If using Identifier 'eqset', the only allowed arguments "+
                                "are: 'eq' OR 'eq, cstep' OR 'eq, cstep, min, max'!");
                        }
                    } catch (NumberFormatException err){
                        throw new InvalidArgsException("One of the args for eqset (not the equation) isn't a double!");
                    }
                    sets.add(new Set(new Equation(args[0], pFactors), min, max, cStep));
                    break;
                case "set":
                    String[] rawStr = val.replaceAll(" ","").replaceAll("\\)\\(",";").replaceAll("\\(","").
                        replaceAll("\\)","").trim().split(";");

                    double[] arr1, arr2;
                    String arrs[];
                    //array 1
                    arrs = rawStr[0].split(",");
                    arr1 = new double[arrs.length];
                    for(int i = 0; i < arrs.length; i++){
                        arr1[i] = Double.parseDouble(arrs[i]);
                    }
                    //array 2
                    arrs = rawStr[1].split(",");
                    arr2 = new double[arrs.length];
                    for(int i = 0; i < arrs.length; i++){
                        arr2[i] = Double.parseDouble(arrs[i]);
                    }
                    
                    sets.add(new Set(arr1, arr2));
                    break;
                default:
                    System.err.println("[ERROR] Unrecognized Argument: '" + id + "'!");
            } 
        } 
        Grapher grapher = new Grapher(equations, sets, gcomp);
        grapher.graph();
        return 0;

    }
}