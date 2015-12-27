package Math.Set;

import Math.MathObject;
import Math.Print;
import Math.Exception.NotDefinedException;
import Math.Equation.EquationSystem;
import java.util.ArrayList;
/**
 * The class that represents Sets in mathematics - that is, each element in them has to be unique.
 * 
 * @author Sam Westerman
 * @version 0.72
 * @since 0.7
 */
public class MathSet<E extends Double> extends NumberCollection<E> {

    protected String notation = "";
    public MathSet(){
        super();
        assert isUnique() : "cannot instatiate a non-unique MathSet.";
    }
    public MathSet(ArrayList<E> pEle){
        super(pEle);
        assert isUnique() : "cannot instatiate a non-unique MathSet.";
    }
    public MathSet(NumberCollection pNumberCollection){
        super(pNumberCollection);
        assert isUnique() : "cannot instatiate a non-unique MathSet.";
    }

    public MathSet(EquationSystem pEqSys){
        super(pEqSys);
        assert isUnique() : "cannot instatiate a non-unique MathSet.";
    }

    public static MathSet<Double> fromSetNotation(String pSetNot){
        MathSet<Double> ret = new MathSet<Double>(eqFromSetNotation(pSetNot));
        ret.setNotation(pSetNot);
        return ret;
    }
    public static MathSet<Double> enumerateSetNotation(String pSetNot){
        MathSet<Double> ret = new MathSet<Double>(eqFromSetNotation(pSetNot));
        return ret;
    }

    /**
     * TODO: JAVADOC
     * returns false if the thing cannot add it
     */
    public boolean add(E pEle){ 
        if(elements.contains(pEle)){
            Print.printw("Trying to add a duplicate element (" + pEle +") to a MathSet, but continuing instead.");
            elements.add(pEle);
            return false;
        }
        elements.add(pEle);
        return true;
    }

    @Override
    public MathSet copy(){
        return new MathSet(elements);
    }

    public String notation(){
        return notation;
    }
    void setNotation(String pSetNot){
        notation = pSetNot;
    }
    /**
     * currently doesnt work with funtions that use commas, lol.
     * TODO: JAVADOC
     */
    private static EquationSystem eqFromSetNotation(String pSetNot){
        pSetNot = pSetNot.replaceAll(" ","");
        if(pSetNot.charAt(0) == '{' && pSetNot.charAt(pSetNot.length() - 1) == '}')
            pSetNot = pSetNot.substring(1, pSetNot.length() - 1);

        pSetNot = pSetNot.replaceAll("\\|", ":"); // {x : ...} OR {x | ...}, not both.
        assert pSetNot.replaceAll("[^:]","").length() == 1; // only can be 1 ":"
        String vars = pSetNot.split(":")[0];
        String firstVar = vars.replaceAll("^[^A-z]*([A-z]+).*$","$1");
        pSetNot = pSetNot.split(":")[1];
        pSetNot = pSetNot.replaceAll(",", "∧");
        String[] equations = pSetNot.split("∧");
        EquationSystem constraints = new EquationSystem().add(vars.split(","));
        return new EquationSystem().add(firstVar + " = firstVar ").add(equations).setConstraints(constraints);
    }

}










