package Math.Set;

import Math.MathObject;
import Math.Print;
import Math.Equation.EquationSystem;
import Math.Equation.Equation;
import Math.Equation.Expression;
import Math.Exception.InvalidArgsException;
import Math.Exception.NotDefinedException;
import Math.Display.Grapher;

import java.util.HashMap;
import java.util.ArrayList;

/**
 * The class that represents Sets in mathamatics. This one also includes a lot of different functions that are utilized
 * in Statistics.
 * 
 * @author Sam Westerman
 * @version 0.1
 */
public class Set implements MathObject{

    /**
     * The first, and primary, list for this class. When graphing, this is the "X" axis.
     */
    private double[] arr1; 

    /**
     * The second list for this class. When graphing, this is the "Y" axis.
     */
    private double[] arr2;

    /**
     * The EquationSystem that is used for this class. Most of the time, it is created via {@link #linReg()}.
     */
    private EquationSystem equationsys;

    /**
     * Calls {@link #Set(double[],double[]) another constructor} passing <code>pArr1</code> as both <code>pArr1</code>,
     * and <code>pArr2</code>.
     * @param pArr1     The array that {@link #arr1} and {@link #arr2} will be based on.
     */
    public Set(double[] pArr1) {
        this(pArr1, pArr1);
    }

    /**
     * Calls {@link #Set(double[],double[]) another constructor} passing {@link #arr1() pSet1.arr1() and pSet2.arr1()}.
     * @param pSet1     The Set that {@link #arr1} will be based on.
     * @param pSet2     The Set that {@link #arr2} will be based on.
     */
    public Set(Set pSet1, Set pSet2) {
        this(pSet1.arr1(),pSet2.arr1());
    }

    /**
     * Calls {@link #Set(double[],double[],EquationSystem) the main constructor} passing <code>pArr1</code> as
     * <code>pArr1</code>, <code>pArr2</code> as <code>pArr2</code>, and a {@link EquationSystem} based on their
     * {@link #linReg(double[],double[]) linear regression}.
     * @param pArr1     The array that {@link #arr1} will be based on.
     * @param pArr2     The array that {@link #arr2} will be based on.
     */
    public Set(double[] pArr1, double[] pArr2) {
        this(pArr1, pArr2, linReg(pArr1, pArr2));
    }

    /**
     * Instantiates {@link #arr1} as <code>pArr1</code>, {@link #arr2} as <code>pArr2</code>, and 
     * {@link #equationsys} as <code>pEq</code>.
     * @param pArr1     The array that {@link #arr1} will be based on.
     * @param pArr2     The array that {@link #arr2} will be based on.
     * @param pEq       The {@link EquationSystem} that {@link #equationsys} will be based off of.
     */
    public Set(double[] pArr1, double[] pArr2, EquationSystem pEq) {
        arr1 = pArr1;
        arr2 = pArr2;
        equationsys = pEq;
    }

    /**
     * Creates a Set based off the {@link EquationSystem} eq. Passes <code>-10, 10, 25</code> to 
     * {@link #Set(EquationSystem,double,double,double) another constructor} as the min, max, and cStep
     * (the cardinality of the Set).
     * @param pEq       The {@link EquationSystem} that the Set will be based off of.
     */
    public Set(EquationSystem pEq) {
        this(pEq, -10, 10, 25);
    }

    // &#060; is less than
    /**
     * Creates a Set based off the {@link EquationSystem} eq.
     * <br>The {@link #arr1} of the Set is defined by the code:
     * <code> int pos = 0;
     * for(double i = min; i &#060; max; i += (max - min) / cStep, pos++)
     *     arr1[pos] = i;</code>
     * <br>The {@link #arr2} of the Set is defined by the code:
     * <code> int pos = 0;
     * for(double i = min; i &#060; max; i += (max - min) / cStep, pos++)
     *     arr2[pos] = pred(i, equationsys);</code>
     * @param pEq       The {@link EquationSystem} that the Set will be based off of.
     * @param min       The starting value for {@link #arr1}.
     * @param max       The ending value for {@link #arr1}.
     * @param cStep     The step size for {@link #arr1}.
     * @see #pred(double,EquationSystem)
     */
    public Set(EquationSystem pEq, double min, double max, double cStep) {
        if(min >= max) {
            throw new InvalidArgsException("When defining a Set with an EquationSystem, the min (" + min +
                                           ") has to be smaller than the max (" + max + ")!");
        } if(cStep == 0) {
            throw new InvalidArgsException("When defining a Set with an EquationSystem, the cStep cannot be 0!");
        }

        equationsys = pEq;
        arr1 = new double[(int) Math.abs(Math.ceil(cStep))];
        arr2 = new double[(int) Math.abs(Math.ceil(cStep))];
        int pos = 0;
        for(double i = min; i < max; i += (max - min) / cStep, pos++) {
            if(pos >= arr1.length)
                break;
            arr1[pos] = i;
            arr2[pos] = pred(i, equationsys);
        }

    }

    /**
     * Returns {@link #arr1}.
     * @return This class's {@link #arr1}.
     */
    public double[] arr1() {
        return arr1;
    }

    /**
     * Returns {@link #arr2}.
     * @return This class's {@link #arr2}.
     */
    public double[] arr2() {
        return arr2;
    }

    /**
     * Returns {@link #equationsys}.
     * @return This class's {@link #equationsys}.
     */
    public EquationSystem equationsys() {
        return equationsys;
    }

    /**
     * Predicts the estimated value of the "yhat" using the the equation generated by {@link #linreg()}. Passes
     * <code>pVal</code> and {@link #linreg() a linear EquationSystem} to
     * {@link #pred(double,EquationSystem) another pred function}.
     * @param pVal       The value to set "y" to when evaluating the equation generated by {@link #linreg()}.
     * @return The result of the evaluated {@link EquationSystem}.
     */
    public double pred(double pVal) {
        return pred(pVal, linReg());
    }

    /**
     * Predicts the estimated value of the "yhat" using the the equation generated by
     * {@link #linreg(double,double) the linear EquationSystem defined by pArr1, and pArr2}. Passes <code>pVal</code> and
     * {@link #linreg(double,double) the linear EquationSystem defined by pArr1, and pArr2} to 
     * {@link #pred(double,EquationSystem) another pred function}.
     * @param pVal      The value to set "y" to when evaluating the equation generated by {@link #linreg()}.
     * @param pArr1     One of the arrays used to generate {@link #linreg() a linear EquationSystem}.
     * @param pArr2     One of the arrays used to generate {@link #linreg() a linear EquationSystem}.
     * @return The result of the evaluated {@link EquationSystem}.
     */
    public double pred(double pVal, double[] pArr1, double[] pArr2) {
        return pred(pVal, linReg(pArr1, pArr2));
    }

    /**
     * Predicts the estimated value of the "yhat" using the the equation generated by {@link #linreg()}. Passes
     * <code>pVal</code>, {@link #linreg() a linear EquationSystem}, and <code>toSolve</code> to
     * {@link #pred(double,EquationSystem,String) the main pred function}.
     * @param pVal      The value to set "y" to when evaluating the equation generated by {@link #linreg()}.
     * @param toSolve   The variable to solve for.
     * @return The result of the evaluated {@link EquationSystem}.
     */
    public double pred(double pVal, String toSolve) {
        return pred(pVal, linReg(), toSolve);
    }

    /**
     * Predicts the estimated value of the "yhat" using the the EquationSystem <code>pEq</code>. Passes
     * <code>pVal</code>, <code>pEq</code>, and "yhat" to
     * {@link #pred(double,EquationSystem,String) the main pred function}.
     * @param pVal      The value to set "y" to when evaluating the EquationSystem <code>pEq</code>.
     * @param pEq       The {@link EquationSystem} that will be evaluated with "yhat" being set to <code>pVal</code>.
     * @return The result of the evaluated {@link EquationSystem}.
     */
    public double pred(double pVal, EquationSystem pEq) {
        return pred(pVal, pEq, "yhat");
    }

    /**
     * Predicts the estimated value of the "yhat" using the the {@link EquationSystem} <code>pEq</code>. Passes a 
     * ArrayList of {@link Equation Equations} of length 1 to
     * {@link EquationSystem#eval(ArrayList,String) EquationSystem's eval function}, with the first element in the
     * Arraylist being instantiated by the following code:
     * <br><code>new ArrayList&#060;Equation&#062;() {{ add(new Equation(toSolve + " = " +pVal));}}</code>
     * @param pVal      The value to set "y" to when evaluating the {@link EquationSystem} <code>pEq</code>.
     * @param pEq       The {@link EquationSystem} that will be evaluated with "yhat" being set to <code>pVal</code>.
     * @param toSolve   The variable to solve for.
     * @return The result of the evaluated {@link EquationSystem}.
     */
    public static double pred(double pVal, EquationSystem pEq, String toSolve) {
        return pEq.eval(new ArrayList<Equation>() {{ add(new Equation(toSolve + " = " +pVal));}}, toSolve);
    }

    public Set es() {return resid();}
    public Set resid() {
        verifySize();
        EquationSystem lreg = linReg();
        double[] resid = new double[arr1.length];
        for(int x = 0; x < arr1.length; x++)
            resid[x] = pred(arr1[x], lreg, "y") - arr2[x];
        return new Set(resid, arr2);
    }


    public EquationSystem quadReg() {return quadReg(arr1, arr2); }
    public static EquationSystem quadReg(double[] pArr1, double[] pArr2) {
        throw new NotDefinedException();
    }

    public EquationSystem linReg() { return linReg(arr1, arr2); }
    public static EquationSystem linReg(double[] pArr1, double[] pArr2) {
        double b1 = r(pArr1, pArr2) * S(pArr2) / S(pArr1);
        double b0 = avg(pArr2) - b1 * avg(pArr1);
        return new EquationSystem().add(new Equation("yhat = b0 + b1 * y"),
                                      EquationSystem.genEq("b0", b0), 
                                      EquationSystem.genEq("b1", b1));

    }

    public double r() { return r(arr1, arr2); }
    public static double r(double[] pArr1, double[] pArr2) {
        verifySize(pArr1, pArr2);
        double sigZxZy = 0;
        for(int i = 0; i < pArr1.length; i++)
            sigZxZy += Z(pArr1[i], pArr1) * Z(pArr2[i], pArr2);
        return sigZxZy /( n(pArr1) - 1 ); }

    public double R2() { return R2(arr1, arr2); }
    public static double R2(double[] pArr1, double[] pArr2) { return Math.pow(r(pArr1, pArr2), 2);}

    public void verifySize() throws InvalidArgsException {verifySize(this);}
    public void verifySize(Set pSet) throws InvalidArgsException {verifySize(pSet.arr1(), pSet.arr2());}
    public static void verifySize(double[] pArr1, double[] pArr2) throws InvalidArgsException{
        if(pArr1.length != pArr2.length)
            throw new InvalidArgsException("The Arrays (" + arrToString(pArr1) + " and (" + arrToString(pArr2) + ") " + 
                "need to be of the same length!");
    }

    public int n() { return n(arr1); }
    public static int n(double[] pArr1) { return pArr1.length; }

    public double Z(double x) { return Z(x, arr1); }
    public static double Z(double x, double[] pArr1) { return (x - avg(pArr1)) / stdev(pArr1); }
    public double[] Z() { return Z(arr1);}
    public static double[] Z(double[] pArr1) {
        double[] ret = new double[pArr1.length];
        for(int x = 0; x < ret.length; x++)
            ret[x] = Z(pArr1[x], pArr1);
        return ret;
    }

    public double avg() { return avg(arr1);}
    public double mean() { return mean(arr1);}
    public static double mean(double[] pArr1) {return avg(pArr1);}    
    public static double avg(double[] pArr1) {
        assert pArr1.length > 0;
        double sum = 0;
        for(double i : pArr1)
            sum += i;
        return sum / pArr1.length;
    }

    public double[] outliers() {return outliers(arr1);}
    public static double[] outliers(double[] pArr1) {
        double std = stdev(pArr1);
        double mean = mean(pArr1);
        int count = 0;
        for(double d : pArr1)
            if(d < mean - std * 1.5 || d > mean + std * 1.5)
                count++;
        double[] ret = new double[count];
        count = 0;
        for(int x = 0; x < pArr1.length; x++) {
            double d = pArr1[x];
            if(d < mean - std * 1.5 || d > mean + std * 1.5) {
                ret[count] = d;
                count++;
            }
        }
        return ret;
    }

    public double stdev() { return stdev(arr1); }
    public double S() { return stdev(arr1); }
    public static double S(double[] pArr1) { return stdev(pArr1); }
    public static double stdev(double[] pArr1) { 
        double sigYy_ = 0;
        double y_  = avg(pArr1);
        for(double y : pArr1)
            sigYy_  += Math.pow(y - y_, 2);
        return Math.sqrt(sigYy_  / (n(pArr1) - 1));
    }

    public double[] fiveNumSum() { return fiveNumSum(arr1); }
    public static double[] fiveNumSum(double[] pArr1) {
        assert pArr1.length > 0;
        double[] sorted = sort(pArr1);
        int length = sorted.length;        
        double[] ret = {sorted[0], 0, 0, 0, sorted[length - 1]};
        ret[3] = length % 4 == 0 ? (sorted[length * 3 / 4 - 1] + sorted[length * 3 / 4]) / 2 : 
                 sorted[length * 3 / 4];
        ret[2] = length % 2 == 0 ? (sorted[length / 2 - 1] + sorted[length / 2]) / 2 : 
                 sorted[length / 2];
        ret[1] = length % 4 == 0 ? (sorted[length / 4 - 1] + sorted[length / 4]) / 2 : 
                 sorted[length / 4];
        return ret;
    } // NOT 100% THIS WORKS

    public double iqr() { return iqr(arr1); }
    public static double iqr(double[] pArr1) {
        double[] sum = fiveNumSum(pArr1);
        return sum[3] - sum[1];
    }

    public double[] sort() { return sort(arr1); }
    public static double[] sort(double[] pArr1) {
        double[] ret = new double[pArr1.length];
        for(int i = 0; i < pArr1.length; i++) ret[i] = pArr1[i];
        //bubble sort b/c screw it
        int x = 0;
        double temp = 0;
        while(x < ret.length - 1) {
            if(ret[x] > ret[x + 1]) {
                temp = ret[x + 1];
                ret[x + 1] = ret[x];
                ret[x] = temp;
                x = -1;
            }
            x++;
        }

        return ret;
    }

    public void printBarGraph() { printBarGraph(arr1); }
    public static void printBarGraph(double[] pArr1) {
        double[] fns = fiveNumSum(pArr1);
        double[] prl = new double[5];
        for(int x = 0; x < fns.length; x++)
            prl[x] = fns[x];

        double iqr = fns[3] - fns[1];

        prl[0] = fns[1] - iqr * 1.5;
        prl[4] = fns[3] + iqr * 1.5;

        boolean is1fence = true, is2fence = true;

        if (fns[0] > prl[0]) { //if the min value is higher than the fence
            prl[0] = fns[0];
            is1fence = false;
        }
        if (fns[4] < prl[4]) { //if the max value is lower than the fence
            prl[4] = fns[4];
            is2fence = false;
        }

        for(int x = 0; x < prl.length; x++ )
            prl[x] = Z(prl[x], pArr1) * 25 + 50;
        
        double upN =  (double)(is1fence ? (fns[1] - iqr * 1.5) : fns[0]);
        double mdN =  (double)(           (fns[2]            )         );
        double dnN =  (double)(is2fence ? (fns[3] + iqr * 1.5) : fns[4]);
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
       Print.print();
    }

    public void printFiveNumSum() { printFiveNumSum(arr1); }
    public static void printFiveNumSum(double[] pArr1) {

        double[] fns = fiveNumSum(pArr1);

        System.out.printf("Min   : %f%nQ1    : %f%nMed   : %f%nQ2    : %f%nMax   : %f%nMean  : %f%nStdev : %f%n",
            fns[0], fns[1], fns[2], fns[3], fns[4], avg(pArr1), stdev(pArr1));
    }
    public void printLinReg() { printLinReg(arr1, arr2); }
    public static void printLinReg(double[] pArr1, double[] pArr2) {
        System.out.println(linReg(pArr1, pArr2));
    }

    public void graphe() {(new Set(arr1, resid().arr1(), linReg(arr1, resid().arr1()))).graph();}

    public void graph() {
        Grapher grapher = new Grapher().add(this);
        grapher.graph();
    }

    public static String arrToString(double[] pArr1) {
        String ret = "(";
        for(double d : pArr1)
            ret += d + ", ";
        return ret.substring(0, ret.length() - 2) + ")[" + pArr1.length + "]";
    }

    @Override
    public String toString() {
        if(arr1.length == 0 && arr2.length == 0 )
            return "Empty Set";
        String ret = "Set";
        if(arr1.length != 0)
            ret += " Arr1: " + arrToString(arr1) + (arr2.length != 0 ? ";" : "");

        if(arr2.length != 0)
            ret += " Arr2: " + arrToString(arr2);
        return ret;
    }

    @Override
    public String toFancyString() {
        throw new NotDefinedException();
    }

    @Override
    public String toFullString() {
        throw new NotDefinedException();
    }

}