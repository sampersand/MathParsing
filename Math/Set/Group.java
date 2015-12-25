package Math.Set;

import Math.MathObject;
import Math.Print;
import static Math.Declare.*;
import Math.Exception.NotDefinedException;
import Math.Equation.EquationSystem;

import java.util.ArrayList;
import java.util.Iterator;

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
 * @version 0.71
 * @since 0.7
 */
public class Group<E extends Double> extends java.util.AbstractList<E> implements MathObject {

    protected ArrayList<E> elements;

    public Group(){
        this(new ArrayList<E>());
    }
    public Group(ArrayList<E> pEle){
        assert pEle != null : "Cannot have a null parameter elements!";
        elements = new ArrayList<E>(){{addAll(pEle);}};
    }
    public Group(Group pGroup){
        this(pGroup.elements()); //no assert here, ;(
    }
    public Group(E[] pEle){
        assert pEle != null : "cannot have a null parameter elements";
        elements = new ArrayList<E>(){{
            for(E n : pEle)
                add(n);
        }};
    }
    public Group(EquationSystem pEqSys){
        throw new NotDefinedException();
    }
    public ArrayList<E> elements(){ return elements; }
    public int size(){ return elements.size();}
    public E get(int pPos){ return elements.get(pPos);}
    public E set(int pPos, E pEle){
        E ret = get(pPos);
        elements.set(pPos, pEle);
        return ret;
    }
    public boolean add(E pEle){ 
        elements.add(pEle);
        return true; // for some bizarre reason, ArrayList does this too.
    }
    public <T extends E> Group<E> add(Group<T> pGroup){
        Group<E> ret = copy();
        for(T e : pGroup) //allows for overriding it in MathSet.
            ret.add(e);
        return ret;
    }

    public E pop(){return pop(size() - 1);}
    public E pop(int pPos){
        E ret = elements.get(pPos);
        elements.remove(pPos);
        return ret;
    }
    public E mean(){
        double sum = 0D;
        for(E e : elements)
            sum = sum + e;
        return (E)new Double(sum / size());
    }
    public boolean contains(Object pObj){
        return elements.contains(pObj);
    }

    // THIS ∪ PRGROUP
    public <T extends E> Group<E> union(Group<T> pGroup){
        return copy().add(pGroup);
    }

    // THIS ∩ PGROUP
    public <T extends E> Group<E> intersect(Group<T> pGroup){
        Group<E> ret = new Group<E>();
        for(E d : this)
            if(pGroup.contains(d))
                ret.add(d);
        return ret;
    }


    public boolean isUnique(){
        Group<E> ms = copy();
        while(ms.size() > 0)
            if(ms.elements().contains(ms.pop()))
                return false;
        return true;
    }

    @Override
    public String toString(){
        return "" + elements;
    }

    @Override
    public String toFancyString(int idtLvl) {
        throw new NotDefinedException();
    }

    @Override
    public String toFullString(int idtLvl) {
        throw new NotDefinedException();
    }

    @Override
    public Group copy(){
        return new Group(elements);
    }

    @Override
    public boolean equals(Object pObj){
        throw new NotDefinedException();
    }




    public Iterator<E> iterator() {
        return (Iterator<E>) new GroupIter();
    }
    public class GroupIter implements Iterator<E> {
        private int i = 0;

        public boolean hasNext() {
            return i < Group.this.size();
        }
        public E next() {
            assert hasNext() : "there should be a next one.";
            return Group.this.get(i++);
        }
    }

}