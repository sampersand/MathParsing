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
    private MathCollection(final EquationSystem pEqSys, double min, double max, double cStep) {
        super(pEqSys, min, max, cStep);
    }

    private static String minregex = "(^\\{.*\\}.*)min:([-.\\d]+)(.*)";
    private static String maxregex = "(^\\{.*\\}.*)max:([-.\\d]+)(.*)";
    private static String stepregex ="(^\\{.*\\}.*)(?:step|cStep|points):([-.\\d]+)(.*)";
    public static MathCollection<Double> fromSetNotation(String pSetNot){
        double min = -10, max = 10, step = 25;
        pSetNot = pSetNot.replaceAll(" ","");
        if(pSetNot.matches(minregex)){
            min = Double.parseDouble(pSetNot.replaceAll(minregex,"$2"));
            pSetNot = pSetNot.replaceAll(minregex, "$1$3");
        }
        if(pSetNot.matches(maxregex)){
            max = Double.parseDouble(pSetNot.replaceAll(maxregex,"$2"));
            pSetNot = pSetNot.replaceAll(maxregex, "$1$3");
        }
        if(pSetNot.matches(stepregex)){
            step = Double.parseDouble(pSetNot.replaceAll(stepregex,"$2"));
            pSetNot = pSetNot.replaceAll(stepregex, "$1$3");
        }
            // max = Double.parseDouble(pSetNot.replaceAll("^\{.*\}.*max:([-\\d.])","$1"));
            // step = Double.parseDouble(pSetNot.replaceAll("^\{.*\}.*(?:step|step|points):([-\\d.])","$1"));
        if(pSetNot.charAt(0) == '{' && pSetNot.charAt(pSetNot.length() - 1) == '}')
            pSetNot = pSetNot.substring(1, pSetNot.length() - 1);

        pSetNot = pSetNot.replaceAll("\\|", ":"); // {x : ...} OR {x | ...}, not both.
        assert pSetNot.replaceAll("[^:]","").length() == 1 : pSetNot; // only can be 1 ":"
        String vars = pSetNot.split(":")[0];
        String firstVar = vars.replaceAll("^[^A-z]*([A-z]+).*$","$1");
        pSetNot = pSetNot.split(":")[1];
        pSetNot = pSetNot.replaceAll(",", "∧");
        String[] equations = pSetNot.split("∧");
        return new MathCollection(new EquationSystem().add("firstVar = " + firstVar).add(equations),
                                  min,
                                  max,
                                  step).setNotation(pSetNot);
    }


    // public static MathCollection<Double> enumerateSetNotation(String pSetNot){
    //     MathCollection<Double> ret = new MathCollection<Double>(eqFromSetNotation(pSetNot));
    //     return ret;
    // }

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
        return elements.add(pEle);
    }

    @Override
    public MathCollection<N> copy(){
        return new MathCollection<N>(){{
            for (N ele : elements)
                add(ele);
        }};
    }

    public String notation(){
        return notation;
    }
    MathCollection setNotation(String pSetNot){
        notation = pSetNot;
        return this;
    }
    public void compress(){
        for(int i = 0; i < size(); i++)
            for(int j = 0; j < size(); j++)
                if(get(i).equals(get(j))){
                    remove(j);
                    break;
                }
    }

}










