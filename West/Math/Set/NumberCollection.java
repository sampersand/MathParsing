package West.Math.Set;

import West.Math.MathObject;
import West.Print;
import West.Math.Equation.EquationSystem;
import West.Math.Equation.Equation;
import West.Math.Exception.NotDefinedException;
import West.Math.Display.Grapher;

import java.util.HashMap;
import java.util.ArrayList;

/**
 * The class that represents NumberCollections in mathamatics. This one also includes a lot of different functions that are utilized
 * in Statistics.
 * 
 * @author Sam Westerman
  * @version 0.90
 * @since 0.72
 */
public class NumberCollection<N extends Number> extends Collection<N> implements MathObject {
    public static class NumberBuilder<N extends Number> extends Builder{

        @Override
        public NumberCollection build(){
            return new NumberCollection(this);
        }
    }
    public NumberCollection(){
        super();
    }

    public NumberCollection(Builder<N> pBuilder) {
        super(pBuilder);
    }

    public NumberCollection(final EquationSystem pEqSys) {
        this(pEqSys, -10, 10, 25);
    }

    public NumberCollection(final EquationSystem pEqSys, double min, double max, double cStep) {
        super();
        String firstVar;
        // if( pEqSys.equations().size() > 0 &&
        //     pEqSys.equations().get(0).subEquations().size() == 2 && 
        //     pEqSys.equations().get(0).subEquations().get(1).equals("firstVar")){ //used for what value to
        //     firstVar = pEqSys.equations().get(0).subEquations().get(0).get(0).genEqString(); //evaluate for.
        //     pEqSys.equations().remove(0);
        // }
        // else
        //     firstVar = "y";
        assert false;
        double i = min;
        // System.out.println(pEqSys);
        // while(i < max) {
        //     add((N)new ((TokenNode)Double(pEqSys).eval(firstVar, new EquationSystem().add("x = " + i))));//will add a NaN to the list
        //     i += (max - min) / cStep;
        // }


    }

    public double pred(double pVal) { //might not have to be double, not sure.
        return pred(pVal, linReg());
    }

    public static double pred(Number pVal, final EquationSystem pEqSys) {
        return pEqSys.eval("y", new EquationSystem().add("x", "" + pVal));
    }

    public static double pred(Number pVal, final EquationSystem pEqSys, final EquationSystem pEqSys2) {
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
        throw new NotDefinedException(); //TODO: THIS
        // NumberCollection<N> ret = new NumberCollection<N>();
        // for(int x = 0; x < size(); x++)
        //     ret.add((N)new Double(pred(get(x), pEq) - pNC.get(x)));
        // return ret;
    }


    public EquationSystem polyReg() {
        return polyReg(enumeration());
    }    
    public <M extends Number> EquationSystem polyReg(NumberCollection<M> pNC) {
        return polyReg(10, pNC);
    }
    public <M extends Number> EquationSystem polyReg(int maxPower, NumberCollection<M> pNC) {
        throw new NotDefinedException();
    }
    public <M extends Number> EquationSystem linReg(NumberCollection<M> pNC){

        double b1 = r(pNC) * pNC.stdev() / stdev();
        double b0 = pNC.mean() - b1 * mean();
        return new EquationSystem().add(new Equation().add("y = b0 + b1 * x"))
                                   .add(new Equation().add("b0 = " + b0)) 
                                   .add(new Equation().add("b1 = " + b1));
    }
    public EquationSystem linReg(){
        return linReg(enumeration());
    }

    public <M extends Number> double r(NumberCollection<M> pNC){
        assert size() == pNC.size() : size() + " ≠ " + pNC.size();
        throw new NotDefinedException(); //TODO: THIS
        // double sigZxZy = 0;
        // for(int i = 0; i < size(); i++)
        //     sigZxZy += Z(get(i)) * pNC.Z(pNC.get(i));
        // return sigZxZy / (size() -1);
    }

    public <M extends Number> double R2(NumberCollection<M> pNC){
        return Math.pow(r(pNC), 2);
    }

    public <M extends Number> double Z(M x) {
        return (Double.parseDouble("" + x) - mean()) / stdev();
    }

    public NumberCollection<N> Z() {
        NumberCollection<N> ret = new NumberCollection<N>();
        throw new NotDefinedException();//TODO: THIS
        // for(N ele : this)
        //     ret.add((N)new Double(Z(ele)));
        // return ret;
    }

    public double avg() {
        return mean();
    }

    public double mean() {
        assert size() != 0 : "cannot take the average of an empty array!";
        throw new NotDefinedException(); //TODO: THIS
        // double sum = 0;
        // for(N e : this)
        //     sum += Double.parseDouble("" + e); //aha cheating
        // return sum / size();
    }

    // public N instantiate(Number num){
    //     try{ 
    //         for(java.lang.reflect.Constructor c : get(0).getClass().getConstructors()){
    //             if(c.getParameterCount() == 1){
    //                 return (N)c.newInstance(c.getParameterTypes()[0].cast(num));
    //             }
    //         }
    //     } catch(InstantiationException err){
    //         System.out.println(err);
    //     } catch(IllegalAccessException err){
    //         System.out.println(err);
    //     } catch(java.lang.reflect.InvocationTargetException err){
    //         System.out.println(err);
    //     }
    //     return null;
    // }

    public NumberCollection<N> outliers() {
        double std = stdev(); //so it wont have to be called very time.
        double mean = mean(); //so it wont have to be called very time.
        NumberCollection<N> ret = new NumberCollection<N>();
        throw new NotDefinedException(); //TODO: THIS
        // for(N e : this)
        //     if(e < mean - std * 3.0 || e > mean + std * 3.0) 
        //         ret.add(e);
        // return ret;
    }

    public double stdev() { 
        double sigYy_ = 0;
        double y_  = mean();
        for(N y : this)
            sigYy_  += Math.pow(Double.parseDouble("" + y) - y_, 2); //aha cheating
        return Math.sqrt(sigYy_  / stdPos(-1));
    }

    public NumberCollection<N> fns() {
        assert size() > 0;
        throw new NotDefinedException(); //TODO: THIS
        // NumberCollection<N> sorted = sort();
        // int size = size(); //so it wont have to be called every time.
        // return new NumberCollection<N>(){{
        //     double q0, q1, q2, q3, q4;
        //     q0 = sorted.get(0);

        //     if(size % 4 == 0) q1 = (sorted.get(size / 4 - 1) + sorted.get(size / 4)) / 2;
        //     else q1 = sorted.get(size / 4);

        //     if(size % 2 == 0) q2 = (sorted.get(size / 2 - 1) + sorted.get(size / 2)) / 2;
        //     else q2 = sorted.get(size / 2);

        //     if(size % 4 == 0) q3 = (sorted.get(size * 3 / 4 - 1) + sorted.get(size * 3 / 4)) / 2;
        //     else q3 = sorted.get(size * 3 / 4);

        //     q4 = sorted.get(-1);

        //     add((N) new Double(q0));
        //     add((N) new Double(q1));
        //     add((N) new Double(q2));
        //     add((N) new Double(q3));
        //     add((N) new Double(q4));
        // }};
        
    }

    public N iqr() {
        NumberCollection<N> fns = fns();
        // return fns.get(3) - fns.get(1);
        throw new NotDefinedException();
        //TODO: THIS
    }

    public NumberCollection<N> sort() {
        NumberCollection<N> ret = copy();
        // bubble sort b/c screw it
        // TODO: BETTER SORT METHOD
        int x = -1;
        N temp;
        throw new NotDefinedException(); //TODO: THIS
        // while(++x < ret.size() - 1) {
        //     if(ret.get(x) > ret.get(x + 1)) {
        //         temp = ret.get(x + 1);
        //         ret.set(x + 1, ret.get(x));
        //         ret.set(x, temp);
        //         x = -1;
        //     }
        // }
        // return ret;
    }

    public void printBoxPlot() {
        //TODO: MAKE TIHS WORK
        //make sure to update the javadoc to reflect fixing this.
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

        if (fns.get(0) > prl[0]) { //if the min value is higher than the fence
            prl[0] = fns.get(0);
            is1fence = false;
        }
        if (fns.get(4) < prl[4]) { //if the max value is lower than the fence
            prl[4] = fns.get(4);
            is2fence = false;
        }

        for(int x = 0; x < prl.length; x++ )
            prl[x] = Z(prl[x]) * 25 + 50;
        
        double upN =  (double)(is1fence ? (fns.get(1) - iqr * 3.0) : fns.get(0));
        double mdN =  (double)(           (fns.get(2)            )         );
        double dnN =  (double)(is2fence ? (fns.get(3) + iqr * 3.0) : fns.get(4));
        // String upperS =
        //         "%" + (prl[0])
        //      + "d%" + (prl[2] - prl[0] + (""+upN).length())
        //      + "d%" + (prl[4] - prl[2] - prl[0] + (""+upN).length() + (""+mdN).length())
        //       + "d\n";
        // System.out.printf(upperS, (int)upN, (int)mdN, (int)dnN);
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
       // Print.print();
       */
        throw new NotDefinedException();
        //TODO: FIX THIS
    }

    public void printFiveNumSum() {
        NumberCollection<N> fns = fns();
        System.out.printf("Min   : \n%nQ1    : \n%nMed   : \n%nQ2    : \n%nMax   : \n%nMean  : \n%nStdev : \n%n",
            fns.get(0), fns.get(1), fns.get(2), fns.get(3), fns.get(4), mean(), stdev());
    }

    public static NumberCollection<Double> enumerationD(double start, double end, double step){
        return new NumberCollection<Double>()
        {{
            for(double i = start; i < end; i+=step)
                add(i);
        }};
    }

    public <M extends Number> void graphe(NumberCollection<M> pNC) {
        //TODO: SEE IF THIS WORKS
        resid(pNC).graph(enumeration()); 
    }


    //TODO: MAKE IT SO YOU CAN GRAPH WITH NO PARAMETER, AND INSTEAD USE THE INDEX OF DIFFERENT ELEMENTS.
    /**
     * pNC is x axis
     * TODO: JAVADOC
     */
    public <M extends Number> void graph(NumberCollection<M> pNC) { //the number line on the bottom can be different.
        NumberCollection<Double> nc1 = new NumberCollection<Double>();

        NumberCollection<Double> nc2 = new NumberCollection<Double>();
        for(Number n : pNC) nc1.add(Double.parseDouble("" + n));
        for(Number n : this) nc2.add(Double.parseDouble("" + n));
        // System.out.println(nc1);
        // System.out.println(nc2);
        new Grapher(nc1, nc2).graph();
    }

    public void graph() {
        graph(enumeration());
    }
    public void graphWLinReg() {
        // Grapher grapher = new Grapher(linReg(),
        //     new ArrayList<ArrayList<NumberCollection<Double>>>(){{
        //         add(new ArrayList<NumberCollection<Double>>(){{
        //             add((NumberCollection<Double>)copy());
        //             add((NumberCollection<Double>)enumeration());
        //         }});
        //     }});
        // grapher.graph();
        throw new NotDefinedException(); //TODO: THIS
        //TODO: FIX THIS
    }

    @Override
    public NumberCollection<Integer> enumeration(){ //should be integer, but its this so it works with other things.
        return (NumberCollection<Integer>)super.enumeration();
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
    public NumberCollection copy(){
        throw new NotDefinedException();
        // return new NumberBuilder<N>().addAll(elements).build();
    }


}