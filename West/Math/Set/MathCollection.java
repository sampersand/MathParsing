package West.Math.Set;

import West.Math.MathObject;
import West.Print;
import West.Math.Display.Grapher;
import West.Math.Equation.EquationSystem;
import java.util.ArrayList;
import West.Math.ComplexNumber;

/**
 * The class that represents Sets in mathematics - that is, each element in them has to be unique.
 * 
 * @author Sam Westerman
 * @version 1.1
 * @since 0.7
 */
public class MathCollection extends NumberCollection<ComplexNumber> {

    protected String notation;
    protected NumberCollection<ComplexNumber> enumer;

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
        enumer = new NumberCollection<ComplexNumber>();
        while(i < max){
            add(pEqSys.eval("firstVar", new EquationSystem().add("x = " + (i+=cStep))));
            enumer.add(new ComplexNumber(i));
        }

    }

    @Override
    public void graph() {
        NumberCollection<ComplexNumber> nc1 = new NumberCollection<ComplexNumber>();
        NumberCollection<ComplexNumber> nc2 = new NumberCollection<ComplexNumber>();
        elements.forEach(nc1::add);
        enumer.forEach(nc2::add);
        new Grapher(nc2, nc1).graph();

    }

    public static EquationSystem eqFromSetNotation(String pSetNot){
        pSetNot = pSetNot.replaceAll(" ","");
        if(pSetNot.charAt(0) == '{' && pSetNot.charAt(pSetNot.length() - 1) == '}')
            pSetNot = pSetNot.substring(1, pSetNot.length() - 1);
        pSetNot = pSetNot.replaceAll("\\|", ":"); //  {x : ...} OR {x | ...}, not both.
        assert pSetNot.replaceAll("[^:]","").length() == 1 : pSetNot; //  only can be 1 ":"
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
    public boolean add(ComplexNumber pEle){
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

    public NumberCollection<ComplexNumber> enumer(){
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










