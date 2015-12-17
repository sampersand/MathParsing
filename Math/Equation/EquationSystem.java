package Math.Equation;

import Math.MathObject;
import Math.Print;
import Math.Exception.TypeMisMatchException;
import Math.Exception.NotDefinedException;
import Math.Exception.InvalidArgsException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * The main class for all equation-related things. It keeps track of different equations, and of names of 
 * {@link CustomFunction}s (and the corresponding classes for them).
 * 
 * @author Sam Westerman
 * @version 0.1
 */
public class EquationSystem implements MathObject, Iterable {

    /**
     * An ArrayList of all {@link Equation}s that are related to this class. The "main" Equation is the first one in the
     * list. All others should be related to them. 
     * <br> Note: This is also where variables are defined. To define <code>x = 4 * 3</code>, you would do
     * <code>add(new Equation().add("x","4 * 3"))</code>.
     */
    protected ArrayList<Equation> equations;

    /**
     * A HashMap of all {@link CustomFunction}s. The key is the user-defined name of it, and the value is the 
     * CustomFunction associated with it.
     * <br> Note: that a user-defined name could be the name of the file that defines
     * the CustomFunction, or another name. For example, a CustomFunction with a {@link Function#name name} of "graphEq"
     * might have a key of "displayEq" instead.
     */
    protected HashMap<String, CustomFunction> functions;


    /**
     * The default constructor for this class. Just passes an empty ArrayList and HashMap to 
     * {@link #EquationSystem(ArrayList,HashMap) the main EquationSystem constructor}.
     */
    public EquationSystem() {
        this(new ArrayList<Equation>(), new HashMap<String, CustomFunction>());
    }

    /**
     * The main constructor for this class. Just instantiates {@link #equations} and {@link #functions} with
     * <code>pEqs</code> and <code>pFuncs</code>, respectively.
     * @param pEqs      An ArrayList of {@link Equation}s, used to instiate {@link #equations}.
     * @param pFuncs    An ArrayList of {@link CustomFunction}s, used to instatiate {@link #functions}.
     */
    public EquationSystem(ArrayList<Equation> pEqs,
                          HashMap<String, CustomFunction> pFuncs) {
        equations = pEqs;
        functions = pFuncs;
    }

    /**
     * Adds <code>pEqSys</code>'s {@link #equations()} to this class's {@link #equations}. Used as a constructor
     * builder.
     * @param pEqSys    The EquationSystem, whose only {@link #equations() equations} will be added (not
     *                  {@link #functions}).
     * @return This class, but with <code>pEqSys</code>'s {@link #equations() equations} added.
     */
    public EquationSystem add(EquationSystem pEqSys) {
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
        if(pFuncs != null && pFuncs.length != 0)
            for(CustomFunction func : pFuncs) {
                functions.put(func.name, func);
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
        if(pFuncs != null && pFuncs.size() != 0)
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
        if(pName != null && pFunc != null)
            functions.put(pName, pFunc);
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
        return functions;
    }

    /**
     * Returns {@link #equations}. Each {@link Equation} in {@link #equations} _should_ (but doesn't have to be) related
     * somehow to this class. Note that this is also where different variables are defined (<code>{"x","5"}</code> means
     * <code>x = 5</code>).
     * @return This class's {@link #equations}.
     */
    public ArrayList<Equation> equations() {
        return equations;
    }

    /**
     * Sets {@link #equations} to <code>pEqs</code>, and then returns this class.
     * @param pEqs      The ArrayList of equations to set {@link #equations} too.
     * @return This class after {@link #equations} is set to <code>pEqs</code>.
     */
    public EquationSystem setEquations(ArrayList<Equation> pEqs) {
        equations = pEqs;
        return this;
    }

    
    /**
     * Trys to isolate <code>toIso</code> on its own, and then returns an EquationSystem, with the first Equation in 
     * {@link #equations()} is equal to <code>toIso</code>.
     * @param toIso     The variable name to be isolated.
     * @return An EquationSystem, where the first Equation is the isolated equation.
     * @throws NotDefinedException  Thrown when there is no known way to isolate the variable
     */
    public EquationSystem isolate(String toIso) throws NotDefinedException {
        throw new NotDefinedException();
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
        return this.copy().add(pEqSys).eval(toEval);
    }

    /**
     * Evaluates the variable <code>toEval</code>.
     * @param toEval    The variable to solve for.
     * @return A double that represents the value of <code>toEval</code>.
     */
    public double eval(String toEval) throws NotDefinedException {
        throw new NotDefinedException();
    }
    /*
    public double eval() throws NotDefinedException {
        return eval(null, null);
    }
    public double eval(String toEval) throws NotDefinedException {
        return eval(null, toEval);
    }
    public double eval(EquationSystem pEqSys) throws NotDefinedException{
        return eval(pEqSys, null);
    }
    */

    /**
     * Graphs this class using {@link Math.Display.Grapher}.
     */
    public void graph() {
        Print.printi("Currently, solve isn't very good. Oh well.");
        throw new NotDefinedException("Implement me!");
        // Grapher grapher = new Grapher(this);
        // grapher.graph();
    }

    @Override
    public EquationSystem copy() {
        return new EquationSystem(equations, functions);
    }

    /**
     * Gets the amount of {@link Equations} (not {@link Expressions}) that are contained within {@link #equations}.
     * @return size of this class's {@link #equations}.
     */
    public int size() {
        return equations.size();
    }

    @Override
    public String toString() {
        String ret = "EquationSystem: Equations: (";
        if(equations == null) {
            ret += "null";
        } else {
            for(Equation eq : equations) {
                ret += "'" + eq + "', ";
            }
        }
        ret = ret.substring(0, ret.length() - (equations.size() == 0 ? 0 : 2));
        ret += "), Functions: (";
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
    public String toFancyString() {
        String ret = "+-----=[EquationSystem]=------\n|";
        if(equations == null) {
            ret += "\n| Null Equations";
        } else if(equations.size() == 0) {
            ret += "\n| Empty Equations";
        } else {
            ret += "\n| Equations: ";
            for(Equation eqs : equations) {
                ret += "\n|\t" + eqs.toFancyString();
            }
        }
        ret += "\n| ";

        if(functions == null) {
            ret += "\n| Null Functions";
        } else if(functions.size() == 0) {
            ret += "\n| Empty Functions";
        } if(functions.size() > 0) {
            ret += "\n| Functions: (stuff in [] are optional)";
            String[] keys = (String[]) functions.keySet().toArray();
            for(String key : keys) {
                ret += "\n|\t" + key + ": " + functions.get(key).toFancyString().replaceAll("\n","\n|\t\t\t") + ")";
            }
        }
        ret += "\n|\n";
        ret += "+-----------------------------\n";
        return ret;
    }

    @Override
    public String toFullString() {
        throw new NotDefinedException();
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
            if(!hasNext())
                throw new NotDefinedException();
            return (E) EquationSystem.this.equations().get(i++);
        }
    }
}
