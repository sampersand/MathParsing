package West.Math.Equation.Function;

import West.Math.MathObject;
import West.Math.Print;
import West.Math.Equation.Function.Function;
import West.Math.Equation.Node;
import West.Math.Equation.EquationSystem;
import West.Math.Equation.Function.CustomFunctions.*;
import West.Math.Exception.NotDefinedException;

import java.lang.reflect.*;

/**
 * A class that all user-defined functions must impliment. 
 * 
 * @author Sam Westerman
 * @version 0.76
 * @since 0.1
 */
public class CustomFunction extends Function implements MathObject {
    
    /**
     * The Class object associated with the name. The object should be stored in
     * <code>West.Math.Equation/CustomFunction/NAME.java</code> where NAME is {@link #name}.
     */
    public Class cl;

    /**
     * Default constructor. Note that this _HAS_ to be overridden with the following code:
     * <br><code>public [Class Name]{
     *    super("[Class Name]");
     * }</code>.
     * <br>By default, just passes <code>null</code> to
     * {@link #CustomFunction(String) another CustomFunction constructor}.
     * @throws IllegalArgumentException When either name, help, and / or syntax is null.
     */
    public CustomFunction() throws IllegalArgumentException{
        this("");
    }

    /**
     * A constructor that just takes the name of the function. Passes <code>pName, null, null</code> (null help and null
     * syntax) to {@link #CustomFunction(String,String,String) the main CustomFunction constructor}.
     * @param pName     The name of the file. pName shouldn't be "foo.class", but just "foo".
     * @throws IllegalArgumentException When either name, help, and / or syntax is null.
     */
    public CustomFunction(String pName) throws IllegalArgumentException{
        this(pName, "", "");
    }

    /**
     * The main constructor for CustomFunction. Takes a name, a help string, and a syntax string.
     * @param pName     The name of the file. pName shouldn't be "foo.class", but just "foo".
     * @param pHelp     The "help" text that will be displayed when the {@link #help()} function is called.
     * @param pSyntax   The "syntax" text that will be displayed when the {@link #syntax()} function is called.
     * @throws IllegalArgumentException When either name, help, and / or syntax is null.
     */
    public CustomFunction(String pName,
                          String pHelp,
                          String pSyntax) throws IllegalArgumentException{
        super(pName, pHelp, pSyntax);
        try {
            if(pName.isEmpty()) {
                Print.printe("Instantiating a CustomFunction without a function associated!");
                cl = null;
            } else {
                cl = Class.forName("West.Math.Equation.CustomFunctions." + name);
            }
        } catch (ClassNotFoundException err) {
                throw new IllegalArgumentException("Cannot instatiate CustomFunction '" + name + 
                    "'! All custom class currently must be in West.Math.Equation/CustomFunctions/<CLASS>!");
        }
    }

    
    /**
     * Gets the help function from the class {@link #cl}.
     * @return The help function from the {@link #cl} class.
     */
    public String getHelp() {
        return (String) getFunc("help");
    }

    /**
     * Gets the help function from the class {@link #cl}.
     * @return The help function from the {@link #cl} class.
     */
    public String getSyntax() {
        return (String) getFunc("syntax");
    }


    /**
     * Gets a function of name <code>pFuncName</code> from the class {@link #cl}.
     * @param pFuncName     The name of the function 
     * @return A function object by the name <code>pFuncName</code>.
     */
    private Object getFunc(String pFuncName) {
        try {
            return cl.getDeclaredMethod(pFuncName).invoke(null);
        } catch (IllegalAccessException err) {
            Print.printe("A IllegalAccessException occured when attempting to get '" + pFuncName + "' " +
                               "of a CustomFunction (File Name: " + name + "): " + err + " | " + err.getMessage() +
                               " | " + err.getCause());
        } catch (NullPointerException err) {
            throw new NotDefinedException("Cannot get the function '" + pFuncName +"' from the CustomFunction '" + name 
                + "'' because it doesnt exist, but should!");
        } catch (NoSuchMethodException err) {
            Print.printe("A NoSuchMethodException occured when attempting to get '" + pFuncName + "' " +
                               "of a CustomFunction (File Name: " + name + "): " + err + " | " + err.getMessage() +
                               " | " + err.getCause());
        } catch (InvocationTargetException err) {
            Print.printe("A InvocationTargetException occured when attempting to get '" + pFuncName + "' " +
                               "of a CustomFunction (File Name: " + name + "): " + err + " | " + err.getMessage() +
                               " | " + err.getCause());
        }
        return null;

    }
    
    /**
     * Takes the parameter {@link Node} (and {@link EquationSystem}, performs whatever this function is defined to do,
     * and returns the result.
     * <br>NOTE: This is kinda a weird and thrown together way of getting a custom class. They have to be in 
     * <code>West.Math.Equation.CustomFunctions</code>, and have to have exec declared.
     * @param pEqSys        An {@link EquationSystem} that contains all relevant information about
     *                      {@link Equation Equations} and {@link Function Functions} is stored.
     * @param pNode         The {@link Node} that is going to be solved.
     * @return A double representing the value of <code>pNode</code>pNode, when solved for with <code>pEqSys</code>.
     * @throws NotDefinedException    Thrown when the function is defined, but how to execute it isn't.
     * @throws IllegalArgumentException   Thrown when the function required parameters, and the ones passed aren't right.
     */
    @Override
    @SuppressWarnings("unchecked") //stupid cl.getDeclaredMethod
    public double exec(final EquationSystem pEqSys,
                       Node pNode) throws
                           NotDefinedException,
                           IllegalArgumentException {
        try {
            Class[] argType = {EquationSystem.class, Node.class};
            Method execMethod = cl.getDeclaredMethod("exec",argType);
            Object[] argListForInvokedExec = new Object[]{pEqSys, pNode};
            return (double)execMethod.invoke(cl.newInstance(), argListForInvokedExec);
        } catch (NoSuchMethodException err) {
            Print.printe("A NoSuchMethodException happened when attempting to execute a " +
                               "custom method in file '" + name + "'. ERROR: " + err + " | MESSAGE:  " +
                                err.getMessage() + " | CAUSE: " + err.getCause() + " | CAUSE'S STACKTRACE:\n");
            err.getCause().printStackTrace();
        } catch (InvocationTargetException err) {
            Print.printe("A InvocationTargetException happened when attempting to execute a " +
                               "custom method in file '" + name + "'. ERROR: " + err + " | MESSAGE:  " +
                                err.getMessage() + " | CAUSE: " + err.getCause() + " | CAUSE'S STACKTRACE:\n");
            err.getCause().printStackTrace();
        } catch (IllegalAccessException err) {
            Print.printe("A IllegalAccessException happened when attempting to execute a " +
                               "custom method in file '" + name + "'. ERROR: " + err + " | MESSAGE:  " +
                                err.getMessage() + " | CAUSE: " + err.getCause() + " | CAUSE'S STACKTRACE:\n");
            err.getCause().printStackTrace();
        } catch (InstantiationException err) {
            Print.printe("A InstantiationException happened when attempting to execute a " +
                               "custom method in file '" + name + "'. ERROR: " + err + " | MESSAGE:  " +
                                err.getMessage() + " | CAUSE: " + err.getCause() + " | CAUSE'S STACKTRACE:\n");
            err.getCause().printStackTrace();
        } return 0;
    }
    @Override
    public String toString() {
        return "CustomFunction '" + name + "'";
    }

    @Override
    public String toFancyString(int idtLvl) {
        String ret = indent(idtLvl) + "CustomFunction '" + name + "':\n";
        ret += indent(idtLvl + 1) + "Help = " + help + "\n";
        ret += indent(idtLvl + 1) + "Syntax = " + syntax + "";
        return ret;
    }

    @Override
    public String toFullString(int idtLvl) {
        String ret = indent(idtLvl) + "CustomFunction:\n";
        ret += indent(idtLvl + 1) + "Name:\n" + indentE(idtLvl + 2) + name + "\n";
        ret += indent(idtLvl + 1) + "Help:\n" + indentE(idtLvl + 2) + help + "\n";
        ret += indent(idtLvl + 1) + "Syntax:\n" + indentE(idtLvl + 2) + syntax + "\n";
        ret += indent(idtLvl + 1) + "Class:\n" + indentE(idtLvl + 2) + cl;
        return ret + "\n" + indentE(idtLvl + 1);
    }
    @Override
    public CustomFunction copy(){
        return new CustomFunction(name, help, syntax);
    }

}