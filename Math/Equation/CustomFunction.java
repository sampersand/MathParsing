package Math.Equation;

import Math.Equation.Exception.NotDefinedException;
import Math.Equation.Exception.InvalidArgsException;
import Math.Equation.CustomFunctions.*;

import Math.Equation.Function;
import Math.Equation.Factors;
import Math.Equation.Node;
import Math.Equation.CustomFunction;

import java.lang.reflect.*;

public class CustomFunction extends Function {
    public static String HELP = "f(a, b, ... n) = 1/a + 1/b + ... + 1/n";
    public static String SYNTAX = "f(a, b, ... n) such that a, b, ..., n are all numbers or variables.";

    public CustomFunction(){
        this(null);
    }
    public CustomFunction(String pVal){
        super(pVal);
    }
    public String toString(){
        return "CustomFunction: '" + fName + "'\nHELP: " + HELP + "\nSYNTAX: " + SYNTAX;
    }
    /** 
     * This thing takes a node (usually the node from {@link #exec(Factors,Node) exec}), and returns an array of the 
     * numerical values of each subnode.
     */
    protected double[] evalNode(Factors pFactors, Node pNode){
        double[] ret = new double[pNode.size()];
        for(int i = 0; i < ret.length; i++) ret[i] = pFactors.eval(pNode.subNodes.get(i));
        return ret;

    }
    /** this is kinda hacked together l0l */
    @Override
    @SuppressWarnings("unchecked") //stupid cl.getDeclaredMethod
    public double exec(Factors pFactors, Node pNode) throws NotDefinedException, InvalidArgsException {
        try{
            Class[] argTypes = {Factors.class, Node.class};
            Class cl = Class.forName("Math.Equation.CustomFunctions." + fName);
            Method execMethod = cl.getDeclaredMethod("exec",argTypes);
            Object[] argListForInvokedExec = new Object[]{pFactors, pNode};
            return (double)execMethod.invoke(cl.newInstance(), argListForInvokedExec);
        } catch (ClassNotFoundException err) {
            System.err.println("[ERROR] A ClassNotFoundException happened when attempting to execute a " +
                               "custom method (File Name: " + fName + "): " + err + " | " + err.getMessage() + " | " +
                                err.getCause());
        } catch (NoSuchMethodException err) {
            System.err.println("[ERROR] A NoSuchMethodException happened when attempting to execute a " +
                               "custom method (File Name: " + fName + "): " + err + " | " + err.getMessage() + " | " +
                                err.getCause());
        } catch (InvocationTargetException err) {
            System.err.println("[ERROR] A InvocationTargetException happened when attempting to execute a " +
                               "custom method (File Name: " + fName + "): " + err + " | " + err.getMessage() + " | " +
                                err.getCause());
        } catch (IllegalAccessException err) {
            System.err.println("[ERROR] A IllegalAccessException happened when attempting to execute a " +
                               "custom method (File Name: " + fName + "): " + err + " | " + err.getMessage() + " | " +
                                err.getCause());
        } catch (InstantiationException err) {
            System.err.println("[ERROR] A InstantiationException happened when attempting to execute a " +
                               "custom method (File Name: " + fName + "): " + err + " | " + err.getMessage() + " | " +
                                err.getCause());
        } return 0;
    }
}