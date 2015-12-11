package Math.Equation;

import Math.MathObject;
import Math.Print;
import Math.Exception.TypeMisMatchException;
import Math.Exception.NotDefinedException;
import Math.Exception.InvalidArgsException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class EquationSystem implements MathObject, Iterable {
    private ArrayList<Equation> equations;
    private HashMap<String, CustomFunction> functions;

    public EquationSystem() {
        this(new ArrayList<Equation>(), new HashMap<String, CustomFunction>());
    }
    public EquationSystem(ArrayList<Equation> pEqs, HashMap<String, CustomFunction> pFuncs) {
        equations = pEqs;
        functions = pFuncs;
    }

    public EquationSystem add(Equation... pEqs) {
        if(pEqs != null && pEqs.length != 0)
            for(Equation eq : pEqs) {
                equations.add(eq);
            }
        return this;
    }
    public EquationSystem add(String... pEqStrings) {
        if(pEqStrings != null && pEqStrings.length != 0)
            for(String eq : pEqStrings) {
                equations.add(new Equation(eq));
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
                functions.put(func.name, func);;
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
