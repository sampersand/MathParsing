package Math.Equation;

import Math.Equation.Exception.NotDefinedException;
import Math.Equation.Exception.InvalidArgsException;

/** 
 * A class that simulates both any kind of non-simple operation and fuctions in Math.
 * Simply, anything that isn't <code>+ - * / ^</code> should be defined using this.
 * For example, in <code>f(x)</code>, this class would represent f.
 * 
 * @author Sam Westerman
 * @version 0.1
 */
public abstract class Function {

    /**
     * A String that holds either the function name ({@link InBuiltFunction}) or the file name ({@link CustomFunction}).
     * OMFG I LOVE THE PUN HERE L0L. "fName" as in "function name". But also "fName" as in "fileName"
     */
    public String fName;

    /**
     * The default constructor for the Function class. Passes null for the pName to the other Constructor.
     */
    public Function() {
        this(null);
    }

    /** 
     * The main cosntructor for the Function class. All it does is instantiates fName.
     * @param pName         The name of the file which stores the code for how to execute the custom function.
     */
    public Function(String pName) {
        fName = pName;
    }

    /** 
     * Gets a string representation of the function. In reality, just returns its name, because a string repr of the 
     * code is wayyy too hard.
     */
    public abstract String toString();
    /** 
     * Takes the different parameter nodes, does whatever operations it was programmed to do, and spits a result back.
     * @param pFactors      A factor class that contains all relevant information about varriables / functions.
     *                      This is where varriable values and function definitions are stored.
     * @param pNode         The Node that is going to be solved.
     * @return A double representing the value of pNode, when solved for with pFactors.
     * @throws NotDefinedException    Thrown when the function is defined, but how to execute it isn't.
     * @throws InvalidArgsException    Thrown when the function required parameters, and the ones passed aren't right.
     */
    public abstract double exec(Factors pFactors, Node pNode) throws NotDefinedException, InvalidArgsException;
}