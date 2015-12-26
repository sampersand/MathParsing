package Math.Set;

import Math.MathObject;
import Math.Print;
import Math.Equation.Domain;
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

    public MathSet(){
        super();
    }
    public MathSet(ArrayList<E> pEle){
        super(pEle);
        assert isUnique();
    }
    public MathSet(NumberCollection pNumberCollection){
        super(pNumberCollection);
        assert isUnique() : "cannot instatiate a non-unique MathSet.";
    }

    public MathSet(String setBuilderNotation){
        this(generateEquationFromSetBuilderNotation(setBuilderNotation));
    }

    public MathSet(EquationSystem pEqSys){
        super(pEqSys);
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

    /**
     * currently doesnt work with funtions that use commas, lol.
     * TODO: JAVADOC
     */
    static EquationSystem generateEquationFromSetBuilderNotation(String pSetNotation){
        pSetNotation = pSetNotation.replaceAll(" ","");
        if(pSetNotation.charAt(0) == '{' && pSetNotation.charAt(pSetNotation.length() - 1) == '}')
            pSetNotation = pSetNotation.substring(1, pSetNotation.length() - 1);

        pSetNotation = pSetNotation.replaceAll("\\|", ":"); // {x : ...} OR {x | ...}, not both.
        assert pSetNotation.replaceAll("[^:]","").length() == 1; // only can be 1 ":"
        String vars = pSetNotation.split(":")[0];
        String firstVar = vars.replaceAll("^[^A-z]*([A-z]+).*$","$1");
        pSetNotation = pSetNotation.split(":")[1];
        pSetNotation = pSetNotation.replaceAll(",", "∧");
        String[] equations = pSetNotation.split("∧");
        return new EquationSystem().add(firstVar + " = firstVar ").add(equations).setDomain(new Domain(vars));
    }

}










