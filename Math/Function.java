import java.lang.reflect.*;
import java.io.*;
/** 
 * A class that simulates both any kind of non-simple operation and fuctions in Math.
 * Simply, anything that isn't <code>+ - * / ^</code> should be defined using this.
 * For example, in <code>f(x)</code>, this class would represent f.
 * 
 * @author Sam Westerman
 * @version 0.1
 */
public class Function {

    /** A String that holds the path to the file that should be executed. */
    public String filePath;

    /**
     * The default constructor for the Function class. Passes null for the pFileName to the other Constructor.
     */
    public Function() {
        this(null);
    }

    /** 
     * The main cosntructor for the Function class. All it does is instantiates filePath.
     * @param pFileName         The name of the file which stores the code for how to execute the custom function.
     */
    public Function(String pFileName) {
        filePath = pFileName;
    }

    /** 
     * Takes the different parameter nodes, does whatever operations it was programmed to do, and spits a result back.
     * @param pFactors      A factor class that contains all relevant information about varriables / functions.
     *                      This is where varriable values and function definitions are stored.
     * @param pNode         The Node that is going to be solved.
     * @return A double representing the value of pNode, when solved for with pFactors.
     */
    public double exec(Factors pFactors, Node pNode) {
        double[] args = new double[pNode.size()];
        for(int i = 0; i < args.length; i++)
            args[i] = pFactors.eval(pNode.get(i));
        switch(pNode.TOKEN.VAL.toLowerCase()) {
            case "sin":
                return Math.sin(args[0]);
            case "cos":
                return Math.cos(args[0]);
            case "tan":
                return Math.tan(args[0]);

            case "sinh":
                return Math.sinh(args[0]);
            case "cosh":
                return Math.cosh(args[0]);
            case "tanh":
                return Math.tanh(args[0]);

            case "asin":
                return Math.asin(args[0]);
            case "acos":
                return Math.acos(args[0]);
            case "atan":
                return Math.atan(args[0]);

            case "abs":
                return Math.abs(args[0]);
            case "ceil":
                return Math.ceil(args[0]);
            case "floor":
                return Math.floor(args[0]);
            case "hypot":
                return Math.hypot(args[0], args[1]);
            case "ln":
                return Math.log(args[0]);
            case "log":
                return Math.log10(args[0]);
            case "random":
            case "rand":
                return Math.random();
            case "round":
                return Math.round(args[0]);
            case "sqrt":
                return Math.sqrt(args[0]);
            case "degr":
                return Math.toDegrees(args[0]);
            case "radi":
                return Math.toRadians(args[0]);

            case "fac":
                double ret = 1;
                for(int x = 1; x <= (int)args[0]; x++)
                    ret *= x;
                return ret;

            default:
                try{
                    Class[] argTypes = {Factors.class, Node.class};
                    Method execMethod = Class.forName(filePath).getDeclaredMethod("exec",argTypes);
                    Object[] argListForInvokedExec = new Object[]{pFactors, pNode};
                    System.out.println("B");
                    return (double)execMethod.invoke(null, argListForInvokedExec);
                } catch (ClassNotFoundException err) {
                    System.err.println("[ERROR] A ClassNotFoundException happened when attempting to execute a " +
                                       "custom method (File Name: " + filePath + "): " + err.getCause());
                } catch (NoSuchMethodException err) {
                    System.err.println("[ERROR] A NoSuchMethodException happened when attempting to execute a " +
                                       "custom method (File Name: " + filePath + "): " + err.getCause());
                } catch (InvocationTargetException err) {
                    System.err.println("[ERROR] A InvocationTargetException happened when attempting to execute a " +
                                       "custom method (File Name: " + filePath + "): " + err.getCause());
                } catch (IllegalAccessException err) {
                    System.err.println("[ERROR] A IllegalAccessException happened when attempting to execute a " +
                                       "custom method (File Name: " + filePath + "): " + err.getCause());
                } return 0;
            }
      }

}