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
        return "Any combination of '@matr','eq', '$graphcomp'.";
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
        if(pNode.get(0).token.VAL.equals("@arr")){
            double[] arr1, arr2;
            String arrs[];
            //array 1
            arrs = pNode.get(1).token.VAL.split(",");
            arr1 = new double[arrs.length];
            for(int i = 0; i < arrs.length; i++){
                arr1[i] = Double.parseDouble(arrs[i]);
            }
            //array 2
            arrs = pNode.get(2).token.VAL.split(",");
            arr2 = new double[arrs.length];
            for(int i = 0; i < arrs.length; i++){
                arr2[i] = Double.parseDouble(arrs[i]);
            }
            
            Set set = new Set(arr1, arr2);
            System.out.println("@@@@@@8");
            System.out.println(set);
            System.out.println("@@@@@@9");
        } else {
            ArrayList<Equation> equations = new ArrayList<Equation>();
            ArrayList<Set> sets = new ArrayList<Set>();
            GraphComponents gcomp = new GraphComponents();
            for(int i = 0; i < pNode.size(); i++){
                if(pNode.get(i).type() != Types.ARGS)
                    throw new InvalidArgsException("uh oh, you need to have each argument to be of type ARG (" + i +
                                                   ") isn't");
            equations.add(new Equation(pNode.get(i).token.VAL, pFactors));
            }
            // equations = new ArrayList<Equation>();
            Grapher grapher = new Grapher(equations, sets, gcomp);
            grapher.graph();
        }
        return 0;

    }
}