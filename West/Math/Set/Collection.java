package West.Math.Set;
import West.Math.MathObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import java.util.function.Consumer;
import java.util.AbstractList;
/**
 * TODO: JAVADOC
 * 
 * @author Sam Westerman
 * @version 1.1
 * @since 0.75
 */ 
public class Collection<E> extends AbstractList<E> implements MathObject, Cloneable{

    protected AbstractList<E> elements;

    // Initializers
    public Collection(){
        this(new ArrayList<E>());}

    public Collection(AbstractList<E> eles){
        elements = eles;}

    //Adding elements
    public Collection<E> addE(Object pObj){
        add((E)pObj);
        return this;
    }

    @Override public Collection<E>   clone    ()             { return new Collection<E>().addAllE(elements);  } // NOT SO SURE THAT THIS IS COPYING ELEMENTS...
    @Override public Iterator<E>     iterator ()             { return (Iterator<E>) new Iter();               }
    @Override public boolean         contains (Object o)     { return elements.contains(o);                   }
    @Override public boolean         add      (E e)          { return elements.add(e);                        }
    @Override public void            add      (int p, E e)   {        elements.add(p, e);                     }
              public E               get      (int p     )   { return elements.get(stdPos(p));                }
              public E               set      (int p, E e)   { return elements.set(stdPos(p), e);          }
              public E               pop      ()             { return remove(-1);                             }
              public E               pop      (int p)        { return remove(p);                              }
              public E               remove   (int p)        { return elements.remove(stdPos(p));             }
              public boolean         isEmpty  ()             { return elements.isEmpty();                     }
              public int             size     ()             { return elements.size();                        }
              public int             stdPos   (int p)        { return (p < 0 ? size() : 0) + p;               }
              public AbstractList<E> elements ()             { return elements;                               }
              public List<E>         subList  (int s)        { return subList(s, size());                     }
              public List<E>         subList  (int s, int e) { return elements.subList(stdPos(s), stdPos(e)); }
              public void            append   (E o)          {        elements.add(o);                        }
              public void            prepend  (E o)          {        elements.add(0, o);                     }
              public void            clear    ()             {        elements.clear();                       }

    // element modifying
    public boolean addAll(Object pObj){
        assert pObj instanceof java.lang.Iterable || pObj instanceof Object[]:
            "Cannot addAll of class type '" +pObj.getClass() +  "' because it isn't an iterable!";
        if(pObj instanceof Object[])
            for(E ele : (E[])pObj)
                addE(ele);
        else
            ((java.lang.Iterable)pObj).forEach(this::addE);
        return true;
    }
    public Collection<E> addAllE(Object pObj){
        addAll(pObj);
        return this;
    }
    public Collection setElements(AbstractList<E> pElements){
        elements = pElements;
        return this;
    }
    public Collection<E> appendC(E obj){// append clone
        Collection<E> ret = clone();
        ret.append(obj);
        return ret;
    }
    public Collection<E> prependC(E obj){ //prepend clone
        Collection<E> ret = clone();
        ret.prepend(obj);
        return ret;
    }
    //misc
    public Collection<Integer> enumeration(){ // should be integer, but its this so it works with other things.
        int size = size();
        return new Collection<Integer>(){{
            for(int i = 0; i < size; i++)
                add(i);
        }};
    }
    @Override
    public void forEach(Consumer<? super E> action){
        for(E e : elements)
            action.accept(e);
    }
    public Collection<E> sort(){
        assert size() > 0;
        assert get(0) instanceof java.lang.Comparable;
        E[] ele = (E[])elements.toArray();
        java.util.Arrays.sort(ele);
        return new Collection<E>().addAllE(ele);
    }


    public boolean isUnique(){
        Collection<E> ms = clone();
        while(ms.size() > 0)
            if(ms.elements().contains(ms.pop()))
                return false;
        return true;
    }

    // Set stuff

    //  THIS ∪ PRGROUP
    public Collection<? extends E> union(AbstractList<? extends E> pCollection){
        return clone().addAllE(pCollection);
    }

    //  THIS ∩ PGROUP
    public Collection<? extends E> intersect(AbstractList<? extends E> pCollection){
        Collection<E> ret = new Collection<E>();
        for(E d : this)
            if(pCollection.contains(d))
                ret.add(d);
        return ret;
    }
    
    //  ¬ THIS
    public Collection<E> not(AbstractList<E> universe){
        Collection<E> me = this;
        return new Collection<E>(){{
            for(E e : universe)
                if(!me.contains(e))
                    add(e);
        }};
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
        return elements.toString();
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

        int columns = 5; //  amount of columns
        int spacing = 10; //  amount of spaces. Note that there will be an additional one between columns
        for(E e : this)
            if(e.toString().length() > spacing)
                spacing = e.toString().length();
        String spaces = "";
        for(int i = 0; i < spacing; i++) spaces += " ";
        ret += indent(idtLvl + 1) + "Elements:";
        for(int i = 0; i < elements.size(); i++){
            if(i % columns == 0){
                if(i > elements.size() - columns - 1){
                    ret += "\n" + indentE(idtLvl + 2);
                } else {
                    ret += "\n" + indent(idtLvl + 2);
                }
            }
            ret += (get(i) + spaces).substring(0, spacing ) + ((i + 1) % columns == 0 ? "" : " # ");
        }
        return ret + "\n" + indentE(idtLvl + 1);

    }


    @Override
    public boolean equals(Object pObj){
        if(pObj == null || !(pObj instanceof Collection))
            return false;
        if(pObj == this)
            return true;
        Collection<E> pelements = (Collection<E>)pObj;
        if(size() != pelements.size())
            return false;
        for(int i = 0; i < elements.size(); i++)
            if(!get(i).equals(pelements.get(i)))
                return false;
        return true;

    }
}