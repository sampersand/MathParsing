package Math.Set;

import Math.MathObject;
import Math.Print;
import static Math.Declare.*;
import Math.Exception.NotDefinedException;
import Math.Equation.EquationSystem;

import java.util.ArrayList;

/**
 * The class that represents collections of numbers in general.
 * 
 * TODO: IMPLEMENT:
 * <ul>pred</ul>
 * <ul>resid</ul>
 * <ul>polyReg</ul>
 * <ul>linREg</ul>
 * <ul>r</ul>
 * <ul>R^2</ul>
 * <ul>n</ul>
 * <ul>Z</ul>
 * <ul>avg</ul>
 * <ul>outlier</ul>
 * <ul>stdev</ul>
 * <ul>fivenumsu</ul>
 * <ul>iqr</ul>
 * <ul>sort</ul>
 * <ul>graph</ul>
 *
 * @author Sam Westerman
 * @version 0.72
 * @since 0.7
 */
public class NumberCollection<E extends Double> extends Collection<E> implements MathObject {


    //TODO: FIGURE OUT THIS AND MATHSET, and how they relate
    public NumberCollection(){
        super();
    }
    public NumberCollection(ArrayList<E> pEle){
        super(pEle);
    }
    public NumberCollection(NumberCollection pNumberCollection){
        super(pNumberCollection);
    }
    public NumberCollection(E... pEle){
        super(pEle);
    }

    public NumberCollection(EquationSystem pEqSys){
        // TODO: DEFINE
        throw new NotDefinedException();
    }

    public E mean(){
        double sum = 0D;
        for(E e : elements)
            sum = sum + e;
        return (E)new Double(sum / size());
    }

}