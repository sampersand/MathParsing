package West.Math.Set;

import West.Math.MathObject;
import West.Print;
import West.Math.Equation.EquationSystem;
import java.util.ArrayList;
/**
 * The class that represents Sets in mathematics - that is, each element in them has to be unique.
 * 
 * @author Sam Westerman
  * @version 0.90
 * @since 0.7
 */
public class MathCollection<N extends Double> extends NumberCollection<N> {
    public static class MathBuilder<M extends Double> extends NumberBuilder{

        public MathBuilder(){
            super();
        }

        @Override
        public MathCollection build(){
            return new MathCollection<M>(this);
        }
    }
    protected String notation = "";

    public MathCollection(){
        super();
        if(!isUnique()){
            Print.printi("Trying to instantiate a non-unique MathCollection. Compressing it down to become unique.");
            compress();
        }
    }
    public MathCollection(MathBuilder<N> builder){
        super(builder);

        if(!isUnique()){
            Print.printi("Trying to instantiate a non-unique MathCollection. Compressing it down to become unique.");
            compress();
        }
    }
    public MathCollection(EquationSystem pEqSys){
        super(pEqSys);
        if(!isUnique()){
            Print.printi("Trying to instantiate a non-unique MathCollection. Compressing it down to become unique.");
            compress();
        }
    }

    public static MathCollection<Double> fromSetNotation(String pSetNot){
        MathCollection<Double> ret = new MathCollection<Double>(eqFromSetNotation(pSetNot));
        ret.setNotation(pSetNot);
        return ret;
    }
    public static MathCollection<Double> enumerateSetNotation(String pSetNot){
        MathCollection<Double> ret = new MathCollection<Double>(eqFromSetNotation(pSetNot));
        return ret;
    }

    /**
     * TODO: JAVADOC
     * returns false if the thing cannot add it
     */
    public boolean add(N pEle){ 
        if(elements.contains(pEle)){
            Print.printw("Trying to add a duplicate element (" + pEle +") to a MathCollection, but continuing instead.");
            elements.add(pEle);
            return false;
        }
        elements.add(pEle);
        return true;
    }

    @Override
    public MathCollection copy(){
        throw new NullPointerException();
        //TODO: DEFINE
        // return new MathBuilder().addAll(elements).build();
    }

    public String notation(){
        return notation;
    }
    void setNotation(String pSetNot){
        notation = pSetNot;
    }
    public void compress(){
        for(int i = 0; i < size(); i++)
            for(int j = 0; j < size(); j++)
                if(get(i).equals(get(j))){
                    remove(j);
                    break;
                }
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










