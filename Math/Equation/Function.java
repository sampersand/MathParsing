package Math.Equation;

import Math.MathObject;
import Math.Exception.NotDefinedException;
import Math.Exception.InvalidArgsException;

/**
 * A class that simulates both any kind of non-simple operation and fuctions in Math.
 * Simply, anything that isn't <code>+ - * / ^</code> should be defined using this.
 * For example, in <code>f(x)</code>, this class would represent f.
 * 
 * @author Sam Westerman
 * @version 0.1
 */
public abstract class Function implements MathObject{

    /**
     * A String that holds either the function name ({@link InBuiltFunction}) or the file name ({@link CustomFunction}).
     * OMFG I LOVE THE PUN HERE L0L. "fName" as in "function name". But also "fName" as in "fileName"
     */
    public String fName;

    private String help;
    private String syntax;

    /**
     * The default constructor for the Function class. Passes null for the pName to the other Constructor.
     */
    public Function() {
        this(null, null, null);
    }

    /**
     * The main cosntructor for the Function class. All it does is instantiates fName.
     * @param pName         The name of the file which stores the code for how to execute the custom function.
     */
    public Function(String pName, String pHelp, String pSyntax) {
        fName = pName;
        help = pHelp;
        syntax = pSyntax;
    }

    public String help() {
        return help;
    }
    public String syntax() {
        return syntax;

        /*
        throw new NotDefinedException("Implement me for your custom method!");

        throw new NotDefinedException("Implement me for your custom method!");
        */
    }

    /**
     * This thing takes a node (usually the node from {@link #exec(Factors,Node) exec}), and returns an array of the 
     * numerical values of each subnode.
     * @param pFactors          The factors that will be used when evaluating pNode.
     * @param pNode             The node to be evaluated.
     * @return An array of doubles, with each position corresponding to the value of each Node of that position in 
     *         {@link Node#subNodes() pNode's subNodes()}.
     */
    protected double[] evalNode(EquationSystem pEq, Node pNode){
        double[] ret = new double[pNode.size()];
        for(int i = 0; i < ret.length; i++) ret[i] = pNode.subNodes().get(i).eval(pEq);
        return ret;

    }
    /**
     * Takes the different parameter nodes, does whatever operations it was programmed to do, and spits a result back.
     * @param pFactors      A factor class that contains all relevant information about variables / functions.
     *                      This is where variable values and function definitions are stored.
     * @param pNode         The Node that is going to be solved.
     * @return A double representing the value of pNode, when solved for with pFactors.
     * @throws NotDefinedException    Thrown when the function is defined, but how to execute it isn't.
     * @throws InvalidArgsException    Thrown when the function required parameters, and the ones passed aren't right.
     */
    public abstract double exec(EquationSystem pEq, Node pNode) throws NotDefinedException, InvalidArgsException;
}