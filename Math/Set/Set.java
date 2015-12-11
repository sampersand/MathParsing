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
public class Set implements MathObject {

    /**
     * The first, and primary, list for this class. Whenever a function is called without parameters, this array will
     * be the one passed. When graphing, this is the "X" axis.
     */
    protected double[] arr1;

    /**
     * The second list for this class. When graphing, this is the "Y" axis.
     */
    protected double[] arr2;

    /**
     * The EquationSystem that is used for this class. Most of the time, it is created via {@link #linReg()}.
     */
    protected EquationSystem equationsys;

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
     * Gets {@link #arr1}.
     * @return This class's {@link #arr1}.
     */
    public double[] arr1() {
        return arr1;
    }

    /**
     * Gets {@link #arr2}.
     * @return This class's {@link #arr2}.
     */
    public double[] arr2() {
        return arr2;
    }

    /**
     * Gets {@link #equationsys}.
     * @return This class's {@link #equationsys}.
     */
    public EquationSystem equationsys() {
        return equationsys;
    }

    /**
     * Calculates and returns the estimated value of the "yhat" using the the equation generated by {@link #linReg()}.
     * Passes <code>pVal</code> and {@link #linReg() a linear equation} to
     * {@link #pred(double,EquationSystem) another pred function}.
     * @param pVal       The value to set "y" to when evaluating the equation generated by {@link #linReg()}.
     * @return The result of the evaluated {@link EquationSystem}.
     */
    public double pred(double pVal) {
        return pred(pVal, linReg());
    }

    /**
     * Calculates and returns the estimated value of the "yhat" using the the equation generated by 
     * {@link #linReg(double[],double[]) the linear equation defined by pArr1, and pArr2}. Passes <code>pVal</code> and
     * {@link #linReg(double[],double[]) the linear equation defined by pArr1, and pArr2} to 
     * {@link #pred(double,EquationSystem) another pred function}.
     * @param pVal      The value to set "y" to when evaluating the equation generated by {@link #linReg()}.
     * @param pArr1     One of the arrays used to generate {@link #linReg() a linear equation}.
     * @param pArr2     One of the arrays used to generate {@link #linReg() a linear equation}.
     * @return The result of the evaluated {@link EquationSystem}.
     */
    public double pred(double pVal, double[] pArr1, double[] pArr2) {
        return pred(pVal, linReg(pArr1, pArr2));
    }

    /**
     * Calculates and returns the estimated value of the "yhat" using the the equation generated by {@link #linReg()}.
     * Passes <code>pVal</code>, {@link #linReg() a linear equation}, and <code>toSolve</code> to
     * {@link #pred(double,EquationSystem,String) the main pred function}.
     * @param pVal      The value to set "y" to when evaluating the equation generated by {@link #linReg()}.
     * @param toSolve   The variable to solve for.
     * @return The result of the evaluated {@link EquationSystem}.
     */
    public double pred(double pVal, String toSolve) {
        return pred(pVal, linReg(), toSolve);
    }

    /**
     * Calculates and returns the estimated value of the "yhat" using the the EquationSystem <code>pEq</code>. Passes
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
     * Calculates and returns the estimated value of the "yhat" using the the {@link EquationSystem} <code>pEq</code>.
     * Passes a ArrayList of {@link Equation Equations} of length 1 to
     * {@link EquationSystem#eval(ArrayList,String) EquationSystem's eval function}, with the first element in the
     * Arraylist being instantiated by the following code:
     * <br><code>new ArrayList&#060;Equation&#062;() {{ add(new Equation(toSolve, pVal));}}</code>
     * @param pVal      The value to set "y" to when evaluating the {@link EquationSystem} <code>pEq</code>.
     * @param pEq       The {@link EquationSystem} that will be evaluated with "yhat" being set to <code>pVal</code>.
     * @param toSolve   The variable to solve for.
     * @return The result of the evaluated {@link EquationSystem}.
     */
    public static double pred(double pVal, EquationSystem pEq, String toSolve) {
        return pEq.eval(new ArrayList<Equation>() {{ add(new Equation().add(toSolve, "" + pVal));}}, toSolve);
    }

    /**
     * Alias for {@link #resid()}.
     * @return the {@link #resid()} residuals of {@link #arr1} and {@link #arr2}.
     * @see <a href="https://en.wikipedia.org/wiki/Errors_and_residuals">Residuals</a>
     */
    public Set es() {
        return resid();
    }
    /**
     * Calculates and returns the residuals of {@link #arr1} and {@link #arr2}.
     * @return the {@link #resid()} residuals of {@link #arr1} and {@link #arr2}.
     * @see <a href="https://en.wikipedia.org/wiki/Errors_and_residuals">Residuals</a>
     */
    public Set resid() {
        return resid(arr1, arr2, linReg());
    }
    /**
     * Calculates and returns the residuals of <code>pArr1</code> and <code>pArr2</code>.
     * @param pArr1     The "x" array that will be used to generate the "y" array of the residule Set. Also,
     *                  The "x" array for the residule Set.
     * @param pArr2     The "y" array that will be used to generate the "y" array of the residule Set.
     * @param pEq       The Equation that the residule set will be based off of.
     * @return a Set created by the residuals of <code>pArr1</code> and <code>pArr2</code>.
     * @see <a href="https://en.wikipedia.org/wiki/Errors_and_residuals">Residuals</a>
     */
    public static Set resid(double[] pArr1, double[] pArr2, EquationSystem pEq) {
        verifySize(pArr1, pArr2);
        double[] resid = new double[pArr1.length];
        for(int x = 0; x < pArr1.length; x++)
            resid[x] = pred(pArr1[x], pEq, "y") - pArr2[x];
        return new Set(resid, pArr2);
    }

    /**
     * Calculates and returns a {@link EquationSystem polynomial equation} based off of {@link #arr1} and
     * {@link #arr2} by passing it to {@link #polyReg(double[],double[])}.
     * NOTE: This isn't implemented yet, and will throw a {@link NotDefinedException}.
     * @return A {@link EquationSystem polynomial equation} generated by {@link #arr1}, and {@link #arr2}
     * @see <a href="https://en.wikipedia.org/wiki/Polynomial_regression">Polynomial Regression</a>
     */
    public EquationSystem polyReg() {
        return polyReg(arr1, arr2);
    }

    /**
     * Calculates and returns a {@link EquationSystem polynomial equation} based off of {@link #arr1} and
     * {@link #arr2} by passing it to {@link #polyReg(double[],double[])}.
     * NOTE: This isn't implemented yet, and will throw a {@link NotDefinedException}.
     * @param pArr1     The "x" array that will be used to generate the {@link EquationSystem polynomial equation}. 
     * @param pArr2     The "y" array that will be used to generate the {@link EquationSystem polynomial equation}.
     * @return A {@link EquationSystem polynomial equation} generated by {@link #arr1}, and {@link #arr2}
     * @see <a href="https://en.wikipedia.org/wiki/Polynomial_regression">Polynomial Regression</a>
     */
    public static EquationSystem polyReg(double[] pArr1, double[] pArr2) {
        throw new NotDefinedException();
    }

    /**
     * Calculates and returns a {@link EquationSystem linear equation} based off this classes {@link #arr1} and {@link #arr2}.
     * Passes {@link #arr1} and {@link #arr2} to {@link #linReg(double[],double[]) the main linReg function)}.
     * @return A {@link EquationSystem linear equation} that is the "best fit line" for {@link #arr1} and {@link #arr2}.
     * @see <a href="https://en.wikipedia.org/wiki/Linear_regression">Linear Regression</a>
     */
    public EquationSystem linReg() {
        return linReg(arr1, arr2);
    }

    /**
     * Calculates and returns a {@link EquationSystem linear equation} based off <code>pArr1</code> and
     * <code>pArr2</code>.
     * @param pArr1         The "x" array that will be used to generate the {@link EquationSystem linear equation}. 
     * @param pArr2         The "y" array that will be used to generate the {@link EquationSystem linear equation}.
     * @return A {@link EquationSystem linear equation} that is the "best fit line" for <code>pArr1</code> and
     *         <code>pArr2</code>.
     * @see <a href="https://en.wikipedia.org/wiki/Linear_regression">Linear Regression</a>
     */
    public static EquationSystem linReg(double[] pArr1, double[] pArr2) {
        double b1 = r(pArr1, pArr2) * S(pArr2) / S(pArr1);
        double b0 = avg(pArr2) - b1 * avg(pArr1);
        return new EquationSystem().add(new Equation().add("yhat","b0 + b1 * y"),
                                      EquationSystem.genEq("b0", b0), 
                                      EquationSystem.genEq("b1", b1));

    }

    /**
     * Calculates and returns the correlation coeffecient for {@link #arr1} and {@link #arr2} by passing them to the
     * {@link #r(double[],double[]) main r funciton}.
     * @return The correlation coeffecient for {@link #arr1} and {@link #arr2}.
     * @see <a href="https://en.wikipedia.org/wiki/Correlation_coefficient">Correlation coeffecient</a>
     */
    public double r() {
        return r(arr1, arr2);
    }

    /**
     * Calculates and returns the Correlation coeffecient for <code>pArr1</code> and <code>pArr2</code>.
     * <br>Note that the cardinality of <code>pArr1</code> and <code>pArr2</code> has to be equal.
     * @param pArr1         The first array that the Correlation coeffecient will be calculated from.
     * @param pArr2         The second array that the Correlation coeffecient will be calculated from.
     * @return The Correlation coeffecient for {@link #arr1} and {@link #arr2}.
     * @see <a href="https://en.wikipedia.org/wiki/Correlation_coefficient">Correlation coeffecient</a>
     */

    public static double r(double[] pArr1, double[] pArr2) {
        verifySize(pArr1, pArr2);
        double sigZxZy = 0;
        for(int i = 0; i < pArr1.length; i++)
            sigZxZy += Z(pArr1[i], pArr1) * Z(pArr2[i], pArr2);
        return sigZxZy /( n(pArr1) - 1 ); }

    /**
     * Calculates and returns the Coefficient of determination of {@link #arr1} and {@link #arr2}. Passes {@link #arr1}
     * and {@link #arr2} to {@link #R2(double[],double[]) the main R2 function}.
     * @return the Coefficient of determination of {@link #arr1} and {@link #arr2}.
     * @see <a href="https://en.wikipedia.org/wiki/Coefficient_of_determination">Coefficient of determination</a>
     */
    public double R2() {
        return R2(arr1, arr2);
    }

    /**
     * Calculates and returns the Coefficient of determination of <code>pArr1</code> and <code>pArr2</code>.
     * @param pArr1         The first array that will be used to find the Coefficient of determination.
     * @param pArr2         The second array that will be used to find the Coefficient of determination.
     * @return the Coefficient of determination of <code>pArr1</code> and <code>pArr2</code>.
     * @see <a href="https://en.wikipedia.org/wiki/Coefficient_of_determination">Coefficient of determination</a>
     */
    public static double R2(double[] pArr1, double[] pArr2) {
        return Math.pow(r(pArr1, pArr2), 2);
    }

    /**
     * Verifies that the cardinality of {@link #arr1} and {@link #arr2} is the same. Passes <code>this</code> to
     * {@link #verifySize(Set) another verifySize function}.
     * @throws InvalidArgsException Thrown if the cardinality of {@link #arr1} and {@link #arr2} aren't equal.
     * @see <a href="https://en.wikipedia.org/wiki/Cardinality">Cardinality</a>
     */
    public void verifySize() throws InvalidArgsException {
        verifySize(this);
    }

    /**
     * Verifies that the cardinality of {@link #arr1 pSet.arr1} and {@link #arr2 pSet.arr1} is the same.
     * Passes {@link #arr1() pSet.arr1()} and {@link #arr2() pSet.arr2()} to the
     * {@link #verifySize(double[],double[]) another verifySize function}.
     * @param pSet      The Set whose {@link #arr1} and {@link #arr2} will be be verrified that their cardinality is the
     *                  same.
     * @throws InvalidArgsException Thrown if the cardinality of {@link #arr1() pSet.arr1()} and
     *                              {@link #arr2() pSet.arr2()} aren't equal.
     * @see <a href="https://en.wikipedia.org/wiki/Cardinality">Cardinality</a>
     */
    public void verifySize(Set pSet) throws InvalidArgsException {
        verifySize(pSet.arr1(), pSet.arr2());
    }

    /**
     * Verifies that the cardinality of <code>pArr1</code> and <code>pArr2</code> is the same. This means that it checks
     * and makes sure that their <code>length</code>s are the same, and if they aren't, it throws a
     * {@link Math.Exception.MathException}.
     * @param pArr1         The first array; <code>pArr1.length</code> will be compared against
     *                      <code>pArr2.length</code>.
     * @param pArr2         The second array; <code>pArr1.length</code> will be compared against
     *                      <code>pArr2.length</code>.
     * @throws InvalidArgsException Thrown if the cardinality of <code>pArr1</code> and <code>pArr2</code> aren't equal.
     * @see <a href="https://en.wikipedia.org/wiki/Cardinality">Cardinality</a>
     */
    public static void verifySize(double[] pArr1, double[] pArr2) throws InvalidArgsException {
        if(pArr1.length != pArr2.length)
            throw new InvalidArgsException("The Arrays (" + arrToString(pArr1) + " and (" + arrToString(pArr2) + ") " + 
                "need to be of the same length!");
    }

    /**
     * Calculates and returns the length of {@link #arr1}. Effectively just an alias for <code>arr1.length</code>.
     * @return The length of {@link #arr1}.
     */
    public int n() {
        return n(arr1);
    }

    /**
     * Calculates and returns the length of <code>pArr1</code>. Effectively just an alias for <code>pArr1.length</code>.
     * @param pArr1     The array whose cardinality will be returned.
     * @return The length of <code>pArr1</code>.
     */
    public static int n(double[] pArr1) {
        return pArr1.length;
    }

    /**
     * Calculates and returns the "Z - Score" of <code>x</code> by passing <code>x</code> and {@link #arr1} to
     * {@link #Z(double,double[]) another Z - Score function}.
     * @param x         The number to find the "Z - Score" of.
     * @return The "Z - Score" of <code>x</code>.
     * @see <a href="https://en.wikipedia.org/wiki/Standard_score">Z - Score</a>
     */
    public double Z(double x) {
        return Z(x, arr1);
    }

    /**
     * Calculates and returns the "Z - Score" of <code>x</code> and <code>pArr1</code>.
     * @param x         The number to find the "Z - Score" of.
     * @param pArr1     The array which the {@link #avg(double[]) Average} and
     *                  {@link #stdev(double[]) Standard Deviation} will be based off of.
     * @return The "Z - Score" of <code>x</code>.
     * @see <a href="https://en.wikipedia.org/wiki/Standard_score">Z - Score</a>
     */
    public static double Z(double x, double[] pArr1) {
        return (x - avg(pArr1)) / stdev(pArr1);
    }

    /**
     * Generates and returns a "normalized" version of {@link #arr1} - it replaces each element with their
     * {@link #Z(double,double[]) Z - Score}. Passes {@link #arr1} to {@link #Z(double[]) another Z - Score function}.
     * @return A "normalized" version of {@link #arr1}.
     * @see <a href="https://en.wikipedia.org/wiki/Standard_score">Z - Score</a>
     */
    public double[] Z() {
        return Z(arr1);
    }

    /**
     * Generates and returns a "normalized" version of {@link #arr1} - it replaces each element with their
     * {@link #Z(double,double[]) Z - Score}.
     * @param pArr1     The array to be "normalized".
     * @return A "normalized" version of {@link #arr1}.
     * @see <a href="https://en.wikipedia.org/wiki/Standard_score">Z - Score</a>
     */
    public static double[] Z(double[] pArr1) {
        double[] ret = new double[pArr1.length];
        for(int x = 0; x < ret.length; x++)
            ret[x] = Z(pArr1[x], pArr1);
        return ret;
    }

    /**
     * Alias for {@link #avg()}.
     * @return The average (mean) of {@link #arr1}.
     */
    public double mean() {
        return mean(arr1);
    }

    /**
     * Alias for {@link #avg(double[])}.
     * @param pArr1         The Array whose average (mean) will be returned
     * @return The average (mean) of <code>pArr1</code>.
     */
    public static double mean(double[] pArr1) {
        return avg(pArr1);
    }

    /**
     * Calculates and returns the average (mean) of {@link #arr1}.
     * @return The average of {@link #arr1}.
     */
    public double avg() {
        return avg(arr1);
    }

    /**
     * Calculates and returns the average (mean) value of <code>pArr1</code>.
     * @param pArr1         The array whose average (mean) will be returned.
     * @return The average (mean) of <code>pArr1</code>.
     * @throws InvalidArgsException Thrown if <code>pArr1.length == 0</code>.
     */
    public static double avg(double[] pArr1) {
        if(pArr1.length == 0)
            throw new InvalidArgsException("pArr1's length is 0! You can't take an average of an empty array!");
        double sum = 0;
        for(double i : pArr1)
            sum += i;
        return sum / pArr1.length;
    }

    /**
     * Calculates and returns the outliers of {@link #arr1}. Just passes {@link #arr1} to
     * {@link #outliers(double[]) the main outliers function}.
     * @return A list of outliers of {@link #arr1}.
     * @see <a href="https://en.wikipedia.org/wiki/Outlier">Outliers</a>
     */
    public double[] outliers() {
        return outliers(arr1);
    }

    /**
     * Calculates and returns the outliers of <code>pArr1</code>. Anything that is outside 3
     * {@link #stdev Standard Deviations} is considered an outlier.
     * @param pArr1     The array that will be checked for outliers.
     * @return A list of outliers of {@link #arr1}.
     * @see <a href="https://en.wikipedia.org/wiki/Outlier">Outliers</a>
     */
    public static double[] outliers(double[] pArr1) {
        double std = stdev(pArr1);
        double mean = mean(pArr1);
        int count = 0;
        for(double d : pArr1)
            if(d < mean - std * 3.0 || d > mean + std * 3.0)
                count++;
        double[] ret = new double[count];
        count = 0;
        for(int x = 0; x < pArr1.length; x++) {
            double d = pArr1[x];
            if(d < mean - std * 3.0 || d > mean + std * 3.0) {
                ret[count] = d;
                count++;
            }
        }
        return ret;
    }

    /**
     * Alias for {@link #stdev()}.
     * @return The Standard Deviation of {@link #arr1}.
     * @see <a href="https://en.wikipedia.org/wiki/Standard_deviation">Standard Deviation</a>
     */
    public double S() {
        return stdev(arr1);
    }

    /**
     * Alias for {@link #stdev(double[])}.
     * @param pArr1     The array whose Standard Deviation will be returned.
     * @return The Standard Deviation of <code>pArr1</code>.
     * @see <a href="https://en.wikipedia.org/wiki/Standard_deviation">Standard Deviation</a>
     */
    public static double S(double[] pArr1) {
        return stdev(pArr1);
    }

    /**
     * Calculates and returns the {@link #stdev(double[]) Standard Deviation} for {@link #arr1}.
     * @return The Standard Deviation for {@link #arr1}.
     * @see <a href="https://en.wikipedia.org/wiki/Standard_deviation">Standard Deviation</a>
     */
    public double stdev() {
        return stdev(arr1);
    }

    /**
     * Calculates and returns the Standard Deviation of <code>pArr1</code>.
     * @param pArr1     The array whose Standard Deviation will be returned.
     * @return The Standard Deviation of <code>pArr1</code>.
     * @see <a href="https://en.wikipedia.org/wiki/Standard_deviation">Standard Deviation</a>
     */
    public static double stdev(double[] pArr1) { 
        double sigYy_ = 0;
        double y_  = avg(pArr1);
        for(double y : pArr1)
            sigYy_  += Math.pow(y - y_, 2);
        return Math.sqrt(sigYy_  / (n(pArr1) - 1));
    }

    /**
     * Calculates and returns the Five Number Summary of {@link #arr1}. Just passes {@link #arr1} to
     * {@link #fiveNumSum(double[]) the main fiveNumSum function}
     * @return An array of length 5 containing the Five Number Summary of {@link #arr1}.
     * @see <a href="https://en.wikipedia.org/wiki/Five-number_summary">Five Number Summar</a>
     */
    public double[] fiveNumSum() {
        return fiveNumSum(arr1);
    }

    /**
     * Calculates and returns the Five Number Summary of <code>pArr1</code>. Note that I'm not 100% that this works,
     * but I'm fairly confident that it does.
     * @param pArr1     The array which the Five Number Summary will be based off of.
     * @return An array of length 5 containing the Five Number Summary of <code>pArr1</code>.
     * @see <a href="https://en.wikipedia.org/wiki/Five-number_summary">Five Number Summar</a>
     */
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
    }

    /**
     * Calculates and Returns the IQR (Interquartile Range) of {@link #arr1}. Just passes {@link #arr1} to
     * {@link #iqr(double[]) the main iqr function}.
     * @return The IQR of {@link #arr1}.
     * @see <a href="https://en.wikipedia.org/wiki/Interquartile_range">Interquartile Range</a>
     */
    public double iqr() {
        return iqr(arr1);
    }

    /**
     * Calculates and Returns the IQR (Interquartile Range) of <code>pArr1</code>.
     * @param pArr1     The array whose IQR will be returned.
     * @return The IQR of <code>pArr1</code>.
     * @see <a href="https://en.wikipedia.org/wiki/Interquartile_range">Interquartile Range</a>
     */
    public static double iqr(double[] pArr1) {
        double[] sum = fiveNumSum(pArr1);
        return sum[3] - sum[1];
    }

    /**
     * Generates and returns a sorted version of {@link #arr1} by using Bubble Sort. Just passes {@link #arr1} to
     *{@link #sort(double[]) the main sort function}.
     * @return A sorted version of {@link #arr1}.
     * @see <a href="https://en.wikipedia.org/wiki/Bubble_sort">Bubble Sort</a>
     */
    public double[] sort() {
        return sort(arr1);
    }

    /**
     * Generates and returns a sorted version of {@link #arr1} by using Bubble Sort. Please not that since Bubble Sort
     * is used, large arrays are going to take a particularly long time. 
     * @param pArr1     The array to sort.
     * @return A sorted version of <code>pArr1</code>.
     * @see <a href="https://en.wikipedia.org/wiki/Bubble_sort">Bubble Sort</a>
     */
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

    /**
     * Prints a box plot of {@link #arr1} to console. Just passes {@link #arr1} to
     * {@link #printBoxPlot(double[]) the main printBoxPlot function}.
     * @see <a href="https://en.wikipedia.org/wiki/Box_plot">Box Plot</a>
     */
    public void printBoxPlot() {
        printBoxPlot(arr1);
    }

    /**
     * Prints a box plot of <code>pArr1</code> to console. Note: This isn't working very well, and will be modified 
     * later when it becomes more relevant. 
     * @param pArr1     The array whose box plot will be printed to console.
     * @see <a href="https://en.wikipedia.org/wiki/Box_plot">Box Plot</a>
     */
    public static void printBoxPlot(double[] pArr1) {
        //make sure to update the javadoc to reflect fixing this.
        double[] fns = fiveNumSum(pArr1);
        double[] prl = new double[5];
        for(int x = 0; x < fns.length; x++)
            prl[x] = fns[x];

        double iqr = fns[3] - fns[1];

        prl[0] = fns[1] - iqr * 3.0;
        prl[4] = fns[3] + iqr * 3.0;

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
        
        double upN =  (double)(is1fence ? (fns[1] - iqr * 3.0) : fns[0]);
        double mdN =  (double)(           (fns[2]            )         );
        double dnN =  (double)(is2fence ? (fns[3] + iqr * 3.0) : fns[4]);
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

    /**
     * Prints a five number summary of {@link #arr1} to console. Just passes {@link #arr1} to
     * {@link #printFiveNumSum(double[]) the main printFiveNumSum function}.
     * @see #fiveNumSum(double[])
     * @see <a href="https://en.wikipedia.org/wiki/Five-number_summary">Five Number Summar</a>
     */
    public void printFiveNumSum() {
        printFiveNumSum(arr1);
    }

    /**
     * Prints a five number summary of <code>pArr1</code> to console. 
     * @param pArr1     The array whose five number summary will be printed to console.
     * @see #fiveNumSum(double[])
     * @see <a href="https://en.wikipedia.org/wiki/Five-number_summary">Five Number Summar</a>
     */
    public static void printFiveNumSum(double[] pArr1) {
        double[] fns = fiveNumSum(pArr1);
        //I replaced all %f with \n. This might make something not work right, i'm not sure.
        System.out.printf("Min   : \n%nQ1    : \n%nMed   : \n%nQ2    : \n%nMax   : \n%nMean  : \n%nStdev : \n%n",
            fns[0], fns[1], fns[2], fns[3], fns[4], avg(pArr1), stdev(pArr1));
    }

    /**
     * Prints out the linear regression of {@link #arr1} and {@link #arr2}. Just passes {@link #arr1} and {@link #arr2}
     * to {@link #printLinReg(double[],double[]) the main printLinReg function}.
     * <br> Note: This is depreciated and will be removed soon. It can be replaced by
     * <code>System.out.println(linReg())</code>.
     * @see #linReg(double[],double[])
     * @see <a href="https://en.wikipedia.org/wiki/Linear_regression">Linear Regression</a>
     * @deprecated 
     */
    public void printLinReg() {
        printLinReg(arr1, arr2);
    }

    /**
     * Prints out the linear regression of <code>pArr1</code> and <code>pArr2</code>.
     * <br> Note: This is depreciated and will be removed soon. It can be replaced by
     * <code>System.out.println(linReg(pArr1,pArr2))</code>.
     * @param pArr1         The "x" array that will be used to generate the 
     *                      {@link #linReg(double[],double[]) linear equation}. 
     * @param pArr2         The "y" array that will be used to generate the
     *                      {@link #linReg(double[],double[]) linear equation}. 
     * @see #linReg(double[],double[])
     * @see <a href="https://en.wikipedia.org/wiki/Linear_regression">Linear Regression</a>
     * @deprecated 
     */
    public static void printLinReg(double[] pArr1, double[] pArr2) {
        System.out.println(linReg(pArr1, pArr2));
    }

    /**
     * Graphs the residules of this set.
     * @see #resid()
     * @see <a href="https://en.wikipedia.org/wiki/Errors_and_residuals">Residuals</a>
     */
    public void graphe() {
        (new Set(arr1, resid().arr1(), linReg(arr1, resid().arr1()))).graph();
    }

    /**
     * Graphs this set using {@link Math.Display.Grapher}.
     * @see Math.Display
     */
    public void graph() {
        Grapher grapher = new Grapher().add(this);
        grapher.graph();
    }

    /**
     * Returns a fancy String representation of <code>pArr1</code>.
     * @param pArr1     The array that will be made into a fancy string.
     * @return A fancy String representation of <code>pArr1</code>.
     */
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