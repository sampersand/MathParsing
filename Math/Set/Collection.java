package Math.Set;
import Math.MathObject;
import java.util.ArrayList;
import java.util.Iterator;

public class Collection<E> extends java.util.AbstractList<E> implements MathObject{

    protected ArrayList<E> elements;

    public Collection(){
        this(new ArrayList<E>());
    }
    public Collection(ArrayList<E> pElements){
        elements = new ArrayList<E>();
        add(pElements);
    }

    public Collection(E... pElements) {
        elements = new ArrayList<E>();
        add(pElements);
    }

    public <T extends E> Collection(Collection<T> pCollection) {
        elements = new ArrayList<E>();
        add(pCollection);
    }
    
    public Collection<E> from(E[] pEle){
        return new Collection<E>(){{
            for(E e : pEle)
                add(e);
        }};
    }
    public Collection<E> add(E... pElements){
        assert elements != null : "elements cannot be null!";
        assert pElements != null : "pElements cannot be null!";
        for(E ele : pElements)
            elements.add(ele);
        return this;
    }
    public Collection<E> add(ArrayList<E> pElements){
        assert elements != null : "elements cannot be null!";
        assert pElements != null : "pElements cannot be null!";
        elements.addAll(pElements);
        return this;
    }
    public <T extends E> Collection<E> add(Collection<T> pCollection){
        assert pCollection != null : "pCollection cannot be null!";
        elements.addAll(pCollection.elements());
        return this;
    }
    public ArrayList<E> elements(){ return elements; }
    public int size(){ return elements.size();}
    public E get(int pPos){ return elements.get(pPos);}
    public E set(int pPos, E pEle){
        E ret = get(pPos);
        elements.set(pPos, pEle);
        return ret;
    }

    public E pop(){return pop(size() - 1);}
    public E pop(int pPos){
        E ret = elements.get(pPos);
        elements.remove(pPos);
        return ret;
    }
    public boolean contains(Object pObj){
        return elements.contains(pObj);
    }

    // THIS ∪ PRGROUP
    public <T extends E> Collection<E> union(Collection<T> pCollection){
        return copy().add(pCollection);
    }

    // THIS ∩ PGROUP
    public <T extends E> Collection<E> intersect(Collection<T> pCollection){
        Collection<E> ret = new Collection<E>();
        for(E d : this)
            if(pCollection.contains(d))
                ret.add(d);
        return ret;
    }

    public <T extends E> Collection<E> not(Collection<T> universe){
        return new Collection<E>(){{
            for(T e : universe)
                if(!contains(e))
                    add(e);
        }};
    }
    
    public boolean isUnique(){
        Collection<E> ms = copy();
        while(ms.size() > 0)
            if(ms.elements().contains(ms.pop()))
                return false;
        return true;
    }

    public Iterator<E> iterator() {
        return (Iterator<E>) new Iter();
    }
    private class Iter implements Iterator<E> {
        private int i = 0;

        public boolean hasNext() {
            return i < Collection.this.size();
        }
        public E next() {
            assert hasNext() : "there should be a next one.";
            return Collection.this.get(i++);
        }
    }


    @Override
    public String toString() {
        return "Collection: elements = " + elements;
    }

    @Override
    public String toFancyString(int idtLvl) {
        if(elements.size() == 0)
            return indent(idtLvl) + "Collection: Empty";
        return toFullString(idtLvl);
    }

    @Override
    public String toFullString(int idtLvl) {
        String ret = indent(idtLvl) + "Collection:\n";

        final int columns = 5; // amount of columns
        final int spacing = 10; // amount of spaces. Note that there will be an additional one between columns
        String spaces = "";
        for(int i = 0; i < spacing; i++) spaces += " ";

        ret += indent(idtLvl + 1) + "Elements:";
        for(int i = 0; i < elements.size(); i++){
            if(i % columns == 0)
                ret += "\n" + indent(idtLvl + 2);
            ret += (get(i) + spaces).substring(0, spacing) + " ";
        }
        return ret;

    }

    @Override
    public Collection<E> copy(){
        return new Collection<E>(elements);
    }

    @Override
    public boolean equals(Object pObj){
        if(pObj == null || !(pObj instanceof Collection))
            return false;
        if(pObj == this)
            return true;
        ArrayList<E> pelements = ((Collection<E>)pObj).elements();
        if(elements.size() != pelements.size())
            return false;
        for(int i = 0; i < elements.size(); i++)
            if(elements.get(i) != pelements.get(i))
                return false;
        return true;

    }
}