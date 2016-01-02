package West.Math.Equation.Function;

import West.Math.MathObject;
import West.Math.Equation.EquationSystem;
import West.Math.Set.Node.TokenNode;
import West.Math.Set.Collection;
import static West.Math.Declare.*;
import West.Math.Exception.NotDefinedException;
import java.util.HashMap;
/**
 * A class that simulates both any kind of operation and any fuction in West.Math.
 * Simply put, anything that isn't a number or variable should be a function somehow.
 * For example, in <code>f(x)</code>, this class would represent f.
 * 
 * @author Sam Westerman
  * @version 0.89
 * @since 0.1
 */
public abstract class Function implements MathObject {
    public interface FuncObj{
        public Double exec(Double[] args);
    }
    /**
     * A String that holds either the function name ({@link InBuiltFunction}) or the file name ({@link CustomFunction}).
     */
    protected String name;

    /**
     * The Help text for this function
     */
    protected String help;

    /**
     * The Syntax text for this function.
     */
    protected String syntax;

    protected int priority;

    protected Collection<Integer> argsLength;

    protected FuncObj funcObj;

    /**
     * The default constructor for the Function class. Instatiates {@link #name}, {@link #help}, and {@link #syntax} as
     * empty strings.
     */
    public Function() {
        this(null, null, null, -1, null, null);
    }
    
    /**
     * The main cosntructor for the Function class. All it does is instantiates name, help, and syntax.
     * @param pName         The name of the function - serves as an identifier for {@link InBuiltFunction}s, and as the
     *                      filename for {@link CustomFunction}s. Cannot be null.
     * @param pHelp         The help string for the function. Cannot be null.
     * @param pSyntax       The syntax string for the function. Cannot be null.
     * @throws IllegalArgumentException When either name, help, and / or syntax is null.
     */
    public Function(String pName,
                    String pHelp,
                    String pSyntax,
                    int pPriority,
                    Collection<Integer> pArgsLength,
                    FuncObj pFuncObj) throws IllegalArgumentException{
        assert pName != null;
        assert pHelp != null;
        assert pSyntax != null;
        assert pArgsLength.size() >= 0;
        funcObj = pFuncObj;
        name = pName;
        help = pHelp;
        syntax = pSyntax;
        priority = pPriority;
        argsLength = pArgsLength;
        if(funcObj == null)
            funcObj = a -> null;
    }

    /**
     * Returns this class's {@link #name}.
     * @return this class's {@link #name}.
     */
    public final String name() {
        return name;
    }

    /**
     * Returns the "help" value for this function.
     * @return A String representing the "help" value.
     */
    public String help() {
        return help;
    }

    /**
     * Returns the "help" value for this function.
     * @return A String representing the "syntax" value.
     */
    public String syntax() {
        return syntax;
    }

    public int priority() {
        return priority;
    }
    public Collection<Integer> argsLength() {
        return argsLength;
    }

    public FuncObj funcObj() {
        return funcObj;
    }

    /**
     * This thing takes a {@link Node} (usually the node from {@link #exec(EquationSystem,Node) exec}), and returns an
     * array of the numerical values of each {@link Node#elements() subNode}.
     * @param pEqSys        The {@link EquationSystem} that will be used when evaluating <code>pNode</code>.
     * @param pNode         The node to be evaluated.
     * @return An array of doubles, with each position corresponding to the value of each Node of that position in 
     *         {@link Node#elements() pNode's elements()}.
     */
    public Object[] evalNode(final EquationSystem pEqSys, TokenNode pNode) {
        Double[] retd = new Double[pNode.size()];
        HashMap<String, Double> rethm = new HashMap<String, Double>();
        for(int i = 0; i < pNode.size(); i++) {
            rethm.putAll(((TokenNode)pNode.elements().get(i)).eval(pEqSys));
            retd[i] = rethm.get(pNode.elements().get(i).toString());
        }
        return new Object[]{retd, rethm};
    }


    /**
     * Takes the parameter {@link Node} (and {@link EquationSystem}, performs whatever this function is defined to do,
     * and returns the result.
     * @param pEqSys        An {@link EquationSystem} that contains all relevant information about
     *                      {@link Equation Equations} and {@link Function Functions} is stored.
     * @param pNode         The {@link Node} that is going to be solved.
     * @return A double representing the value of <code>pNode</code>, when solved for with <code>pEqSys</code>.
     * @throws NotDefinedException    Thrown when the function is defined, but how to execute it isn't.
     * @throws IllegalArgumentException   Thrown when the function required parameters, and the ones passed aren't right.
     */
    public HashMap<String,Double> exec(final EquationSystem pEqSys, TokenNode pNode) {
        Object[] rargs = evalNode(pEqSys, pNode);
        Double[] args = (Double[])rargs[0];
        assert argsLength.contains(args.length) || argsLength.contains(-1) :
        "'" +name+ "' got incorrect args. Allowed args: " + argsLength + ", inputted args = '"+pNode + "'("+args.length+")";
        HashMap<String, Double> rethm = (HashMap<String, Double>)rargs[1];
        // assert false :"\n" + rethm + "\n"+ pNode.toString() + "\n" + pNode.toFancyString() + " \nargs:" + args[0] + 
        // "," + args[1] + "\nthis:\n"+toFancyString;
        return addArgs(rethm, pNode.toString(), funcObj.exec(args));
    }

    public HashMap<String, Double> addArgs(HashMap<String, Double> hm, String key, Double val){
        hm.put(key, val);
        return hm;
    }

    @Override
    public boolean equals(Object pObj){
        if(pObj == null || !(pObj instanceof  Function))
            return false;
        if(this == (Function)pObj)
            return true;
        return name.equals(((Function)pObj).name()) &&
               help.equals(((Function)pObj).help()) &&
               syntax.equals(((Function)pObj).syntax());
    }

    /**
     * Gets the inverse of this function - that is, what function should be done to undo this one. <br>The inverse of 
     * <code>+</code> is <code>-</code>, and the inverse of <code>cos</code> is <code>arccos</code>.
     * @return The inverse of this function.
     * @throws NotDefinedException  Thrown when the inverse hasn't been defined yet, or there is no known
     * @deprecated Not defined yet, will be in the future.
     */
    public Function inverse() throws NotDefinedException{
        throw new NotDefinedException();
    }


}