package Math.Set;

import Math.MathObject;
import Math.Print;
import static Math.Declare.*;
import Math.Exception.NotDefinedException;
import Math.Equation.EquationSystem;

import java.util.ArrayList;

/**
 * The class that represents Groups of numbers in general.
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
public class Group<E extends Double> extends Collection<E> implements MathObject {

    protected ArrayList<E> elements;

    public Group(){
        super();
    }
    public Group(ArrayList<E> pEle){
        super(pEle);
    }
    public Group(Group pGroup){
        super(pGroup);
    }
    public Group(E[] pEle){
        super(pEle);
    }

    public Group(EquationSystem pEqSys){
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