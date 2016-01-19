package West.Math.Set;

import West.Math.MathObject;
import West.Print;
import West.Math.Display.Grapher;
import West.Math.Equation.EquationSystem;
import java.util.ArrayList;
/**
 * The class that represents Sets in mathematics - that is, each element in them has to be unique.
 * 
 * @author Sam Westerman
 * @version 1.1
 * @since 0.7
 */
public class MathCollection extends NumberCollection<Double> {

    protected String notation;
    protected NumberCollection<Double> enumer;

    private static String minregex = "(^\\{.*\\}.*)min:([-.\\d]+)(.*)";
    private static String maxregex = "(^\\{.*\\}.*)max:([-.\\d]+)(.*)";
    private static String stepregex ="(^\\{.*\\}.*)step:([-.\\d]+)(.*)";
    
    public MathCollection(){
        super();
        notation = null;
        enumer = null;
    }
    public MathCollection(EquationSystem pEqSys){
        this(pEqSys, -10, 10, 0.1);
    }
    public MathCollection(String setnot, double min, double max, double step){
        this(eqFromSetNotation(setnot), -10, 10, 0.1);
    }
    private MathCollection(final EquationSystem pEqSys, double min, double max, double cStep) {
        super();
        double i = min;
        assert cStep > 0;
        enumer = new NumberCollection<Double>();
        while(i < max){
            add(pEqSys.eval("firstVar", new EquationSystem().add("x = " + (i+=cStep))));
            enumer.add(i);
        }

    }

    @Override
    public void graph() {
        NumberCollection<Double> nc1 = new NumberCollection<Double>();
        NumberCollection<Double> nc2 = new NumberCollection<Double>();
        elements.forEach(nc1::add);
        enumer.forEach(nc2::add);
        new Grapher(nc2, nc1).graph();

    }

    public static EquationSystem eqFromSetNotation(String pSetNot){
        pSetNot = pSetNot.replaceAll(" ","");
        if(pSetNot.charAt(0) == '{' && pSetNot.charAt(pSetNot.length() - 1) == '}')
            pSetNot = pSetNot.substring(1, pSetNot.length() - 1);
        pSetNot = pSetNot.replaceAll("\\|", ":"); // {x : ...} OR {x | ...}, not both.
        assert pSetNot.replaceAll("[^:]","").length() == 1 : pSetNot; // only can be 1 ":"
        String vars = pSetNot.split(":")[0];
        String firstVar = vars.replaceAll("^[^A-z]*([A-z]+).*$","$1");
        pSetNot = pSetNot.split(":")[1];
        String[] equations = pSetNot.split("∧");
        return new EquationSystem().add("firstVar = " + firstVar).add(equations);
    }



    /**
     * TODO: JAVADOC
     * returns false if the thing cannot add it
     */
    @Override
    public boolean add(Double pEle){
        if(elements.contains(pEle) && !pEle.isNaN())
            return false;
        return elements.add(pEle);
    }

    @Override
    public MathCollection copy(){
        return new MathCollection(){{
            elements.forEach(this::add);
        }};
    }

    public String notation(){
        return notation;
    }

    public NumberCollection<Double> enumer(){
        return enumer;
    }

    public MathCollection setNotation(String pSetNot){
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










