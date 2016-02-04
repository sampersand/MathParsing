package West.Math.Equation;

import West.Math.MathObject;
import West.Print;
import static West.Math.Declare.*;
import West.Math.Display.Grapher;
import West.Math.Display.GraphComponents;
import West.Math.Equation.Function.CustomFunction;
import java.util.HashMap;
import java.util.Iterator;
import West.Math.Set.Collection;
import West.Math.Set.Node.TokenNode;
import West.Math.Operable;
import West.Math.Complex;
import java.util.List;

/**
 * The main class for all equation-related things. It keeps track of different equations, and of names of 
 * {@link CustomFunction}s (and the corresponding classes for them).
 * 
 * @author Sam Westerman
 * @version 1.1
 * @since 0.1
 */
public class EquationSystem implements MathObject{

    /**
     * An Collection of all {@link Equation}s that are related to this class. The "main" Equation is the first one in the
     * list. All others should be related to them. 
     * <br> Note: This is also where variables are defined. To define <code>x = 4 * 3</code>, you would do
     * <code>add(new Equation().add("x","4 * 3"))</code>.
     * no items in here should ever be null.
     */
    protected Collection<Equation> equations;

    /**
     * A HashMap of all {@link CustomFunction}s. The key is the user-defined name of it, and the value is the 
     * CustomFunction associated with it.
     * <br> Note: that a user-defined name could be the name of the file that defines
     * the CustomFunction, or another name. For example, a CustomFunction with a {@link Function#name name} of "graphEq"
     * might have a key of "displayEq" instead.
     * no items in here should ever be null.
     */
    protected HashMap<String, CustomFunction> functions;

    /**
     * The default constructor for this class. Just passes an empty Collection and HashMap to 
     * {@link #EquationSystem(Collection,HashMap) the main EquationSystem constructor}.
     */
    public EquationSystem() {
        this(new Collection<Equation>(), new HashMap<String, CustomFunction>());
    }

    /**
     * The main constructor for this class. Just instantiates {@link #equations} and {@link #functions} with
     * <code>pEqs</code> and <code>pFuncs</code>, respectively.
     * @param pEqs      An Collection of {@link Equation}s, used to instiate {@link #equations}.
     * @param pFuncs    An Collection of {@link CustomFunction}s, used to instatiate {@link #functions}.
     */
    public EquationSystem(List<Equation> pEqs,
                          HashMap<String, CustomFunction> pFuncs) {
        equations = new Collection<Equation>().addAllE(pEqs);
        functions = new HashMap<String, CustomFunction>();
        functions.putAll(pFuncs);
    }

    public static <K,V> EquationSystem fromHashMap(HashMap<K, V> hm){
        EquationSystem ret = new EquationSystem();
        for(K k : hm.keySet())
            ret.add(k + " = " + hm.get(k));
        return ret;
    }
    /**
     * Adds <code>pEqSys</code>'s {@link #equations()} to this class's {@link #equations}. Used as a constructor
     * builder.
     * @param pEqSys    The EquationSystem, whose only {@link #equations() equations} will be added (not
     *                  {@link #functions}).
     * @return This class, but with <code>pEqSys</code>'s {@link #equations() equations} added.
     */
    public EquationSystem add(EquationSystem pEqSys) {
        assert pEqSys != null;
        pEqSys.equations().forEach(this::add);
        return this;
    }

    /**
     * Adds the passed {@link Equation}s to {@link #equations}, and then returns this class. Used as a constructor
     * builder.
     * @param pEqs    A variable amount of {@link Equation} arguemnts, all of which will be added to {@link #equations}.
     * @return This class, but with <code>pEqs</code> added to {@link #equations}.
     */
    public EquationSystem add(Equation... pEqs) {
        equations.addAll(pEqs); // doesnt check for null
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
            assert eq != null;
            equations.add(new Equation().add(eq));
            assert new Equation().add(eq) != null;
        }
        return this;
    }

    /**
     * Adds <code>pEqs</code> to {@link #equations}, and then returns this class. Used as a constructor
     * builder.
     * @param pEqs      An Collection of {@link Equation}s, all of which will be added to {@link #equations}.
     * @return This class, but with <code>pEqs</code> added to {@link #equations}.
     */
    public EquationSystem add(List<Equation> pEqs) {
        assert pEqs != null;
        pEqs.forEach(this::add);
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
            assert func.names() != null; // this shoulda been taken care of in the constructor.
            //  functions.add(func);
            assert false;
        }
        return this;
    }

    /**
     * Adds <code>pFuncs</code> to {@link #functions}, and then returns this class. Used as a constructor
     * builder.
     * @param pFuncs      An Collection of {@link CustomFunction}s, all of which will be added to {@link #functions}.
     * @return This class, but with <code>pFuncs</code> added to {@link #equations}.
     */
    public EquationSystem add(HashMap<String, CustomFunction> pFuncs) {
        assert pFuncs != null;
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
        assert pName != null;
        assert pFunc != null;
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
        assert functions != null; //  it should always be instatiated by the constructor.
        return functions;
    }

    /**
     * Returns {@link #equations}. Each {@link Equation} in {@link #equations} _should_ (but doesn't have to be) related
     * somehow to this class. Note that this is also where different variables are defined (<code>{"x","5"}</code> means
     * <code>x = 5</code>).
     * @return This class's {@link #equations}.
     */
    public Collection<Equation> equations() {
        assert equations != null; //  it should always be instatiated by the constructor.
        return equations;
    }

    /**
     * Evaluates the variable <code>toEval</code>, using {@link EquationSystem#equations() the equations of pEqSys}
     * before checking {@link #equations this class's equations}.
     * @param toEval    The variable to solve for.
     * @param pEqSys    The EquationSystem that will be evaluated.
     * @return A double that represents the value of <code>toEval</code>.
     */
    public Operable eval(String toEval,
                       EquationSystem pEqSys) {
        return clone().add(pEqSys).eval(toEval);
    }

    public Operable eval(String toEval, EquationSystem pEqSys, HashMap<String, Operable> hm){
        return clone().add(pEqSys).eval(toEval, hm);
    }
    
    /**
     * Evaluates the variable <code>toEval</code>.
     * @param toEval    The variable to solve for.
     * @return A double that represents the value of <code>toEval</code>.
     */
    public Operable eval(String toEval) {
        return eval(toEval, new HashMap<String, Operable>());
    }
    public Operable eval(String toEval, HashMap<String, Operable> hm) {
        assert equations.size() != 0 : "Cannot evaluate an EquationSystem with no equations!";
        EquationSystem eqsys = clone().isolate(toEval);
        Operable ret = eqsys.equations().get(0).subEquations().
                eval(hm, eqsys).
                get(toEval);
        return ret;

    }

    public Double evald(String toEval, EquationSystem pEqSys){
        return eval(toEval, pEqSys).toDouble();
    }
    
    public Double evald(String toEval){
        return eval(toEval).toDouble();
    }
    
    /**
     * Trys to isolate <code>toIso</code> on its own, and then returns an EquationSystem, with the first Equation in 
     * {@link #equations()} is equal to <code>toIso</code>.
     * @param toIso     The variable name to be isolated.
     * @return An EquationSystem, where the first Equation is the isolated equation.
     * @throws UnsupportedOperationException Thrown when there is no known way to isolate the variable
     */
    public EquationSystem isolate(String toIso) throws UnsupportedOperationException {
        for(int i = 0; i < equations.size(); i++){
            if(equations.get(i).getVar().equals(toIso)){
                equations.prepend(equations.pop(i));
                break;
            }
        }
        return this;
    }

    public Equation getEq(String var){ // gets an equation that starts with 'var'
        for(Equation eq : equations)
            if(eq.getVar().equals(var))
                return eq;
        System.err.println("[Warning] var '" + var + "' isn't in the EquationSystem '" + this + "'");
        return new Equation();
    }
    
    /**
     * Graphs the first equation in {@link #equations} using {@link West.Math.Display.Grapher}.
     */
    public void graph(GraphComponents pGComp){
        Print.printi("Currently, solve isn't very good. Oh well.");
        new Grapher(new EquationSystem().add(
                    new Collection<Equation>(){{
                        for(String e : pGComp.depVars())
                            this.addE(getEq(e));
                    }}), this, null, pGComp).graph();

    }
    public void graph(String indep, String... dep) { // graphs the thing with independent being "x" and dependant being "y"
        graph(new GraphComponents(indep, dep));
    }

    /**
     * Gets the amount of {@link Equations} (not {@link Expressions}) that are contained within {@link #equations}.
     * @return size of this class's {@link #equations}.
     */
    public int size() {
        return equations.size();
    }


    public static String appendMetricSuffix(Complex param, int normStuffOnly){
        return 
                (param.isReal() ? appendMetricSuffix(param.real(), normStuffOnly) + 
                                (param.isImag() ? " + " : "") : "")+
                (param.isImag() ? appendMetricSuffix(param.imag(), normStuffOnly)+"j": 
                                (param.isReal() ? "" : param.toString()));
    }
    public static String appendMetricSuffix(Double param, int normStuffOnly){
        if(param == null || param.isNaN() || normStuffOnly == 0)
            return param.toString();
        boolean NSO = normStuffOnly == 1;
        int size = new Double(Math.log10(param)).intValue(); 
        if(size > +24 && !NSO) return param / Math.pow(10, +24) + " Y";
        if(size < -24 && !NSO) return param / Math.pow(10, -24) + " y";
        if(size > +12 &&  NSO) return param / Math.pow(10, +12) + " T";
        if(size < -12 &&  NSO) return param / Math.pow(10, -12) + " p";
        switch(size){
            case +24:                     if(!NSO) return param / Math.pow(10, +24) + "Y" ; //  yotta
            case +21: case +22: case +23: if(!NSO) return param / Math.pow(10, +21) + "Z" ; //  zetta
            case +18: case +19: case +20: if(!NSO) return param / Math.pow(10, +18) + "E" ; //  exa
            case +15: case +16: case +17: if(!NSO) return param / Math.pow(10, +15) + "P" ; //  peta
            case +12: case +13: case +14:          return param / Math.pow(10, +12) + "T" ; //  tera
            case + 9: case +10: case +11:          return param / Math.pow(10, + 9) + "G" ; //  giga
            case + 6: case + 7: case + 8:          return param / Math.pow(10, + 6) + "M" ; //  mega
            case + 3: case + 4: case + 5:          return param / Math.pow(10, + 3) + "k" ; //  kilo
            case + 2:                     if(!NSO) return param / Math.pow(10, + 2) + "h" ; //  hecto
            case + 1:                     if(!NSO) return param / Math.pow(10, + 1) + "da"; //  deca
            case -24:                     if(!NSO) return param / Math.pow(10, -24) + "y" ; //  yocto
            case -21: case -22: case -23: if(!NSO) return param / Math.pow(10, -21) + "z" ; //  zepto
            case -18: case -19: case -20: if(!NSO) return param / Math.pow(10, -18) + "a" ; //  atto
            case -15: case -16: case -17: if(!NSO) return param / Math.pow(10, -15) + "f" ; //  femto
            case -12: case -13: case -14: if(!NSO) return param / Math.pow(10, -12) + "p" ; //  pico
            case - 9: case -10: case -11: if(!NSO) return param / Math.pow(10, - 9) + "n" ; //  nano
            case - 6: case - 7: case - 8:          return param / Math.pow(10, - 6) + "μ" ; //  micro
            case - 3: case - 4: case - 5:          return param / Math.pow(10, - 3) + "m" ; //  milli
            case - 1:                     if(!NSO) return param / Math.pow(10, - 1) + "d" ; //  deci
            case - 2:                              return param / Math.pow(10, - 2) + "c" ; //  centi
            case + 0: default:                     return param                     + ""  ; //  --
        }
    }

    public boolean exprExists(String expr){
        for(Equation eq : equations){
            if(eq.getVar().equals(expr))
                return true;
        }
        return false;
    }

    public boolean isEmpty(){
        return equations.size() == 0;
    }
    @Override
    public String toString() {
        String ret = "Equations = (";
        for(Equation eq : equations) {
            ret += "'" + eq + "', ";
        }
        ret = ret.substring(0, ret.length() - (equations.size() == 0 ? 0 : 2));
        ret += "), Functions = (";
        for(String name : functions.keySet()) {
            ret += "'" + name + ":" + functions.get(name) + "', ";
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

        ret += "\n" + indentE(idtLvl + 2);
        ret += "\n" + indent(idtLvl + 1) + "Functions:";
        for(Object key : functions.keySet().toArray()) {
            ret += "\n" + indent(idtLvl + 2) + "'" + key + "' = " + functions.get("" + key).names();
        }
        ret += "\n" + indentE(idtLvl + 2) + "\n" + indentE(idtLvl + 1);

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
        return ret + "\n" + indentE(idtLvl + 1);
    }

    @Override
    public EquationSystem clone() {
        return new EquationSystem(equations, functions);
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
        for(int i = 0; i < size(); i++){
            if(!equations.get(i).equals(peqsys.equations().get(i)))
                return false;
        }
        return true;
    }
}
