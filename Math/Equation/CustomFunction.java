package Math.Equation;

import Math.Exception.NotDefinedException;
import Math.Exception.InvalidArgsException;
import Math.Exception.DoesntExistException;
import Math.Equation.CustomFunctions.*;

import Math.Equation.Function;
import Math.Equation.Factors;
import Math.Equation.Node;
import Math.Equation.CustomFunction;

import java.lang.reflect.*;

/** 
 * Note: all user-defined functions (as opposed to pre-defined) must inherit from this class.
 */
public class CustomFunction extends Function {
    public Class cl;
    public CustomFunction(){
        this("");
    }
    public CustomFunction(String pVal){
        super(pVal);
        try {
            if(pVal.equals("")){
                System.err.println("[ERROR] Instantiating a CustomFunction without a class associated!");
                cl = null;
            } else{
                cl = Class.forName("Math.Equation.CustomFunctions." + fName);
                if(cl == null)
                    throw new ClassNotFoundException();
            }
        } catch (ClassNotFoundException err) {
            throw new DoesntExistException("CustomFunction '" + fName + 
                                            "' doesn't exist! in Math.Equation.CustomFunctions.*");
        }
    }
    public String getHelp() {
        return (String) getFunc("help");
    }
    public String getSyntax() {
        return (String) getFunc("syntax");
    }

    public static String help() {
        throw new NotDefinedException("Implement me for your custom method!");
    }
    public static String syntax() {
        throw new NotDefinedException("Implement me for your custom method!");
    }

    private Object getFunc(String pName){
        try{
            return cl.getDeclaredMethod(pName).invoke(null);
        } catch (IllegalAccessException err) {
            System.err.println("[ERROR] A IllegalAccessException occured when attempting to get '" + pName + "' " +
                               "of a CustomFunction (File Name: " + fName + "): " + err + " | " + err.getMessage() +
                               " | " + err.getCause());
        } catch (NullPointerException err) {
            throw new NotDefinedException("Hey, the CustomFunction '" + fName + "' doesn't have a '" + pName + 
                "' function and needs one!");
        } catch (NoSuchMethodException err) {
            System.err.println("[ERROR] A NoSuchMethodException occured when attempting to get '" + pName + "' " +
                               "of a CustomFunction (File Name: " + fName + "): " + err + " | " + err.getMessage() +
                               " | " + err.getCause());
        } catch (InvocationTargetException err) {
            System.err.println("[ERROR] A InvocationTargetException occured when attempting to get '" + pName + "' " +
                               "of a CustomFunction (File Name: " + fName + "): " + err + " | " + err.getMessage() +
                               " | " + err.getCause());
        // } catch (InstantiationException err) {
        //     System.err.println("[ERROR] A InstantiationException occured when attempting to get '" + pName + "' " +
        //                        "of a CustomFunction (File Name: " + fName + "): " + err + " | " + err.getMessage() +
        //                        " | " + err.getCause());
        }
        return null;

    }
    /**
     * This thing takes a node (usually the node from {@link #exec(Factors,Node) exec}), and returns an array of the 
     * numerical values of each subnode.
     * @param pFactors          The factors that will be used when evaluating pNode.
     * @param pNode             The node to be evaluated.
     * @return An array of doubles, with each position corresponding to the value of each Node of that position in 
     *         {@link Node#subNodes pNode's subNodes}.
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
            Method execMethod = cl.getDeclaredMethod("exec",argTypes);
            Object[] argListForInvokedExec = new Object[]{pFactors, pNode};
            return (double)execMethod.invoke(cl.newInstance(), argListForInvokedExec);
        } catch (NoSuchMethodException err) {
            System.err.println("[ERROR] A NoSuchMethodException happened when attempting to execute a " +
                               "custom method in file '" + fName + "' " + err + " | " + err.getMessage() + " | " +
                                err.getCause());
        } catch (InvocationTargetException err) {
            System.err.println("[ERROR] A InvocationTargetException happened when attempting to execute a " +
                               "custom method in file '" + fName + "' " + err + " | " + err.getMessage() + " | " +
                                err.getCause());
        } catch (IllegalAccessException err) {
            System.err.println("[ERROR] A IllegalAccessException happened when attempting to execute a " +
                               "custom method in file '" + fName + "' " + err + " | " + err.getMessage() + " | " +
                                err.getCause());
        } catch (InstantiationException err) {
            System.err.println("[ERROR] A InstantiationException happened when attempting to execute a " +
                               "custom method in file '" + fName + "' " + err + " | " + err.getMessage() + " | " +
                                err.getCause());
        } return 0;
    }
    public String toString(){
            return "CustomFunction '" + fName + "'.";
    }

}