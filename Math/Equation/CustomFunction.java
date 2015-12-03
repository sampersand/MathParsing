package Math.Equation;

import Math.MathObject;
import Math.Print;

import Math.Equation.Function;
import Math.Equation.Node;
import Math.Equation.CustomFunction;

import Math.Equation.CustomFunctions.*;

import Math.Exception.NotDefinedException;
import Math.Exception.InvalidArgsException;
import Math.Exception.DoesntExistException;

import java.lang.reflect.*;

/** 
 * Note: all user-defined functions (as opposed to pre-defined) must inherit from this class.
 */
public class CustomFunction extends Function implements MathObject{
    public Class cl;

    public CustomFunction(){
        this(null, null, null);
    }
    public CustomFunction(String pVal){
        this(pVal, null, null);
    }

    public CustomFunction(String pVal, String pHelp, String pSyntax){
        super(pVal, pHelp, pSyntax);
        try {
            if(pVal.equals("")){
                Print.printe("Instantiating a CustomFunction without a function associated!");
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

    // public static String help() {
    //     throw new NotDefinedException("Implement me for your custom method!");
    // }
    // public static String syntax() {
    //     throw new NotDefinedException("Implement me for your custom method!");
    // }

    private Object getFunc(String pName){
        try{
            return cl.getDeclaredMethod(pName).invoke(null);
        } catch (IllegalAccessException err) {
            Print.printe("A IllegalAccessException occured when attempting to get '" + pName + "' " +
                               "of a CustomFunction (File Name: " + fName + "): " + err + " | " + err.getMessage() +
                               " | " + err.getCause());
        } catch (NullPointerException err) {
            throw new NotDefinedException("Hey, the CustomFunction '" + fName + "' doesn't have a '" + pName + 
                "' function and needs one!");
        } catch (NoSuchMethodException err) {
            Print.printe("A NoSuchMethodException occured when attempting to get '" + pName + "' " +
                               "of a CustomFunction (File Name: " + fName + "): " + err + " | " + err.getMessage() +
                               " | " + err.getCause());
        } catch (InvocationTargetException err) {
            Print.printe("A InvocationTargetException occured when attempting to get '" + pName + "' " +
                               "of a CustomFunction (File Name: " + fName + "): " + err + " | " + err.getMessage() +
                               " | " + err.getCause());
        }
        return null;

    }
    /** this is kinda hacked together l0l */
    @Override
    @SuppressWarnings("unchecked") //stupid cl.getDeclaredMethod
    public double exec(EquationSystem pEq, Node pNode) throws NotDefinedException, InvalidArgsException {
        try{
            Class[] argType = {EquationSystem.class, Node.class};
            Method execMethod = cl.getDeclaredMethod("exec",argType);
            Object[] argListForInvokedExec = new Object[]{pEq, pNode};
            return (double)execMethod.invoke(cl.newInstance(), argListForInvokedExec);
        } catch (NoSuchMethodException err) {
            Print.printe("A NoSuchMethodException happened when attempting to execute a " +
                               "custom method in file '" + fName + "'. ERROR: " + err + " | MESSAGE:  " +
                                err.getMessage() + " | CAUSE: " + err.getCause() + " | CAUSE'S STACKTRACE:\n");
            err.getCause().printStackTrace();
        } catch (InvocationTargetException err) {
            Print.printe("A InvocationTargetException happened when attempting to execute a " +
                               "custom method in file '" + fName + "'. ERROR: " + err + " | MESSAGE:  " +
                                err.getMessage() + " | CAUSE: " + err.getCause() + " | CAUSE'S STACKTRACE:\n");
            err.getCause().printStackTrace();
        } catch (IllegalAccessException err) {
            Print.printe("A IllegalAccessException happened when attempting to execute a " +
                               "custom method in file '" + fName + "'. ERROR: " + err + " | MESSAGE:  " +
                                err.getMessage() + " | CAUSE: " + err.getCause() + " | CAUSE'S STACKTRACE:\n");
            err.getCause().printStackTrace();
        } catch (InstantiationException err) {
            Print.printe("A InstantiationException happened when attempting to execute a " +
                               "custom method in file '" + fName + "'. ERROR: " + err + " | MESSAGE:  " +
                                err.getMessage() + " | CAUSE: " + err.getCause() + " | CAUSE'S STACKTRACE:\n");
            err.getCause().printStackTrace();
        } return 0;
    }
    @Override
    public String toString(){
        return "CustomFunction: '" + fName + "'";
    }

    @Override
    public String toFancyString(){
        throw new NotDefinedException("define me!");
    }
    
    @Override
    public String toFullString(){
        throw new NotDefinedException("define me!");
    }

}