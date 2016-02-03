package West.Math.Set;

import West.Math.MathObject;
import West.Print;
import West.Math.Equation.EquationSystem;
import West.Math.Complex;
import West.Math.Equation.Equation;
import West.Math.Display.Grapher;
import West.Math.Complex;
import West.Math.Display.GraphComponents;

import java.util.HashMap;
import java.util.ArrayList;

/**
 * The class that represents NumberCollections in mathamatics. This one also includes a lot of different functions that are utilized
 * in Statistics.
 * 
 * @author Sam Westerman
 * @version 1.1
 * @since 0.72
 */
public class NumberCollection<N extends Number> extends Collection<N> implements MathObject {
    public NumberCollection(){
        super();
    }
    public NumberCollection(ArrayList<N> arl) {
        super(arl);
    }

    public NumberCollection(final EquationSystem pEqSys) {
        this(pEqSys, -10, 10, 25);
    }

    public NumberCollection(final EquationSystem pEqSys, double min, double max, double cStep) {
        super();
        //  if( pEqSys.equations().size() > 0 &&
        //      pEqSys.equations().get(0).subEquations().size() == 2 && 
        //      pEqSys.equations().get(0).subEquations().get(1).equals("firstVar")){ // used for what value to
        //      firstVar = pEqSys.equations().get(0).subEquations().get(0).get(0).genEqString(); // evaluate for.
        //      pEqSys.equations().remove(0);
        //  }
        //  else
        //      firstVar = "y";
        double i = min;
        while(i < max) {
            add((N) pEqSys.eval("firstVar", new EquationSystem().add("x = " + (i+=cStep))));
            //  i+=(max - min) / cStep;
        }


    }
    @Override
    public NumberCollection<N> addAllE(Object pObj){
        super.addAllE(pObj);
        return this;
    }

    public NumberCollection<N> addE(Object pObj){
        super.addE(pObj);
        return this;
    }
    public Complex pred(N pVal) { // might not have to be double, not sure.
        return pred(pVal, linReg());
    }

    public static Complex pred(Number pVal, final EquationSystem pEqSys) {
        return pEqSys.eval("y", new EquationSystem().add("x", "" + pVal));
    }

    public static Complex pred(Number pVal, final EquationSystem pEqSys, final EquationSystem pEqSys2) {
        return pEqSys.eval("y", pEqSys2.add("x = " + pVal));
    }


    public NumberCollection<N> es() {
        return resid();
    }

    public <M extends Number> NumberCollection<N> resid(NumberCollection<M> pNC) {
        return resid(pNC, linReg(pNC));
    }
    public NumberCollection<N> resid() {
        return resid(enumeration());
    }

    public <M extends Number> NumberCollection<N> resid(NumberCollection<M> pNC, EquationSystem pEq) {
        assert size() == pNC.size();
        throw new NullPointerException(); // TODO: THIS
        //  NumberCollection<N> ret = new NumberCollection<N>();
        //  for(int x = 0; x < size(); x++)
        //      ret.add((N)new Double(pred(get(x), pEq) - pNC.get(x)));
        //  return ret;
    }


    public EquationSystem polyReg() {
        return polyReg(enumeration());
    }    
    public <M extends Number> EquationSystem polyReg(NumberCollection<M> pNC) {
        return polyReg(10, pNC);
    }
    public <M extends Number> EquationSystem polyReg(int maxPower, NumberCollection<M> pNC) {
        throw new NullPointerException();
    }
    public <M extends Number> EquationSystem linReg(NumberCollection<M> pNC){

        Complex b1 = r(pNC).mult(pNC.stdev().div(stdev()));
        Complex b0 = pNC.mean().minus(b1.mult(mean()));
        return new EquationSystem().add(new Equation().add("y = b0 + b1 * x"))
                                   .add(new Equation().add("b0 = " + b0)) 
                                   .add(new Equation().add("b1 = " + b1));
    }

    /**
     * Used internally when multiple linear regressions are needed, and are required to be distinct.
     */
    private <M extends Number> EquationSystem linReg(NumberCollection<M> pNC, int n){

        Complex b1 = r(pNC).mult(pNC.stdev()).div(stdev());
        Complex b0 = pNC.mean().minus(b1.mult(mean()));
        return new EquationSystem()
                .add(new Equation().add("__y" + n + "__ = __b0_" + n + "__  +  __b1_" + n + "__ * x"))
                .add(new Equation().add("__b0_" + n + "__ = " + b0)) 
                .add(new Equation().add("__b1_" + n + "__ = " + b1));
    }

    public EquationSystem linReg(){
        return linReg(enumeration());
    }

    public Complex r(NumberCollection<? extends Number> pNC){
        assert size() == pNC.size() : "size '" + size() + "' ≠ '" + pNC.size() + "'.";
        Complex sigZxZy = Complex.ZERO;
        for(int i = 0; i < size(); i++){
            sigZxZy = sigZxZy.plus(Z(get(i)).mult(pNC.Z(pNC.get(i))));
        }
        return sigZxZy.div(new Complex(size() - 1));
    }

    public Complex R2(NumberCollection<? extends Number> pNC){
        return r(pNC).pow(2d);
    }

    public Complex Z(Number x) {
        return new Complex(x).minus(mean()).div(stdev());
    }

    public NumberCollection<Complex> Z() {
        NumberCollection<Complex> ret = new NumberCollection<Complex>();
         for(N ele : this)
             ret.add(Z(ele));
         return ret;
    }

    public Complex mean() {
        assert size() != 0 : "cannot take the average of an empty array!";
         Complex sum = Complex.ZERO;
         for(N e : this)
             sum = sum.plus(new Complex(e)); // aha cheating
         return sum.div(new Complex(size()));
    }

    public NumberCollection<N> outliers() {
        Complex std = stdev(); // so it wont have to be called very time.
        Complex mean = mean(); // so it wont have to be called very time.
        NumberCollection<N> ret = new NumberCollection<N>();
         // for(N e : this)
         //     if(e < mean - std * 3.0 || e > mean + std * 3.0) 
         //         ret.add(e);
         return ret;
    }

    public Complex stdev() { 
        Complex sigYy_ = Complex.ZERO;
        Complex y_  = mean();
        for(N y : this){
            sigYy_ = sigYy_.plus(new Complex(y).minus(y_).pow(2d));
        }
        return sigYy_.div(new Complex(size() - 1)).sqrt();
    }

    public NumberCollection<N> fns() {
        assert size() > 0;
        throw new NullPointerException(); // TODO: THIS
        //  NumberCollection<N> sorted = sort();
        //  int size = size(); // so it wont have to be called every time.
        //  return new NumberCollection<N>(){{
        //      double q0, q1, q2, q3, q4;
        //      q0 = sorted.get(0);

        //      if(size % 4 == 0) q1 = (sorted.get(size / 4 - 1) + sorted.get(size / 4)) / 2;
        //      else q1 = sorted.get(size / 4);

        //      if(size % 2 == 0) q2 = (sorted.get(size / 2 - 1) + sorted.get(size / 2)) / 2;
        //      else q2 = sorted.get(size / 2);

        //      if(size % 4 == 0) q3 = (sorted.get(size * 3 / 4 - 1) + sorted.get(size * 3 / 4)) / 2;
        //      else q3 = sorted.get(size * 3 / 4);

        //      q4 = sorted.get(-1);

        //      add((N) new Double(q0));
        //      add((N) new Double(q1));
        //      add((N) new Double(q2));
        //      add((N) new Double(q3));
        //      add((N) new Double(q4));
        //  }};
        
    }

    public N iqr() {
        NumberCollection<N> fns = fns();
        //  return fns.get(3) - fns.get(1);
        throw new NullPointerException();
        // TODO: THIS
    }

    public NumberCollection<N> sort() {
        NumberCollection<N> ret = clone();
        //  bubble sort b/c screw it
        //  TODO: BETTER SORT METHOD
        int x = -1;
        N temp;
        throw new NullPointerException(); // TODO: THIS
        //  while(++x < ret.size() - 1) {
        //      if(ret.get(x) > ret.get(x + 1)) {
        //          temp = ret.get(x + 1);
        //          ret.set(x + 1, ret.get(x));
        //          ret.set(x, temp);
        //          x = -1;
        //      }
        //  }
        //  return ret;
    }

    public void printBoxPlot() {
        // TODO: MAKE TIHS WORK
        // make sure to update the javadoc to reflect fixing this.
        /*
        NumberCollection<N> fns = fns();
        N[] prl = new N[5];
        assert fns.size() == prl.length;

        for(int x = 0; x < fns.size(); x++)
            prl[x] = fns.get(x);

        N iqr = iqr();

        prl[0] = fns.get(1) - iqr * 3.0;
        prl[4] = fns.get(3) + iqr * 3.0;

        boolean is1fence = true, is2fence = true;

        if (fns.get(0) > prl[0]) { // if the min value is higher than the fence
            prl[0] = fns.get(0);
            is1fence = false;
        }
        if (fns.get(4) < prl[4]) { // if the max value is lower than the fence
            prl[4] = fns.get(4);
            is2fence = false;
        }

        for(int x = 0; x < prl.length; x++ )
            prl[x] = Z(prl[x]) * 25 + 50;
        
        double upN =  (double)(is1fence ? (fns.get(1) - iqr * 3.0) : fns.get(0));
        double mdN =  (double)(           (fns.get(2)            )         );
        double dnN =  (double)(is2fence ? (fns.get(3) + iqr * 3.0) : fns.get(4));
        //  String upperS =
        //          "%" + (prl[0])
        //       + "d%" + (prl[2] - prl[0] + (""+upN).length())
        //       + "d%" + (prl[4] - prl[2] - prl[0] + (""+upN).length() + (""+mdN).length())
        //        + "d\n";
        //  System.out.printf(upperS, (int)upN, (int)mdN, (int)dnN);
        for(int i = 0; i < 101; i ++) {
                 if(i == (int)prl[0]) Print.printnl('|');
            else if(i == (int)prl[1]) Print.printnl('|');
            else if(i == (int)prl[2]) Print.printnl('|');
            else if(i == (int)prl[3]) Print.printnl('|');
            else if(i == (int)prl[4]) Print.printnl('|');
            else if(i <  prl[0]) Print.printnl(' ');            
            else if(i >  prl[0] && i < prl[1]) Print.printnl('-');
            else if(i >  prl[1] && i < prl[2]) Print.printnl('#');
            else if(i >  prl[2] && i < prl[3]) Print.printnl('#');
            else if(i >  prl[3] && i < prl[4]) Print.printnl('-');
            else if(i >  prl[4]) Print.printnl(' ');
        }
       //  Print.print();
       */
        throw new NullPointerException();
        // TODO: FIX THIS
    }

    public void printFiveNumSum() {
        NumberCollection<N> fns = fns();
        System.out.printf("Min   : \n%nQ1    : \n%nMed   : \n%nQ2    : \n%nMax   : \n%nMean  : \n%nStdev : \n%n",
            fns.get(0), fns.get(1), fns.get(2), fns.get(3), fns.get(4), mean(), stdev());
    }

    public <M extends Number> void graphe(NumberCollection<M> pNC) {
        // TODO: SEE IF THIS WORKS
        resid(pNC).graph(enumeration()); 
    }


    // TODO: MAKE IT SO YOU CAN GRAPH WITH NO PARAMETER, AND INSTEAD USE THE INDEX OF DIFFERENT ELEMENTS.
    /**
     * pNC is x axis
     * TODO: JAVADOC
     */
    public <M extends Number> void graph(NumberCollection<M> pNC) { // the number line on the bottom can be different.
        NumberCollection<Complex> nc1 = new NumberCollection<Complex>();

        NumberCollection<Complex> nc2 = new NumberCollection<Complex>();
        for(Number n : pNC) nc1.add(new Complex(n));
        for(Number n : this) nc2.add(new Complex(n));
        new Grapher(nc1, nc2).graph();
    }

    public void graph() {
        graph(enumeration());
    }
    public <M extends Number> void graphWithLinReg(NumberCollection<M> pNC) {
        graphWithLinReg(pNC, new GraphComponents());
    }

    public <M extends Number> void graphWithLinReg(NumberCollection<M> pNC, GraphComponents gComp) {
        NumberCollection<Complex> yaxis = new NumberCollection<Complex>();
        for(N n : clone()){
            yaxis.add(new Complex(n));
        }
        NumberCollection<Complex> xaxis = new NumberCollection<Complex>();
        forEach(e -> xaxis.add(new Complex(e.doubleValue())));
        graphMultiWithLinReg(new Collection<Collection<NumberCollection<Complex>>>(){{
                                        add(new Collection<NumberCollection<Complex>>(){{
                                            add(yaxis);
                                            add(xaxis);
                                        }});
                                    }},
                                gComp
                                );
    }
    public static <M extends Number> void graphMultiWithLinReg(
                                Collection<Collection<NumberCollection<Complex>>> pCollections,
                                GraphComponents gComp) {
        EquationSystem allEquations = new EquationSystem();
        EquationSystem toGraph = new EquationSystem();
        EquationSystem linreg;
        int num = 0; // im using it like this because it's sleeker with the for each statement.
        for(Collection<NumberCollection<Complex>> setpair : pCollections){
            assert setpair != null && setpair.size() == 2 : "Set Pairs have to be in the format 'X axis, Y axis'. " + 
                (setpair == null ? "the set pairs were null" : setpair.size() + " was the size") +
                " for the given Collection!";
            linreg = setpair.get(0).linReg(setpair.get(1), num);
            System.out.println(setpair.get(0) + " >> linreg with << " + setpair.get(1) + "\t\t becomes:\t"+linreg);
            allEquations.add(linreg);
            assert linreg.getEq("__y" + num + "__") != null;
            toGraph.add(linreg.getEq("__y" + num++ + "__"));
        }
        Grapher grapher = new Grapher(toGraph, allEquations, pCollections, gComp);
        grapher.graph();
    }

    @Override
    public NumberCollection<Integer> enumeration(){ // should be integer, but its this so it works with other things.
        return new NumberCollection<Integer>().addAllE(super.enumeration());
    }
    @Override
    public String toString() {
        return "Number" + super.toString();
    }

    @Override
    public String toFancyString(int idtLvl) {
        if(size() == 0)
            return indent(idtLvl) + "Empty NumberCollection";
        return toFullString(idtLvl);
    }
    @Override
    public NumberCollection<N> clone(){
         return new NumberCollection<N>().addAllE(elements);
    }


}