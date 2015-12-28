package West.Math.Equation;

import West.Math.MathObject;
import West.Math.Print;
import static West.Math.Declare.*;
import West.Math.Display.Grapher;
import West.Math.Exception.TypeMisMatchException;
import West.Math.Exception.NotDefinedException;
import West.Math.Equation.Function.CustomFunction;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import West.Math.Set.CompareCollection;
/**
 * The main class for all equation-related things. It keeps track of different equations, and of names of 
 * {@link CustomFunction}s (and the corresponding classes for them).
 * 
 * @author Sam Westerman
 * @version 0.75
 * @since 0.1
 */
public class EquationSystem implements MathObject, Iterable {

    /**
     * An ArrayList of all {@link Equation}s that are related to this class. The "main" Equation is the first one in the
     * list. All others should be related to them. 
     * <br> Note: This is also where variables are defined. To define <code>x = 4 * 3</code>, you would do
     * <code>add(new Equation().add("x","4 * 3"))</code>.
     * no items in here should ever be null.
     */
    protected ArrayList<Equation> equations;

    /**
     * A HashMap of all {@link CustomFunction}s. The key is the user-defined name of it, and the value is the 
     * CustomFunction associated with it.
     * <br> Note: that a user-defined name could be the name of the file that defines
     * the CustomFunction, or another name. For example, a CustomFunction with a {@link Function#name name} of "graphEq"
     * might have a key of "displayEq" instead.
     * no items in here should ever be null.
     */
    protected HashMap<String, CustomFunction> functions;

    /** TODO: JAVADOC */
    protected EquationSystem constraints;

    /**
     * The default constructor for this class. Just passes an empty ArrayList and HashMap to 
     * {@link #EquationSystem(ArrayList,HashMap) the main EquationSystem constructor}.
     */
    public EquationSystem() {
        this(new ArrayList<Equation>(), new HashMap<String, CustomFunction>(), null);
    }

    /**
     * The main constructor for this class. Just instantiates {@link #equations} and {@link #functions} with
     * <code>pEqs</code> and <code>pFuncs</code>, respectively.
     * @param pEqs      An ArrayList of {@link Equation}s, used to instiate {@link #equations}.
     * @param pFuncs    An ArrayList of {@link CustomFunction}s, used to instatiate {@link #functions}.
     * @param pConstraints   TODO: JAVADOC
     */
    public EquationSystem(ArrayList<Equation> pEqs,
                          HashMap<String, CustomFunction> pFuncs,
                          EquationSystem pConstraints) {
        equations = new ArrayList<Equation>();
        if(pEqs != null && pEqs.size() != 0)
            equations.addAll(pEqs);
        functions = new HashMap<String, CustomFunction>();
        if(pFuncs != null && pFuncs.size() != 0)
            functions.putAll(pFuncs);
        if(pConstraints != null)
            constraints = pConstraints;
    }

    /**
     * Adds <code>pEqSys</code>'s {@link #equations()} to this class's {@link #equations}. Used as a constructor
     * builder.
     * @param pEqSys    The EquationSystem, whose only {@link #equations() equations} will be added (not
     *                  {@link #functions}).
     * @return This class, but with <code>pEqSys</code>'s {@link #equations() equations} added.
     */
    public EquationSystem add(EquationSystem pEqSys) {
        if(pEqSys == null){
            Print.printi("pEqSys is null; not adding it to 'equations'.");
            return this;
        }
        equations.addAll(pEqSys.equations());
        return this;
    }

    /**
     * Adds the passed {@link Equation}s to {@link #equations}, and then returns this class. Used as a constructor
     * builder.
     * @param pEqs    A variable amount of {@link Equation} arguemnts, all of which will be added to {@link #equations}.
     * @return This class, but with <code>pEqs</code> added to {@link #equations}.
     */
    public EquationSystem add(Equation... pEqs) {
        for(Equation eq : pEqs) {
            if(eq == null){
                Print.printi("A passed Equation is null; not adding it to 'equations'.");
                continue;
            }
            equations.add(eq);
        }
        return this;
    }

    /**
     * Adds {@link Equation}s (each instatiated by their respective passed String) to to {@link #equations}, and returns
     * this class. Used as a constructor builder.
     * @param pEqStrings    A variable amount of String arguments, each of which will be used to instatiate a
     *                      {@link Equation}. Note that each String should represent a single {@link Equation} (and 
     *                      so, should have at least 1 of "=", "&gt;" or "&lt;").
     * @return This class, but with {@link Equation}s based off <code>pEqStrings</code> added to {@link #equations}.
     */
    public EquationSystem add(String... pEqStrings) {
        for(String eq : pEqStrings) {
            if(eq == null){
                Print.printi("A passed String is null; not adding it to 'equations'.");
                continue;
            }
            equations.add(new Equation().add(eq));
        }
        return this;
    }

    /**
     * Adds <code>pEqs</code> to {@link #equations}, and then returns this class. Used as a constructor
     * builder.
     * @param pEqs      An ArrayList of {@link Equation}s, all of which will be added to {@link #equations}.
     * @return This class, but with <code>pEqs</code> added to {@link #equations}.
     */
    public EquationSystem add(ArrayList<Equation> pEqs) {
        if(pEqs == null){
            Print.printi("pEqs is null; not adding it to 'equations'.");
            return this;
        }
        equations.addAll(pEqs);
        return this;
    }

    /**
     * Adds the passed {@link CustomFunction}s to {@link #functions}, and then returns this class. Used as a constructor
     * builder. <br> Note: Each function's key in {@link #functions} will be {@link CustomFunction#name() it's name}.
     * @param pFuncs    A variable amount of {@link CustomFunction} arguemnts, all of which will be added to
     *                  {@link #functions}.
     * @return This class, but with <code>pFuncs</code> added to {@link #functions}.
     */
    public EquationSystem add(CustomFunction... pFuncs) {
        if(pFuncs == null){
            Print.printi("pFuncs is null; not adding it to 'functions'.");
            return this;
        } else if(pFuncs.length == 0){
            Print.printi("pFuncs was empty; not adding it to 'functions'.");
            return this;
        }
        for(CustomFunction func : pFuncs) {
            if(func == null){
                Print.printi("A passed CustomFunction is null; not adding it to 'functions'.");
                continue;
            }
            assert func.name() != null; //this shoulda been taken care of in the constructor.
            functions.put(func.name(), func);
        }
        return this;
    }

    /**
     * Adds <code>pFuncs</code> to {@link #functions}, and then returns this class. Used as a constructor
     * builder.
     * @param pFuncs      An ArrayList of {@link CustomFunction}s, all of which will be added to {@link #functions}.
     * @return This class, but with <code>pFuncs</code> added to {@link #equations}.
     */
    public EquationSystem add(HashMap<String, CustomFunction> pFuncs) {
        if(pFuncs == null){
            Print.printi("pFuncs is null; not adding it to 'functions'.");
            return this;
        }
        functions.putAll(pFuncs);
        return this;
    }

    /**
     * Adds <code>pFunc</code> with the key <code>pName</code> to {@link #functions}, and then returns this class. Used
     * as a constructor builder.
     * @param pName     The key that will be used when putting <code>pFunc</code> into {@link #functions}.
     * @param pFunc     The {@link CustomFunction} that will be added to {@link #functions}.
     * @return This class, but with <code>pFunc</code> added to {@link #functions}.
     */
    public EquationSystem add(String pName,
                              CustomFunction pFunc) {
        if(pName == null){
            Print.printi("pName is null; not adding pFunc to 'functions'.");
            return this;
        } else if(pFunc == null){
            Print.printi("pFunc is null; not adding it to 'functions'.");
            return this;
        }
        functions.put(pName, pFunc);
        return this;
    }

    /**
     * TODO: JAVADOC
     */ 

    public EquationSystem setConstraints(EquationSystem pConstraints){
        constraints = pConstraints;
        return this;
    }

    /**
     * TODO: JAVADOC
     */
    public EquationSystem constraints(){
        return constraints;
    }
    public EquationSystem addConstraint(String equation){
        if(constraints == null)
            constraints = new EquationSystem();
        constraints.add(equation);
        return this;

    }

    /**
     * Returns {@link #functions}. {@link #functions} is a HashMap with the String as the key, and 
     * {@link CustomFunction}. <br>Note: The key is what should be used when defining equations.
     * <br>Example: {@link #functions} might be defined as (pseudocode) <code>functions = {"fac",
     * CustomFunction("factorial")}</code>. The class "factorial" is linked to  "fac", so
     * <br><code>y = fac(x)</code> would work, but <code>y = factorial(x)</code> wouldn't.
     * @return This class's {@link #functions}.
     */
    public HashMap<String, CustomFunction> functions() {
        assert functions != null; // it should always be instatiated by the constructor.
        return functions;
    }

    /**
     * Returns {@link #equations}. Each {@link Equation} in {@link #equations} _should_ (but doesn't have to be) related
     * somehow to this class. Note that this is also where different variables are defined (<code>{"x","5"}</code> means
     * <code>x = 5</code>).
     * @return This class's {@link #equations}.
     */
    public ArrayList<Equation> equations() {
        assert functions != null; // it should always be instatiated by the constructor.
        return equations;
    }

    /**
     * Sets {@link #equations} to <code>pEqs</code>, and then returns this class.
     * @param pEqs      The ArrayList of {@link Equation}s to set {@link #equations} to.
     * @return This class after {@link #equations} is set to <code>pEqs</code>.
     */
    public EquationSystem setEquations(ArrayList<Equation> pEqs) {
        if(pEqs == null){
            Print.printi("pEqs is null; not setting 'equations' to it.");
            return this;
        }
        equations = pEqs;
        return this;
    }


    /**
     * Sets {@link #functions} to <code>pFuncs</code>, and then returns this class.
     * @param pFuncs      The ArrayList of {@link Function}s to set {@link #functions} to.
     * @return This class after {@link #functions} is set to <code>pFuncs</code>.
     */
    public EquationSystem setFunctions(HashMap<String, CustomFunction> pFuncs) {
        if(pFuncs == null){
            Print.printi("pFuncs is null; not setting 'equations' to it.");
            return this;
        }
        functions = pFuncs;
        return this;
    }

    /**
     * Evaluates the variable <code>toEval</code>, using {@link EquationSystem#equations() the equations of pEqSys}
     * before checking {@link #equations this class's equations}.
     * @param toEval    The variable to solve for.
     * @param pEqSys    The EquationSystem that will be evaluated.
     * @return A double that represents the value of <code>toEval</code>.
     */
    public double eval(String toEval,
                       EquationSystem pEqSys) throws NotDefinedException {
        return copy().add(pEqSys).eval(toEval);
    }

    /**
     * Evaluates the variable <code>toEval</code>.
     * @param toEval    The variable to solve for.
     * @return A double that represents the value of <code>toEval</code>.
     */
    public double eval(String toEval) throws NotDefinedException {
        EquationSystem isod = copy().isolate(toEval);
        if(isod.equations().size() == 0){
            Print.printw("Trying to evaluated an EquationSystem with no equations! returning 0");
            return 0;
        }
        return isod.equations().get(0).expressions().get(0).node().eval(isod);

    }

    
    /**
     * Trys to isolate <code>toIso</code> on its own, and then returns an EquationSystem, with the first Equation in 
     * {@link #equations()} is equal to <code>toIso</code>.
     * @param toIso     The variable name to be isolated.
     * @return An EquationSystem, where the first Equation is the isolated equation.
     * @throws NotDefinedException  Thrown when there is no known way to isolate the variable
     */
    public EquationSystem isolate(String toIso) throws NotDefinedException {
        // if(this.equations().get(0). //if the first equation's
        //     expressions().get(0). // first expression's
        //     node().get(0). // node's first subnode's
        //     token().val(). // token's value
        //     equals(toIso)) // is the string to isolate, return this.
        //     return this;
        return this;
    }

    /**
     * Checks to see if this is an isolate EquationSystem.
     * @return true if this is an {@link #isolate() isolated EquationSystem}.
     */
    public boolean isolated(){
        for(Equation eq : equations)
            if(eq.expressions().size() > 0 && eq.expressions().get(0).node().size() != 1)
                return false;
        return true;
    }

    /**
     * Sees if <code>pVar</code> is defined on the left-hand side of any equation.
     */
    public boolean varExist(String pVar){
        for(Equation eq : equations){
            if(eq == null) continue;
            for(Expression expr : eq.expressions()){
                if(expr == null) continue;
                    if(expr.node().get(0).token().val().equals(pVar))
                        return true;
            }
        }
        return false;
    }

    /**
     * Graphs the first equation in {@link #equations} using {@link West.Math.Display.Grapher}.
     */
    public void graph() {
        Print.printi("Currently, solve isn't very good. Oh well.");
        new Grapher(this).graph();
    }

    /**
     * Gets the amount of {@link Equations} (not {@link Expressions}) that are contained within {@link #equations}.
     * @return size of this class's {@link #equations}.
     */
    public int size() {
        return equations.size();
    }
    public boolean isInBounds(Token t, double pVal){
        if(t == null || pVal == Double.NaN || !varExist(t.val()))
            return false;
        if(constraints == null)
            return true;
        return true;
    }
    @Override
    public String toString() {
        String ret = "EquationSystem: Equations = (";
        if(equations == null) {
            ret += "null";
        } else {
            for(Equation eq : equations) {
                ret += "'" + eq + "', ";
            }
        }
        ret = ret.substring(0, ret.length() - (equations.size() == 0 ? 0 : 2));
        ret += "), Functions = (";
        if(functions == null) {
            ret += "null";
        } else {
            for(String name : functions.keySet()) {
                ret += "'" + name + ":" + functions.get(name) + "', ";
            }
        }
        return ret.substring(0, ret.length() - (functions.size() == 0 ? 0 : 2)) + ")";
    }

    @Override
    public String toFancyString(int idtLvl) {
        String ret = indent(idtLvl) + "EquationSystem:";
        ret += "\n" + indent(idtLvl + 1) + "Equations:";

        for(Equation eqs : equations) {
            ret += "\n" + eqs.toFancyString(idtLvl + 2);
        }

        ret += "\n" + indent(idtLvl + 1) + "Functions:";
        for(Object key : functions.keySet().toArray()) {
            ret += "\n" + indent(idtLvl + 2) + "'" + key + "' = " + functions.get("" + key).name();
        }

        ret += "\n" + indent(idtLvl + 1) + "Constraints:";
        if(constraints != null)
            ret += "\n" + constraints.toFancyString(idtLvl + 2);

        return ret;
    }

    @Override
    public String toFullString(int idtLvl) {
        String ret = indent(idtLvl) + "EquationSystem:";
        ret += "\n" + indent(idtLvl + 1) + "Equations:";

        for(Equation eqs : equations) {
            ret += "\n" + eqs.toFullString(idtLvl + 2);
        }
        ret += "\n" + indentE(idtLvl + 2);
        ret += "\n" + indent(idtLvl + 1) + "Functions:";
        for(Object key : functions.keySet().toArray()) {
            ret += "\n" + indent(idtLvl + 2) + "'" + key + "':\n";
            ret += functions.get("" + key).toFullString(idtLvl + 3);
            ret += "\n" + indentE(idtLvl + 3);
        }
        ret += "\n" + indent(idtLvl + 1) + "Constraints:";
        if(constraints != null)
            ret += "\n" + constraints.toFullString(idtLvl + 2);
        ret += "\n" + indentE(idtLvl + 2);
        return ret + "\n" + indentE(idtLvl + 1);
    }

    /**
     * Ok, currently, this can only be used as the following:
     * <code>for(Object obj : eqsys) {obj = (Equation)obj</code>.<br>
     * <code>for(Equation eq : eqsys{}</code> will crash ;(
     * @return An {@link java.util.Iterator Iterator} for equations. used in for Each loop
     */
    public Iterator<Equation> iterator() {
        return (Iterator<Equation>)(new EqSysIterator());
    }
    public class EqSysIterator<E> implements Iterator<E> {
        private int i = 0;

        public boolean hasNext() {
            return i < EquationSystem.this.equations.size();
        }
        public E next() {
            assert hasNext() : "there should be a next one.";
            return (E) EquationSystem.this.equations().get(i++);
        }
    }

    @Override
    public EquationSystem copy() {
        return new EquationSystem(equations, functions, constraints);
    }

    /**
     * note that this only takes into account equations, and not functions.
     * TODO: JAVADOC
     */
    @Override
    public boolean equals(Object pObj){
        if(pObj == null || !(pObj instanceof EquationSystem))
            return false;
        if(this == pObj)
            return true;
        EquationSystem peqsys = (EquationSystem)pObj;
        if(peqsys.size() != size())
            return false;
        for(int i = 0; i < size(); i++)
            if(!equations.get(i).equals(peqsys.equations().get(i)))
                return false;
        return true;
    }
}
