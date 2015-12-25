package Math.Set;

import Math.MathObject;
import Math.Print;
import static Math.Declare.*;
import Math.Exception.NotDefinedException;

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
 * @version 0.7
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
    public E pop(){return pop(size() - 1);}
    public E pop(int pPos){
        E ret = elements.get(pPos);
        elements.remove(pPos);
        return ret;
    }
    public E mean(){
        double sum = 0;
        for(E d : elements)
            sum = sum + d;
        return (E)new Double(sum / size());
    }

    public Group union(Group pGroup){
        Group ret = new Group();
        for(E d : this);
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




    public Iterator<Double> iterator() {
        return (Iterator<Double>) new GroupIter();
    }
    // private class Itr implements Iterator<E> {
    //     int cursor;       // index of next element to return
    //     int lastRet = -1; // index of last element returned; -1 if no such
    //     int expectedModCount = modCount;

    //     public boolean hasNext() {
    //         return cursor != size;
    //     }

    //     @SuppressWarnings("unchecked")
    //     public E next() {
    //         checkForComodification();
    //         int i = cursor;
    //         if (i >= size)
    //             throw new NoSuchElementException();
    //         Object[] elementData = ArrayList.this.elementData;
    //         if (i >= elementData.length)
    //             throw new ConcurrentModificationException();
    //         cursor = i + 1;
    //         return (E) elementData[lastRet = i];
    //     }

    //     public void remove() {
    //         if (lastRet < 0)
    //             throw new IllegalStateException();
    //         checkForComodification();

    //         try {
    //             ArrayList.this.remove(lastRet);
    //             cursor = lastRet;
    //             lastRet = -1;
    //             expectedModCount = modCount;
    //         } catch (IndexOutOfBoundsException ex) {
    //             throw new ConcurrentModificationException();
    //         }
    //     }

    //     @Override
    //     @SuppressWarnings("unchecked")
    //     public void forEachRemaining(Consumer<? super E> consumer) {
    //         Objects.requireNonNull(consumer);
    //         final int size = ArrayList.this.size;
    //         int i = cursor;
    //         if (i >= size) {
    //             return;
    //         }
    //         final Object[] elementData = ArrayList.this.elementData;
    //         if (i >= elementData.length) {
    //             throw new ConcurrentModificationException();
    //         }
    //         while (i != size && modCount == expectedModCount) {
    //             consumer.accept((E) elementData[i++]);
    //         }
    //         // update once at end of iteration to reduce heap write traffic
    //         cursor = i;
    //         lastRet = i - 1;
    //         checkForComodification();
    //     }

    //     final void checkForComodification() {
    //         if (modCount != expectedModCount)
    //             throw new ConcurrentModificationException();
    //     }
    // }

    // /**

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