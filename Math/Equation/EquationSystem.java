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
    public EquationSystem(ArrayList<Equation> pEqs, HashMap<String, CustomFunction> pFuncs) {
        equations = pEqs;
        functions = pFuncs;
    }

    /**
     * Adds the passed {@link Equation}s to {@link #equations}, and returns this class. Used as a constructor builder.
     * @param pEqs      A variable amount of arguemnts, all of which will be added to pEqs.
     * @return This class, with <code>pEqs</code> added to {@link #equaitons}.
     */
    public EquationSystem add(Equation... pEqs) {
        for(Equation eq : pEqs) {
            equations.add(eq);
        }
        return this;
    }
    public EquationSystem add(String... pEqStrings) {
        if(pEqStrings != null && pEqStrings.length != 0){
            equations.add(new Equation());
            for(String eq : pEqStrings) {
                equations.add(new Equation().add(eq));
            }
        }
        return this;
    }
    public EquationSystem add(ArrayList<Equation> pEqs) {
        if(pEqs != null && pEqs.size() != 0)
            equations.addAll(pEqs);
        return this;
    }
    public EquationSystem add(CustomFunction... pFuncs) {
        if(pFuncs != null && pFuncs.length != 0)
            for(CustomFunction func : pFuncs) {
                functions.put(func.name, func);
            }
        return this;
    }
    public EquationSystem add(HashMap<String, CustomFunction> pFuncs) {
        if(pFuncs != null && pFuncs.size() != 0)
            functions.putAll(pFuncs);
        return this;
    }
    public EquationSystem add(String pName, CustomFunction pFunc) {
        if(pName != null && pFunc != null)
            functions.put(pName, pFunc);
        return this;
    }

    public HashMap<String, CustomFunction> functions() {
        return functions;
    }
    public ArrayList<Equation> equations() {
        return equations;
    }
    public void setEquations(ArrayList<Equation> pEqs) {
        equations = pEqs;
    }

    public Expression mainEq() {
        if(equations.size() == 0 || equations.get(0).expressions().size() == 0)
            throw new NotDefinedException("Expressions needs to have at least one equation!");
        return equations.get(0).expressions().get(0);
    }


    public static double eval(EquationSystem pEqSys, ArrayList<Equation> pExpr, String toSolve) throws NotDefinedException{
        EquationSystem eqsys = pEqSys.copy().add(pExpr);
        eqsys.setEquations(eqsys.solveFor(toSolve));
        return 0;
    }

    public static ArrayList<Equation> solveFor(EquationSystem pEqSys, String str) {
        return pEqSys.solveFor(str);
    }

    public ArrayList<Equation> solveFor(String str) {
        throw new NotDefinedException();
    }

    public double eval(ArrayList<Equation> pExpr, String toSolve) throws NotDefinedException {
        return eval(this, pExpr, toSolve);
    }
    public double eval() throws NotDefinedException {
        return eval(null, null);
    }
    public double eval(String toSolve) throws NotDefinedException {
        return eval(null, toSolve);
    }
    public double eval(ArrayList<Equation> pExpr) throws NotDefinedException{
        return eval(pExpr, null);
    }

    public static Equation genEq(Object... pObjs) { 
        Equation ret = new Equation();
        int i = 0;
        while(i < pObjs.length) {
            i++;
            if(pObjs[i] instanceof Expression) {
                ret.add((Expression)pObjs[i]);
                continue;
            }
            Expression expr1 = pObjs[i] instanceof Expression ? (Expression)pObjs[i] : new Expression(pObjs[i]);
            Expression expr2 = pObjs[i + 1] instanceof Expression ? (Expression)pObjs[i + 1] : new Expression(pObjs[i + 1]);
            ret.add(expr1, expr2);
            i++;
        }
        return ret;
    }




    public void graph() {
        Print.printi("Currently, solve isn't very good. Oh well.");
        throw new NotDefinedException("Implement me!");
        // Grapher grapher = new Grapher(this);
        // grapher.graph();
    }
    public EquationSystem copy() {
        return new EquationSystem(equations, functions);
    }
    public int size() {
        return equations.size();
    }
    public String toStr() {
        return toString();
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
     * <code>for(Object obj : eqsys){obj = (Equation)obj</code>.<br>
     * <code>for(Equation eq : eqsys{}</code> will crash ;(
     * @return An {@link java.util.Iterator Iterator} for equations. used in for Each loop
     */
    public Iterator<Equation> iterator(){
        return (Iterator<Equation>)(new EqSysIterator());
    }
    public class EqSysIterator<E> implements Iterator<E> {
        private int i = 0;

        public boolean hasNext(){
            return i < EquationSystem.this.equations.size();
        }
        public E next(){
            if(!hasNext())
                throw new NotDefinedException();
            return (E) EquationSystem.this.equations().get(i++);
        }
    }
}
