package Math.Set;

import Math.MathObject;
import Math.Print;
import Math.Equation.EquationSystem;
import Math.Equation.Equation;
import Math.Equation.Expression;
import Math.Exception.NotDefinedException;
import Math.Display.Grapher;

import java.util.HashMap;
import java.util.ArrayList;

/**
 * The class that represents NumberCollections in mathamatics. This one also includes a lot of different functions that are utilized
 * in Statistics.
 * 
 * @author Sam Westerman
 * @version 0.72
 * @since 0.72
 */
public class NumberCollection<E extends Double> extends Collection<E> implements MathObject {

    public NumberCollection(){
        super();
    }
    public NumberCollection(ArrayList<E> pElements){
        super(pElements);
    }

    public NumberCollection(E... pElements) {
        super(pElements);
    }

    public <T extends E> NumberCollection(NumberCollection<T> pCollection) {
        super(pCollection);
    }

    public NumberCollection(final EquationSystem pEqSys) {
        this(pEqSys, (E) new Double(-10D), (E) new Double(10D), (E) new Double(25D));
    }

    public NumberCollection(final EquationSystem pEqSys, E min, E max, E cStep) {
        // if(min >= max) {
        //     throw new IllegalArgumentException("When defining a NumberCollection with an EquationSystem, the min (" + min +
        //                                    ") has to be smaller than the max (" + max + ")!");
        // } if(cStep == 0) {
        //     throw new IllegalArgumentException("When defining a NumberCollection with an EquationSystem, the cStep cannot be 0!");
        // }
        throw new NotDefinedException(); //TODO
        // arr1 = new double[(int) Math.abs(Math.ceil(cStep))];
        // arr2 = new double[(int) Math.abs(Math.ceil(cStep))];
        // int pos = 0;
        // for(double i = min; i < max; i += (max - min) / cStep, pos++) {
        //     if(pos >= arr1.length)
        //         break;
        //     arr1[pos] = i;
        //     arr2[pos] = pred(i, pEqSys);
        // }

    }

    public double pred(double pVal) {
        return pred(pVal, linReg(enumeration()));
    }

    public static double pred(Number pVal, final EquationSystem pEqSys) {
        return pEqSys.eval("yhat", new EquationSystem().add("y", "" + pVal));
    }

    public static double pred(Number pVal, final EquationSystem pEqSys, final EquationSystem pEqSys2) {
        return pEqSys.eval("yhat", pEqSys2);
    }

    public NumberCollection<E> es() {
        return resid(enumeration());
    }

    public <T extends E> NumberCollection<E> resid(NumberCollection<T> pNC) {
        return resid(pNC, linReg(pNC));
    }

    public <T extends E> NumberCollection<E> resid(NumberCollection<T> pNC, EquationSystem pEq) {
        assert size() == pNC.size();
        NumberCollection<E> ret = new NumberCollection<E>();
        for(int x = 0; x < size(); x++)
            if(true)
            throw new NotDefinedException();
            // ret.add(pred(get(x), pEq) - pNC.get(x));
        return ret;
    }

    public <T extends E> EquationSystem polyReg(NumberCollection<T> pNC) {
        return polyReg(10, pNC);
    }
    public <T extends E> EquationSystem polyReg(int maxPower, NumberCollection<T> pNC) {
        throw new NotDefinedException();
    }
    public <T extends E> EquationSystem linReg(NumberCollection<T> pNC){
        /*
                double b1 = r(pArr1, pArr2) * stdev(pArr2) / stdev(pArr1);
                double b0 = mean(pArr2) - b1 * mean(pArr1);
                return new EquationSystem().add(new Equation().add("yhat","b0 + b1 * y"),
                                                new Equation().add("b0", "" + b0), 
                                                new Equation().add("b1", "" + b1));

        */
        return polyReg(1, enumeration());
    }


    public <T extends E> E r(NumberCollection<T> pNC){
        assert size() == pNC.size();
        if(true)
        throw new NotDefinedException();
        // E sigZxZy = 0D; //TODO: FIX "R"
        // for(int i = 0; i < size(); i++)
        //     sigZxZy += Z(get(i)) * pNC.Z(get(i));
        // return sigZxZy / (size() -1);
    }

    public <T extends E> E R2(NumberCollection<T> pNC){
        throw new NotDefinedException();
        //TODO: FIX THIS
        // return Math.pow(r(pNC), 2);
    }

    public <T extends E> E Z(E x) {
        throw new NotDefinedException();
        //TODO: FIX THIS
        // return (x - mean()) / stdev();
    }

    public NumberCollection<E> Z() {
        NumberCollection<E> ret = new NumberCollection<E>();
        for(E ele : this)
            ret.add(Z(ele));
        return ret;
    }

    public E avg() {
        return mean();
    }
    public E mean() {
        assert size() != 0 : "cannot take the average of an empty array!";
        // E sum = 0D;
        // for(E e : this)
        //     sum += e;
        // return sum / size();
        throw new NotDefinedException();
        //TODO: FIX THIS
    }

    public Collection<E> outliers() {
        E std = stdev(); //so it wont have to be called very time.
        E mean = mean(); //so it wont have to be called very time.
        NumberCollection<E> ret = new NumberCollection<E>();
        for(E e : this)
            if(e < mean - std * 3.0 || e > mean + std * 3.0) 
                ret.add(e);
        return ret;
    }

    public E stdev() { 
        // E sigYy_ = 0D;
        // E y_  = mean();
        // for(E y : this)
        //     sigYy_  += Math.pow(y - y_, 2);
        // return Math.sqrt(sigYy_  / (size() - 1));
        throw new NotDefinedException();
        //TODO: FIX THIS
    }

    public NumberCollection<E> fns() {
        assert size() > 0;
        NumberCollection<E> sorted = sort();
        int size = size(); //so it wont have to be called every time.
        throw new NotDefinedException();
        //TODO: FIX THIS
        // return new NumberCollection<E>(){{
        //     add(sorted.get(0));
        //     add(size % 4 == 0 ? (sorted.get(size / 4 - 1) + sorted.get(size / 4)) / 2 :  sorted.get(size / 4));
        //     add(size % 2 == 0 ? (sorted.get(size / 2 - 1) + sorted.get(size / 2)) / 2 : sorted.get(size / 2));
        //     add(size % 4 == 0 ? (sorted.get(size * 3 / 4 - 1) + sorted.get(size * 3 / 4)) / 2 : sorted.get(size * 3 / 4));
        // }};
        
    }

    public E iqr() {
        NumberCollection<E> fns = fns();
        // return fns.get(3) - fns.get(1);
        throw new NotDefinedException();
        //TODO: FIX THIS
    }

    public NumberCollection<E> sort() {
        NumberCollection<E> ret = copy();
        // bubble sort b/c screw it
        // TODO: BETTER SORT METHOD
        int x = -1;
        throw new NotDefinedException();
        //TODO: FIX THIS
        // E temp = 0;
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
        NumberCollection<E> fns = fns();
        E[] prl = new E[5];
        assert fns.size() == prl.length;

        for(int x = 0; x < fns.size(); x++)
            prl[x] = fns.get(x);

        E iqr = iqr();

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
        NumberCollection<E> fns = fns();
        System.out.printf("Min   : \n%nQ1    : \n%nMed   : \n%nQ2    : \n%nMax   : \n%nMean  : \n%nStdev : \n%n",
            fns.get(0), fns.get(1), fns.get(2), fns.get(3), fns.get(4), mean(), stdev());
    }

    public NumberCollection<E> enumeration(){ //should be integer, but its this so it works with other things.
        throw new NotDefinedException(); //TODO
        // return new NumberCollection<E>(){{
        //     for(int i = 0; i < size(); i++)
        //         add(i);
        // }};
    }

    public <T extends E> void graphe(NumberCollection<T> pNC) {
        //TODO: SEE IF THIS WORKS
        resid(pNC).graph(this); 
    }


    //TODO: MAKE IT SO YOU CAN GRAPH WITH NO PARAMETER, AND INSTEAD USE THE INDEX OF DIFFERENT ELEMENTS.
    public <T extends E> void graph(NumberCollection<T> pNC) { //the number line on the bottom can be different.
        Grapher grapher = new Grapher(this);
        grapher.graph();
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

    // @Override
    // public String toFullString(int idtLvl) {
    //     String ret = indent(idtLvl) + "NumberCollection:\n";

    //     int columns = 5; // amount of columns
    //     int spacing = 10; // amount of spaces. Note that there will be an additional one between columns
    //     String spaces = "";
    //     for(int i = 0; i < spacing; i++) spaces += " ";

    //     //TODO: Change this to ArrayList when this class is updated.
    //     ret += indent(idtLvl + 1) + "Array 1:";
    //     if(arr1 == null || arr1.length == 0){
    //         ret += "\n" + indent(idtLvl + 2) + "empty";
    //     } else {
    //         for(int i = 0; i < arr1.length; i++){
    //             if(i % columns == 0)
    //                 ret += "\n" + indent(idtLvl + 2);
    //             ret += (arr1[i] + spaces).substring(0, spacing) + " ";
    //         }
    //     }

    //     //TODO: Change this to ArrayList when this class is updated.
    //     ret += "\n" + indent(idtLvl + 1) + "Array 2:";
    //     if(arr2 == null || arr2.length == 0){
    //         ret += "\n" + indent(idtLvl + 2) + "empty";
    //     } else {
    //         for(int i = 0; i < arr2.length; i++){
    //             if(i % columns == 0)
    //                 ret += "\n" + indent(idtLvl + 2);
    //             ret += (arr2[i] + spaces).substring(0, spacing) + " ";
    //         }
    //     }
    //     return ret;

    // }

    @Override
    public NumberCollection copy(){
        return new NumberCollection(elements);
    }

    // @Override
    // public boolean equals(Object pObj){
    //     if(pObj == null || !(pObj instanceof  NumberCollection))
    //         return false;
    //     if(pObj == this)
    //         return true;
    //     double[] parr1 = ((NumberCollection)pObj).arr1();
    //     if(arr1.length != parr1.length)
    //         return false;
    //     double[] parr2 = ((NumberCollection)pObj).arr2();
    //     if(arr2.length != parr2.length)
    //         return false;
    //     for(int i = 0; i < arr1.length; i++)
    //         if(arr1[i] != parr1[i])
    //             return false;
    //     for(int i = 0; i < arr2.length; i++)
    //         if(arr2[i] != parr2[i])
    //             return false;
    //     return true;

    // }

}