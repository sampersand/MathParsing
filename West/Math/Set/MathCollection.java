package West.Math.Set;

import West.Math.MathObject;
import West.Print;
import West.Math.Display.Grapher;
import West.Math.Equation.EquationSystem;
import java.util.ArrayList;
import West.Math.Complex;
import West.Math.Set.Node.TokenNode;
import java.util.HashMap;
import West.Math.Operable;

/**
 * The class that represents Sets in mathematics - that is, each element in them has to be unique.
 * 
 * @author Sam Westerman
 * @version 1.1
 * @since 0.7
 */
public class MathCollection extends NumberCollection<Complex> {

    protected String notation;
    protected NumberCollection<Complex> enumer;

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
    public MathCollection(TokenNode tn, HashMap<String, Operable> hm, final EquationSystem pEqSys){

        int s = tn.size();
        System.out.println(tn);
        assert s == 3 || s == 4 : tn;
        assert !tn.get(0).isFinal() && 
                tn.get(0).get(0).isFinal(): tn.get(0);
        assert tn.get(1).token().isDelim() &&
                (tn.get(1).token().val().equals("|") ||
                    tn.get(1).token().val().equals(":")
                ) : tn.get(1) + " needs to be `:INCR`";
        assert tn.get(2).token().isDelim() &&
               tn.get(2).token().val().equals(":") : tn.get(2) + " needs to be `:CONDITS`";
        assert s == 3 || tn.get(3).token().isDelim() &&
                         tn.get(3).token().val().equals("@")
                : tn.get(3) + " needs to be `@(START, END, STEP)` (can be omitted)";
        assert s == 3 || tn.get(3).size() == 3 :
                tn.get(3) + " needs to be `@(START, END, STEP)` (can be omitted)";
        // MathCollection col = new MathCollection(tn.toString());
        // Collection<Complex> col = new Collection<Complex>();
        // String toeval = tn.get(0).get(0).token().val();
        // String toincr = tn.get(1).get(0).token().val();
        // TokenNode condits = tn.get(2).get(0);
        // Double min = s == 3 ? 1d : ((Complex)tn.get(3).get(0).evald(hm, eqsys)).
        //                                     aIsOnlyReal().real();
        // Double max = s == 3 ? Complex.P_INF.real() : ((Complex)tn.get(3).get(1).evald(hm, eqsys)).
        //                                     aIsOnlyReal().real();
        // Double step = ((Complex)tn.get(3).get(2).evald(hm, eqsys)).aIsOnlyReal().real();
        // assert !min.isNaN();
        // assert !max.isNaN();
        // assert !step.isNaN();
        // for(Double d = min; d < max; d+=step){
        //     System.out.println(new EquationSystem().//add(eqsys).
        //             add(toincr + "=" + d).
        //             add(toincr + "=" + d).add(condits) // this should be fixed...
        //             );
        //      Complex d2 = new EquationSystem().//add(eqsys).
        //             add(toincr + "=" + d).add(toeval + "=" + condits) // this should be fixed...
        //             .eval(toeval, hm);
        //      if(!d2.isNaN())
        //          col.add(d2);
        // }
        // return new Complex(col.size());

        // if(setnot.charAt(0) == '{' && setnot.charAt(setnot.length() - 1) == '}')
        //     setnot = setnot.substring(1, setnot.length() - 1);
        // setnot = setnot.replaceAll("\\|", ":"); //  {x : ...} OR {x | ...}, not both.
        // assert setnot.replaceAll("[^:]","").equals("::") : setnot; //  only can be 2 ":"
        // String toincr = setnot.split(":")[0];
        // String firstVar = vars.replaceAll("^[^A-z]*([A-z]+).*$","$1");
        // setnot = setnot.split(":")[1];
        // String[] equations = setnot.split("∧");
        // return new EquationSystem().add("firstVar = " + firstVar).add(equations);
    }
    private MathCollection(final EquationSystem pEqSys, double min, double max, double cStep) {
        super();
        double i = min;
        assert cStep > 0;
        enumer = new NumberCollection<Complex>();
        while(i < max){
            add(pEqSys.eval("firstVar", new EquationSystem().add("x = " + (i+=cStep))));
            enumer.add(new Complex(i));
        }

    }

    // @Override
    // public void graph(boolean linreg) {
    //     NumberCollection<Complex> nc1 = new NumberCollection<Complex>();
    //     NumberCollection<Complex> nc2 = new NumberCollection<Complex>();
    //     elements.forEach(nc1::add);
    //     enumer.forEach(nc2::add);
    //     new Grapher(nc2, nc1).graph();

    // }

    // public static EquationSystem eqFromSetNotation(String setnot){
    //     setnot = setnot.replaceAll(" ","");
    //     if(setnot.charAt(0) == '{' && setnot.charAt(setnot.length() - 1) == '}')
    //         setnot = setnot.substring(1, setnot.length() - 1);
    //     setnot = setnot.replaceAll("\\|", ":"); //  {x : ...} OR {x | ...}, not both.
    //     assert setnot.replaceAll("[^:]","").length() == 1 : setnot; //  only can be 1 ":"
    //     String vars = setnot.split(":")[0];
    //     String firstVar = vars.replaceAll("^[^A-z]*([A-z]+).*$","$1");
    //     setnot = setnot.split(":")[1];
    //     String[] equations = setnot.split("∧");
    //     return new EquationSystem().add("firstVar = " + firstVar).add(equations);
    // }



    /**
     * TODO: JAVADOC
     * returns false if the thing cannot add it
     */
    @Override
    public boolean add(Complex pEle){
        if(elements.contains(pEle) && !pEle.isNaN())
            return false;
        return elements.add(pEle);
    }

    @Override
    public MathCollection clone(){
        return new MathCollection(){{
            elements.forEach(this::add);
        }};
    }

    public String notation(){
        return notation;
    }

    public NumberCollection<Complex> enumer(){
        return enumer;
    }

    public MathCollection setNotation(String setnot){
        notation = setnot;
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










